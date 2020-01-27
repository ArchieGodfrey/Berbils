package com.berbils.game.Handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Entities.FireStation.FireStation;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Projectiles;
import com.berbils.game.Entities.Towers.Tower;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

public class GameContactListener implements ContactListener
	{
	public GameContactListener()
		{
		super();
		}

	/***
	 * This method is called whenever two objects initially collide
 	 * @param contact The two objects that have collided
	 */
	@Override
	public void beginContact(Contact contact)
		{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		// User Data should always be set to the instance of the class the
		// Box2D object is for so used to determine whats colliding
		Object fixtureAUserData = fixtureA.getBody().getUserData();
		Object fixtureBUserData = fixtureB.getBody().getUserData();
		if (fixtureA == null || fixtureB == null) {
			return;
		}
		// Fire engine getting in range of a tower
		else if (this.fireEngContactTowerSensor(fixtureAUserData,
												fixtureBUserData)) {
			this.getTowerObject(fixtureAUserData, fixtureBUserData)
				.setTarget(this.getFireEngineObject(fixtureAUserData,
													fixtureBUserData).getBody());
		}
		// A projectile hitting a fire engine
		else if (this.projectileContactFireEngine(fixtureAUserData,
												  fixtureBUserData)) {
			this.getFireEngineObject(fixtureAUserData, fixtureBUserData)
				.takeDamage(this.getProjectilesObject(fixtureAUserData,
													  fixtureBUserData).getDamage());
			this.getProjectilesObject(fixtureAUserData, fixtureBUserData)
				.collided(this.getProjectilesFixture(fixtureA, fixtureB));
		}
		// Projectile hitting a tower
		else if (this.projectileContactTower(fixtureAUserData,
											 fixtureBUserData)) {
			this.getTowerObject(fixtureAUserData, fixtureBUserData)
				.takeDamage(this.getProjectilesObject(fixtureAUserData,
													  fixtureBUserData).getDamage());
			this.getProjectilesObject(fixtureAUserData, fixtureBUserData)
				.collided(this.getProjectilesFixture(fixtureA, fixtureB));
		}
		// Fire engine touching the fire station
		else if (this.fireEngineContactFireStation(fixtureAUserData,
												   fixtureBUserData)) {
			this.getFireStationObject(fixtureAUserData, fixtureBUserData)
				.collided(this.getFireEngineFixture(fixtureA, fixtureB));
		}
		// Projectile hitting scenery
		else if (this.projectileContactScenery(fixtureA, fixtureB)) {
			this.getProjectilesObject(fixtureAUserData, fixtureBUserData)
				.collided(this.getProjectilesFixture(fixtureA, fixtureB));
		}
		else {
			return;
		}
		}

	/***
	 * This method is called whenever two objects no longer collide
	 * @param contact The two objects that have stopped colliding
	 */
	@Override
	public void endContact(Contact contact)
		{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		Object fixtureAUserData = fixtureA.getBody().getUserData();
		Object fixtureBUserData = fixtureB.getBody().getUserData();

		if (fixtureA == null || fixtureB == null) {
			return;
		}
		// Fire engine no longer in range of tower
		else if (this.fireEngContactTowerSensor(fixtureAUserData,
												fixtureBUserData)
			&& this.getTowerFixture(fixtureA, fixtureB).isSensor()) {
			this.getTowerObject(fixtureAUserData, fixtureBUserData).setTarget(
				null);
		}
		// Fire engine left fire station
		else if (this.fireEngineContactFireStation(fixtureAUserData,
												   fixtureBUserData)) {
			this.getFireEngineObject(fixtureAUserData,
									 fixtureBUserData).leftFireStation = true;
		}
		else {
			return;
		}
		}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
		{
		}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
		{
		}

	/**
	 *  A Method to check if the two objects colliding are a fire engine and a
	 *  @{@link Tower} sensor, telling us it is either in range or out of range.
	 *
 	 * @param obj1 one of the objects in the collision
	 *
	 * @param obj2 one of the objects in the collision
	 *
	 * @return true if one object is a @{@link Tower} and the other object is a fire
	 * 		   engine, else false
	 */
	private boolean fireEngContactTowerSensor(Object obj1, Object obj2)
		{
		return ( ( obj1 instanceof Tower && obj2 instanceof FireEngine )
			|| ( obj1 instanceof FireEngine && obj2 instanceof Tower ) );
		}

	/**
	 * Gets the @{@link Tower} object out of the two objects collided
	 *
	 * @param obj1 one of the objects in the collision
	 * @param obj2 one of the objects in the collision
	 * @return Returns which of the objects are a {@link Tower}, the first
	 * 		   object if they are both {@link Tower}s
	 */
	private Tower getTowerObject(Object obj1, Object obj2)
		{
		if (obj1 instanceof Tower) {
			return (Tower) obj1;
		}
		else if (obj2 instanceof Tower) {
			return (Tower) obj2;
		}
		else {
			throw new IllegalArgumentException("Neither arguments are towers");
		}
		}

	/**
	 * Gets the {@link FireEngine} object out of the two objects collided
	 *
	 * @param obj1 one of the objects in the collision
	 * @param obj2 one of the objects in the collision
	 * @return Returns which of the objects are a {@link FireEngine}, the first object
	 * 		   if they are both {@link FireEngine}s
	 */
	private FireEngine getFireEngineObject(Object obj1, Object obj2)
		{
		if (obj1 instanceof FireEngine) {
			return (FireEngine) obj1;
		}
		else if (obj2 instanceof FireEngine) {
			return (FireEngine) obj2;
		}
		else {
			throw new IllegalArgumentException(
				"Neither arguments are fire engines");
		}
		}

	/**
	 *  A Method to check if the two objects colliding are a projectile and a
	 * 	fire engine
	 *
	 * @param obj1 one of the objects in the collision
	 *
	 * @param obj2 one of the objects in the collision
	 *
	 * @return true if one object is a projectile and the other object
	 * 		   is a fire engine, else false
	 */
	private boolean projectileContactFireEngine(Object obj1, Object obj2)
		{
		return ( ( obj1 instanceof Projectiles && obj2 instanceof FireEngine )
			|| ( obj1 instanceof FireEngine && obj2 instanceof Projectiles ) );
		}

	/**
	 * Gets the Projectiles object out of the two objects collided
	 *
	 * @param obj1 one of the objects in the collision
	 * @param obj2 one of the objects in the collision
	 * @return Returns which of the objects are a Projectile, the first object
	 * 		   if they are both Projectiles
	 */
	private Projectiles getProjectilesObject(Object obj1, Object obj2)
		{
		if (obj1 instanceof Projectiles) {
			return (Projectiles) obj1;
		}
		else if (obj2 instanceof Projectiles) {
			return (Projectiles) obj2;
		}
		else {
			throw new IllegalArgumentException(
				"Neither arguments are projectiles");
		}
		}


	/**
	 * Determines which {@link Fixture} is attached to a {@link Projectiles} object and
	 * 			  returns it.Returns the first of the fixtures if both are
	 * 			  {@link Projectiles};
	 *
	 * @param fixOne One of the fixtures involved in the collision
	 * @param fixTwo One of the fixtures involved in the collision
	 * @return Returns the {@link Fixture} of the {@link Projectiles} object
	 */
	private Fixture getProjectilesFixture(Fixture fixOne, Fixture fixTwo)
		{
		if (fixOne.getBody().getUserData() instanceof Projectiles) {
			return fixOne;
		}
		else if (fixTwo.getBody().getUserData() instanceof Projectiles) {
			return fixTwo;
		}
		else {
			throw new IllegalArgumentException(
				"Neither arguments are projectiles");
		}
		}

	/**
	 *  A Method to check if the two objects colliding are a projectile and a
	 *  tower
	 *
	 * @param obj1 one of the objects in the collision
	 *
	 * @param obj2 one of the objects in the collision
	 *
	 * @return true if one object is a projectile and the other object is a
	 * 		   tower, else false
	 */
	private boolean projectileContactTower(Object obj1, Object obj2)
		{
		return ( ( obj1 instanceof Projectiles && obj2 instanceof Tower )
			|| ( obj1 instanceof Tower && obj2 instanceof Projectiles ) );
		}

	/**
	 *  A Method to check if the two objects colliding are a fire engine and a
	 *  fire station
	 *
	 * @param obj1 one of the objects in the collision
	 *
	 * @param obj2 one of the objects in the collision
	 *
	 * @return true if one object is a fire sation and the other object is a 
	 * 		   fire engine, else false
	 */
	private boolean fireEngineContactFireStation(Object obj1, Object obj2)
		{
		return ( ( obj1 instanceof FireEngine && obj2 instanceof FireStation )
			|| ( obj1 instanceof FireStation && obj2 instanceof FireEngine ) );
		}

	/**
	 * Gets the FireStation object out of the two objects collided
	 *
	 * @param obj1 one of the objects in the collision
	 * @param obj2 one of the objects in the collision
	 * @return Returns which of the objects are a Fire Station, the first object
	 * 		   if they are both Fire Stations
	 */
	private FireStation getFireStationObject(Object obj1, Object obj2)
		{
		if (obj1 instanceof FireStation) {
			return (FireStation) obj1;
		}
		else if (obj2 instanceof FireStation) {
			return (FireStation) obj2;
		}
		else {
			throw new IllegalArgumentException(
				"Neither arguments are Fire Station objects");
		}
		}

	/**
	 * Determines which {@link Fixture} is attached to a {@link Tower} object and returns it.
	 * Returns the first of the fixtures if both are {@link Tower};
	 *
	 * @param fixOne One of the fixtures involved in the collision
	 * @param fixTwo One of the fixtures involved in the collision
	 * @return Returns the {@link Fixture} of the {@link FireEngine} object
	 */
	private Fixture getFireEngineFixture(Fixture fixOne, Fixture fixTwo)
		{
		if (fixOne.getBody().getUserData() instanceof FireEngine) {
			return fixOne;
		}
		else if (fixTwo.getBody().getUserData() instanceof FireEngine) {
			return fixTwo;
		}
		else {
			throw new IllegalArgumentException(
				"Neither arguments are FireEngine objects");
		}
		}

	/**
	 * Determines which {@link Fixture} is attached to a {@link Tower} object and returns it.
	 * Returns the first of the fixtures if both are {@link Tower};
	 *
	 * @param fixOne One of the fixtures involved in the collision
	 * @param fixTwo One of the fixtures involved in the collision
	 * @return Returns the {@link Fixture} of the {@link Tower} object
	 */
	private Fixture getTowerFixture(Fixture fixOne, Fixture fixTwo)
		{
		if (fixOne.getBody().getUserData() instanceof Tower) {
			return fixOne;
		}
		else if (fixTwo.getBody().getUserData() instanceof Tower) {
			return fixTwo;
		}
		else {
			throw new IllegalArgumentException("Neither arguments are Towers");
		}
		}


	/**
	 *  A Method to check if the two objects colliding are a {@link Projectiles} and a
	 *  piece of scenery. Requires fixtures as scenery is defined by its mask
	 *  and category bits.
	 *
	 * @param fixOne One of the fixtures involved in the collision
	 *
	 * @param fixTwo One of the fixtures involved in the collision
	 *
	 * @return true if one {@link Fixture} is part of the scenery and the other object
	 * 		   is a {@link Projectiles}, else false
	 */
	private boolean projectileContactScenery(Fixture fixOne, Fixture fixTwo)
		{
		return ( ( fixOne.getUserData() instanceof Projectiles
			&& fixOne.getFilterData().categoryBits == Kroy.CAT_SCENERY )
			|| ( fixOne.getFilterData().categoryBits == Kroy.CAT_SCENERY
			&& fixTwo.getUserData() instanceof Projectiles ) );
		}
	}
