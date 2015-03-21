package characterlib;

import org.newdawn.slick.tiled.TiledMap;

/**
 * NPC friendly characters, inclduing elvira and other quest characters
 * 
 * @author Jono
 *
 */
public class Villager extends NPC {
 /**
  * True if the villager is elvira
  */
	private Boolean isElvira;
	
	public Villager() {
		super();
		this.setIsElvira(false);
	}
	
	public Villager(float xpos, float ypos, String spriteFile, TiledMap map) {
		super(xpos, ypos, spriteFile, map);
		this.isElvira = false;
	}
	
	public Villager(float xpos, float ypos, String spriteFile, TiledMap map, Boolean elvira) {
		super(xpos, ypos, spriteFile, map);
		this.isElvira = elvira;
	}

	public void setIsElvira(Boolean isElvira) {
		this.isElvira = isElvira;
	}

	public Boolean getIsElvira() {
		return isElvira;
	}
	
	
}
