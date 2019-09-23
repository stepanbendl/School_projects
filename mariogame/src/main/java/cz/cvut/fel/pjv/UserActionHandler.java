/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.GameStatus.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import static cz.cvut.fel.pjv.Utils.*;
import javafx.scene.input.KeyCode;

/**
 *
 * @author stepanbendl
 */
public class UserActionHandler implements EventHandler<KeyEvent>{
    private final Group root = new Group();
    private final Scene scene = new Scene(root, INIT_APP_WIDTH, INIT_APP_HEIGHT);
    private static SpriteManager spriteManager;
    private final GameLoopHandler gameLoopHandler;

    /**
     *
     * @param gameLoopHandler
     */
    public UserActionHandler(GameLoopHandler gameLoopHandler) {
        this.gameLoopHandler = gameLoopHandler;
    }

    
    @Override
    public void handle(KeyEvent event) {
        Player player = spriteManager.getHero();
        boolean jumped = false;

        switch (getEventCode(event)) {
            case "A":
                player.move(false);
                break;
            case "D":
                player.move(true);
                break;
            case "W":
                player.jump();
                jumped = true;
                break;
            default:
                break;
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED) && !jumped){
            player.stopMoving();
        }
        if (event.getCode() == KeyCode.SPACE){
            gameLoopHandler.init();
        }
        if (event.getCode() == KeyCode.ESCAPE && event.getEventType() == KeyEvent.KEY_PRESSED){
            if(gameLoopHandler.getGameStatus() == PLAYING){
                gameLoopHandler.pauseGame();
            } else {
                gameLoopHandler.resumeGame();
            }
        }
        if (event.getCode().toString().equals("L") && event.getEventType() == KeyEvent.KEY_PRESSED){
            gameLoopHandler.loadGame();
        }
    }
    
    /**
     * sets sprite manager to work with
     * @param spriteManager
     */
    public static void setSpriteManager(SpriteManager spriteManager) {
        UserActionHandler.spriteManager = spriteManager;
    }
    
    private String getEventCode(KeyEvent event){
     return event.getCode().toString();
    }       
            
    /**
     *
     * @return scene
     */
    public Scene getScene(){
        return scene;
    }

    
}
