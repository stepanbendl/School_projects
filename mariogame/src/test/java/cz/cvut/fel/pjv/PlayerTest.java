/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stepanbendl
 */
public class PlayerTest {
    
    public PlayerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of jump method, of class Player.
     */
    @Test
    public void testJump() {
        Player instance = new Player(0);
        instance.jump();
        assertEquals(Utils.JUMPING_VEL_Y, instance.getVelY(), 0);
    }

    /**
     * Test of move method, of class Player.
     */
    @Test
    public void testMove() {
        Boolean toRight = true;
        Player instance = new Player(0);
        instance.move(toRight);
        assertEquals(Utils.PLAYER_VEL_X, instance.getVelX(), 0);
        toRight = false;
        instance.move(toRight);
        assertEquals(-Utils.PLAYER_VEL_X, instance.getVelX(), 0);
    }

    /**
     * Test of stopMoving method, of class Player.
     */
    @Test
    public void testStopMoving() {
        Player instance = new Player(0);
        instance.stopMoving();
        assertEquals(0, instance.getVelX(), 0);
    }


    /**
     * Test of die method, of class Player.
     */
    @Test
    public void testDie() {
        Player instance = new Player(0);
        instance.die();
        assertEquals(2, instance.getHealth());
        instance.die();
        assertEquals(1, instance.getHealth());
        
    }

    /**
     * Test of getHealth method, of class Player.
     */
    @Test
    public void testGetHealth() {
        Player instance = new Player(0);
        int expResult = 3;
        int result = instance.getHealth();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of addHealth method, of class Player.
     */
    @Test
    public void testAddHealth() {
        Player instance = new Player(0);
        instance.addHealth();
        assertEquals(3, instance.getHealth());
        instance.die();
        instance.die();
        instance.addHealth();
        assertEquals(2, instance.getHealth());
    }

    /**
     * Test of getScore method, of class Player.
     */
    @Test
    public void testGetScore() {
        Player instance = new Player(0);
        int expResult = 0;
        int result = instance.getScore();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setScore method, of class Player.
     */
    @Test
    public void testSetScore() {
        int score = 50;
        Player instance = new Player(0);
        instance.setScore(score);
        assertEquals(score, instance.getScore());
        
    }
    
}
