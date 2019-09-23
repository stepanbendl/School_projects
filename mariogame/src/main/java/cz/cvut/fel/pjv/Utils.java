

package cz.cvut.fel.pjv;

/**
 *
 * @author Čumák
 */
public class Utils {
    
    static final double INIT_APP_WIDTH = 1200;
    static final double INIT_APP_HEIGHT = 800;
    static final double HORIZON_POS = INIT_APP_HEIGHT * 3 / 4;
      
    static final double PIXEL_SIZE = 5;
    static final double BLOCK_ALTITUDE = 100;
    static final double PLAYER_VEL_X = 5;
    static final double ENEMY_VEL_X = 8;
    static final double JUMPING_VEL_Y = 12d;
    static final double GRAVITY = 0.38;
    
    
    static final int HERO_ID = 0;
    static final double HERO_WIDTH = 45.0;
    
    static int spriteCount = 0;
    
    static final String DEFAULT_MAP_STRING = "XX0XXX1XX2XXX1X1XX3";
}
