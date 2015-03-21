package characterlib;

import static config.GlobalConfiguration.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Class for all enemy units that react to player's attacks.
 * 
 * @author Jono
 *
 */
public class Enemy extends NPC {
	
	/**
	 * Toggle which turns on the "chase" mechanism
	 */
	private Boolean chasePlayer;
	/**
	 * Toggle which turns on the "attack" mechanism
	 */
	private Boolean attackPlayer;
	/**
	 * Sets whether the unit is dead or not.
	 */
	private Boolean isDead;
	/**
	 * Sets to "true" if the unit is Draelic
	 */
	private Boolean isBoss;

	/**
	 * Default constructor, calls parent default and sets all other values to false
	 */
	public Enemy() {
		super();
		
		this.isBoss = false;
		this.chasePlayer = false;
		this.attackPlayer = false;
		this.isDead = false;
	}
	
	/**
	 * Default constructor for setting some parameters, calls parent and sets all other values to false
	 */
	public Enemy(float xpos, float ypos, String spriteFile, TiledMap map) {
		super(xpos, ypos, spriteFile, map);
		
		this.isBoss = false;
		this.chasePlayer = false;
		this.attackPlayer = false;
		this.isDead = false;
	}
	
	/**
	 * Default constructor for setting some parameters, calls parent and sets all other values to false
	 */	
	public Enemy(float xpos, float ypos, String spriteFile, TiledMap map, int hp, int sp, int ar, int at) {
		super(xpos, ypos, spriteFile, map, hp, sp, ar, at);
		
		this.isBoss = false;
		this.chasePlayer = false;
		this.attackPlayer = false;
		this.isDead = false;
	}
	
	/**
	 * Default constructor for setting some parameters, calls parent and sets all other values to false<br/>
	 * apart from "boss" which is set to true if the character is Draelic (argument boss = true)
	 */
	public Enemy(float xpos, float ypos, String spriteFile, TiledMap map, int hp, int sp, int ar, int at, Boolean boss) {
		super(xpos, ypos, spriteFile, map, hp, sp, ar, at);
		
		this.isBoss = boss;
		this.chasePlayer = false;
		this.attackPlayer = false;
		this.isDead = false;
	}
	
///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Checks if the player is within attacking distance, or in the "field of view"<br/>
	 * If the player is within the field of view, the enemy unit will chase after the player<br/>
	 * If the player is within attacking distance, the enemy unit will pause and attack the player
	 */
	public void checkAwareOfPlayer() {
		if(this.getXPosition() >= PLAYER_START_POS_X - ATTACK_RANGE && this.getXPosition() <= PLAYER_START_POS_X + ATTACK_RANGE 
				&& this.getYPosition() >= PLAYER_START_POS_Y - ATTACK_RANGE && this.getYPosition() <= PLAYER_START_POS_Y + ATTACK_RANGE) {
			this.setAwareOfPlayer(true);
			this.setRandomMovementEnabled(false);
			this.setAttackPlayer(true);
		}
		else if(this.getXPosition() >= PLAYER_START_POS_X - FOV && this.getXPosition() <= PLAYER_START_POS_X + FOV 
				&& this.getYPosition() >= PLAYER_START_POS_Y - FOV && this.getYPosition() <= PLAYER_START_POS_Y + FOV) {
			this.setAwareOfPlayer(true);
			this.setRandomMovementEnabled(false);
			this.setAttackPlayer(false);
		}
		else {
			this.setAwareOfPlayer(false);
			this.setRandomMovementEnabled(true);
			this.setAttackPlayer(false);
		}
	}
	
	/**
	 * Engages the unit's chasing algorithm. If the unit is aware, but not attacking the player, <br/>
	 * then the unit will attempt to move within attacking distance.<br/>
	 * The unit will be unable to move towards the player if blocked by an obstacle.
	 * 
	 * @param baseSpeed The distance to move in this frame
	 */
	public void chasePlayer(float baseSpeed) {
		this.resetBlocks();
		this.canMove();
		
		if(this.getAwareOfPlayer() && !this.getAttackPlayer()) {
			float distX = (PLAYER_START_POS_X - this.getXPosition());
			
			float distY = (PLAYER_START_POS_Y - this.getYPosition());
			
			float distTotal = (float)Math.sqrt(distX*distX + distY*distY);
			
			float dX = (distX/distTotal) * baseSpeed;
			float dY = (distY/distTotal) * baseSpeed;
			
			if(dX < 0 && !this.getBlockWest()) {
				this.moveX((float)dX);
			}
			else if(dX > 0 && !this.getBlockEast()) {
				this.moveX((float)dX);
			}
			
			if(dY < 0 && !this.getBlockNorth()) {
				this.moveY((float)dY);
			}
			else if(dY > 0 && !this.getBlockSouth()) {
				this.moveY((float)dY);
			}
			
		}
	}
	
