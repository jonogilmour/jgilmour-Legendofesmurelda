/** 
 * SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jonathan Gilmour <gilmour>
 */
package config;

import static config.GlobalConfiguration.NIL;
import static config.GlobalConfiguration.UNSEEN_TILES_X;
import static config.GlobalConfiguration.UNSEEN_TILES_Y;

/**
 * Global class for storing set variables that are globally accessible. This class is<br/> 
 * not meant to be instantiated, rather it is a single reference point for constants<br/>
 * across the entire program, like a configuration file.<br/><br/> 
 * 
 * Every class in the program should import everything from this class.
 * 
 */
public abstract class GlobalConfiguration {
	
	/**
	 * Screen resolution x value
	 */
	public static final int SCREEN_RESOLUTION_X = 800;
	
	/**
	 * Screen resolution y
	 */
	public static final int SCREEN_RESOLUTION_Y = 600;
	
	public static final int PANEL_HEIGHT = 70;
	
	/**
	 * Default data folder path
	 */
	public static final String DATA_FOLDER = "base/";
	
	/**
	 * Default item image folder
	 */
	public static final String ITEM_FOLDER = DATA_FOLDER + "items/";
	
	/**
	 * Default item sprite
	 */
	public static final String DEFAULT_ITEM_SPRITE = ITEM_FOLDER + "default.png";
	
	/**
	 * Default sprite image folder
	 */
	public static final String SPRITE_FOLDER = DATA_FOLDER + "units/";
	
	/**
	 * Default character sprite location
	 */
	public static final String DEFAULT_CHARACTER_SPRITE = SPRITE_FOLDER + "default.png";
	
	/**
	 * The default folder containing all tile sets
	 */
	public final static String DEFAULT_TILESET_FOLDER = DATA_FOLDER + "";
	
	/**
	 * The default tile map path
	 */
	public final static String DEFAULT_TILE_MAP = DEFAULT_TILESET_FOLDER + "map.tmx";
	
	/**
	 * The default scripting folder
	 */
	public final static String SCRIPT_FOLDER = DATA_FOLDER + "scripting/";
	
	/**
	 * The default conversation file
	 */
	public final static String DEFAULT_DIALOGUE = SCRIPT_FOLDER + "default.txt";
	
	/**
	 * A zero value
	 */
	public static final int NIL = 0;
	
	/**The global threshold for player collision. Increasing this value increases the area that
	 * blocking tiles can cross onto the player sprite.*/
	public static final int MOVEMENT_THRESHOLD = 20;
	
	/**
	 * Initial armor value for the player
	 */
	public static final int INITIAL_ARMOR = NIL;
	
	/**
	 * Initial speed value for the player
	 */
	public static final int INITIAL_SPEED = 600;
	
	/**
	 * Initial attack value for the player
	 */
	public static final int INITIAL_ATTACK = 26;
	
	/**
	 * Initial position value for the player
	 */
	public static final float INITIAL_POSITION = 0.0f;
	
	/**
	 * The distance a unit must stray from their bounding box before a new box location is set
	 */
	public static final int BREAK_BOX = 300;
	
	/**
	 * The default initial health for a character
	 */
	public static final int INITIAL_HEALTH = 100;
	
	/**
	 * Unused.
	 */
	public static final int MAX_DIALOGUE_LENGTH = 5;
	
	/**Initial player start x-position*/
	public final static float PLAYER_START_POS_X = SCREEN_RESOLUTION_X/2 - 36;
	
	/**Initial player start y-position*/
	public final static float PLAYER_START_POS_Y = SCREEN_RESOLUTION_Y/2 - 36;
	
	/**
	 * The number of tiles to the X edges of the screen which are "unseen", to make the map seem continuous
	 */
	public static final int UNSEEN_TILES_X = 3;
	
	/**
	 * The number of tiles to the Y edges of the screen which are "unseen", to make the map seem continuous
	 */
	public static final int UNSEEN_TILES_Y = 2;
	
	/**
	 * The amount to bring the blocking detection points in on NPC units
	 */
	public static final int PADDING = 10;

	/**
	 * Initial camera x-position
	 */
	public final static int CAMERA_START_POS_X = -UNSEEN_TILES_X*72;
	
	/**Initial camera y-position*/
	public final static int CAMERA_START_POS_Y = -UNSEEN_TILES_Y*72;
	
	/**Initial tile x-position*/
	public final static int INITIAL_TILE_X = 3;
	
	/**Initial tile y-position*/
	public final static int INITIAL_TILE_Y = 4;
	
	/**Signifies a single pixel*/
	public final static int ONE_PIXEL = 1;
	
	/**The global player movement speed. Increasing this speeds up the game*/
	public final static float SPEED_MULTIPLIER = .25f;
	
	/**
	 * Sets the location of the 0,0 point of the map in space
	 */
	public final static int ZERO_MAP_POSITION_X = (UNSEEN_TILES_X + INITIAL_TILE_X)*72; 
	
	/**
	 * Sets the location of the 0,0 point of the map in space
	 */
	public final static int ZERO_MAP_POSITION_Y = (UNSEEN_TILES_Y + INITIAL_TILE_Y)*72; 
	
	/**
	 * The size of the bounding box for NPC movement
	 */
	public final static int BOUNDING_BOX_SIZE = 36;
	
	/**
	 * The field of view of agressive enemies
	 */
	public static final int FOV = 150;
	
	/**
	 * The field of view of passive enemies
	 */
	public static final int FOV_PASSIVE = 200;
	
	/**
	 * The attack range of agressive enemies and the player
	 */
	public static final int ATTACK_RANGE = 50;
	
	/**
	 * Identification number of the Sword item
	 */
	public static final int SWORD = 0;
	
	/**
	 * Identification number of the Elixir item
	 */
	public static final int ELIXIR = 1;
	
	/**
	 * Identification number of the Book item
	 */
	public static final int BOOK = 2;
	
	/**
	 * Identification number of the Amulet item
	 */
	public static final int AMULET = 3;
	
	/**
	 * The amount of time a passive enemy will rest after going out of range of the player
	 */
	public static final int REST_TIMER = 700;
}
