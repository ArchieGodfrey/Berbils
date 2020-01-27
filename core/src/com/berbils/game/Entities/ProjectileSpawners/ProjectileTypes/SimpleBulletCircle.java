package com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.berbils.game.Screens.PlayScreen;

/**
 * A simple circle projectile that is destroyed on collision
 */
public class SimpleBulletCircle extends Projectiles
	{
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
	 * @param textureFilepath	The file path to the texture for the sprite,
	 * 	 * 	                    NOTE - If this is null a sprite will
	 * 	 * 	                    not be created
	 *
	 * @param screen			The screen the projectile will be spawned on
	 */
	public SimpleBulletCircle(
		float projectileSpeed,
		float projectileDiameter,
		int damage,
		float maxRange,
		String textureFilepath,
		PlayScreen screen)
		{
		super(projectileSpeed,
			  projectileDiameter,
			  damage,
			  maxRange,
			  textureFilepath,
			  screen);
		}

	/**
	 * Destroys the sprite and removes its spawn point from the array list
	 * stored within the superclass
	 *
	 * @param projectile The projectile fixture collided
	 */
	@Override
	public void collided(Fixture projectile)
		{
		// Set its linear velocity to 0 so it can not travel any further
		// whilst the world is updating , prevents multiple attempts to
		// destroy the body
		projectile.getBody().setLinearVelocity(0, 0);
		this.spriteHandler.destroySpriteAndBody(projectile);
		this.deleteInitialSpawnPoint(projectile);
		}
	}