	/**
	 * Engages the unit's random movement algorithm.<br/>
	 * If the unit is not aware of the player, nor attacking the player, the unit will move at random.<br/>
	 * A random "dice" is rolled, and the result determines whether to stay put, or move along the x/y axis<br/>
	 * The chance of a "stay put" result is significantly higher than all the other results, so the unit <br/>
	 * is more likely to stay still in a frame, resulting in more realistic movement.<br/>
	 * 
	 * The unit moves along the edges of an invisible box of a predetermined size, and cannot move beyond <br/>
	 * the bounds of this box, unless chasing the player.<br/>
	 * If the unit chases the player outside of the bounding box's perimeter, the unit will attempt to find its<br/>
	 * way back to the box's location.<br/>
	 * However, if the unit has moved a certain distance away from its original point, it will set a new point to<br/>
	 * walk around from its current location
	 */
	public void moveAtRandom(float baseSpeed) {this.resetBlocks();
		this.canMove();
		double randyNum = Math.random();
		this.checkAwareOfPlayer();
		baseSpeed *= 1.1;
		
		if(this.getRandomMovementEnabled()) {
			
			if(this.getDistanceTraveledX() > BREAK_BOX || this.getDistanceTraveledX() < -BREAK_BOX || this.getDistanceTraveledY() > BREAK_BOX || this.getDistanceTraveledY() < -BREAK_BOX) {
				this.setDistanceTraveledX(0);
				this.setDistanceTraveledY(0);
			}
			
			
			if(this.getLockMovementUp() && !this.getBlockNorth() && this.getDistanceTraveledY() >= -BOUNDING_BOX_SIZE) { 
				this.moveY(-baseSpeed);
			}
			
			else if(this.getLockMovementDown() && !this.getBlockSouth() && this.getDistanceTraveledY() <= BOUNDING_BOX_SIZE) {
				this.moveY(baseSpeed);
			}
			
			else if(this.getLockMovementLeft() && !this.getBlockWest() && this.getDistanceTraveledX() >= -BOUNDING_BOX_SIZE) {
				this.moveX(-baseSpeed);
			}
			
			else if(this.getLockMovementRight() && !this.getBlockEast() && this.getDistanceTraveledX() <= BOUNDING_BOX_SIZE) {
				this.moveX(baseSpeed);
			}
			
			else if(randyNum < 0.96) {
				//do nothing;
			}
			else if(randyNum < 0.97) {
				this.resetLocks();
				if(this.getDistanceTraveledY() <= -BOUNDING_BOX_SIZE || this.getBlockNorth()) { //if youve gone too far or are blocked, turn off the lock
					this.setBlockNorth(true);
				}
				
				else {
					this.moveY(-baseSpeed);
					this.setLockMovementUp(true);
				}
			}
			else if(randyNum < 0.98) {
				this.resetLocks();
				if(this.getDistanceTraveledX() <= -BOUNDING_BOX_SIZE || this.getBlockWest()) { //if youve gone too far or are blocked, turn off the lock
					this.setBlockWest(true);
				}
				
				else {
					this.moveX(-baseSpeed);
					this.setLockMovementLeft(true);
				}
			}
			else if(randyNum < 0.99) {
				this.resetLocks();
				if(this.getDistanceTraveledY() >= BOUNDING_BOX_SIZE || this.getBlockSouth()) {
					this.setBlockSouth(true);
				}
				else {
					this.moveY(baseSpeed);
					this.setLockMovementDown(true);
				}
			}
			else {
				this.resetLocks();
				if(this.getDistanceTraveledX() >= BOUNDING_BOX_SIZE || this.getBlockEast()) {
					this.setBlockEast(true);
				}
				else {
					this.moveX(baseSpeed);
					this.setLockMovementRight(true);
				}
			}
		}
		
		else this.chasePlayer(baseSpeed);
		
	}
	
	/**
	 * Default render method. renders the unit at its current position, as well as the health bar
	 */
	public void render(Graphics g) {
		sprite.draw(this.xPosition, this.yPosition);
		g.setColor(new Color(0,0,0));
		g.fillRect(this.getXPosition() - 16, this.getYPosition() - 6, 100, 8);
		g.setColor(new Color(255,0,0));
		g.fillRect(this.getXPosition() - 16, this.getYPosition() - 16, ((100/this.getMaxHealth()) * this.getHealth()), 12);
		g.setColor(Color.yellow);
        String text = this.getHealth() + "/" + this.getMaxHealth();
        g.drawString(text, this.getXPosition() - 16, this.getYPosition() - 6);
		 
		
	}
	
	/**
	 * Secondary render method. Renders the unit and its health bar at a point passed in to the method
	 */
	public void render(Graphics g, float x, float y) {
		
		sprite.draw(x, y);
		g.setColor(new Color(0,0,0));
		g.fillRect(this.getXPosition() - 16, this.getYPosition() - 6, 100, 12);
		g.setColor(new Color(255,0,0));		
		g.fillRect(this.getXPosition() - 16, this.getYPosition() - 6, 100 * this.getHealth()/this.getMaxHealth(), 12);
		g.setColor(Color.yellow);
        String text = this.getHealth() + "/" + this.getMaxHealth();
        g.drawString(text, this.getXPosition() + 5, this.getYPosition() - 9);
	}

/////////////////////////////////////////////////////////////////////////////////////
	
	//GETTERS AND SETTERS
	public void setChasePlayer(Boolean chasePlayer) {
		this.chasePlayer = chasePlayer;
	}

	public Boolean getChasePlayer() {
		return chasePlayer;
	}

	public void setAttackPlayer(Boolean attackPlayer) {
		this.attackPlayer = attackPlayer;
	}

	public Boolean getAttackPlayer() {
		return attackPlayer;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public Boolean getIsDead() {
		return isDead;
	}

	public void setIsBoss(Boolean isBoss) {
		this.isBoss = isBoss;
	}

	public Boolean getIsBoss() {
		return isBoss;
	}
}
