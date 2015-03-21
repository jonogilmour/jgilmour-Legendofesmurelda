package characterlib;

import static config.GlobalConfiguration.*;
import wrapperlib.CoordinatePoint;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.tiled.TiledMap;
/**
 * The over-arching class for non playable units
 * 
 * @author Jono
 *
 */
public class NPC extends GameCharacter {
	 /**
	  * Random movement toggle
	  */
	private Boolean randomMovementEnabled;
	/**
	 * Player awareness toggle
	 */
	private Boolean awareOfPlayer;

	/**
	 * These positions only change when the character actually actively moves<br/>
	 * vs x and y positions which change when the camera OR the character moves
	 */
	private float positionOnMapX;
	
	/**
	 * These positions only change when the character actually actively moves<br/>
	 * vs x and y positions which change when the camera OR the character moves
	 */
	private float positionOnMapY;
	
	/**
	 * Movement "lock" toggles used in random movement
	 */
	private Boolean lockMovementUp;
	private Boolean lockMovementDown;
	private Boolean lockMovementLeft;
	private Boolean lockMovementRight;
	
	/**
	 * A reference to the map file, to gain access to its fields
	 */
	private TiledMap mapReference;
	
	/**
	 * Default constructor
	 */
	public NPC() {
		super(INITIAL_POSITION, INITIAL_POSITION);
		
		this.randomMovementEnabled = true;
	}
	
	public NPC(float xpos, float ypos, String spriteName, TiledMap map) {
		super(spriteName, xpos, ypos);
		this.positionOnMapX = xpos + ZERO_MAP_POSITION_X;
		this.positionOnMapY = ypos + ZERO_MAP_POSITION_Y;
		
		this.lockMovementDown = false;
		this.lockMovementUp = false;
		this.lockMovementLeft = false;
		this.lockMovementRight = false;
		
		this.awareOfPlayer = false;
		
		this.mapReference = map;
		
		this.randomMovementEnabled = true;
	}
	
	public NPC(float xpos, float ypos, String spriteName, TiledMap map, int hp, int sp, int ar, int at) {
		super(spriteName, xpos, ypos, hp, sp, ar, at);
		
		this.positionOnMapX = xpos + ZERO_MAP_POSITION_X;
		this.positionOnMapY = ypos + ZERO_MAP_POSITION_Y;
		
		this.lockMovementDown = false;
		this.lockMovementUp = false;
		this.lockMovementLeft = false;
		this.lockMovementRight = false;
		
		this.awareOfPlayer = false;
		
		this.mapReference = map;
		
		this.randomMovementEnabled = true;
	}
	
