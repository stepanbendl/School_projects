/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.GameStatus.*;
import static cz.cvut.fel.pjv.SpriteID.*;
import static cz.cvut.fel.pjv.Utils.*;
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import jdk.nashorn.internal.objects.Global;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author Čumák
 */
public class GameLoopHandler {

    private SpriteManager spriteManager;
    private final GameCanvas gameCanvas;
    private GameStatus gameStatus;

    /**
     *
     * @param spriteManager sprite manager to work with
     * @param gameCanvas canvas to render to
     */
    public GameLoopHandler(SpriteManager spriteManager, GameCanvas gameCanvas) {
        this.spriteManager = spriteManager;
        this.gameCanvas = gameCanvas;
        this.gameStatus = NOT_STARTED;
        start();
    }

    /**
     * Starts game after hittig spacebar
     */
    public void init() {
        gameStatus = PLAYING;
    }

    /**
     * pauses game after hitting escape key
     * After the game is paused, saves current game into a JSON file
     */
    public void pauseGame() {
        gameStatus = PAUSED;
        saveGame();
    }
    
    private void saveGame(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("board.json"), spriteManager);
            Logger.getLogger(GameLoopHandler.class.getName()).log(Level.SEVERE, "Saving");
        } catch (IOException ex) {
            Logger.getLogger(GameLoopHandler.class.getName()).log(Level.SEVERE, "Saving failed", ex);
        }
    }
    
    private SpriteManager loadSprites(){
            SpriteManager toRet = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            toRet = objectMapper.readValue(new File("board.json"), SpriteManager.class);
            Logger.getLogger(GameLoopHandler.class.getName()).log(Level.SEVERE, "Loading");
        } catch (IOException ex) {
            Logger.getLogger(GameLoopHandler.class.getName()).log(Level.SEVERE, "Loading failed", ex);
        }
        return toRet;
    }

    /**
     * Resumes game after hitting spacebar or escape key
     */
    public void resumeGame() {
        gameStatus = PLAYING;
    }
    
    /**
     * loads game from file - json document
     */
    public void loadGame(){
        
        spriteManager = loadSprites();
        spriteManager.setSprites();
        gameCanvas.setSpriteManager(spriteManager);
        UserActionHandler.setSpriteManager(spriteManager);

    }

    /**
     *
     * @return currently used sprite manager
     */
    public SpriteManager getSpriteManager() {
        return spriteManager;
    }
    

    private void start() {
        spriteManager.setSprites();
        gameCanvas.setSpriteManager(spriteManager);
        UserActionHandler.setSpriteManager(spriteManager);
        new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if(now - lastUpdate >= 1_000_000_000/ 120){
                    doGameLoop();
                    lastUpdate = now;
                }
            }
        }.start();
    }

    private Player getPlayer() {
        return spriteManager.getHero();
    }

    private void updateGround() {
        if (getPlayer().getX() > 700) {
            for (Sprite sprite : spriteManager.collectSprites()) {
                sprite.setX(sprite.getX() - 5);
            }
        } else if (getPlayer().getX() < 0) {
            getPlayer().setX(0);
        }

    }

    private void checkGameOver() {
        Player player = getPlayer();

        if (!player.checkIsAlive()) {
            gameStatus = DIED;
        }
        if (checkReachedCastle()) {
            gameStatus = FINISHED;
        }
    }

    private void updateSprites() {
        updateGround();
        for (Sprite sprite : spriteManager.collectSprites()) {
            sprite.updateLocation();
        }
    }

    private Sprite getClosestSprite(Sprite player) {
        double minDistance = Global.Infinity;
        Sprite toRet = player;
        for (Sprite sprite : spriteManager.collectSprites()) {
            if (sprite.id != HERO_ID) {
                if (Math.abs(sprite.getX() - player.getX()) < minDistance) {
                    minDistance = Math.abs(sprite.getX() - player.getX());
                    toRet = sprite;
                }
            }
        }
        return toRet;
    }
 /**
  * checks collision for enemy creatures with unmovable sprites
  */
    private void checkEnemyCollisions() {

        for (Sprite unmovableSprite : spriteManager.collectSprites()) {
            if (unmovableSprite.getVelX() == 0) {
                for (Sprite movableSprite : spriteManager.collectSprites()) {
                    if (movableSprite.getVelX() != 0) {
                        if (movableSprite.calculateBounds().intersects(unmovableSprite.calculateBounds())) {
                            movableSprite.changeMoveDirection();
                        }
                    }
                }
            }
        }
    }

    /**
     * Realizes interaction between player and unmovable blocks
     */
    private void checkPlayerCollisions() {

        Player player = getPlayer();

        for (Sprite sprite : spriteManager.collectSprites()) {
            if (sprite.type != SpriteID.HERO) {
                if (sprite.getType() == FLOWER || sprite.getType() == COIN) {
                    pickUp(sprite);
                }
                if ((player.getY() + player.getHeight() - sprite.getY()) < 30) {
                    checkIntersectionFromTop(sprite, player);
                }
                if (sprite.getType() == BLOCK) {
                    checkIntersectionFromBottom(sprite);
                }
                //player to left from checked sprite (start checking while the distance is less than 30)
                if ((player.getX() + player.getWidth() - sprite.getX()) < 30) {
                    checkIntersectionFromLeft(sprite, player);
                }
                if (player.getX() - (sprite.getX() + sprite.getWidth()) < 30) {
                    checkIntersectionFromRight(sprite, player);
                }
            }
        }
        checkShouldFall(getClosestSprite(player), player);
    }

    /**
     * checks all collision
     */
    private void checkCollisions() {
        checkPlayerCollisions();
        checkEnemyCollisions();
    }

    /**
     * Checks if sprite should be falling or stay on an unmovable block
     *
     * @param unmovable unmovable sprite - the one the player should be standing on
     * @param player player
     */
    private void checkShouldFall(Sprite unmovable, Player player) {
        Rectangle2D unmovableBounds = unmovable.calculateBounds();
        Rectangle2D playerBounds = player.calculateBounds();

        if (player.getY() < Utils.HORIZON_POS - player.getHeight()) {
            if (!player.isJumping() && (playerBounds.getMaxX() < unmovableBounds.getMinX()
                    || playerBounds.getMinX() > unmovableBounds.getMaxX())) {
                if (!player.isFalling()) {
                    player.setFalling(true);
                }
            }
        }
    }

    /**
     * Realizes collecting of collectable items (coin, flower)
     * 
     * @param sprite the item to pick up
     */
    private void pickUp(Sprite sprite) {
        Player player = getPlayer();
        Rectangle2D spriteBounds = sprite.calculateBounds();

        if (sprite.getType() == FLOWER) {
            if (spriteBounds.intersects(player.calculateBounds())) {

                spriteManager.removeSprite(sprite.getId());
                player.setY(player.getY() - sprite.getHeight());
                player.addHealth();

            }
        }

        if (sprite.getType() == COIN) {
            if (spriteBounds.intersects(player.calculateBounds())) {
                spriteManager.removeSprite(sprite.getId());
                player.setScore(player.getScore() + 10);
                player.setY(player.getY() - sprite.getHeight());
            }
        }
    }

    /**
     * Checks if movable sprite hits unmovable sprite - in that case stops movement of movable sprite
     * Otherwise does nothing
     * 
     * @param unmovable sprite that doesn't move
     * @param movable sprite that moves
     */
    private void checkIntersectionFromRight(Sprite unmovable, Sprite movable) {
        Rectangle2D unmovableBounds = unmovable.calculateBounds();
        Rectangle2D movableBounds = movable.calculateBounds();

        if (unmovableBounds.intersects(movableBounds)
                && movableBounds.getMinX() < unmovableBounds.getMaxX()) {
            movable.setX(unmovableBounds.getMaxX());
            if (movable.equals(getPlayer()) && unmovable.getType() == TURTLE) {
                getPlayer().die();
                unmovable.setVelX(-unmovable.getVelX());
            }
        }
    }

    /**
     * Checks if movable sprite hits unmovable sprite - in that case stops movement of movable sprite
     * Otherwise does nothing
     * 
     * @param unmovable sprite that doesn't move
     * @param movable sprite that moves
     */
    private void checkIntersectionFromLeft(Sprite unmovable, Sprite movable) {
        Rectangle2D unmovableBounds = unmovable.calculateBounds();
        Rectangle2D movableBounds = movable.calculateBounds();

        if (unmovable.type == CASTLE) {
            return;
        }

        if (unmovableBounds.intersects(movableBounds)
                && movableBounds.getMaxX() > unmovableBounds.getMinX()) {
            movable.setX(unmovableBounds.getMinX() - movable.getWidth());
            if (movable.equals(getPlayer()) && unmovable.getType() == TURTLE) {
                getPlayer().die();
                unmovable.setVelX(-unmovable.getVelX());
            }
        }
    }

    
    /**
     * Checks if movable sprite hits unmovable sprite - in that case stops falling of movable sprite
     * Otherwise does nothing
     * 
     * @param unmovable sprite that doesn't move
     * @param movable sprite that moves
     */
    private void checkIntersectionFromTop(Sprite unmovable, Sprite movable) {
        Rectangle2D unmovableBounds = unmovable.calculateBounds();
        Rectangle2D movableBounds = movable.calculateBounds();

        if (unmovableBounds.intersects(movableBounds)
                && movableBounds.getMaxY() > unmovableBounds.getMinY()) {
            movable.setY(unmovableBounds.getMinY() - movable.getHeight());
            movable.setFalling(false);
            if (movable.equals(getPlayer()) && unmovable.getType() == TURTLE) {
                int id = unmovable.getId();
                spriteManager.removeSprite(id);
                getPlayer().setScore(getPlayer().getScore() + 20);
            }
        }
    }

    
        /**
     * Checks if movable sprite hits unmovable sprite - in that case stops jumping of movable sprite
     * and makes movable sprite to fall
     * Otherwise does nothing
     * 
     * @param unmovable sprite that doesn't move
     * @param movable sprite that moves
     */
    private void checkIntersectionFromBottom(Sprite block) {
        Rectangle2D blockBounds = block.calculateBounds();
        Rectangle2D playerBounds = getPlayer().calculateBounds();

        if (blockBounds.intersects(playerBounds)
                && blockBounds.getMaxY() > playerBounds.getMinY()) {
            getPlayer().setJumping(false);
            getPlayer().setFalling(true);
            getPlayer().setY(blockBounds.getMaxY());
            Block thisBlock = (Block) block;
            popSomething(thisBlock);
            thisBlock.breakBlock();
        }
    }

    /**
     * Checks if the player has reached castle - game finished
     * @return true - player reached castle, false - otherwise
     */
    private boolean checkReachedCastle() {
        Player player = getPlayer();
        Sprite castle = spriteManager.findCastle();

        return castle.calculateBounds().getMinX() < player.getX();
    }

    
    /**
     * Checks if the block is not broken - if it is, does nothing
     * otherwise pops coin or flower randomly
     * 
     * @param block block that should be poping something
     */
    private void popSomething(Block block) {
        if (!block.isBroken()) {
            int toPop = block.decideWhatToPop();
            if (toPop == 1) {
                //coin
                Coin coin = new Coin(0);
                coin.setAttributes(new Image("coin.png"));
                double xToPlace = block.getX() + 8;
                double yToPlace = block.getY() - coin.getHeight() - PIXEL_SIZE;
                coin.setPosition(xToPlace, yToPlace);
                spriteManager.addSprite(coin);
                spriteManager.setSprites();
            } else {
                // fire flower
                PowerUp flower = new PowerUp(0);
                flower.setAttributes(new Image("fireFlower.png"));
                flower.setX(block.getX());
                flower.setY(block.getY() - flower.getHeight() - PIXEL_SIZE);
                spriteManager.addSprite(flower);
                spriteManager.setSprites();
            }
        }
    }
    
    /**
     * Calls gameCanvas to redraw all sprites and background
     */
    private void render() {
        gameCanvas.redraw();
    }

    
    /**
     * Manages actuall game loop
     * periodical call
     * Depending on the game status, decides what to do
     */
    private void doGameLoop() {
        switch (gameStatus) {
            case PLAYING:
                checkCollisions();
                updateSprites();
                checkGameOver();
                render();
                break;
            case DIED:
                gameCanvas.drawGameOver();
                break;
            case FINISHED:
                gameCanvas.drawFinishedLevel();
                break;
            case NOT_STARTED:
                gameCanvas.drawStartScreen();
                break;
        }
    }

    /**
     *
     * @return status of the game
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

}
