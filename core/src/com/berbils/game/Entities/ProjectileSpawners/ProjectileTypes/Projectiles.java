package com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.berbils.game.Entities.EntityTypes.CircleGameEntity;
import com.berbils.game.Screens.PlayScreen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is the basis for all projectiles and any projectile must extend
 * this, creates all necessary definitions and allows for easy spawning of
 * multiple projectiles without creating many class object instances
 */
public abstract class Projectiles extends CircleGameEntity
	{

	/** The speed at which the projectile travels in meters */
	protected float projectileSpeed;

	/** The max distance in meters the projectile can travel before being
	destroyed, acting the same as if it had collided */
	protected float	maxRange;


	/** The damage each projectile does on collision */
	protected int damage;


	/** A HashMap for keeping track of a projectiles initial spawn point so
	 * that if they travel outside of their max range they can be destroyed
	 */
	protected HashMap<Fixture, Vector2> projectileInitialSpawnPoints;

	/**
	 * This constructor instantiates required variables, creates a fixture
	 * and body definition but does not create any Box2D bodies, Box2d fixtures
	 * or sprites
	 *
	 * @param projectileSpeed 	The speed at which the projectile travels in meters
	 *
	 * @param projectileDiameter The diameter of the projectile to be created
	 *                           in meters
	 *
	 * @param damage			The damage the projectile does
	 *
	 * @param maxRange			The maximum distance the projectile can
	 *                          travel before being destroyed by callings
	 *                          its collide method, in meters
	 *
	 * @param textureFilePath	The file path to the texture for the sprite,
	 * 	 * 	                    NOTE - If this is null a sprite will
	 * 	 * 	                    not be created
	 *
	 * @param screen			The screen the projectile will be spawned on
	 */
	public Projectiles(
		float projectileSpeed,
		float projectileDiameter,
		int damage,
		float maxRange,
		String textureFilePath,
		PlayScreen screen)
		{
		super(screen, projectileDiameter, textureFilePath, false, 3);
		super.storeUserData(this);
		this.projectileSpeed = projectileSpeed;
		this.damage = damage;
		this.maxRange = maxRange;
		this.projectileInitialSpawnPoints = new HashMap<Fixture, Vector2>();
		}

	/**
	 * Getter for the damage the projectile does
	 *
	 * @return the damage the projectile does
	 */
	public int getDamage()
		{
		return this.damage;
		}

	/**
	 * Getter for the projectile speed of the objected
	 *
	 * @return the projectiles speed in meters
	 */
	public float getProjectileSpeed()
		{
		return this.projectileSpeed;
		}

	/**
	 * Method for adding to the HashMap the fixture and a copy of its position
	 *
	 * @param fixture The fixture of the projectile just created/spawned
	 */
	public void storeInitialSpawnPoints(Fixture fixture)
		{
		this.projectileInitialSpawnPoints.put(fixture,
											  fixture.getBody().getPosition().cpy());
		}

	/**
	 * Method for removing a fixture and its associated spawn point from the
	 * hashmap
	 *
	 * @param fixture The fixture of the projectile just destroyed;
	 */
	protected void deleteInitialSpawnPoint(Fixture fixture)
		{
		this.projectileInitialSpawnPoints.remove(fixture);
		}

	/***
	 * Must be called when the world is updated and is there to check how
	 * far the projectile has travelled , removing it if it has travelled for
	 * further than its max range dictates
	 *
	 * @param deltaTime The time in seconds that have elapsed in world time
	 * 	                (Excludes time taken to draw, render etc) since the
	 * 	                last Gdx delta call.
	 */
	public void update(float deltaTime)
		{
		this.checkMaxProjectileTravelDist();
		}


		/**
		 * A method that iterates through the initial spawn point array,
		 * checking whether each fixture body has travevlled further than its
		 * max distance dictates, removing it if so
		 */
	public void checkMaxProjectileTravelDist()
		{
		Iterator<Map.Entry<Fixture, Vector2>> iterator =
			this.projectileInitialSpawnPoints.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Fixture, Vector2> entry = iterator.next();
			Fixture key = entry.getKey();
			float distanceTravelled = entry.getValue().dst(key.getBody().getPosition());
			Projectiles currentProjType = (Projectiles) key.getBody().getUserData();
			if (distanceTravelled > currentProjType.getMaxRange()) {
				iterator.remove();
				currentProjType.collided(key);
			}
		}
		}

	/**
	 * Getter for the max range of the projectile
	 *
	 * @return returns the max range of the projectile in meters
	 */
	public float getMaxRange()
		{
		return this.maxRange;
		}

	/** All projectiles must have a collidede method 
	 * @param projectile The projectile that collided
	*/
	public abstract void collided(Fixture projectile);
	}
