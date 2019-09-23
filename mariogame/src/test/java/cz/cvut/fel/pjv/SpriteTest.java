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
 * @author Čumák
 */
public class SpriteTest {
    
    public SpriteTest() {
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
     * Test of setPosition method, of class Sprite.
     */
    @Test
    public void testSetPosition() {
        double x = 10.0;
        double y = 20.0;
        Sprite instance = new Sprite();
        instance.setPosition(x, y);
        assertEquals(10, instance.getX(),0);
        assertEquals(20, instance.getY(),0);
        
    }

    /**
     * Test of updateLocation method, of class Sprite.
     */
    @Test
    public void testUpdateLocation() {
        double velX = 5;
        Sprite instance = new Sprite();
        instance.setX(5);
        instance.setJumping(false);
        instance.setFalling(false);
        instance.setVelX(velX);
        instance.updateLocation();
        assertEquals(10, instance.getX(), 0);
    }

    /**
     * Test of changeMoveDirection method, of class Sprite.
     */
    @Test
    public void testChangeMoveDirection() {
        Sprite instance = new Sprite();
        instance.setVelX(6);
        instance.changeMoveDirection();
        assertEquals(-6, instance.getVelX(), 0);

    }
    
}
