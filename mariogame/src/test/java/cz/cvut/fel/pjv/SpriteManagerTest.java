/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import java.util.List;
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
public class SpriteManagerTest {
    
    public SpriteManagerTest() {
    }
    
    private static Player sprite;
    
    @BeforeClass
    public static void setUpClass() {        
        sprite = new Player(0);
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
     * Test of addSprite method, of class SpriteManager.
     */
    @Test
    public void testAddSprite() {
        SpriteManager instance = new SpriteManager();
        instance.addSprite((Player)sprite);

        instance.setSprites();
        assertEquals(sprite, instance.findSprite(0));
        
    }

    /**
     * Test of setSprites method, of class SpriteManager.
     */
    @Test
    public void testSetSprites() {
        SpriteManager instance = new SpriteManager();
        instance.addSprite(sprite);
        instance.setSprites();
        
        Sprite result = instance.collectSprites()[0];
        
        assertEquals(sprite, result);
    }

    /**
     * Test of collectSprites method, of class SpriteManager.
     */
    @Test
    public void testCollectSprites() {
        SpriteManager instance = new SpriteManager();
        Sprite[] expResult = {sprite,sprite};
        
        instance.addSprite(sprite);
        instance.addSprite(sprite);
        instance.setSprites();
        
        Sprite[] result = instance.collectSprites();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of findSprite method, of class SpriteManager.
     */
    @Test
    public void testFindSprite() {
        int id = 0;
        SpriteManager instance = new SpriteManager();
        instance.addSprite(sprite);
        instance.setSprites();
        
        Sprite expResult = sprite;
        Sprite result = instance.findSprite(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of getHero method, of class SpriteManager.
     */
    @Test
    public void testGetHero() {
        SpriteManager instance = new SpriteManager();
        Sprite expResult = sprite;
        instance.addSprite(sprite);
        instance.setSprites();
        Sprite result = (Sprite) instance.getHero();
        assertEquals(expResult, result);

    }

    /**
     * Test of findCastle method, of class SpriteManager.
     */
    @Test
    public void testFindCastle() {
        SpriteManager instance = new SpriteManager();
        Sprite expResult = null;
        Sprite result = instance.findCastle();
        assertEquals(expResult, result);
        Castle castle = new Castle(2);
        instance.addSprite(castle);
        instance.setSprites();
        result = instance.findCastle();
        assertEquals(castle, result);

    }

    /**
     * Test of removeSprite method, of class SpriteManager.
     */
    @Test
    public void testRemoveSprite() {
        int id = 0;
        SpriteManager instance = new SpriteManager();
        instance.addSprite(sprite);
        instance.setSprites();
        
        Sprite[] expResult = {sprite};
        
        assertArrayEquals(expResult, instance.collectSprites());
        
        instance.removeSprite(id);
        expResult = new Sprite[0];
        
        assertArrayEquals(expResult, instance.collectSprites());
    }

}
