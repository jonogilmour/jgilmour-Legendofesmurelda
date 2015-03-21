/** 
 * SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jonathan Gilmour <gilmour>
 */
package environmentlib;

import config.GlobalConfiguration;
import java.awt.Font;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

/** 
 * Main class for the Role-Playing Game engine.
 * Handles initialisation, input and rendering.
 */
public class RPG extends BasicGame {
	
    private World world;

    /**
     * Default constructor, executes parent constructor
     */
    public RPG() {
    	
        super("Legend of Esmurelda");
    }

    /** Initialise the game state; create a new world; set the target frame rate to 60fps;
     * 
     * @param gc The Slick game container object.
     */
    @Override
    public void init(GameContainer gc) throws SlickException {
        world = new World();
        gc.setTargetFrameRate(60);
    }

    /** 
     * <div>
     * Update the game state for a frame following these steps:
     * <br/><br/>
     * 1. Instantiate an Input object to retrieve inputs<br/>
     * 2. Create x/y player movement variables<br/>
     * 3. Check which keys are held down. If up/down/left/right keys are pressed, set the dire-
     * <br/>ction variables to suitable values to be passed to the player object. If the key is not 
     * <br/>being pressed, reset the player's distance travelled for that direction (gets rid of re-
     * <br/>sidual movement)<br/>
     * 4. Send movement data to the world object for processing into actual rendered effects
     * </div><br/>
     * 
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    	
        Input input = gc.getInput();

        float dir_x = 0;
        float dir_y = 0;
        
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y += 1;
        else 
        	world.resetPlayerDistanceTravelled(false, true);
        
        if (input.isKeyDown(Input.KEY_UP))
            dir_y -= 1;
        else 
        	world.resetPlayerDistanceTravelled(false, true);
        
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x -= 1;
        else 
        	world.resetPlayerDistanceTravelled(true, false);
        
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x += 1;
        else 
        	world.resetPlayerDistanceTravelled(true, false);
        
        
        if(input.isKeyDown(Input.KEY_1))
        	gc.setTargetFrameRate(60);
        
        if(input.isKeyDown(Input.KEY_2))
        	gc.setTargetFrameRate(10);

        world.update(dir_x, dir_y, delta);
    }

    
    /** Render the entire screen by delegating to the World object.
     * 
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g) throws SlickException {
        world.render(g);
    }

    
    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args) throws SlickException {
    	
        AppGameContainer app = new AppGameContainer(new RPG());
        app.setShowFPS(false);
        app.setDisplayMode(GlobalConfiguration.SCREEN_RESOLUTION_X, 
        					GlobalConfiguration.SCREEN_RESOLUTION_Y, 
        					 false);
        
        app.start();
    }
}
