/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;


import static cz.cvut.fel.pjv.SpriteID.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static cz.cvut.fel.pjv.Utils.*;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


/**
 *
 * @author stepanbendl
 */
public class GameCanvas extends Canvas {


    private SpriteManager spriteManager;
    private Map<SpriteID, Image> imagePool;
    
    /**
     *
     * @param spriteManager sprites to render
     */
    public GameCanvas(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
        imagePool = new HashMap<>();
        fillImagePool();
    }
    private void fillImagePool(){
        imagePool.put(SpriteID.HERO, new Image("marioRight.png"));
        imagePool.put(SpriteID.PIPE, new Image("pipeSmall.png"));
        imagePool.put(SpriteID.BLOCK, new Image("blockUnused.png"));
        imagePool.put(SpriteID.TURTLE, new Image("enemy.png"));
        imagePool.put(SpriteID.CASTLE, new Image("castle.png"));
        imagePool.put(SpriteID.COIN, new Image("coin.png"));
        imagePool.put(SpriteID.FLOWER, new Image("fireFlower.png"));
        imagePool.put(SpriteID.BLOCK_USED, new Image("blockUsed.png"));
    }
    
    private void renderBackground(GraphicsContext gc){
        double width = getWidth();
        double height = getHeight();
        Player player =  spriteManager.getHero();
        double heroX = player.getX();
        String score = "Score: " + player.getScore();
        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, heroX + width + 150, HORIZON_POS);
        gc.setFill(Color.CHOCOLATE);
        gc.fillRect(0, HORIZON_POS, heroX + width + 150, PIXEL_SIZE);
        gc.setFill(Color.GREENYELLOW);
        gc.fillRect(0, HORIZON_POS + PIXEL_SIZE, heroX + width + 150, height);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(20));
        gc.fillText(score, INIT_APP_WIDTH - 200, 40);
        drawHealth(gc);
    }
    
    private void drawHealth(GraphicsContext gc){
        Image hearth = new Image("heart.png");
        Player player = spriteManager.getHero();
        int health = player.getHealth();
        double delta = hearth.getWidth() + 20;
        for (int i = 0; i < health; i++) {
            gc.drawImage(hearth, 0 + delta, 30);
            delta += hearth.getWidth();
            
        }     
    }
    
    /**
     * Draw screen before the game starts 
     */
    public void drawStartScreen(){
        GraphicsContext gc = getGraphicsContext2D();
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, INIT_APP_WIDTH + 50, INIT_APP_HEIGHT + 50);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(50));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PRESS SPACE TO START", INIT_APP_WIDTH / 2, INIT_APP_HEIGHT / 2, INIT_APP_WIDTH);
        
    }
    
    /**
     * Draw screen when the player dies
     */
    public void drawGameOver(){
        GraphicsContext gc = getGraphicsContext2D();
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, INIT_APP_WIDTH + 50, INIT_APP_HEIGHT + 50);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(50));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("YOU DIED", INIT_APP_WIDTH / 2, INIT_APP_HEIGHT / 2, INIT_APP_WIDTH);
    }
    
    /**
     * Draw screen when the player reaches finish point
     */
    public void drawFinishedLevel(){
        GraphicsContext gc = getGraphicsContext2D();
        Player player = spriteManager.getHero();
        String score = "Score: " + player.getScore();
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, INIT_APP_WIDTH + 50, INIT_APP_HEIGHT + 50);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(50));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("LEVEL COMPLETE", INIT_APP_WIDTH / 2, INIT_APP_HEIGHT / 2, INIT_APP_WIDTH);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(30));
        gc.fillText(score, INIT_APP_WIDTH / 2,  INIT_APP_HEIGHT / 2 - 80, INIT_APP_WIDTH);
    }
    

    /**
     * Set another sprite manager as default
     * @param spriteManager 
     */
    public void setSpriteManager(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }
    
    /**
     * Render all sprites in current sprite manager
     * Based on type, x coord and y coord
     * Gets images from image pool
     */
    public void redraw(){
        GraphicsContext gc = getGraphicsContext2D();
        renderBackground(gc);
        for (Sprite sprite : spriteManager.collectSprites()) {
            switch(sprite.type){
                case HERO:
                    gc.drawImage(imagePool.get(HERO), sprite.getX(), sprite.getY());
                    break;
                case PIPE:
                    gc.drawImage(imagePool.get(PIPE), sprite.getX(), sprite.getY());
                    break;
                case TURTLE:
                    gc.drawImage(imagePool.get(TURTLE), sprite.getX(), sprite.getY());
                    break;
                case BLOCK:
                    gc.drawImage(imagePool.get(BLOCK), sprite.getX(), sprite.getY());
                    break;
                case CASTLE:
                    gc.drawImage(imagePool.get(CASTLE), sprite.getX(), sprite.getY());
                    break;
                case COIN:
                    gc.drawImage(imagePool.get(COIN), sprite.getX(), sprite.getY());
                    break;
                case FLOWER:
                    gc.drawImage(imagePool.get(FLOWER), sprite.getX(), sprite.getY());
                    break;
                case BLOCK_USED:
                    gc.drawImage(imagePool.get(BLOCK_USED), sprite.getX(), sprite.getY());
                    break;
            }
        }
    }
}
