package com.berbils.game.Entities.ProjectileSpawners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Projectiles;
import com.berbils.game.Screens.PlayScreen;

/***
 * This class is used to create simple projectile spawners that just fire a
 * projectile at a target position
 */
public class BasicProjectileSpawner extends Weapon
	{

	/**
	 *
	 *
	 * @param fireRatePerSecond 	The number of projectiles that are
	 *                              spawned per second
	 *
	 * @param projectileTypePassed	The projectile type that will be spawned
	 */
	public BasicProjectileSpawner(
		double fireRatePerSecond,
		Projectiles projectileTypePassed)
		{
		super(fireRatePerSecond, projectileTypePassed);
		}

	/**
	 * Spawns a projectile in the direction of a target, also updates the
	 * last time a projectile was spawned to limit fire rate.
	 *
	 * @param spawnPos	The position the projectile will be spawned from in
	 *                 	meters
	 *
	 * @param targetPos The target posiion the projectile will be launched
	 *                  towards in meters
	 */
	@Override
	public void attack(Vector2 spawnPos, Vector2 targetPos)
		{
		if (super.canFire()) {
			super.spawnProjectile(spawnPos,
								  super.getTargetDir(spawnPos, targetPos));
			this.startTime = TimeUtils.millis();
		}
		}
	}
