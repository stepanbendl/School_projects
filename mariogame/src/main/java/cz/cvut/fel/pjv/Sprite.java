

package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Utils.GRAVITY;
import static cz.cvut.fel.pjv.Utils.HORIZON_POS;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 *
 * @author Čumák
 */
public class Sprite {

    /**
     * type of sprite
     */
    protected SpriteID type;

    /**
     * id of sprite
     */
    protected int id;    
    private double x, y, width, height;
    private double velX, velY;
    private boolean jumping, falling;

    /**
     * Constructor for jackson - DO NOT USE
     */
    public Sprite() {
    }
    
    /**
     * 
     * @param type SpriteID - type of new sprite
     * @param id id of new sprite
     */
    public Sprite(SpriteID type, int id){
        this.type = type;
        this.id = id;
        
        setPosition(x,y);
        setVelX(0);
        setVelY(0);
    }

    /**
     * Sets x and y position of sprite
     * 
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Updates location of current sprite based on x and y velocity, x and y position
     * jumping and falling attributes, horizon positon and gravity
     */
    public void updateLocation() {
        if(jumping && velY <= 0){
            jumping = false;
            falling = true;
        }
        else if(jumping){
            velY = velY - GRAVITY;
            y = y - velY;
        }
        
        if(falling){
            y = y + velY;
            velY = velY + GRAVITY;
        }

        if(this.getY() > Utils.HORIZON_POS - this.height){
           falling = false;
           this.setY(Utils.HORIZON_POS - this.height);
        }
   
        x += velX;

    }
    
    /**
     *
     * @param image sets width, height and y coord of sprite based on what image will be render 
     * for this sprite
     */
    public void setAttributes(Image image){
        this.y = HORIZON_POS - image.getHeight();
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    
    /**
     * changes X velocity to negative X velocity
     */
    public void changeMoveDirection(){
        setVelX(-getVelX());
    }

    /**
     *
     * @return X
     */
    public double getX() {
      return x;
    }

    /**
     *
     * @return type
     */
    public SpriteID getType() {
        return type;
    }
    
    /**
     *
     * @return id
     */
    public int getId(){
        return id;
    }
    
    /**
     *
     * @return y
     */
    public double getY() {
      return y;
    }

    /**
     *
     * 
     * @param x
     */
    public void setX(double x) {
      this.x = x;
    }

    /**
     *
     * @param y
     */
    public void setY(double y) {
      this.y = y;
    }

    /**
     *
     * @return width
     */
    public double getWidth() {
      return width;
    }

    /**
     *
     * @return height
     */
    public double getHeight() {
      return height;
    }

    /**
     *
     * @return x velocity
     */
    public double getVelX() {
        return velX;
    }

    /**
     *
     * @return y velocity
     */
    public double getVelY() {
        return velY;
    }
        
    /**
     *
     * @param velX
     */
    public void setVelX(double velX) {
        this.velX = velX;
    }

    /**
     *
     * @param velY
     */
    public void setVelY(double velY) {
        this.velY = velY;
    }

    /**
     *
     * @return jumping status
     */
    public boolean isJumping(){
        return jumping;
    }
    
    /**
     *
     * @return falling status
     */
    public boolean isFalling(){
        return falling;
    }

    /**
     *
     * @param falling
     */
    public void setFalling(boolean falling) {
        this.falling = falling;
    }
    
    /**
     *
     * @param jumping
     */
    public void setJumping(Boolean jumping){
        this.jumping = jumping;
    }

    /**
     *
     * @return bounds of the sprite
     */
    public Rectangle2D calculateBounds() {
      return new Rectangle2D(x, y, width, height);
    }

        
}
