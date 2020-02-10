package com.berbils.game.Entities.Minigame;

import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.MiniGameScreen;

public class Spawner extends BoxGameEntity
	{

	/**
	 * A constant speed multiplier used by the {@link com.berbils.game.Tools.InputManager} to change
	 * the amount of force applied and therefore the speed
	 */
	private float speed;

	/**
	 * A constant used to determine the width of the screen
	 */
	private MiniGameScreen screen;

	/** Direction to move the spawner in */ 
	private Vector2 trajectory;

	/**
	 * This constructor assigns required variables and
	 * instance for use.It only creates a fixture and body definition, no
	 * sprite, body or fixture generation.
	 *
	 * @param screen 			The Screen the entity object is located and will be
	 *               			created
	 *
	 * @param dimensions 		The size dimensions of the entity in meters
	 *
	 * @param speed 			The speed of the spawner, how fast the ufo
	 *                          will move
	 *
	 * @param textureFilePath  The file path to the sprite texture
	 *                         Note - If passd as null no sprite will be
	 *                         created
	 */
	public Spawner(
		MiniGameScreen screen,
		Vector2 dimensions,
		float speed,
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

		this.screen = screen;
		this.defineStats(speed);
		}


	/**
	 * A method for initialising most of the alien variables
	 * Is here to make the code more readable and separate the initialisation
	 * of some variables from being directly in the constructor
	 *
	 * @param speed 	The speed of the alien, how fast it
	 * 	 *          	will move
	 */
	private void defineStats(float speed)
		{
		this.speed = speed;
		this.trajectory = new Vector2(speed, 0);
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
		this.setPosition(spawnPos);
		}

	/**Get the x coordinate of the spawner**/
	private float getX(){return this.getBody().getPosition().x;}

	/**
	 * Move the spawner from one side of the screen to another
	 * 
	 * @param allowMovement A boolean to determine if the spawner can move or not
	 */
	public void move(boolean allowMovement) {
		// Move right until it reaches the right screen edge
		if (this.getX() > (this.screen.getCamera().viewportWidth / Kroy.PPM) - this.getSizeDims().x * 4) {
			this.trajectory = new Vector2(-speed, 0);
		} else {
			// Move left until it reaches the left screen edge
			if (this.getX() < this.getSizeDims().x * 2) {
				this.trajectory = new Vector2(speed, 0);
			}
		}

		// Only move if not beaming
		if (allowMovement) {
			this.getBody().applyForceToCenter(this.trajectory, true);
		}
	}

	/**
	 * Stop the spawners movement then randomise it's new direction
	 */
	public void randomiseTrajectory() {
		this.getBody().applyForceToCenter(this.getTrajectory().rotate(180).scl(8), true);
		this.trajectory.rotate(Math.random() > 0.5 ? 180 : 0);
	}


	/**
	 * Get the current trajectory of the spawer
	 * 
	 * @return the current trajectory of the spawner
	 */
	public Vector2 getTrajectory() {
		return this.trajectory.cpy();
	}
}
