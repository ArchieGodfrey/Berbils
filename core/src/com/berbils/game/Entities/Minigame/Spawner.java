package com.berbils.game.Entities.Minigame;

import com.badlogic.gdx.math.MathUtils;
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
	public float speed;

	/**
	 * A constant used to determine how many aliens the spawner instance will
	 * create.
	 */
	public int alienCount;


	public Vector2 trajectory;
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
	 * @param alienCount		The max number of aliens the spawner will spawn.
	 *
	 * @param textureFilePath  The file path to the sprite texture
	 *                         Note - If passd as null no sprite will be
	 *                         created
	 */
	public Spawner(
		MiniGameScreen screen,
		Vector2 dimensions,
		float speed,
		int alienCount,
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

		this.defineStats(speed, alienCount);
		}


	/**
	 * A method for initialising most of the alien variables
	 * Is here to make the code more readable and separate the initialisation
	 * of some variables from being directly in the constructor
	 *
	 * @param speed 	The speed of the alien, how fast it
	 * 	 *          	will move
	 *
	 * @param alienCount The max number of aliens the spawner will spawn.
	 */
	private void defineStats(float speed, int alienCount)
		{
		this.speed = speed;
		this.alienCount = alienCount;
		this.trajectory = new Vector2(1, 0);
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

	public void move(){
		float x = this.getX();
		if(x > 19){
			trajectory.x = -1;
		}
		else{
			if(x < 1){
				trajectory.x = 1;
			}
		}
		trajectory.nor().scl(this.speed);

		this.getBody().applyForceToCenter(trajectory, true);
	}
	}
