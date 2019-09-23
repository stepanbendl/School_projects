/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.HERO;
import static cz.cvut.fel.pjv.Utils.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepanbendl
 */
public class Player extends Sprite{
    private int score,health;

    /**
     *
     * @param x just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public Player(int x) {
        super(HERO, HERO_ID);
        this.health = 3;
        this.score = 0;
    }   

    /**
     * constructor for jackson - DO NOT USE THIS
     */
    public Player() {
        
    }   
    
    /**
     * Realizes the jumping process
     */
    public void jump(){
        if(!isJumping() && !isFalling()){
            setJumping(true);
            setVelY(JUMPING_VEL_Y);
        }
    }
    
    /**
     * Realizes moving
     * 
     * @param toRight
     */
    public void move(Boolean toRight){
        if(toRight){
            setVelX(PLAYER_VEL_X);
        } else {
            setVelX(-PLAYER_VEL_X);
        }
        Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Position: " + this.getPosition());
    }
    
    /**
     * Realizes stop of movement
     */
    public void stopMoving(){
        setVelX(0);
    }
    
    /**
     *
     * @return status of hero (alive/dead)
     */
    public boolean checkIsAlive(){
        return this.health > 0;
    }
    
    /**
     * Makes hero loose one health
     *  
     */
    public void die(){
        if(health > 0){
            this.health--;
        }
        Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Health: " + this.getHealth());
    }

    /**
     * Increase health attribute by 1
     */
    public void addHealth() {
        if(health < 3){
            this.health++;
        }
        Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Health: " + this.getHealth());
    }
    
    private String getPosition(){
        return " X: " + this.getX() + " Y: " + this.getY();
    }

    /**
     *
     * Getter - score
     * 
     * @return score 
     */
    public int getScore() {
        return score;
    }

    /**
     * 
     * @return health
     */
    public int getHealth() {
        return health;
    }
    
    
    
    /**
     * Setter - score
     * 
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }
    
}
