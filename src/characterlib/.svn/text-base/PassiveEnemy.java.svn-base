package characterlib;

import static config.GlobalConfiguration.*;

import org.newdawn.slick.tiled.TiledMap;
/**
 * Passive enemies that run away when attacked
 * 
 * @author Jono
 *
 */
public class PassiveEnemy extends Enemy {

	private Boolean avoidPlayer;
	private float restTimer;
	private Boolean atRest;
	
	public PassiveEnemy() {
		super();
		
		this.restTimer = 0;
		this.setAvoidPlayer(false);
		this.atRest = false;
	}
	
	public PassiveEnemy(float xpos, float ypos, String spriteFile, TiledMap map, int hp, int sp, int ar, int at) {
		super(xpos, ypos, spriteFile, map, hp, sp, ar, at);
		
		this.restTimer = 0;
		this.setAvoidPlayer(false);
		this.atRest = false;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Checks if the player is within the field of view or in attacking range, and runs away in<br/>
	 * certain situations
	 * @param baseSpeed
	 */
	public void checkAwareOfPlayer(float baseSpeed) {
		if(this.getXPosition() >= PLAYER_START_POS_X - ATTACK_RANGE && this.getXPosition() <= PLAYER_START_POS_X + ATTACK_RANGE 
				&& this.getYPosition() >= PLAYER_START_POS_Y - ATTACK_RANGE && this.getYPosition() <= PLAYER_START_POS_Y + ATTACK_RANGE) {

			this.setAtRest(false);
			this.setAvoidPlayer(true);
			this.setAttackPlayer(true);
			this.setRandomMovementEnabled(false);
			this.setRestTimer(REST_TIMER);
			System.out.println("RUN!");
			this.runAway(baseSpeed);
		}
		else if(this.getAvoidPlayer() && this.getXPosition() >= PLAYER_START_POS_X - FOV_PASSIVE && this.getXPosition() <= PLAYER_START_POS_X + FOV_PASSIVE 
				&& this.getYPosition() >= PLAYER_START_POS_Y - FOV_PASSIVE && this.getYPosition() <= PLAYER_START_POS_Y + FOV_PASSIVE) {

			this.setAtRest(false);
			this.setAvoidPlayer(true);
			this.setAttackPlayer(false);
			this.setRandomMovementEnabled(false);
			this.setRestTimer(REST_TIMER);
			System.out.println("RUN AWAYY!");
			this.runAway(baseSpeed);
		}
		else {
			if(this.getRestTimer() <= 0) {
				this.setRandomMovementEnabled(true);
				this.setAvoidPlayer(false);
				this.moveAtRandom(baseSpeed);
				this.setAtRest(false);
				System.out.println("MOVINGATRANDOM");
			}
			else {
				System.out.println("WAITING...");
				this.setAtRest(true);
				this.setRestTimer(this.getRestTimer() - baseSpeed);
			}

		}
			
		
	}
	
	/**
	 * Initiates the evasion algorithm, which is similar to the enemy chasing algorithm<br/>
	 * but in the opposite directions.
	 * 
	 * @param baseSpeed
	 */
	public void runAway(float baseSpeed) {
		
		this.resetBlocks();
		this.canMove();
		
		if(this.getAvoidPlayer() || this.getAttackPlayer()) {
			float distX = (PLAYER_START_POS_X - this.getXPosition());
			
			float distY = (PLAYER_START_POS_Y - this.getYPosition());
			
			float distTotal = (float)Math.sqrt(distX*distX + distY*distY);
			
			float dX = (distX/distTotal) * -baseSpeed;
			float dY = (distY/distTotal) * -baseSpeed;
			
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

/////////////////////////////////////////////////////////////////////////////////
	public void setAvoidPlayer(Boolean avoidPlayer) {
		this.avoidPlayer = avoidPlayer;
	}

	public Boolean getAvoidPlayer() {
		return avoidPlayer;
	}

	public void setRestTimer(float restTimer) {
		this.restTimer = restTimer;
	}

	public float getRestTimer() {
		return restTimer;
	}

	public void setAtRest(Boolean atRest) {
		this.atRest = atRest;
	}

	public Boolean getAtRest() {
		return atRest;
	}
}
