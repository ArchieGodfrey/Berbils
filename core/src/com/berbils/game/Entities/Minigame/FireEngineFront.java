package com.berbils.game.Entities.Minigame;

import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

/**
 * A class used to generate Box Entitys and a sprite with in built resources
 * used as limitations such as ammo for firing projectiles or health
 * <P>Contains all methods required to be used by a player </P>
 *
 */
public class FireEngineFront extends BoxGameEntity
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
	public FireEngineFront(
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
	 * Method called to represent fire engine death, updates scores, the
	 * current screen shown and what state the game will be in after the
	 * screens shown.
	 */
	public void onDeath()
		{
		if (this.isAlive) {
			this.isAlive = false;
			Kroy game = this.screen.getGame();
			this.screen.updatePlayerScore(-200);
			this.screen.getGame().setScreen(game.gameScreen);
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
		System.out.println("fire" + this.entityBody.getUserData());
        super.createSprite();
        // Rotate to face aliens
        this.getBody().setAngularVelocity(16);
		}

	/**
	 * Resets the current health and current water atributes to their max values
	 */
	public void reset()
		{
		this.currentHealth = this.maxHealth;
		this.currentWater = this.maxWater;
		}


	}
