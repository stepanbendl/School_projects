/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.FLOWER;
import static cz.cvut.fel.pjv.Utils.*;


/**
 *
 * @author Čumák
 */
public class PowerUp extends Sprite{
    
    /**
     *
     * @param a just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public PowerUp(int a) {
        super(FLOWER, ++spriteCount);
    }
    
    /**
     * Constructor for jackson - DO NOT USE
     */
    public PowerUp(){
        
    }
    
}