	public NPC(float xpos, float ypos) {
		super(xpos, ypos);
		
		this.randomMovementEnabled = true;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

	
	/**
	 * Turns off all movement locks
	 */
	protected void resetLocks() {
		this.setLockMovementDown(false);
		this.setLockMovementUp(false);
		this.setLockMovementLeft(false);
		this.setLockMovementRight(false);
	}
	
	/**
	 * Turns off all blocking
	 */
	protected void resetBlocks() {
		this.setBlockEast(false);
		this.setBlockWest(false);
		this.setBlockSouth(false);
		this.setBlockNorth(false);
	}
	
	/**
	 * Engages the unit's random movement algorithm, used for villagers (enemies have altered movement methods)<br/>
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
	public void moveAtRandom(float baseSpeed) {
		//System.out.println(this.getBlockNorth() + "n and s" + this.getBlockSouth() + " with TL: " + this.getTileAboveLeft() + ", and TR: " + this.getTileAboveRight());
		
		this.checkAwareOfPlayer();
		
		//System.out.println(this.getDistanceTraveledY());
		
		if(this.getRandomMovementEnabled()) {
			
			this.resetBlocks();
			this.canMove();
			double randyNum = Math.random();
			baseSpeed *= 1.1;
			
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
		
	}
	
	/**
   * Checks left/right/above/below the player and freezes the camera in those directio-<br/>
   * ns if the player is blocked by certain terrain<br/><br/>
   * 
   * <strong>Precondition:</strong> <code>speed</code> is a valid integer<br/>
   * <strong>Postcondition:</strong> The player is frozen in zero or more directions<br/><br/>
   */
	public void canMove() {
		
  	
	  	//freeze north
	  	if(mapReference.getTileProperty(this.getTileAboveLeft(), "block", "0").equals("1") ||
	  		mapReference.getTileProperty(this.getTileAboveRight(), "block", "0").equals("1")) 
	  	{
	  		this.setBlockNorth(true);
	  	}
	  	
	  	//freeze south
	  	if(mapReference.getTileProperty(this.getTileBelowRight(), "block", "0").equals("1") ||
	  		mapReference.getTileProperty(this.getTileBelowLeft(), "block", "0").equals("1")	) 
	  	{
	  		
	  		this.setBlockSouth(true);
	  	}
	  	
	  	//freeze west
	  	if(mapReference.getTileProperty(this.getTileLeftTop(), "block", "0").equals("1") ||
	  		mapReference.getTileProperty(this.getTileLeftBottom(), "block", "0").equals("1")) 
	  	{
	  		this.setBlockWest(true);
	  	}
	  	
	  	//freeze east
	  	if(mapReference.getTileProperty(this.getTileRightTop(), "block", "0").equals("1") ||
	  		mapReference.getTileProperty(this.getTileRightBottom(), "block", "0").equals("1")) 
	  	{
	  		this.setBlockEast(true);
	  	}
  	
	}
	
	/**
     * Gets the tile ID at the aboveLeft detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> land is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
	public int getTileAboveLeft() {
    	return mapReference.getTileId(
    			(int)((this.getPositionOnMapX() + MOVEMENT_THRESHOLD + PADDING)/mapReference.getTileWidth()), 
    			(int)((this.getPositionOnMapY() + MOVEMENT_THRESHOLD)/mapReference.getTileHeight()), 
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
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + mapReference.getTileWidth() - MOVEMENT_THRESHOLD - PADDING)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + MOVEMENT_THRESHOLD)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the leftTop detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileLeftTop() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + MOVEMENT_THRESHOLD)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + MOVEMENT_THRESHOLD + PADDING)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the leftBottom detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileLeftBottom() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + MOVEMENT_THRESHOLD)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + mapReference.getTileHeight() - MOVEMENT_THRESHOLD - PADDING)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the rightTop detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileRightTop() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + mapReference.getTileWidth() - MOVEMENT_THRESHOLD)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + MOVEMENT_THRESHOLD + PADDING)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the rightBottom detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileRightBottom() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + mapReference.getTileWidth() - MOVEMENT_THRESHOLD)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + mapReference.getTileHeight() - MOVEMENT_THRESHOLD - PADDING)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the belowRight detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileBelowRight() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + mapReference.getTileWidth() - MOVEMENT_THRESHOLD - PADDING)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + mapReference.getTileHeight() - MOVEMENT_THRESHOLD/2)/mapReference.getTileWidth()), 
				0);
    }
    
    /**
     * Gets the tile ID at the belowLeft detection point<br/><br/>
     * 
     * <strong>Precondition:</strong> mapReference is a valid World object<br/>
     * <strong>Postcondition:</strong> The tile id is returned<br/><br/>
     * 
     * @return The tileID of the tile
     */
    public int getTileBelowLeft() {
    	
    	return mapReference.getTileId( 
				(int)((this.getPositionOnMapX() + MOVEMENT_THRESHOLD + PADDING)/mapReference.getTileWidth()), 
				(int)((this.getPositionOnMapY() + mapReference.getTileHeight() - MOVEMENT_THRESHOLD/2)/mapReference.getTileWidth()), 
				0);
    }
	
    /**
     * Checks if the player is within the field of view
     */
	public void checkAwareOfPlayer() {
		if(this.getXPosition() > SCREEN_RESOLUTION_X/2 - FOV && this.getXPosition() < SCREEN_RESOLUTION_X/2 + FOV 
				&& this.getYPosition() > SCREEN_RESOLUTION_Y/2 - FOV && this.getYPosition() < SCREEN_RESOLUTION_Y/2 + FOV) {
			
			this.setAwareOfPlayer(true);
			this.setRandomMovementEnabled(false);
			
		}
		else {
			this.setAwareOfPlayer(false);
			this.setRandomMovementEnabled(true);
		}
	}
	
	/**
	 * Alters the X position of a player object.
	 * 
	 * @param amount An integer value specifying how much to move the object (-1 for one pixel left, +1 for one pixel right, 0 for no movement)
	 * @return true if the operation completed successfully, false otherwise
	 */
	public void moveX(float amount) {
		this.resetBlocks();
		this.canMove();
		if(amount < 0) {
			this.setXPosition(this.getXPosition() + amount);
			this.setPositionOnMapX(this.getPositionOnMapX() + amount);
			this.increaseDistanceTraveled(amount, 0);
		}
		else if(amount > 0) {
			this.setXPosition(this.getXPosition() + amount);
			this.setPositionOnMapX(this.getPositionOnMapX() + amount);
			this.increaseDistanceTraveled(amount, 0);
		}
		
		
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
	 * Alters the Y position of the player object 
	 * 
	 * @param amount An integer value specifying how much to move the object (-1 for one pixel up, +1 for one pixel down, 0 for no movement)
	 * @return true if the operation completed successfully, false otherwise
	 */
	public void moveY(float amount) {
		this.resetBlocks();
		this.canMove();
		if(amount < 0) {
			this.setYPosition(this.getYPosition() + amount);
			this.setPositionOnMapY(this.getPositionOnMapY() + amount);
			this.increaseDistanceTraveled(0, amount);
		}
		else if(amount > 0) {
			this.setYPosition(this.getYPosition() + amount);
			this.setPositionOnMapY(this.getPositionOnMapY() + amount);
			this.increaseDistanceTraveled(0, amount);
		}
	}

	
/////////////////////////////////////////////////////////////////////////////////////////////////
	public void setRandomMovementEnabled(Boolean randomMovementEnabled) {
		this.randomMovementEnabled = randomMovementEnabled;
	}

	public Boolean getRandomMovementEnabled() {
		return randomMovementEnabled;
	}

	public void setAwareOfPlayer(Boolean awareOfPlayer) {
		this.awareOfPlayer = awareOfPlayer;
	}

	public Boolean getAwareOfPlayer() {
		return awareOfPlayer;
	}
	
	public Boolean getLockMovementUp() {
		return this.lockMovementUp;
	}

	public void setLockMovementUp(Boolean locMovementUp) {
		this.lockMovementUp = locMovementUp;
	}

	public Boolean getLockMovementDown() {
		return lockMovementDown;
	}

	public void setLockMovementDown(Boolean lockMovementDown) {
		this.lockMovementDown = lockMovementDown;
	}

	public Boolean getLockMovementLeft() {
		return lockMovementLeft;
	}

	public void setLockMovementLeft(Boolean lockMovementLeft) {
		this.lockMovementLeft = lockMovementLeft;
	}

	public Boolean getLockMovementRight() {
		return lockMovementRight;
	}

	public void setLockMovementRight(Boolean lockMovementRight) {
		this.lockMovementRight = lockMovementRight;
	}

	public float getPositionOnMapX() {
		return positionOnMapX;
	}

	public void setPositionOnMapX(float positionOnMapX) {
		this.positionOnMapX = positionOnMapX;
	}

	public float getPositionOnMapY() {
		return positionOnMapY;
	}

	public void setPositionOnMapY(float positionOnMapY) {
		this.positionOnMapY = positionOnMapY;
	}


}
