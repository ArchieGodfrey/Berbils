package com.berbils.game.Entities.FireEngines;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;
import com.berbils.game.Tools.Pathfinding;

/**
 * A class used to generate Box Entitys and a sprite with in built resources
 * used as limitations such as ammo for firing projectiles or health
 * <P>Contains all methods required to be used by a player </P>
 *
 */
public class FireEngine extends BoxGameEntity
	{

	/** The current water left in the fire engine, once this reaches zero it
	 * can no longer fire */
	public int currentWater;

	/** The maximum water the fire engine holds, this is what it starts off
	 * with when it is spawned or reset
	 */
	private int maxWater;

	/**
	 * The current health of the fire engine, once this reaches zero the
	 * fire engine "dies" and the onDeath() method is called
	 */
	public int currentHealth;

	/**The maximum health the fire engine can have, this is what it starts off
	 * with when it is spawned or reset
	 *
	 */
	private int maxHealth;

	/**
	 * A constant speed multiplier used by the {@link com.berbils.game.Tools.InputManager} to change
	 * the amount of force applied and therefore the speed
	 */
	public float speed;

	/**
	 * A boolean value used in conjuction with the @{@link com.berbils.game.Entities.FireStation.FireStation} class to
	 * determine whether the player should interact with the Fire Station
	 */
	public boolean leftFireStation;

	/**
	 * The {@link Weapon} class instance assigned to the fire engine
	 * allowing it to the @fire method and attack
	 */
	private Weapon weapon;

	/**
	 * A boolean used to check whether the fire engine is "alive" to
	 * determine whether it should be despawned and destroyed.Is also
	 * required to prevent multiple accidental onDeath() calls
	 */
	private boolean isAlive;

	/**
	 * The start and end positions for the fire engine
	 */
    private Vector2 start;
	private Vector2 goal;

	/**
	 * Whether the fireengine moves from goal-to-start or start-to-goal
	 */
	private boolean swapDirection;

	/**
	 * The instance to generate paths
	 */
	private Pathfinding pathfinder;
	
	/**
	 * The path it will move along
	 */
    private ArrayList<Vector2> path;

	/**
	 * This constructor assigns required variables and sets up the weapon class
	 * instance for use.It only creates a fixture and body definition, no
	 * sprite, body or fixture generation.
	 *
	 * @param screen 			The Screen the entity object is located and will be
	 *               			created
	 *
	 * @param dimensions 		The size dimensions of the entity in meters
	 *
	 * @param weapon 			The weapon instance that the fire engine will use to
	 *               			fire projectiles
	 *
	 * @param maxWater 			The max "ammo" resource for the weapon , it is
	 *                      	consumed each time the fire engine instance
	 *                      	fires and is the value the water resource is
	 *                      	reset to
	 *
	 * @param speed 			The speed of the fire engine, how fast it
	 *                          will move
	 *
	 * @param maxHealth			The max health for the fire engine instance
	 *                          and represents the maximum amount of
	 *                          damage the fire engine can take before death
	 *
	 * @param textureFilePath  The file path to the sprite texture
	 *                         Note - If passd as null no sprite will be
	 *                         created
	 */
	public FireEngine(
		PlayScreen screen,
		Vector2 dimensions,
		Weapon weapon,
		int maxWater,
		float speed,
		int maxHealth,
		String textureFilePath)
		{
		super(
			screen,
			dimensions,
			textureFilePath,
			false,
			2 // Sprite Layer is two as needs to be drwan on top of both
						// towers and fire station
		);
		super.setFixtureCategory(Kroy.CAT_FRIENDLY, Kroy.MASK_FRIENDLY);
		super.setBodyDefAngularDampening(10);
		super.setBodyDefLinearDampening(10);

		this.weapon = weapon;
		this.weapon.setFixtureCategory(Kroy.CAT_PROJECTILE_FRIENDLY,
									   Kroy.MASK_FRIENDLY_PROJECTILE);

		defineStats(maxWater, speed, maxHealth);

		// Set default texture
		}

	/**
	 * A method for initialising most of the fire engine variables
	 * Is here to make the code more readable and separate the initialisation
	 * of some variables from being directly in the constructor
	 *
	 * @param maxWater 	The max "ammo" resource for the weapon , it is
	 *                 	consumed each time the fire engine instance
	 * 	               	fires and is the value the water resource is
	 * 	               	reset to
	 *
	 * @param speed 	The speed of the fire engine, how fast it
	 * 	 *          	will move
	 *
	 * @param maxHealth The max health for the fire engine instance
	 *                  and represents the maximum amount of
	 *                  damage the fire engine can take before death
	 */
	private void defineStats(int maxWater,float speed, int maxHealth)
		{
		this.currentWater = maxWater;
		this.maxWater = maxWater;
		this.speed = speed;
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.leftFireStation = true;
		this.isAlive = true;
		}

	/**
	 *Calls the fire method of the Weapon attached to the fire engine,
	 * spawning projectiles in the direction of the co-ords passed.Also
	 * updates water resource to mimic consumption.
	 *
	 * @param targetPos The vector co-ordinates of the target in meters
	 */
	public void fire(Vector2 targetPos)
		{
		this.weapon.attack(this.entityBody.getPosition(), targetPos);
		currentWater -= 1;
		}

	/**
	 * Method for reducing the current health of the fire engine instance and
	 * checking whether the health reaches zero
	 *
	 * @param damageTaken the amount the fire engine health should be reduced by
	 */
	public void takeDamage(int damageTaken)
		{
		this.currentHealth -= damageTaken;
		this.screen.updatePlayerScore(-damageTaken);
		if (this.currentHealth <= 0) {
			this.onDeath();
		}
		}

	/**
	 * UPDATED Method @author Archie Godfrey
	 * Method is now public so it can be overriden in FireEngineFront
	 * 
	 * Method called to represent fire engine death, updates scores, the
	 * current screen shown and what state the game will be in after the
	 * screens shown.
	 */
	public void onDeath()
		{
		if (this.isAlive) { //
			this.isAlive = false;
			/* Regardless of rest of the game the screen should acknowledge
			* fire engine destruction and display fire-engine-destroyed screen
			* The button for selecting that fire engine instance should also
			* be removed
			* */
			Kroy game = this.screen.getGame();
			this.screen.fireEngineDestroyed();

			//NEW line @author Matteo Barberis
			this.screen.removeOptionFromMenu();

			if (this.screen.allFireEnginesDestroyed()) {
				this.screen.getGame().setScreen(game.gameOverScreen);
				game.gameOverScreen.setTimer(2, game.mainMenu);
				game.createAllScreens();
			}
			else {
				game.fireEngineDestroyedScreen.setTimer(2,
														game.gameScreen);
				this.spriteHandler.destroySpriteAndBody(this.entityFixture);
				this.screen.updatePlayerScore(-200);
				this.screen.setSelectionOverlayVisibility(true);
				game.setScreen(game.fireEngineDestroyedScreen);
			}
		}
		}

	/**
	 * This method sets the body position, then creates the body, fixture and
	 * creates an attached sprite
	 *
	 * @param spawnPos The position in meters where the fire engine should be
	 *                   created with the center of the fire engine being at
	 *                   this position.
	 */
	public void spawn(Vector2 spawnPos)
		{
		super.setSpawnPosition(spawnPos);
		super.createBodyCopy();
		super.createFixtureCopy();
		super.setUserData(this);
		super.createSprite();
		}


	/**
	 * NEW Method @author Archie Godfrey
	 * Get the x coordinate of the patrol**/
	private float getX(){return this.getBody().getPosition().x;}

	/**
	 * NEW Method @author Archie Godfrey
	 * Get the y coordinate of the patrol**/
	private float getY(){return this.getBody().getPosition().y;}

	/**
	 * NEW Method @author Archie Godfrey
	 * Method for moving the patrol towards a specified point
	 *
	 * @param targetVector the position ot move the patrol to
	 */
	public void moveTowards(Vector2 targetVector)
		{
			// Get the centre of the Fire Engine
			Vector2 fireEngineCentre = new Vector2(this.getX() + this.getSizeDims().x / 2,this.getY() + this.getSizeDims().y / 2);

			// Work out the vector between them and scale by speed
			Vector2 trajectory = targetVector.cpy().sub(fireEngineCentre);
			trajectory.nor().scl(this.speed);

			// Apply force to the Fire Engine
			this.getBody().applyForceToCenter(trajectory, true);
		}
        

        /**
		 * NEW Method @author Archie Godfrey
         * Called every frame, move the Fire Engine along it's path
         */
        public void update() {
			// If there is a path, follow it
			if (this.path != null && this.path.size() > 1) {
				Vector2 nextPosition = this.path.get(0);
				
				// Positions on the path are integer floats
				float roundedX = (float) Math.ceil(this.getX());
				float roundedY = (float) Math.ceil(this.getY());

				// Difference between the next point and current position
				Vector2 difference = nextPosition.cpy().sub(new Vector2(roundedX, roundedY));
				// If not at next position, move towards it
				if (difference.len2() > 1) {
					moveTowards(nextPosition);
				} else {
					// Otherwise remove it from the path
					this.path.remove(0);
				}
			} else if (this.path != null) {
				// Swap direction when at the end
				if (swapDirection) {
					this.path = this.pathfinder.find(goal, start);
					this.swapDirection = false;
				} else {
					this.path = this.pathfinder.find(start, goal);
					this.swapDirection = true;
				}
			}
		}
	
	/**
	 * NEW Method @author Archie Godfrey
	 * 
	 * Set the path the fire engine will follow
	 * 
	 * @param start 	The start of the path to follow
	 * @param goal 		The end of the path to follow
	 */
	public void createPath(Vector2 start,Vector2 goal) {
		super.setFixtureCategory(Kroy.CAT_FRIENDLY, Kroy.MASK_UFO);
		this.start = start;
		this.goal = goal;
		this.pathfinder = new Pathfinding(this.screen.getMapLoader().map);
		this.path = this.pathfinder.find(start, goal);
	}

	/**
	 * UPDATED Method @author Archie Godfrey
	 * Resets the current health and current water atributes to their max values
	 * 
	 * @param resetStats	A boolean for whether the health and water of the
	 * 						fire engine should be reset to full
	 */
	public void reset(boolean resetStats)
		{
		if (resetStats) {
			this.currentHealth = this.maxHealth;
			this.currentWater = this.maxWater;
		}
		this.leftFireStation = true;
		this.screen.setSelectionOverlayVisibility(false);
		}
	
	/**
	 * NEW Method @author Archie Godfrey
	 * 
	 * Set whether the fire engine is alive or not
	 * 
	 * @param alive True if alive, false if dead
	 */
	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}

	/**
	 * NEW Method @author Archie Godfrey
	 * 
	 * Get whether the fire engine is alive or not
	 * 
	 * @return whether the fire engine is alive or not
	 */
	public boolean getAlive() {
		return this.isAlive;
	}
}
