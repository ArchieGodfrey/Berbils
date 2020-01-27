package com.berbils.game.Entities.ProjectileSpawners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Projectiles;
import com.berbils.game.Screens.PlayScreen;

/***
 * This class is used to create projectile spawners that fire projectiles at
 * set intervals every time an attack call is made
 */
public class SpokeProjectileSpawner extends Weapon
	{

	/** The angle that the spawner will move by each time an attack call is
	 * made, increasing it would increase the distance between the projecties
	 * of two attack calls
	 */
	private float startPosIncrement;

	/** The interval between each spawn point angle direction  */
	private float increment;

	/** The angle which the projectiles will start creating intervals from
	 * Stores how far round the circle the projectiles will spawn */
	private float startPos;

	/** The number of spokes or points at which a projectile will spawn per
	 * attack call
	 */
	private int noOfSpawnPoints;

	/**
	 * Instantiates variables required, including startPos, the increment and
	 * startPosIncrement
	 *
	 * @param fireRatePerSecond	The number of attack calls per second
	 *
	 * @param projectileType	The projectile type that will be spawned
	 *
	 * @param noOfSpawnPoints    The number of spokes or points at which a
	 *                           projectile will spawn per attack call
	 */
	public SpokeProjectileSpawner(
		double fireRatePerSecond,
		Projectiles projectileType,
		int noOfSpawnPoints)
		{
		super(fireRatePerSecond, projectileType);
		this.defineStats(noOfSpawnPoints);
		}

	/**
	 * Calculates and assigns the startPosIncrement and increment, also
	 * assigns the number of spawn points
	 *
	 * @param noOfSpawnPoints The number of spokes or points at which a
	 *                        projectile will spawn per attack call
	 */
	private void defineStats(int noOfSpawnPoints)
		{
		this.noOfSpawnPoints = noOfSpawnPoints;
		startPosIncrement = MathUtils.PI / 36;
		increment = ( MathUtils.PI2 ) / noOfSpawnPoints;;
		}

	/**
	 * Spawns a set number of projectiles in a spoke pattern, the number of
	 * spokes being depdendant upon pre-defined variables.Then increments the
	 * spawn position so the spokes shift slightly round in a circle,
	 * spawning at a slightly different point next attack call.Also updates the
	 * last time a projectile was spawned to limit fire rate.
	 *
	 * @param spawnPos	The position the projectile will be spawned from in
	 *                 	meters
	 *
	 * @param targetPos The target position the projectile will be launched
	 *               towards in meters, Note- This is redundant in this method
	 */
	@Override
	public void attack(Vector2 spawnPos, Vector2 targetPos)
		{

		if (super.canFire()) {
			Vector2 projectileVel = new Vector2();
			for (int i = 0; i < noOfSpawnPoints; i++) {
				float angle = startPos + ( i * increment );
				projectileVel.x = MathUtils.cos(angle);
				projectileVel.y = MathUtils.sin(angle);
				super.spawnProjectile(spawnPos, projectileVel);
			}
			startPos += startPosIncrement;
			startTime = TimeUtils.millis();
		}
		}

	}
