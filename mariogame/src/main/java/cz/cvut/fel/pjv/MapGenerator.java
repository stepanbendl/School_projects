/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Utils.BLOCK_ALTITUDE;
import static cz.cvut.fel.pjv.Utils.HORIZON_POS;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author stepanbendl
 */
public class MapGenerator {

    private SpriteManager spriteManager;
    private Scanner sc;
    private double xPos = 0;

    /**
     *
     * @param spriteManager
     */
    public MapGenerator(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }

    /**
     * Generates sprites defined in given text file and set its X coord
     */
    public void generateMap() {
        String pattern = readFile();
        char[] seed = pattern.toCharArray();
        boolean castleGenerated = false;
        for (char letter : seed) {
            castleGenerated = generateSprite(letter);
        }
        if (!castleGenerated){
            generateSprite('4');
        }
    }

    // 0 - hero, 1 - pipe, 2 - enemy, 3 - block, 4-castle
    /**
     * the actuall sprite generation
     * @param letter
     * @return if castle was generated
     */
    private boolean generateSprite(char letter) {
        xPos += Utils.HERO_WIDTH + Utils.PIXEL_SIZE;
        switch (letter) {
            case '0':
                Player hero = new Player(0);
                hero.setX(xPos);
                hero.setAttributes(new Image("marioRight.png"));
                xPos += hero.getWidth();
                spriteManager.addSprite(hero);
                return false;
            case '1':
                Pipe pipe = new Pipe(0);
                pipe.setX(xPos);
                pipe.setAttributes(new Image("pipeSmall.png"));
                xPos += pipe.getWidth();
                spriteManager.addSprite(pipe);
                return false;
            case '2':
                Enemy enemy = new Enemy(0);
                enemy.setX(xPos);
                enemy.setAttributes(new Image("enemy.png"));
                xPos += enemy.getWidth();
                spriteManager.addSprite(enemy);
                return false;
            case '3':
                Block block = new Block(0);
                block.setX(xPos);
                block.setAttributes(new Image("blockUnused.png"));
                block.setY(HORIZON_POS - block.getHeight() - BLOCK_ALTITUDE);
                xPos += block.getWidth();
                spriteManager.addSprite(block);
                return false;
            case '4':
                Castle castle = new Castle(0);
                castle.setX(xPos);                
                castle.setAttributes(new Image("castle.png"));
                xPos += castle.getWidth();
                spriteManager.addSprite(castle);
                return true;
            case 'X':
                return false;
            default:
                xPos -= Utils.HERO_WIDTH + Utils.PIXEL_SIZE;
                return false;
        }
    }

    
    /**
     * Realizes reading file
     * @return string with file text
     */
    private String readFile() {
        String fileText = Utils.DEFAULT_MAP_STRING;
        try {
            sc = new Scanner(new File("./src/main/resources/input.txt"));
            fileText = sc.nextLine();
        } catch (FileNotFoundException ex) { 
            Logger.getLogger(MapGenerator.class.getName()).log(Level.SEVERE, "File not found", ex);
        }
        return fileText;
    }

}
