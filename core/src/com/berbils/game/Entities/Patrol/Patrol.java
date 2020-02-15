package com.berbils.game.Entities.Patrol;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;
import com.berbils.game.Tools.Pathfinding;

/**
 * A class used to generate the patrol entity. It will follow
 * a path and if it collides with a fire engine it will
 * start the minigame
 *
 */
public class Patrol extends BoxGameEntity
	{
	/**
	 * The start and end positions for the patrol
	 */
    private Vector2 start;
    private Vector2 goal;
	
	/**
	 * The path it will move along
	 */
    private ArrayList<Vector2> path;

	/**
	 * A constant speed multiplier
	 */
	public float speed;

	/**
	 * This constructor assigns required variables for use.
	 * It only creates a fixture and body definition, no
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
	 * @param start				The position the patrol will start at
	 * 
	 * @param goal				The position the patrol will move to
	 *
	 * @param textureFilePath  The file path to the sprite texture
	 *                         Note - If passd as null no sprite will be
	 *                         created
	 */
	public Patrol(
		PlayScreen screen,
		Vector2 dimensions,
        float speed,
        Vector2 start,
        Vector2 goal,
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
		super.setFixtureCategory(Kroy.CAT_ENEMY, Kroy.MASK_ENEMY);
		super.setBodyDefAngularDampening(10);
		super.setBodyDefLinearDampening(10);

		// Create the path the patrol will follow
        Pathfinding pathfinder = new Pathfinding(this.screen.getMapLoader().map);
        this.path = pathfinder.find(start, goal);
        this.start = start;
        this.goal = goal;
		this.speed = speed;
        }

    /**Get the x coordinate of the patrol**/
	private float getX(){return this.getBody().getPosition().x;}

	/**Get the y coordinate of the patrol**/
	private float getY(){return this.getBody().getPosition().y;}

	/**
	 * Method for moving the patrol towards a specified point
	 *
	 * @param targetVector the position ot move the patrol to
	 */
	public void moveTowards(Vector2 targetVector)
		{
			// Get the centre of the patrol
			Vector2 patrolCentre = new Vector2(this.getX() + this.getSizeDims().x / 2,this.getY() + this.getSizeDims().y / 2);

			// Work out the vector between them and scale by speed
			Vector2 trajectory = targetVector.sub(patrolCentre);
			trajectory.nor().scl(this.speed);

			// Apply force to the patrol
			//this.getBody().setTransform(this.getBody().getPosition(), trajectory.angle());
			this.getBody().applyForceToCenter(trajectory, true);
		}
        

        /**
         * Called every frame, move the patrol along it's path
         */
        public void update() {
            // Path to take
            System.out.println(this.path);

			if (this.path != null) {
				// Move towards next point
				moveTowards(this.path.get(0));
			}

        }

		/**
		 * Called when a patrol collides with a fire engine. Start the minigame and
		 * destroy the patrol
		 */
		public void collided() {
			this.screen.getGame().setScreen(this.screen.getGame().getNewMinigameScreen());
			this.screen.destroyBody(this.getFixture().getBody());
			this.spriteHandler.destroySpriteAndBody(this.getFixture());
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
}
