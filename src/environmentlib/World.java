/** 
 * SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jonathan Gilmour <gilmour>
 */
package environmentlib;

import characterlib.*;
import java.util.*;
import itemlib.Item;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Color;
import static config.GlobalConfiguration.*;


/** 
 * Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World {
	
	/*INSTANCE VARIABLES*/
	/**Land tilemap for the background*/
	private TiledMap land;
	/**Current x-coordinate of the "camera"*/
	private int cameraXPosition;
	/**Current y-coordinate of the "camera"*/
	private int cameraYPosition;
	/**If true, the player cannot move in the Northern direction*/
	private Boolean cameraFreezeNorth;
	/**If true, the player cannot move in the Southern direction*/
	private Boolean cameraFreezeSouth;
	/**If true, the player cannot move in the Eastern direction*/
	private Boolean cameraFreezeEast;
	/**If true, the player cannot move in the Western direction*/
	private Boolean cameraFreezeWest;
	/**List of characters that currently exist in the world*/
	private ArrayList<Enemy> enemyList;
	/**List of characters that currently exist in the world*/
	private ArrayList<PassiveEnemy> passiveEnemyList;
	/**List of characters that currently exist in the world*/
	private ArrayList<Villager> villagerList;
	/**List of characters that currently exist in the world*/
	private ArrayList<Item> itemList;
	private Image panel;
	
	private Boolean endGame;
	
	
	
	/**The player object*/
	private Player player1;
	/**The value to pad the bounding collision box of the player in by*/
	private int padding;
	/**The X tile grid location to start drawing the map (from the top left)*/
	private int horizontalTileDrawLocation;
	/**The Y tile grid location to start drawing the map (from the top left)*/
	private int verticalTileDrawLocation;
	
	
	
	/*LARGE SCALE METHODS*/
	/**
	 * Default World constructor
	 * 
	 * <strong>Precondition:</strong> <code>land</code> and <code>player1</code> are both objects of the <code>TiledMap</code> and <code>Player</code> classes respectively
	 * <strong>Postcondition:</strong> A new default tilemap is assigned; 
	 * 	camera x and y positions are set to their defaults;
	 * 	camera freeze variables all set to false;
	 *  padding is set to zero value;
	 * 
	 * 
	 * @throws SlickException TiledMap fails to initialise
	 */
    public World() throws SlickException {
        this.land = new TiledMap(DEFAULT_TILE_MAP, DEFAULT_TILESET_FOLDER);
        this.player1 = new Player("playerR.png", PLAYER_START_POS_X, PLAYER_START_POS_Y);
        
        this.enemyList = new ArrayList<Enemy>();
        this.passiveEnemyList = new ArrayList<PassiveEnemy>();
        this.villagerList = new ArrayList<Villager>();
        this.itemList = new ArrayList<Item>();

        this.addAllUnits();
        
        this.cameraXPosition = CAMERA_START_POS_X;
        this.cameraYPosition = CAMERA_START_POS_Y;
        
        this.endGame = false;
        
        this.horizontalTileDrawLocation = INITIAL_TILE_X;
        this.verticalTileDrawLocation = INITIAL_TILE_Y;
        
        this.padding = NIL;
        
        this.panel = new Image(DATA_FOLDER + "panel.png");
        
        this.cameraFreezeNorth = 
        	this.cameraFreezeSouth = 
        		this.cameraFreezeEast = 
        			this.cameraFreezeWest = false;
    }

    /** 
     * Update the game state for a frame.
     * 
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(float dir_x, float dir_y, int delta) throws SlickException {
    	
    	float baseSpeed = SPEED_MULTIPLIER * delta;
    	
    	dir_y *= baseSpeed;
    	dir_x *= baseSpeed;
    	
    	this.setPadding((int)baseSpeed);
    	
   
    	this.movePlayer(dir_x, dir_y);

    	this.moveWorld((int)baseSpeed);
    	
    	for(Villager charac : villagerList) {
        	charac.moveAtRandom(baseSpeed);
        }
    	
    	for(PassiveEnemy charac : passiveEnemyList) {
    		if(!charac.getIsDead()) {
    			charac.checkAwareOfPlayer(baseSpeed);
	        	if(charac.getAttackPlayer()) {
	        		this.player1.battle(charac);
	        		this.player1.setSpeed(this.player1.getSpeed() - baseSpeed);
	        	}
	        	
    		}
        }
    	
    	for(Enemy charac : enemyList) {
    		if(!charac.getIsDead()) {
	        	charac.moveAtRandom(baseSpeed);
	        	if(charac.getAttackPlayer()) {
	        		this.player1.battle(charac);
	        		this.player1.setSpeed(this.player1.getSpeed() - baseSpeed);
	        		charac.setSpeed(charac.getSpeed() - baseSpeed);
	        		if(this.player1.getHealth() <= 0) {
	        			this.resetMap();
	        		}
	        	}
    		}
        }
    	
    	for(Item item : itemList) {
    		if(item.getXPosition() >= PLAYER_START_POS_X - this.land.getTileWidth() && 
    				item.getXPosition() <= PLAYER_START_POS_X + this.land.getTileWidth() &&
    				item.getYPosition() >= PLAYER_START_POS_Y - this.land.getTileHeight() && 
    				item.getYPosition() <= PLAYER_START_POS_Y + this.land.getTileHeight()) {
    			item.equip(this.player1);
    		}
        	
        }
    	
    	if(this.getEndGame() && !this.player1.getElixirNotHeld()) {
    		this.addVillager(500, 300, "prince.png");
    		this.player1.setElixirNotHeld(true);
    	}
    	//System.out.println(this.player1.getMaxSpeed() + "    " + itemList.get(0).getSpeedLevel());
    	
    	
    }

    /** 
     * Render the entire screen, so it reflects the current game state.<br/><br/>
     * 
     * <strong>Precondition:</strong> <code>g</code> is a valid Graphics instance<br/>
     * <strong>Postcondition:</strong> The state of the <code>land</code> object is updated;
     * Every character in the list <code>characterList</code> is updated;
     * <br/><br/>
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g) throws SlickException {
        land.render(
        		this.getCameraX(), 
        		this.getCameraY(), 
        			this.horizontalTileDrawLocation, 
        			this.verticalTileDrawLocation, 
        				this.findNumberOfTilesVisibleX(), 
        				this.findNumberOfTilesVisibleY());
        
        this.player1.render(g, PLAYER_START_POS_X, PLAYER_START_POS_Y);
        
        
        
        for(Enemy charac : enemyList) {
        	if(!charac.getIsDead()) {
        		charac.render(g, charac.getXPosition(), charac.getYPosition());
        	}
        	else {
        		if(charac.getIsBoss() && this.player1.getElixirNotHeld()) {
        			this.addItem(charac.getXPosition(), charac.getYPosition(), "Elixir of Life", "elixir.png", 0, 0, 0, 0, ELIXIR);
        		}
        	}
        }
        
        for(PassiveEnemy charac : passiveEnemyList) {
        	if(!charac.getIsDead()) {
        		charac.render(g, charac.getXPosition(), charac.getYPosition());
        	}
        }
        
        for(Item item : itemList) {
        	item.render();
        }
        
        for(Villager charac : villagerList) {
        	charac.render(g, charac.getXPosition(), charac.getYPosition());
        	
        	if(!player1.getElixirNotHeld() && charac.getIsElvira() && charac.getAwareOfPlayer() && !this.getEndGame()) {
        		
        		this.setEndGame(true);
        		
        	}
        	else if(charac.getIsElvira() && charac.getAwareOfPlayer() && this.player1.getHealth() == this.player1.getMaxHealth() && !this.getEndGame()) {
        		System.out.println("Come see me if you are hurt!");
        	}
        	else if(charac.getIsElvira() && charac.getAwareOfPlayer() && !this.getEndGame()) {
        		System.out.println("All healed up!");
        		this.player1.setHealth(this.player1.getMaxHealth());
        	}
        	else if(charac.getIsElvira() && charac.getAwareOfPlayer()) {
        		System.out.println("Thank you! The prince is saved!!");
        	}
        }
        
        
        //render inventory panel
        this.renderPanel(g); 
        
        
    }
    

    
    
    
    /*SHORTCUT METHODS*/
    /**
     * <div>A shortcut method to move the camera by some amount (pixels) left/right</div>
     * <br/>
     * <div>
     * <strong>Precondition:</strong> <code>in</code> must be an integer<br/>
     * <strong>Postcondition:</strong> The x position of the camera (<code>cameraXPosition</code>) is changed by the amount <code>in</code>
     * </div><br/>
     * @param in The number of pixels to move the camera by
     */
    public void moveCameraX(int in) {
    	this.setCameraX(this.getCameraX() + in);
    	
    	
    }
    
    /**
     * <div>A shortcut method to move the camera by some amount (pixels) up/down</div>
     * <br/>
     * <div>
     * <strong>Precondition:</strong> <code>in</code> must be an integer<br/>
     * <strong>Postcondition:</strong> The y position of the camera (<code>cameraYPosition</code>) is changed by the amount <code>in</code>
     * </div><br/>
     * @param in The number of pixels to move the camera by
     */
    public void moveCameraY(int in) {
    	this.setCameraY(this.getCameraY() + in);
    	
    	
    }
    
    /**
     * Adds a Villager to the game
     */
    public void addVillager(float xPos, float yPos, String sprite) {
    	try {
    		Villager ref = new Villager(xPos, yPos, sprite, land);
    		villagerList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR in method World.addVillager: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Adds a Villager to the game
     */
    public void addVillager(float xPos, float yPos, String sprite, Boolean elvira) {
    	try {
    		Villager ref = new Villager(xPos, yPos, sprite, land, elvira);
    		villagerList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR adding Elvira: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Adds a enemy to the game
     */
    public void addEnemy(float xPos, float yPos, String spriteName, int hp, int sp, int ar, int at) {
    	try {
    		Enemy ref = new Enemy(xPos, yPos, spriteName, land, hp, sp, ar, at);
    		enemyList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR in method World.addEnemy: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Adds a passivbe enemy to the game
     */
    public void addPassiveEnemy(float xPos, float yPos, String spriteName, int hp, int sp, int ar, int at) {
    	try {
    		PassiveEnemy ref = new PassiveEnemy(xPos, yPos, spriteName, land, hp, sp, ar, at);
    		passiveEnemyList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR in method World.addPassiveEnemy: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Adds a enemy to the game
     */
    public void addEnemy(float xPos, float yPos, String spriteName, int hp, int sp, int ar, int at, Boolean boss) {
    	try {
    		Enemy ref = new Enemy(xPos, yPos, spriteName, land, hp, sp, ar, at, boss);
    		enemyList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR in method World.addEnemy: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Adds a item to the game
     */
    public void addItem(float xPos, float yPos, String itemName, String spriteName, int hp, int sp, int ar, int at, int id) {
    	try {
    		Item ref = new Item(xPos, yPos, itemName, spriteName, at, sp, ar, hp, id);
    		this.itemList.add(ref);
    	}
    	catch (Exception e) {
    		System.out.println("ERROR in method World.addItem: ");
    		System.out.println(e);
    	}
    }
    
    /**
     * Resets player distance travelled
     * @param X
     * @param Y
     */
    public void resetPlayerDistanceTravelled(Boolean X, Boolean Y) {
    	if(X) this.player1.setDistanceTraveledX(0);
    	if(Y) this.player1.setDistanceTraveledY(0);
    }
     /**
      * Adds all units to the world
      */
    private void addAllUnits() {
    	this.addVillager(400, 300, "peasant.png");
    	this.addVillager(300, 100, "shaman.png", true);
    	
    	this.addPassiveEnemy(700, 900, "dreadbat.png", 100, 0, 0, 0);
    	this.addPassiveEnemy(400, 850, "dreadbat.png", 100, 0, 0, 0);
    	this.addPassiveEnemy(200, 800, "dreadbat.png", 100, 0, 0, 0);
    	this.addPassiveEnemy(900, 400, "dreadbat.png", 100, 0, 0, 0);
        
    	if(this.player1.getElixirNotHeld()) {
    		this.addEnemy(1600, 100, "necromancer.png", 140, 400, 0, 30, true);
    	}
    	
        this.addEnemy(1650, 550, "skeleton.png", 100, 500, 0, 16);
        this.addEnemy(1900, 250, "skeleton.png", 100, 500, 0, 16);
        this.addEnemy(1600, 500, "skeleton.png", 100, 500, 0, 16);
        this.addEnemy(2100, 350, "skeleton.png", 100, 500, 0, 16);
        
        this.addEnemy(72, 2000, "bandit.png", 40, 200, 0, 8);
        this.addEnemy(250, 2300, "bandit.png", 40, 200, 0, 8);
        this.addEnemy(425, 1800, "bandit.png", 40, 200, 0, 8);
        this.addEnemy(450, 2350, "bandit.png", 40, 200, 0, 8);
        this.addEnemy(800, 1400, "bandit.png", 40, 200, 0, 8);
        this.addEnemy(800, 1600, "bandit.png", 40, 200, 0, 8);
        
        this.addEnemy(850, 1850, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(950, 1900, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(2500, 700, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(2350, 700, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(1700, 900, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(2150, 1050, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(1650, 950, "zombie.png", 60, 800, 0, 10);
        this.addEnemy(1600, 1050, "zombie.png", 60, 800, 0, 10);
        
        if(this.player1.getBookNotHeld()) {
        	this.addItem(1500, 1000, "Tome of Agility", "book.png", 0, 150, 0, 0, BOOK);
        }
        if(this.player1.getAmuletNotHeld()) {
        	this.addItem(300, 2350, "Amulet of Vitality", "amulet.png", 50, 0, 0, 0, AMULET);
        }
        if(this.player1.getSwordNotHeld()) {
        	this.addItem(1500, 575, "Sword of Strength", "sword.png", 0, 0, 0, 10, SWORD);
        }
        
        
    }
    
    /**
     * Resets mapo position on player death
     */
    public void resetMap() {
        
        this.enemyList = new ArrayList<Enemy>();
        this.villagerList = new ArrayList<Villager>();
        
        this.addAllUnits();
        
        this.cameraXPosition = CAMERA_START_POS_X;
        this.cameraYPosition = CAMERA_START_POS_Y;
        
        this.horizontalTileDrawLocation = INITIAL_TILE_X;
        this.verticalTileDrawLocation = INITIAL_TILE_Y;
        
        
        
        this.padding = NIL;
        
        try {
        	this.panel = new Image(DATA_FOLDER + "panel.png");
        }
        catch(SlickException e) {
        	System.out.println("AGH!");
        }
        this.cameraFreezeNorth = 
        	this.cameraFreezeSouth = 
        		this.cameraFreezeEast = 
        			this.cameraFreezeWest = false;
        
        this.player1.setHealth(this.player1.getMaxHealth());
        this.player1.setSpeed(NIL);
    }
    
    
    
    /*MOVEMENT METHODS*/
    /**
     * Checks whether the camera is touching any edge of the map. If it is, then the cam-
     * era is "frozen" for that direction
     */
    public void checkBoundaries() {
    	if(this.getCameraX() >= 0) {
    		this.setCameraFreezeWest(true);
    		this.setCameraX(0);
    	}
    	
    	if(this.getCameraX() <= (-land.getWidth() * land.getTileWidth()) + SCREEN_RESOLUTION_X) {
    		this.setCameraFreezeEast(true);
    		this.setCameraX((-land.getWidth() * land.getTileWidth()) + SCREEN_RESOLUTION_X);
    	}
    	
    	if(this.getCameraY() >= 0) { 
    		this.setCameraFreezeNorth(true);
    		this.setCameraY(0);
    	}
    	
    	if(this.getCameraY() <= (-land.getHeight() * land.getTileHeight()) + SCREEN_RESOLUTION_Y) {
    		this.setCameraFreezeSouth(true);
    		this.setCameraY((-land.getHeight() * land.getTileHeight()) + SCREEN_RESOLUTION_Y);
    	}
    }
    
    /**
     * Resets movement freeze values to false
     */
    public void resetFreeze() {
    	this.setCameraFreezeEast(false);
    	this.setCameraFreezeWest(false);
    	this.setCameraFreezeNorth(false);
    	this.setCameraFreezeSouth(false);
    }
    
    /**
     * Moves the world by some pixel value underneath the player to simulate movement.Wo-<br/>
     * rks by figuring out if the player has travelled at least one pixel in a certain d-<br/>
     * irection (by the <code>distanceTraveled</code> field) and tells the land texture to "catch up"<br/> 
     * by moving the land x pixels in the specified direction (where x is the number of <br/>
     * pixels the player has crossed).<br/><br/>
     * 
     * DEPRECATED::: First, the method checks if the player can move by invoking the <code>checkBoundaries()</code> 
     * and <code>canMove</code>, methods (resetting them to false initially). :::<br/><br/> 
     * 
     * The method then checks the distance the player has travelled (in pixels). If th-<br/>
     * is value exceeds a single pixel, then the land needs to be updated to reflect this <br/>
     * movement. If the X distance is positive, the player has moved East, and vice versa<br/>
     * for negative distances. The same applies to the Y distance, where positive moveme-<br/>
     * nt is in the Southern direction, and negative in the Northern direction.<br/><br/>
     * 
     * If the distance is found to exceed or match one pixel, the value to move the land<br/>
     * by (speed) is checked. The land will always move by at least one pixel to make up <br/>
     * for the player's movement into the next pixel (because the function only does som-<br/>
     * ething if the player has moved at least one pixel), so for rapid frame refreshing, <br/>
     * a speed of <1.0 is checked. Otherwise the map is moved by the number of pixels th-<br/>
     * e player has moved across (for example, if from one frame to the next the player <br/>
     * has moved 6 pixels (common in low FPS situations), then the land must move 6 pixe-<br/>
     * ls). <br/><br/>
     * 
     * Finally, the distance that the map has moved must be deducted from the distance t-<br/>
     * he player travelled (before the catch-up), because the map has now caught up thos-<br/>
     * e pixels. This is done by reducing the distance travelled by the distance the cam-<br/>
     * era has moved, but if the distance travelled is now below 0 (physically impossibl-<br/>
     * e, you can't move negative distances) the distance must then be set to 0. Also, i-<br/>
     * f we reduce the distance and it is still over or equal to 1 pixel, then it is red-<br/>
     * uced below 1 such that the game does not automatically try to "catch up" to the p-<br/>
     * layer after the player has stopped moving.<br/><br/>
     * 
     * <strong>Precondition:</strong> <code>speed</code> is a valid number
     * <strong>Postcondition:</strong> The land is moved by a certain amount
     * 
     * @param speed How many pixels the player has moved between frames
     */
    public void moveWorld(int speed) {
    	this.resetFreeze();
    	//this.checkBoundaries();
    	this.canMove(speed);
    	
    	if(!this.getCameraFreezeEast() && player1.getDistanceTraveledX() >= ONE_PIXEL) {
    		
    		if(speed == 0) {
    			this.moveCameraX(-1);
    			player1.increaseDistanceTraveled(-1,0);
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setXPosition(charac.getXPosition() - 1);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setXPosition(charac.getXPosition() - 1);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setXPosition(charac.getXPosition() - 1);
    	        }
    			
    			for(Item item : itemList) {
    	        	item.setXPosition(item.getXPosition() - 1);
    	        }
    		}
    		else {
    			this.moveCameraX(-speed);
    			player1.increaseDistanceTraveled(-speed,0);
    			if(player1.getDistanceTraveledX() <= 0) {
    				player1.setDistanceTraveledX(0);
    			}
    			if(player1.getDistanceTraveledX() >= 1) {
    				player1.increaseDistanceTraveled(-1, 0);
    			}
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setXPosition(charac.getXPosition() - speed);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setXPosition(charac.getXPosition() - speed);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setXPosition(charac.getXPosition() - speed);
    	        }
    			
    			for(Item item : itemList) {
    	        	item.setXPosition(item.getXPosition() - speed);
    	        }
    		}
    	}
    	
    	if(!this.getCameraFreezeWest() && player1.getDistanceTraveledX() <= -ONE_PIXEL) {
    		
    		if(speed == 0) {
    			this.moveCameraX(1);
    			player1.increaseDistanceTraveled(1,0);
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setXPosition(charac.getXPosition() + 1);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setXPosition(charac.getXPosition() + 1);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setXPosition(charac.getXPosition() + 1);
    	        }
    			for(Item item : itemList) {
    	        	item.setXPosition(item.getXPosition() + 1);
    	        }
    		}
    		else {
    			this.moveCameraX(speed);
    			player1.increaseDistanceTraveled(speed,0);
    			if(player1.getDistanceTraveledX() >= 0) {
    				player1.setDistanceTraveledX(0);
    			}
    			if(player1.getDistanceTraveledX() <= -1) {
    				player1.increaseDistanceTraveled(1, 0);
    			}
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setXPosition(charac.getXPosition() + speed);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setXPosition(charac.getXPosition() + speed);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setXPosition(charac.getXPosition() + speed);
    	        }
    			for(Item item : itemList) {
    	        	item.setXPosition(item.getXPosition() + speed);
    	        }
    		}
    	}
    	
    	if(!this.getCameraFreezeSouth() && player1.getDistanceTraveledY() >= ONE_PIXEL) {
    		
    		if(speed == 0) {
    			this.moveCameraY(-1);
    			player1.increaseDistanceTraveled(0,-1);
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setYPosition(charac.getYPosition() - 1);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setYPosition(charac.getYPosition() - 1);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setYPosition(charac.getYPosition() - 1);
    	        }
    			for(Item item : itemList) {
    	        	item.setYPosition(item.getYPosition() - 1);
    	        }
    		}
    		else {
    			this.moveCameraY(-speed);
    			player1.increaseDistanceTraveled(0,-speed);
    			if(player1.getDistanceTraveledY() <= 0) {
    				player1.setDistanceTraveledY(0);
    			}
    			if(player1.getDistanceTraveledY() >= 1) {
    				player1.increaseDistanceTraveled(0, -1);
    			}
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setYPosition(charac.getYPosition() - speed);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setYPosition(charac.getYPosition() - speed);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setYPosition(charac.getYPosition() - speed);
    	        }
    			for(Item item : itemList) {
    	        	item.setYPosition(item.getYPosition() - speed);
    	        }
    		}
    	}
    	
    	else if(!this.getCameraFreezeNorth() && player1.getDistanceTraveledY() <= -ONE_PIXEL) {
    		
    		if(speed == 0) {
    			this.moveCameraY(1);
    			player1.increaseDistanceTraveled(0,1);
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setYPosition(charac.getYPosition() + 1);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setYPosition(charac.getYPosition() + 1);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setYPosition(charac.getYPosition() + 1);
    	        }
    			for(Item item : itemList) {
    	        	item.setYPosition(item.getYPosition() + 1);
    	        }
    		}
    		else {
    			this.moveCameraY(speed);
    			player1.increaseDistanceTraveled(0,speed);
    			if(player1.getDistanceTraveledY() >= 0) {
    				player1.setDistanceTraveledY(0);
    			}
    			if(player1.getDistanceTraveledY() <= -1) {
    				player1.increaseDistanceTraveled(0, 1);
    			}
    			
    			for(PassiveEnemy charac : passiveEnemyList) {
    	        	charac.setYPosition(charac.getYPosition() + speed);
    	        }
    			for(Enemy charac : enemyList) {
    	        	charac.setYPosition(charac.getYPosition() + speed);
    	        }
    			for(Villager charac : villagerList) {
    	        	charac.setYPosition(charac.getYPosition() + speed);
    	        }
    			for(Item item : itemList) {
    	        	item.setYPosition(item.getYPosition() + speed);
    	        }
    		}
    	}
    	
    	/*
    	 * This section checks if the map has moved a whole tile up/down/left/right and then
    	 * redraws the section of the tilemap that is visible
    	 */
    	if(this.getCameraX() <= -72) {
        	this.moveCameraX(72);
        	this.horizontalTileDrawLocation++;
        }
        
        if(this.getCameraY() <= -72) {
        	this.moveCameraY(72);
        	this.verticalTileDrawLocation++;
        }
        
        if(this.getCameraX() > 0) {
        	this.moveCameraX(-72);
        	this.horizontalTileDrawLocation--;
        }
        
        if(this.getCameraY() > 0) {
        	this.moveCameraY(-72);
        	this.verticalTileDrawLocation--;
        }
        
    }
    
    /**
     * Moves the player if and only if the movement direction isn't blocked<br/><br/>
     * 
     * <strong>Precondition:</strong> xAmount and yAmount are floating point numbers<br/>
     * <strong>Postcondition:</strong> The player is moved by these amounts, or isn't moved at all<br/><br/>
     * 
     * @param xAmount The amount to move the player along the X axis (in pixels)
     * @param yAmount The amount to move the player along the Y axis (in pixels)
     */
    public void movePlayer(float xAmount, float yAmount) {
    	
    	
    	
    	if(xAmount < 0) {
    		if(!this.getCameraFreezeWest()) this.player1.moveX(xAmount);
    	}
    	if(xAmount > 0) {
    		if(!this.getCameraFreezeEast()) this.player1.moveX(xAmount);
    	}
    	
    	if(yAmount < 0) {
    		if(!this.getCameraFreezeNorth()) this.player1.moveY(yAmount);
    	}
    	if(yAmount > 0) {
    		if(!this.getCameraFreezeSouth()) this.player1.moveY(yAmount);
    	}
    }
    
    /**
     * Checks left/right/above/below the player and freezes the camera in those directio-<br/>
     * ns if the player is blocked by certain terrain<br/><br/>
     * 
     * <strong>Precondition:</strong> <code>speed</code> is a valid integer<br/>
     * <strong>Postcondition:</strong> The player is frozen in zero or more directions<br/><br/>
     */
    public void canMove(int speed) {
    	
    	//freeze north
    	if(land.getTileProperty(this.getTileAboveLeft(), "block", "0").equals("1") ||
    		land.getTileProperty(this.getTileAboveRight(), "block", "0").equals("1")) 
    	{
    		this.setCameraFreezeNorth(true);
    	}
    	
    	//freeze south
    	if(land.getTileProperty(this.getTileBelowRight(), "block", "0").equals("1") ||
    		land.getTileProperty(this.getTileBelowLeft(), "block", "0").equals("1")	) 
    	{
    		this.setCameraFreezeSouth(true);
    		
    	}
    	
    	//freeze west
    	if(land.getTileProperty(this.getTileLeftTop(), "block", "0").equals("1") ||
    		land.getTileProperty(this.getTileLeftBottom(), "block", "0").equals("1")) 
    	{
    		this.setCameraFreezeWest(true);
    	}
    	
    	//freeze east
    	if(land.getTileProperty(this.getTileRightTop(), "block", "0").equals("1") ||
    		land.getTileProperty(this.getTileRightBottom(), "block", "0").equals("1")) 
    	{
    		this.setCameraFreezeEast(true);
    	}
    	
    }
    
    
    /*TILE METHODS*/
    /**
     * Gets the grid index of the tile in the (0 + offset) X position of the camera<br/
     * The value of the draw location of the tiles in the x axis is then added to <br/>
     * it for an accurate reading<br/><br/>
     * 
     * <strong>Precondition:</strong> <code>offset</code> is a valid integer<br/>
     * <strong>Postcondition:</strong> The x position of the specified location is found<br/><br/>
     * 
     * 
     * @return The horizontal grid value of the tile as an integer if the offset is a valid area in the game window, if not (eg is negative or beyond the horizontal resolution) then -1 is returned.
     */
    public int findTileXLocation(int offset) {
    	
    	return ( ( ( -this.getCameraX() + offset ) / land.getTileWidth() ) + this.getHorizontalTileDrawLocation());
    }
    
    /**
     * Gets the grid index of the tile in the (0 + offset) Y position of the camera<br/>
     * The value of the draw location of the tiles in the y axis is then added to <br/>
     * it for an accurate reading<br/><br/>
     * 
     * <strong>Precondition:</strong> <code>offset</code> is a valid integer<br/>
     * <strong>Postcondition:</strong> The y position of the specified location is found<br/><br/>
     * 
     * 
     * @return The vertical grid value of the tile as an integer if the offset is a valid area in the game window, if not (eg is negative or beyond the vertical resolution) then -1 is returned.
     */
    public int findTileYLocation(int offset) {
    	
    	return ( ( (-this.getCameraY() + offset) / land.getTileHeight() ) + this.getVerticalTileDrawLocation());
    }
    
    /**
     * The following methods provide the basis for collision detection. The player sprite box 
     * is shaped as:
     * 
     * 		|  +   +  |
     *		+         +
     * 		|         |
     * 		+         +
     * 		|  +   +  |
     * 
     * Where the '+' are detection points. For example, the "aboveLeft" detection point is the 
     * left '+' on the top side of the square, and the "leftTop" point is the upper '+' on the 
     * left side of the square.
     * The higher the constant MOVEMENT_THRESHOLD is, the closer inward each point is.
     * The padding variable is a changing value that varies as the delta varies, to ensure that 
     * changes in FPS don't break the blocking system
     */
    
    /**
     * Gets the tile ID at the aboveLeft detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileAboveLeft() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) - land.getTileWidth()/2 + MOVEMENT_THRESHOLD + this.padding), 
				this.findTileYLocation( (SCREEN_RESOLUTION_Y/2) - land.getTileHeight()/2 + MOVEMENT_THRESHOLD), 
				0);
    }
    
    /**
     * Gets the tile ID at the aboveRight detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileAboveRight() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) + land.getTileWidth()/2 - MOVEMENT_THRESHOLD - this.padding), 
				this.findTileYLocation( (SCREEN_RESOLUTION_Y/2) - land.getTileHeight()/2 + MOVEMENT_THRESHOLD), 
				0);
    }
    
    /**
     * Gets the tile ID at the leftTop detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileLeftTop() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) - land.getTileWidth()/2+ MOVEMENT_THRESHOLD), 
				this.findTileYLocation(SCREEN_RESOLUTION_Y/2 - land.getTileHeight()/2 + MOVEMENT_THRESHOLD + this.padding), 
				0);
    }
    
    /**
     * Gets the tile ID at the leftBottom detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileLeftBottom() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) - land.getTileWidth()/2 + MOVEMENT_THRESHOLD), 
				this.findTileYLocation(SCREEN_RESOLUTION_Y/2 + land.getTileHeight()/2 - MOVEMENT_THRESHOLD - this.padding), 
				0);
    }
    
    /**
     * Gets the tile ID at the rightTop detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileRightTop() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) + land.getTileWidth()/2 - MOVEMENT_THRESHOLD), 
				this.findTileYLocation(SCREEN_RESOLUTION_Y/2 - land.getTileHeight()/2 + MOVEMENT_THRESHOLD + this.padding), 
				0);
    }
    
    /**
     * Gets the tile ID at the rightBottom detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileRightBottom() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) + land.getTileWidth()/2 - MOVEMENT_THRESHOLD), 
				this.findTileYLocation(SCREEN_RESOLUTION_Y/2 + land.getTileHeight()/2 - MOVEMENT_THRESHOLD - this.padding), 
				0);
    }
    
    /**
     * Gets the tile ID at the belowRight detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileBelowRight() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) + land.getTileWidth()/2 - MOVEMENT_THRESHOLD - this.padding), 
				this.findTileYLocation( (SCREEN_RESOLUTION_Y/2) + land.getTileHeight()/2 - MOVEMENT_THRESHOLD/2), 
				0);
    }
    
    /**
     * Gets the tile ID at the belowLeft detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileBelowLeft() {
    	
    	return land.getTileId( 
				this.findTileXLocation( (SCREEN_RESOLUTION_X/2) - land.getTileWidth()/2 + MOVEMENT_THRESHOLD + this.padding), 
				this.findTileYLocation( (SCREEN_RESOLUTION_Y/2) + land.getTileHeight()/2 - MOVEMENT_THRESHOLD/2), 
				0);
    }
    
    /**
     * Returns the number of tiles visible in the camera horizontally
     * 
     * @return The number of tiles visible horizontally
     */
    public int findNumberOfTilesVisibleX() {
    	
    	return (int)( (SCREEN_RESOLUTION_X/land.getTileWidth()) + 2 );
    }
    
    /**
     * Returns the number of tiles visible in the camera vertically
     * 
     * @return The number of tiles visible vertically
     */
    public int findNumberOfTilesVisibleY() {
    	
    	return (int)( (SCREEN_RESOLUTION_Y/land.getTileHeight()) + 2 );
    }
    
    public void initiatePlayerMovement() {
    	player1.setFreeMovement(true);
    }
    
    
    
    /*GETTERS*/
    public int getCameraX() {
    	return this.cameraXPosition;
    }
  
    public int getCameraY() {
    	return this.cameraYPosition;
    }
    
	public Boolean getCameraFreezeNorth() {
		return this.cameraFreezeNorth;
	}

	public Boolean getCameraFreezeSouth() {
		return this.cameraFreezeSouth;
	}

	public Boolean getCameraFreezeEast() {
		return this.cameraFreezeEast;
	}

	public Boolean getCameraFreezeWest() {
		return this.cameraFreezeWest;
	}
	
	public int getHorizontalTileDrawLocation() {
		return this.horizontalTileDrawLocation;
	}
	
	public int getVerticalTileDrawLocation() {
		return this.verticalTileDrawLocation;
	}
	
	public int getPadding() {
		return this.padding;
	}
	
	
	
	/*SETTERS*/
	public void setCameraX(int in) {
    	this.cameraXPosition = in;
    }
    
    public void setCameraY(int in) {
    	this.cameraYPosition = in;
    }
    
    public void setCameraFreezeNorth(Boolean in) {
		this.cameraFreezeNorth = in;
	}

    public void setCameraFreezeSouth(Boolean in) {
		this.cameraFreezeSouth = in;
	}
    
	public void setCameraFreezeEast(Boolean in) {
		this.cameraFreezeEast = in;
	}
	
	public void setCameraFreezeWest(Boolean in) {
		this.cameraFreezeWest = in;
	}
	
	public void setHorizontalTileDrawLocation(int in) {
		this.horizontalTileDrawLocation = in;
	}
	
	public void setVerticalTileDrawLocation(int in) {
		this.verticalTileDrawLocation = in;
	}
 
	public void setPadding(int in) {
		this.padding = in;
	}
	
	/** Renders the player's status panel.
     * @param g The current Slick graphics context.
     */
    public void renderPanel(Graphics g)
    {
        // Panel colours
        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle
        int inv_x, inv_y;           // Coordinates to draw inventory item

        float health_percent;       // Player's health, as a percentage

        // Panel background image
        panel.draw(0, SCREEN_RESOLUTION_Y - PANEL_HEIGHT);

        // Display the player's health
        text_x = 15;
        text_y = SCREEN_RESOLUTION_Y - PANEL_HEIGHT + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        text = this.player1.getHealth() + "/" + this.player1.getMaxHealth();                                

        bar_x = 90;
        bar_y = SCREEN_RESOLUTION_Y - PANEL_HEIGHT + 20;
        bar_width = 90;
        bar_height = 30;
        health_percent = (float)this.player1.getHealth()/(float)this.player1.getMaxHealth();                         // TODO: HP / Max-HP
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        
        // Display the player's damage and cooldown
        text_x = 200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = Integer.toString(this.player1.getAttack());                                    // TODO: Damage
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = Integer.toString(this.player1.getMaxSpeed());                                    // TODO: Cooldown
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", 420, text_y);
        bar_x = 490;
        bar_y = SCREEN_RESOLUTION_Y - PANEL_HEIGHT + 10;
        bar_width = 288;
        bar_height += 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);

        inv_x = 490;
        inv_y = SCREEN_RESOLUTION_Y - PANEL_HEIGHT
            + ((PANEL_HEIGHT - 72) / 2);
        for(Item item : itemList) {
    		if(item.getHeld()) {
    			item.equip(this.player1);
    			item.getSprite().draw(inv_x, inv_y);
    			inv_x += 72;
    		}
        }
    }

	public void setEndGame(Boolean endGame) {
		this.endGame = endGame;
	}

	public Boolean getEndGame() {
		return endGame;
	}


}  