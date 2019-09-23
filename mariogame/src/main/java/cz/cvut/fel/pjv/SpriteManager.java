/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import static cz.cvut.fel.pjv.SpriteID.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Čumák
 */
public class SpriteManager {
    
    private List<Player> players = new ArrayList<>();
    private List<Pipe> pipes = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Castle> castles = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    
    @JsonIgnore
    private List<Sprite> allSprites = new ArrayList<>();
    
    /**
     *
     */
    public SpriteManager() {
    }

    /**
     * Adds sprite to relevant list, based on its type
     * 
     * To use collectSprite function, the list needs to be transfered into sprite list using setSprites() function
     * @param sprite the sprite to add
     */
    public void addSprite(Sprite sprite){
        switch(sprite.type){
                case HERO:
                    players.add((Player) sprite);
                    break;
                case PIPE:
                    pipes.add((Pipe) sprite);
                    break;
                case TURTLE:
                    enemies.add( (Enemy) sprite);
                    break;
                case BLOCK:
                    blocks.add( (Block) sprite);
                    break;
                case BLOCK_USED:
                    blocks.add( (Block) sprite);
                    break;
                case CASTLE:
                    castles.add((Castle) sprite);
                    break;
                case COIN:
                    coins.add( (Coin) sprite);
                    break;
                case FLOWER:
                    powerUps.add( (PowerUp) sprite);
                    break;
            }
    }

    /**
     * Transfers all of the lists with sprites of different types to one list with sprites
     */
    public void setSprites() {
        allSprites.clear();
        for(Sprite sprite : players){
            allSprites.add(sprite);
        }
        for(Sprite sprite : pipes){
            allSprites.add(sprite);
        }
        for(Sprite sprite : blocks){
            allSprites.add(sprite);
        }
        for(Sprite sprite : enemies){
            allSprites.add(sprite);
        }
        for(Sprite sprite : castles){
            allSprites.add(sprite);
        }
        for(Sprite sprite : coins){
            allSprites.add(sprite);
        }
        for(Sprite sprite : powerUps){
            allSprites.add(sprite);
        }
    }
    
    /**
     *
     * @return all sprites in an array
     */
    public Sprite[] collectSprites(){
        return allSprites.toArray(new Sprite[allSprites.size()]);
    }

    /**
     * finds sprite according to it's id
     * 
     * @param id id of sprite
     * @return the sprite with id in param or null, if the sprite does not exists
     */
    public Sprite findSprite(int id) {
      Sprite toRet = null;
      for(Sprite sprite : collectSprites()){
          if(sprite.id == id){
              toRet = sprite;
          }
      }
      return toRet;
    }
    
    /**
     * Gets hero sprite
     * 
     * @return hero sprite
     */
    @JsonIgnore
    public Player getHero(){
        return players.get(0);
    }
    
    /**
     * finds first castle in list
     * 
     * @return castle - if it exists, null if no castle exists
     */
    public Sprite findCastle() {
        for(Sprite sprite : collectSprites()){
          if(sprite.type == CASTLE){
              return sprite;
          }
      }
        return null;
    }

    /**
     *
     * removes sprite according to it's id
     * 
     * @param id id of the sprite to remove
     */
    public void removeSprite(int id){
      try{
        for(Sprite sprite : collectSprites()){
            if (sprite.id == id){
                  allSprites.remove(sprite);
                  removeSprite(sprite);
                }
            }
        }  catch (Exception e){}
    }
    
    private void removeSprite(Sprite sprite){
                switch(sprite.type){
                case HERO:
                    players.remove(sprite);
                    break;
                case PIPE:
                    pipes.remove(sprite);
                    break;
                case TURTLE:
                    enemies.remove(sprite);
                    break;
                case BLOCK:
                    blocks.remove(sprite);
                    break;
                case BLOCK_USED:
                    blocks.remove(sprite);
                    break;
                case CASTLE:
                    castles.remove(sprite);
                    break;
                case COIN:
                    coins.remove(sprite);
                    break;
                case FLOWER:
                    powerUps.remove(sprite);
                    break;
            }
    }

    /**
     *
     * @return
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     *
     * @return
     */
    public List<Pipe> getPipes() {
        return pipes;
    }

    /**
     *
     * @return
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     *
     * @return
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     *
     * @return
     */
    public List<Castle> getCastles() {
        return castles;
    }

    /**
     *
     * @return
     */
    public List<Coin> getCoins() {
        return coins;
    }

    /**
     *
     * @return
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
  
    
}
