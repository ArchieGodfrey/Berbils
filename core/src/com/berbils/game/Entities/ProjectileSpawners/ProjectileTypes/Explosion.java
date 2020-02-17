package com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Handlers.SpriteHandler;
import com.berbils.game.Screens.PlayScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class creates explosion effects, applying a texture and applying
 * knockback and damage to certain objects in range
 */
public class Explosion
	{
	/** The file path to the explosion texture */
	private String textureFilePath;

	/** used to determine the amount of force applied to bodies within range
	 * of the explosion
	 */
	private float knockBackPower;

	/** The diameter of the explosion, used to determine the size of the
	 * explosion texture and how far it can affect bodies and apply
	 * knockback/damage.
	 */
	private float explosionDiam;

	/**
	 *  The maximum blast force an object can experience from a single ray,
	 *  this must be set to a reasonable amount to prevent objects at very low
	 *  distance to the center receiving extremely high values due to the
	 *  divide by distance ^ 2
	 */
	private float maxBlastForce;

	/** The number of rays sent out when an explosion occurs, the higher the
	 * rays the more accurate the explosion on affecting objects in its
	 * radius.
	 */
	private int noOfRays;

	/** The amount of health damage an explosion applies to objects within
	 * its radius, note - This damage is only applied once  */
	private int explosionDamage;

	/**
	 * The SpriteHandler for the screen currently being created upon,
	 * required to draw the explosion texture
	 */
	private SpriteHandler spriteHandler;

	/**
	 * The world attached to the screen, which the raycasts and sprite will be
	 * created on
	 */
	private World world;
	private HashMap<Sprite, Float> explosionSpawnTime;
	private float explosionTime = 0.75f;
	private ArrayList<FireEngine> fireEngHitList;

	public Explosion(
		PlayScreen screen,
		float explosionDiam,
		String textureFilePath,
		float knockBackPower, int explosionDamage)
		{
		this.noOfRays = 36;
		// The knockback power must be scaled by the number of rays else
		// increasing the number of rays would also affect the knockback
		// power as multiple rays can hit a single object
		this.knockBackPower = knockBackPower / this.noOfRays;
		this.explosionDiam = explosionDiam;
		this.textureFilePath = textureFilePath;
		this.world = screen.getWorld();
		this.maxBlastForce = knockBackPower;
		this.explosionDamage = explosionDamage;
		this.explosionSpawnTime = new HashMap<Sprite, Float>();
		this.spriteHandler = screen.getSpriteHandler();
		}

	/**
	 * Creates an explosive effect originating from the explosion center by
	 * sending out a number of raycasts in a circle, each raycast that
	 * contacts a fire engine applies a linear impulse depdenent upon the
	 * knock back power and distance from explosion centre
	 *
	 * @param explosionCenter The centre of the explosion
	 */
	public void explode(Vector2 explosionCenter)
		{
		this.fireEngHitList = new ArrayList<FireEngine>();
		Vector2 explosionSize = new Vector2(this.explosionDiam,
											this.explosionDiam);
		this.determineFixturesWithinExplosion(explosionCenter);
		Sprite newSprite = this.spriteHandler.createNewSprite(this.textureFilePath,
															  explosionCenter,
															  explosionSize,
															  1);
		this.explosionSpawnTime.put(newSprite, 0f);
		}

	/**
	 * A method for determining which fixtures are within range of the
	 * explosion, then if they are instanes of {@link FireEngine} knock back
	 * force will be applied scaled by the inverse square of the distance
	 *
	 * @param explosionCenter The position in meters where the explosion
	 *                        center is
	 */
	private void determineFixturesWithinExplosion(final Vector2 explosionCenter)
		{
		float rotateAngle = MathUtils.PI2 / noOfRays;
		Vector2 rayCastEnd = new Vector2();
		Vector2 rayDir;
		for (int i = 0; i < this.noOfRays; i++) {
			rayDir = new Vector2(MathUtils.cos(( i * rotateAngle )),
								 MathUtils.sin(( i * rotateAngle )));
			rayCastEnd = rayDir.scl(this.explosionDiam / 2);
			rayCastEnd.add(explosionCenter);
			RayCastCallback callback =
				new RayCastCallback()
					{
					@Override
					public float reportRayFixture(
						Fixture fixture, Vector2 point, Vector2 normal, float fraction)
						{
						if (fixture.getUserData() instanceof FireEngine) {
							applyKnockback(explosionCenter, point, fixture);
							return fraction;
						}
						else {
							return 1;
						}
						}
					};
			this.world.rayCast(callback, explosionCenter, rayCastEnd);
		}
		return;
		}

	/**
	 * This method applies a linear impulse,  dependent upon the knock back
	 * force and distance from the center the fixture is, at the point of
	 * contact passed
	 *
	 * @param explosionCenter 		The position in meters where the explosion
	 * 	 *                    		center is
	 *
	 * @param blastPointOfContact	The position in pixels where the ray
	 *                              initially collided with the fixture
	 *
	 * @param fixture				The fixture within range of the explosion
	 *                              and will have knockback applied to it
	 *
	 *
	 */
	private void applyKnockback(
		Vector2 explosionCenter, Vector2 blastPointOfContact, Fixture fixture)
		{
		float distance = explosionCenter.dst(blastPointOfContact);
		float blastForce = Math.min(this.knockBackPower / ( distance * distance ),
									this.maxBlastForce);
		Vector2 impulseDir = ( blastPointOfContact.sub(explosionCenter) ).scl(
			blastForce);
		fixture.getBody().applyLinearImpulse(impulseDir,
											 blastPointOfContact,
											 true);
		FireEngine fireEngHit = ( (FireEngine) fixture.getUserData() );
		if (!this.fireEngHitList.contains(fireEngHit)) {
			this.fireEngHitList.add(fireEngHit);
			fireEngHit.takeDamage(this.explosionDamage);
		}
		}

	/**
	 * Must be called when the world is updated and is there to check how
	 * long the explosion texture has been on screen for, removing it if it
	 * has been on for longer than the explosion time
	 *
	 * @param deltaTime The time in seconds that have elapsed in world time
	 * 	                (Excludes time taken to draw, render etc) since the
	 * 	                last Gdx delta call.
	 */
	public void update(float deltaTime)
		{
		Iterator<Map.Entry<Sprite, Float>> iterator =
			this.explosionSpawnTime.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Sprite, Float> entry = iterator.next();
			Sprite sprite = entry.getKey();
			float timeAlive = entry.getValue();
			timeAlive += deltaTime;
			entry.setValue(timeAlive);
			if (timeAlive > this.explosionTime) {
				iterator.remove();
				this.spriteHandler.destroySprite(sprite);
			}
		}
		}
	}

