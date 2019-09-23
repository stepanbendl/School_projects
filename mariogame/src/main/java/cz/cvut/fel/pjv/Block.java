/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.SpriteID.BLOCK;
import static cz.cvut.fel.pjv.SpriteID.BLOCK_USED;
import static cz.cvut.fel.pjv.Utils.*;
import java.util.Random;

/**
 *
 * @author Čumák
 */
public class Block extends Sprite {

    private boolean broken;

    /**
     *
     * @param x just to differ from the constructor jackson uses - enter random int, doesn't matter what
     */
    public Block(int x) {
        super(BLOCK, ++spriteCount);
        this.broken = false;
    }

    /**
     * constructor for jackson - DO NOT USE THIS
     */
    public Block(){
        
    }

    /**
     * Makes the block look broken and prevents from breaking it again + the block should pop something
     */
    public void breakBlock() {
        this.type = BLOCK_USED;
    }

    /**
     * Decides what will be poped from the broken block
     *
     * @return  1 - coin, 2 - flower
     */
    public int decideWhatToPop() {
        Random random = new Random();

        if (random.nextInt() % 2 == 0) {
            return 1;
        }
        return 2;
    }
    
    /**
     *
     * @return status of the block (broken/unbroken)
     */
    public boolean isBroken(){
        return broken;
    }

}
