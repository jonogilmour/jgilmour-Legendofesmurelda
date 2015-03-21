package itemlib;

import org.newdawn.slick.*;

import characterlib.Player;

import static config.GlobalConfiguration.*;

/**
 * Items that reside in the world and the player's inventory
 * 
 * @author Jono
 *
 */
public class Item {
	
	private int ID;
	private String name;
	private Image sprite;
	
	private int attackLevel;
	private int armorLevel;
	private int speedLevel;
	private int healthLevel;
	private int numberOfTimesUsed;
	
	private float xPosition;
	private float yPosition;
	
	private Boolean held;
	
	
	public Item() {
		this.ID = -1;
		this.setName("default");
		this.attackLevel =
			this.armorLevel =
				this.speedLevel = 
					this.healthLevel =
						this.numberOfTimesUsed = 0;
		
		try {
			this.setSprite(new Image("base/items/default.png"));
		} 
		
		catch (SlickException e) {
			System.out.println("Error in Item constructor for item with ID" 
					+ this.ID + ": Invalid sprite file path");
			e.printStackTrace();
		}
	}
	
	public Item(float xpos, float ypos, String nm, String spriteImage, int at, int sp, int ar, int hp, int id) {
		System.out.println("THIS: "+sp);
		this.attackLevel = at;
		this.armorLevel = ar;
		this.speedLevel = sp;
		this.healthLevel = hp;
		this.numberOfTimesUsed = 0;
		this.ID = id;
		this.setName(nm);
		this.held = false;
		
		this.xPosition = xpos;
		this.yPosition = ypos;
		
		try {
			this.sprite = new Image(ITEM_FOLDER + spriteImage);
		} 
		
		catch (SlickException e) {
			System.out.println("Error in Item constructor for item with ID" 
					+ this.ID + ": Invalid sprite file path");
			e.printStackTrace();
		}
	}
	
	/**
	 * Renders the item at the current location
	 */
	public void render() {
		if(!this.getHeld()) this.getSprite().draw(this.getXPosition(), this.getYPosition());
	}
	
	/**
	 * Causes the item to be placed into the player's inventory, removing it from the world as well
	 * @param target
	 */
	public void equip(Player target) {
		if(!this.getHeld()) {
			target.setAttack(target.getAttack() + this.getAttackLevel());
			target.setArmor(target.getArmor() + this.getArmorLevel());
			target.setMaxHealth(target.getMaxHealth() + this.getHealthLevel());
			target.setMaxSpeed(target.getMaxSpeed() - this.getSpeedLevel());
			this.setHeld(true);
			
			if(this.getID() == SWORD) {
				target.setSwordNotHeld(false);
			}
			else if(this.getID() == ELIXIR) {
				target.setElixirNotHeld(false);
			}
			else if(this.getID() == BOOK) {
				target.setBookNotHeld(false);
			}
			else if(this.getID() == AMULET) {
				target.setAmuletNotHeld(false);
			}
		}
	}
	
	//////////////////////////////////////////////////

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}

	public int getAttackLevel() {
		return attackLevel;
	}

	public void setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
	}

	public int getArmorLevel() {
		return armorLevel;
	}

	public void setArmorLevel(int armorLevel) {
		this.armorLevel = armorLevel;
	}

	public int getSpeedLevel() {
		return speedLevel;
	}

	public void setSpeedLevel(int speedLevel) {
		this.speedLevel = speedLevel;
	}

	public int getHealthLevel() {
		return healthLevel;
	}

	public void setHealthLevel(int healthLevel) {
		this.healthLevel = healthLevel;
	}

	public int getNumberOfTimesUsed() {
		return numberOfTimesUsed;
	}

	public void setNumberOfTimesUsed(int numberOfTimesUsed) {
		this.numberOfTimesUsed = numberOfTimesUsed;
	}

	public void setXPosition(float xPosition) {
		this.xPosition = xPosition;
	}

	public float getXPosition() {
		return xPosition;
	}

	public void setYPosition(float yPosition) {
		this.yPosition = yPosition;
	}

	public float getYPosition() {
		return yPosition;
	}

	public void setHeld(Boolean held) {
		this.held = held;
	}

	public Boolean getHeld() {
		return held;
	}
	
}
