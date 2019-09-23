/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.CASTLE;
import static cz.cvut.fel.pjv.Utils.*;


/**
 *
 * @author stepanbendl
 */
public class Castle extends Sprite{
    
    /**
     *
     * @param x just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public Castle(int x) {
        super(CASTLE, ++spriteCount);
    }

    /**
     * constructor for jackson - please dont use this
     */
    public Castle() {
        
    }
    
}
