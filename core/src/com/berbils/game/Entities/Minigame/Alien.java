package com.berbils.game.Entities.Minigame;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.MiniGameScreen;

/**
 * NEW CLASS
 * Creates the alien entity used within the minigame
 */
public class Alien extends BoxGameEntity
	{
	/**
	 * The current health of the alien, once this reaches zero the
	 * fire engine "dies" and the onDeath() method is called
	 */
	public int currentHealth;

	/**The maximum health the alien can have, this is what it starts off
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
	 * The original scale of the alien
	 */
	public Vector2 originalScale;

	/**
	 * A boolean used to check whether the alien is "alive" to
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
	public Alien(
		MiniGameScreen screen,
		Vector2 dimensions,
		float speed,
		int maxHealth,
		String textureFilePath)
		{
		super(
			screen,
			dimensions,
			textureFilePath,
			false,
			2
		);
		super.setFixtureCategory(Kroy.CAT_ENEMY, Kroy.MASK_ENEMY);
		super.setBodyDefAngularDampening(10);
		super.setBodyDefLinearDampening(10);

		this.originalScale = dimensions;
		defineStats(speed, maxHealth);
		}

	/***
	 * NEW METHOD @author Archie Godfrey
	 * Method for changing the size of an entity
	 */
	public void scaleEntity(float scale) {
		Vector2 newScale = new Vector2(originalScale.x * scale, originalScale.y * scale);
		this.setScale(newScale);
	}

	/**
	 * A method for initialising most of the alien variables
	 * Is here to make the code more readable and separate the initialisation
	 * of some variables from being directly in the constructor
	 *
	 * @param speed 	The speed of the alien, how fast it
	 * 	 *          	will move
	 *
	 * @param maxHealth The max health for the alien instance
	 *                  and represents the maximum amount of
	 *                  damage the alien can take before death
	 */
	private void defineStats(float speed, int maxHealth)
		{
		this.speed = speed;
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.isAlive = true;
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

	/**Get the x coordinate of the alien**/
	private float getX(){return this.position.x;}

	/**Get the y coordinate of the alien**/
	private float getY(){return this.position.y;}

	/**
	 * Method for moving the alien towards a specified point
	 *
	 * @param targetVector the position ot move the alien to
	 */
	public void moveTowards(Vector2 targetVector)
		{
			// Get the centre of the alien
			Vector2 alienCentre = new Vector2(this.getX() + this.getSizeDims().x / 2,this.getY() + this.getSizeDims().y / 2).scl(1 / Kroy.PPM); 

			// Work out the vector between them and scale by speed
			Vector2 trajectory = targetVector.sub(alienCentre);
			trajectory.nor().scl(this.speed);

			// Apply force to the alien
			//this.getBody().setTransform(this.getBody().getPosition(), trajectory.angle());
			this.getBody().applyForceToCenter(trajectory, true);
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

	public void onDeath()
		{
		this.spriteHandler.destroySpriteAndBody(this.entityFixture);
		}

	}
