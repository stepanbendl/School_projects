/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.COIN;
import static cz.cvut.fel.pjv.Utils.spriteCount;

/**
 *
 * @author Čumák
 */
public class Coin extends Sprite{
    
    /**
     * 
     * @param x just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public Coin(int x) {
        super(COIN, ++spriteCount);
    }
    
    /**
     * contstructor for jackson - DO NOT USE THIS
     */
    public Coin(){
        
    }
    
}
