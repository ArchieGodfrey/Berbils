package com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

/**
 * Class used for creating simple circle projectiles that explode on
 * collision
 */
public class ExplodingBulletCircle extends Projectiles
	{

	/** The Explosion class instance used to create the explosion effect on
	 * collision
	 */
	private Explosion explosionObject;

	/**
	 * Constructor defines the Explosion object to be used to create the
	 * explosions and will create the fixture and body definition using the
	 * passed arguments
	 *
	 * @param projectileSpeed 	The speed at which the projectile travels in
	 *                          meters
	 *
	 * @param projectileDiam 	The diameter size of the projectile in meters
	 *
	 * @param damage			The damage each projectile does on collision
	 *
	 * @param explosionDiam 	The diameter of the resulting explosion once
	 *                          collision occurs, determines the size of
	 *                          the explosion texture and the range at which
	 *                          damage and knockback occurs
	 *
	 * @param maxRange			The max distance in meters the projectile can
	 *                          travel before being destroyed, acting the
	 *                          same as if it had collided
	 *
	 * @param knockbackPower	The amount of knockback an explosion will
	 *                          apply to Fire Engines in the explosion radius
	 *
	 * @param textureFilePath   The file path to the texture for the sprite,
	 * 		  	                note - If this is null a sprite will
	 * 	 	 	                not be created
	 *
	 * @param screen            The Screen containing the world where the
	 *                          projectile will be created
	 */
	public ExplodingBulletCircle(
		float projectileSpeed,
		float projectileDiam,
		int damage,
		float explosionDiam,
		float maxRange,
		float knockbackPower,
		String textureFilePath,
		PlayScreen screen)
		{
		super(projectileSpeed,
			  projectileDiam,
			  damage,
			  maxRange,
			  textureFilePath,
			  screen);
		this.damage = damage;
		this.screen = screen;
		this.explosionObject =
			new Explosion(this.screen,
						  explosionDiam,
						  Kroy.EXPLOSION_TEX,
						  knockbackPower,
						  this.damage / 5);
		}

	/**
	 * This update method is used to ensure Explosion textures dissapear
	 * after the correct amount of time and that projectiles can only travel
	 * a max distance of their max range as defined.
	 *
	 * @param deltaTime The time in seconds that have elapsed in world time
	 *                  (Excludes time taken to draw, render etc) since the
	 *                  last Gdx delta call.
	 */
	@Override
	public void update(float deltaTime) //
		{
		this.explosionObject.update(deltaTime);
		super.update(deltaTime);
		}

	/**
	 * Destroys the projectile and its associated sprite, also calls the
	 * explosion effect and removes the projectile from a list so its
	 * distance from its spawn point is no longer checked
	 *
	 * @param projectile The projectile's (fixture) that collided with another
	 *                   Box2d object
	 */
	@Override
	public void collided(Fixture projectile)
		{
		this.deleteInitialSpawnPoint(projectile);
		this.explosionObject.explode(projectile.getBody().getPosition());
		this.spriteHandler.destroySpriteAndBody(projectile);
		}
	}
