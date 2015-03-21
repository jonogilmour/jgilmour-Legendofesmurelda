/** 
 * SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jonathan Gilmour <gilmour>
 */
package characterlib;

import org.newdawn.slick.*;
import static config.GlobalConfiguration.*;

/**
 * A class for any character that moves independently of the map.
 * 
 * @author Jono
 *
 */
public abstract class GameCharacter {
	
	/**Counts the number of instantiated characters in the current level*/
	static int numberOfObjects;
	/**Unused variable at the moment*/
	int health, armor, attack; 
	float speed;
	int maxHealth;
	int maxSpeed;
	/**The character's x position on the camera*/
	float xPosition; 
	/**The character's y position on the camera*/
	float yPosition;
	/**The character's sprite image*/
	Image sprite;
	private Boolean flippedSprite;
	

	/**The character's index in the character array*/
	int characterID;
	/**The distance the character has travelled in the +-X direction*/
	float distanceTraveledX;
	/**The distance the character has travelled in the +-Y direction*/
	float distanceTraveledY;
	
	/**If true, the player cannot move in the Northern direction*/
	private Boolean blockNorth;
	/**If true, the player cannot move in the Southern direction*/
	private Boolean blockSouth;
	/**If true, the player cannot move in the Eastern direction*/
	private Boolean blockEast;
	/**If true, the player cannot move in the Western direction*/
	private Boolean blockWest;
	
	
	
	
	
	/*CONSTRUCTORS*/
	/**
	 * Default constructor<br/><br/>
	 * 
	 * <b>Precondition:</b> none<br/>
	 * <b>Postcondition:</b> <br/>All default values set;<br/> 
	 * <code>numberOfObjects</code> incremented; <br/>
	 * <code>sprite</code> is set to default sprite;<br/><br/>
	 */
	public GameCharacter() {
		this.health = this.maxHealth = INITIAL_HEALTH;
		this.armor = INITIAL_ARMOR;
		this.speed = NIL;
		this.attack = INITIAL_ATTACK;
		
		this.maxSpeed = INITIAL_SPEED;
		
		this.xPosition = INITIAL_POSITION;
		this.yPosition = INITIAL_POSITION;
		
		this.blockEast = false;
		this.blockSouth = false;
		this.blockNorth = false;
		this.blockWest = false;
		
		this.characterID = numberOfObjects;
		numberOfObjects++;
		
		this.flippedSprite = false;
		
		this.distanceTraveledX = NIL;
		this.distanceTraveledY = NIL;
		
		setSprite(DEFAULT_CHARACTER_SPRITE);
	}
	
	/**
	 * Another constructor that initialises the sprite image to a selected version<br/><br/>
	 * 
	 * <b>Precondition:</b> <code>spriteImageName</code> is a valid image name under the default path<br/>
	 * <b>Postcondition:</b><br/> All default values set;<br/> 
	 * <code>image</code> is set to the provided sprite image; <br/><br/>
	 * 
	 * @param spriteImageName The name of the sprite image under the default asset folder
	 */
	public GameCharacter(String spriteImageName) {
		this.health = this.maxHealth = INITIAL_HEALTH;
		this.armor = INITIAL_ARMOR;
		this.speed = INITIAL_SPEED;
		this.attack = INITIAL_ATTACK;
		
		this.maxSpeed = INITIAL_SPEED;
		
		this.xPosition = INITIAL_POSITION;
		this.yPosition = INITIAL_POSITION;
		
		this.blockEast = false;
		this.blockSouth = false;
		this.blockNorth = false;
		this.blockWest = false;
		
		this.characterID = numberOfObjects;
		numberOfObjects++;
		
		this.flippedSprite = false;
		
		this.distanceTraveledX = NIL;
		this.distanceTraveledY = NIL;
		
		setSprite(SPRITE_FOLDER + spriteImageName);
	}
	
	/**
	 * Another constructor<br/><br/>
	 * 
	 * <b>Precondition:</b> <code>xpos</code> and <code>ypos</code> are valid floating point numbers;<br/>
	 * <b>Postcondition:</b><br/> <code>Health</code>, <code>Armor</code>, <code>Speed</code> set to default values; <br/>
	 * 	<code>xPosition</code> and <code>yPosition</code> set to given <code>xpos</code> and <code>ypos</code> values; <br/>
	 * 	<code>sprite</code> is set to default sprite;<br/>
	 *  <code>characterID</code> is set to the number of objects so far (for referencing); <br/>
	 * 	<code>numberOfObjects</code> is incremented;<br/><br/>
	 * 
	 * @param xpos Sets the initial x-coordinate to render the image at
	 * @param ypos Sets the initial y-coordinate to render the image at
	 */
	public GameCharacter(float xpos, float ypos) {
		this.health = this.maxHealth = INITIAL_HEALTH;
		this.armor = INITIAL_ARMOR;
		this.speed = INITIAL_SPEED;
		this.attack = INITIAL_ATTACK;
		
		this.maxSpeed = INITIAL_SPEED;
		
		this.xPosition = xpos;
		this.yPosition = ypos;
		
		this.blockEast = false;
		this.blockSouth = false;
		this.blockNorth = false;
		this.blockWest = false;
		
		this.characterID = numberOfObjects;
		numberOfObjects++;
		
		this.flippedSprite = false;
		
		this.distanceTraveledX = NIL;
		this.distanceTraveledY = NIL;
		
		setSprite(DEFAULT_CHARACTER_SPRITE);
	}
	
