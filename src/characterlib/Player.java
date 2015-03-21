/** 
 * SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jonathan Gilmour <gilmour>
 */
package characterlib;

import static config.GlobalConfiguration.*;
import java.util.Random;
import org.newdawn.slick.Graphics;

/**
 * A stub class for the player. Will be added to later to include health, armor, and 
 * other fields specific to player objects
 * 
 * @author Jono
 *
 */
public final class Player extends GameCharacter {
	
	private Boolean freeMovement;
	
	private Boolean elixirNotHeld;
	private Boolean bookNotHeld;
	private Boolean amuletNotHeld;
	private Boolean swordNotHeld;
	
	/*CONSTRUCTORS*/
	/**
	 * Default Constructor calls default Character constructor
	 */
	public Player() {
		super();
		
		this.freeMovement = false;
		this.elixirNotHeld = true;
		this.bookNotHeld = true;
		this.amuletNotHeld = true;
		this.swordNotHeld = true;
	}
	
	/**
	 * Another constructor. Calls corresponding Character constructor
	 * 
	 * @param spriteImageName Name of the sprite image file
	 */
	public Player(String spriteImageName) {
		super(spriteImageName);
		
		this.freeMovement = false;
		this.elixirNotHeld = true;
		this.bookNotHeld = true;
		this.amuletNotHeld = true;
		this.swordNotHeld = true;
	}
	
	/**
	 * Another constructor. Calls corresponding Character constructor
	 * 
	 * @param spriteImageName Name of the sprite image file
	 * @param xpos X position of the player object
	 * @param ypos Y position of the player object
	 */
	public Player(String spriteImageName, float xpos, float ypos) {
		super(spriteImageName, xpos, ypos);
		
		this.freeMovement = false;
		this.elixirNotHeld = true;
		this.bookNotHeld = true;
		this.amuletNotHeld = true;
		this.swordNotHeld = true;
	}
	
	/**
	 * Another constructor. Calls corresponding Character constructor
	 * 
	 * @param xpos X position of the player object
	 * @param ypos Y position of the player object
	 */
	public Player(float xpos, float ypos) {
		super(xpos, ypos);
		
		this.freeMovement = false;
		this.elixirNotHeld = true;
		this.bookNotHeld = true;
		this.amuletNotHeld = true;
		this.swordNotHeld = true;
	}
	
	/**
	 * Renders the player at its current position
	 */
	public void render(Graphics g) {
		
		sprite.draw(this.xPosition, this.yPosition);
	}
	
	/**
	 * Renders the player at a chosen position
	 */
	public void render(Graphics g, float x, float y) {
		
		sprite.draw(x, y);
	}
	
	/**
	 * Alters the X position of a player object.
	 * 
	 * @param amount An integer value specifying how much to move the object (-1 for one pixel left, +1 for one pixel right, 0 for no movement)
	 * @return true if the operation completed successfully, false otherwise
	 */
	public void moveX(float amount) {
		
		this.setXPosition(this.getXPosition() + amount);
		this.increaseDistanceTraveled(amount, 0);
		
		if(amount < 0 && !this.getFlippedSprite()) {
			this.sprite = this.sprite.getFlippedCopy(true, false);
			this.setFlippedSprite(true);
		}
		
		else if(amount > 0 && this.getFlippedSprite()) {
			this.sprite = this.sprite.getFlippedCopy(true, false);
			this.setFlippedSprite(false);
		}
	}
	
	/**
	 * The player attacks the enemy passed in as an argument, and gets damaged by that enemy
	 * @param charac The character to attack
	 */
	public void battle(Enemy charac) {
		Random dice = new Random();
		
		if(this.getSpeed() <= 0) {
			charac.setHealth(charac.getHealth() - dice.nextInt(this.getAttack()));
			this.setSpeed(this.getMaxSpeed());
		}
		
		if(charac.getHealth() <= 0) {
			charac.setIsDead(true);
		}
		
		if(charac.getSpeed() <= 0) {
			this.setHealth(this.getHealth() - dice.nextInt(charac.getAttack()));
			charac.setSpeed(charac.getMaxSpeed());
		}
		
		if(this.getHealth() <= 0) {
			this.die();
		}	
		
	}
	
	/**
	 * One way battling with passive enemies
	 * 
	 * @param charac The character to attack
	 */
	public void battle(PassiveEnemy charac) {
		Random dice = new Random();
		
		if(this.getSpeed() <= 0) {
			charac.setHealth(charac.getHealth() - dice.nextInt(this.getAttack()));
			this.setSpeed(this.getMaxSpeed());
		}
		
		if(charac.getHealth() <= 0) {
			charac.setIsDead(true);
		}	
		
	}
	
	/**
	 * Causes the player to reset back to original position
	 */
	public void die() {
		this.setXPosition(PLAYER_START_POS_X);
		this.setYPosition(PLAYER_START_POS_Y);
	}
	
	/**
	 * Alters the Y position of the player object 
	 * 
	 * @param amount An integer value specifying how much to move the object (-1 for one pixel up, +1 for one pixel down, 0 for no movement)
	 * @return true if the operation completed successfully, false otherwise
	 */
	public void moveY(float amount) {
		
		this.setYPosition(this.getYPosition() + amount);
		this.increaseDistanceTraveled(0, amount);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void setFreeMovement(Boolean in) {
		this.freeMovement = in;
	}
	
	public Boolean getFreeMovement() {
		return this.freeMovement;
	}

	public Boolean getElixirNotHeld() {
		return elixirNotHeld;
	}

	public void setElixirNotHeld(Boolean elixirNotHeld) {
		this.elixirNotHeld = elixirNotHeld;
	}

	public Boolean getBookNotHeld() {
		return bookNotHeld;
	}

	public void setBookNotHeld(Boolean bookNotHeld) {
		this.bookNotHeld = bookNotHeld;
	}

	public Boolean getAmuletNotHeld() {
		return amuletNotHeld;
	}

	public void setAmuletNotHeld(Boolean amuletNotHeld) {
		this.amuletNotHeld = amuletNotHeld;
	}

	public Boolean getSwordNotHeld() {
		return swordNotHeld;
	}

	public void setSwordNotHeld(Boolean swordNotHeld) {
		this.swordNotHeld = swordNotHeld;
	}
	
	
}
