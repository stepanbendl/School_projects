/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.TURTLE;
import static cz.cvut.fel.pjv.Utils.*;

/**
 *
 * @author Čumák
 */
public class Enemy extends Sprite {

    /**
     *
     * @param a just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public Enemy(int a) {
        super(TURTLE, ++spriteCount);
        setVelX(ENEMY_VEL_X); 
    }
    
    /**
     * constructor for jackson - DO NOT USE THIS 
     */
    public Enemy(){
        
    }

}
