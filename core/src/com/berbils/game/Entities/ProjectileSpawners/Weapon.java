package com.berbils.game.Entities.ProjectileSpawners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Projectiles;

public abstract class Weapon
	{

	/** The last time the weapon called fired (called the attack method) */
	protected long startTime;

	/** The amount of time required where the weapon can not attack, to match
	 *  the fire rate per second
	 */
	protected double fireDelay;

	/** The projectile type that the weapon will spawn */
	protected Projectiles projectileType;

	/**
	 *
	 *  Instantiates variables, sets the projectiles to be sensors
	 *  and calculates the fire delay
	 *
	 * @param fireRatePerSecond The number of attack calls per second
	 *
	 * @param projectileType	The projectile type to be spawned
	 */
	public Weapon(double fireRatePerSecond,
				  Projectiles projectileType)
		{
		this.projectileType = projectileType;
		this.projectileType.setFixtureDefSensor(true);
		this.setupFireRate(fireRatePerSecond);
		}

	/**
	 * Calculates the amount of time required between attack calls
	 *
 	 * @param fireRatePerSecond The number of attack calls per second
	 */
	private void setupFireRate(double fireRatePerSecond)
		{
		this.startTime = 0;
		this.fireDelay = 1 / fireRatePerSecond * 1000;
		}

	/**
	 * Method for creating a projectiles body, fixture and sprite before g
	 * iving it a velocity appropriate for the projectiles speed and storing
	 * its initial spawn point.
	 *
	 * @param spawnPos		The position in meters where the projectiles are
	 *                         being spawned from
	 *
	 * @param projectileVel	The velocity of th projectileVel being the
	 *                         direction vector between the spawn pos and target
	 */
	protected void spawnProjectile(Vector2 spawnPos, Vector2 projectileVel)
		{
			System.out.println("SPAWN");
		this.projectileType.setSpawnPosition(spawnPos);
		this.projectileType.createBodyCopy();
		this.setLinearVelocity(projectileVel.scl(this.projectileType.getProjectileSpeed()));
		this.projectileType.createFixtureCopy();
		this.projectileType.setUserData(this.projectileType);
		this.projectileType.createSprite();
		this.projectileType.storeInitialSpawnPoints(this.projectileType.getFixture());
		}

	/**
	 * Sets the current projectiles velocity
	 *
	 * @param vel The velocity the projectile will be given
	 */
	protected void setLinearVelocity(Vector2 vel)
		{
		this.projectileType.getBody().setLinearVelocity(vel);
		}

	/**
	 * A method for getting the direction as a vector from the spawn point to
	 * the target position
	 *
	 * @param spawnPoint	The location the projectiles are being spawned
	 *                      from in meters
	 *
	 * @param targetPos		The location of the target in meters
	 *
	 * @return				The direction vector from the projectile spawn
	 * 						point to the target pos
	 */
	protected Vector2 getTargetDir(Vector2 spawnPoint, Vector2 targetPos)
		{
		float angleToTarget = MathUtils.atan2(targetPos.y - spawnPoint.y,
											  targetPos.x - spawnPoint.x);
		return ( new Vector2(MathUtils.cos(angleToTarget),
							 MathUtils.sin(angleToTarget)) );
		}

	/**
	 * Tells us whether enough time has passed for the Weapon to fire again
	 *
	 * @return true if the weapon can fire, false if the weapon can not fire
	 */
	protected boolean canFire()
		{
		return ( TimeUtils.timeSinceMillis(startTime) > this.fireDelay );
		}

	/** All projectile spawners must have an attack method 
	 * @param spawnPos The spawn point for the projectiles
	 * @param target	The target the projectiles will travel to
	*/
	public abstract void attack(Vector2 spawnPos, Vector2 target);

	/**
	 * A method for setting all future Box2D fixture creations to have the
	 * passed in collision filtering
	 *
	 * @param catBits The category bits used for Box2D collision filtering
	 * @param maskBits The mask bits used for Box2D collision filtering
	 */
	public void setFixtureCategory(short catBits, short maskBits)
		{
		this.projectileType.setFixtureCategory(catBits, maskBits);
		}
	}