	/**
	 * Another constructor<br/><br/>
	 * 
	 * <b>Precondition:</b> <code>spriteImageName</code> is a valid image name under the default path; xpos and ypos are valid floating point numbers;
	 * <b>Postcondition:</b> <br/>Health, Armor, Speed set to default values; 
	 * 	<br/>xPosition and yPosition set to given xpos and ypos values; 
	 * 	<br/>sprite is set to the sprite at path <code>spriteImageName</code>;
	 *  <br/>characterID is set to the number of objects so far (for referencing); 
	 * 	<br/>numberOfObjects is incremented;<br/><br/>
	 * 
	 * @param spriteImageName The name of the sprite image under the default asset folder
	 * @param xpos Sets the initial x-coordinate to render the image at
	 * @param ypos Sets the initial y-coordinate to render the image at
	 */
	public GameCharacter(String spriteImageName, float xpos, float ypos) {
		this.health = this.maxHealth = INITIAL_HEALTH;
		this.armor = INITIAL_ARMOR;
		this.speed = INITIAL_SPEED;
		this.attack = INITIAL_ATTACK;
		
		this.maxSpeed = INITIAL_SPEED;
		
		this.xPosition = xpos;
		this.yPosition = ypos;
		
		this.blockEast = false;
		this.blockSouth = false;
		this.blockNorth = false;
		this.blockWest = false;
		
		this.characterID = numberOfObjects;
		numberOfObjects++;
	
		
		this.flippedSprite = false;
		
		this.distanceTraveledX = NIL;
		this.distanceTraveledY = NIL;
		
		setSprite(SPRITE_FOLDER + spriteImageName);
	}
	
	public GameCharacter(String spriteImageName, float xpos, float ypos, int hp, int sp, int ar, int at) {
		this.health = this.maxHealth = hp;
		this.armor = ar;
		this.speed = NIL;
		this.attack = at;
		
		this.maxSpeed = sp;
		
		this.xPosition = xpos;
		this.yPosition = ypos;
		
		this.blockEast = false;
		this.blockSouth = false;
		this.blockNorth = false;
		this.blockWest = false;
		
		this.characterID = numberOfObjects;
		numberOfObjects++;
	
		
		this.flippedSprite = false;
		
		this.distanceTraveledX = NIL;
		this.distanceTraveledY = NIL;
		
		setSprite(SPRITE_FOLDER + spriteImageName);
	}
	
	
	/*RENDERING METHODS*/
	/**
	 * Renders the character on screen
	 */
	public void render(Graphics g) {
		sprite.draw(this.xPosition, this.yPosition);
		 
		
	}
	
	public void render(Graphics g, float x, float y) {
		
		sprite.draw(x, y);
	}
	
	
	/*SHORTCUT METHODS*/
	/**
	 * Shortcut method to increase the X/Y distance travelled by the character<br/>
	 * 
	 * <b>Precondition:</b> <code>inX</code> and <code>inY</code> are valid floating point numbers;
	 * <b>Postcondition:</b> <br/>The distance travelled in the x/y directions is updated;<br/><br/>
	 * 
	 * @param inX The value to increment the X distance by
	 * @param inY The value to increment the Y distance by
	 */
	public void increaseDistanceTraveled(float inX, float inY) {
		this.setDistanceTraveledX(this.getDistanceTraveledX() + inX);
		this.setDistanceTraveledY(this.getDistanceTraveledY() + inY);
	}
	
	/*SETTERS*/
	public void setHealth(int in) {
		this.health = in;
	}
	
	public void setArmor(int in) {
		this.armor = in;
	}
	
	public void setSpeed(int in) {
		this.speed = in;
	}
	
	public void setXPosition(float in) {
		this.xPosition = in;
	}
	
	public void setYPosition(float in) {
		this.yPosition = in;
	}
	
	public void setSprite(String in) {
		try {
			this.sprite = new Image(in);
		}
		catch (SlickException err) {
			System.out.println("Error in method \"setSprite\": No such resource " + in);
			System.exit(0);
		}
	}
	
	public void setDistanceTraveledX(float in) {
		this.distanceTraveledX = in;
	}
	
	public void setDistanceTraveledY(float in) {
		this.distanceTraveledY = in;
	}
	
	
	/* GETTERS*/
	public int getHealth() {
		return this.health;
	}
	
	public int getArmor() {
		return this.armor;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getXPosition() {
		return this.xPosition;
	}
	
	public float getYPosition() {
		return this.yPosition;
	}
	
	public float getDistanceTraveledX() {
		return this.distanceTraveledX;
	}
	
	public float getDistanceTraveledY() {
		return this.distanceTraveledY;
	}
	
	public int getImageHeight() {
		return this.sprite.getHeight();
	}
	
	public int getImageWidth() {
		return this.sprite.getWidth();
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public Boolean getBlockNorth() {
		return blockNorth;
	}

	public void setBlockNorth(Boolean blockNorth) {
		this.blockNorth = blockNorth;
	}

	public Boolean getBlockSouth() {
		return blockSouth;
	}

	public void setBlockSouth(Boolean blockSouth) {
		this.blockSouth = blockSouth;
	}

	public Boolean getBlockEast() {
		return blockEast;
	}

	public void setBlockEast(Boolean blockEast) {
		this.blockEast = blockEast;
	}

	public Boolean getBlockWest() {
		return blockWest;
	}

	public void setBlockWest(Boolean blockWest) {
		this.blockWest = blockWest;
	}
	
	public Boolean getFlippedSprite() {
		return flippedSprite;
	}

	public void setFlippedSprite(Boolean flippedSprite) {
		this.flippedSprite = flippedSprite;
	}
}
