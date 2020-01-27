package com.berbils.game.Entities.ProjectileSpawners;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Projectiles;
import com.berbils.game.Screens.PlayScreen;

/***
 * This class is used to create projectile spawners that fire a set
 * number of projectiles in a random direction from the origin
 */
public class RandomDirProjectileSpawner extends Weapon
	{



	/**  The number of projectiles spawned per attack call */
	private int projectileSpawnNo;

	/**
	 * Instantiates variables required
	 *
	 * @param fireRatePerSecond	The number of attack calls per second
	 *
	 * @param projectileType	The projectile type that will be spawned
	 *
	 * @param projSpawnNo      	The number of projectiles spawned per attack
	 * 	 *                      call
	 */
	public RandomDirProjectileSpawner(
		double fireRatePerSecond,
		Projectiles projectileType,
		int projSpawnNo)
		{
		super(fireRatePerSecond, projectileType);
		this.projectileSpawnNo = projSpawnNo;
		}

	/**
	 * Spawns a set number of projectiles in random directions,also updates the
	 * last time a projectile was spawned to limit fire rate.
	 *
	 * @param spawnPos	The position the projectile will be spawned from in
	 *                 	meters
	 *
	 * @param targetPos The target position the projectile will be launched
	 *              	towards in meters, Note- This is redundant in this method
	 */
	@Override
	public void attack(Vector2 spawnPos, Vector2 targetPos)
		{
		if (super.canFire()) {
			float angle;
			Vector2 projectileVel = new Vector2();
			for (int i = 0; i < this.projectileSpawnNo; i++) {
				angle = MathUtils.random(0, MathUtils.PI2);
				projectileVel.x = MathUtils.cos(angle);
				projectileVel.y = MathUtils.sin(angle);
				super.spawnProjectile(spawnPos, projectileVel);
			}
			this.startTime = TimeUtils.millis();
		}
		}
	}
