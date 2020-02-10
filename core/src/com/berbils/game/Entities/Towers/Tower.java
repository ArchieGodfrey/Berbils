package com.berbils.game.Entities.Towers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.berbils.game.Entities.EntityTypes.CircleGameEntity;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.Explosion;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

//TEMPORARY
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Creates a tower game object, an enemy object that attacks if the player
 * gets within a set range
 */
public class Tower extends CircleGameEntity
	{

	/**The weapon object the tower will use to spawn projectiles */
	private Weapon towerWeapon;

	/** The boolean for telling the tower whether it has a target and can
	 * fire (if also alive), also used for determining which texture the tower
	 * needs currently.
	 */
	private boolean isActive;

	/**
	 * Boolean for determining whether the fire engine is alive or not alive
	 * and should be destroyed
	 */
	private boolean isAlive;

	/** The current target for teh tower to fire at, if null the tower is
	 * automatically set to not active
	 */
	private Body currentTarget;

	/** The file path to the texture for when the tower has a target */
	private String engagedTowerTextureFP;

	/** The file path to the texture for when the tower does not have a
	 * target */
	private String disengagedTowerTextureFP;

	/** The tower sensor, a circle sensor the size of the tower range, it is
	 * used for determining whether a target is within a certain distance of
	 * the tower and can therefore be attacked
	 */
	private CircleGameEntity towerSensor;

	/** The range that the tower can attack within, in meters */
	private float range;

	/**
	 * The current health of the tower, once this reaches zero the tower "dies"
	 * and the onDeath() method is called
	 */
 	private float currentHealth;

	/**
	 * The max health for the Tower instance and represents the maximum
	 * amount of damage the Tower can take before death
	 */
	private float maxHealth;

	/** The sprite for the healthbar displaed above the tower */
	private Sprite healthBar;

	/**The position of the healthBar in meters */
	private Vector2 healthBarPos;

	/** The size of the healthbar in meters */
	private Vector2 healthBarSize;

	/** The explosion class object used to create an explosion upon tower
	 * death */
	private Explosion explosionOnDeath;

	/**
	 * Creates the tower base body, fixture and sprite.Also creates the tower
	 * sensor body and fixture in addition to variable assignment.
	 *
	 * @param diameter The diameter of the tower base in meters
	 *
	 * @param range        The range of the tower in meters (The
	 * 	 *                 size of the tower sensor entity)
	 *
	 * @param maxHealth    The max health for the Tower instance
	 * 	  	 	           and represents the maximum amount of
	 * 	  	 	           damage the Tower can take before death
	 *
	 * @param pos			The position of the tower in meters
	 *
	 * @param screen		The screen the tower will be created in
	 *
	 * @param weapon		The weapon object the tower will use to spawn
	 *                      projectiles
	 *
	 * @param textureFilePathDisengaged	The file path for the texture for when
	 *                              the tower does not have a target in
	 *                              range
	 *
	 * @param textureFilePathEngaged      The file path for the texture for when
	 * 	                            the tower does have a target in range
	 */
	public Tower(
		float diameter,
		float range,
		int maxHealth,
		Vector2 pos,
		PlayScreen screen,
		Weapon weapon,
		String textureFilePathDisengaged,
		String textureFilePathEngaged)
		{
		super(screen,
			  diameter,
			  pos,
			  textureFilePathDisengaged,
			  true,
			  Kroy.CAT_ENEMY,
			  Kroy.MASK_ENEMY,
			  1);
		super.setUserData(this);
		this.defineStats(range,
						 diameter,
						 maxHealth,
						 textureFilePathDisengaged,
						 textureFilePathEngaged);
		this.createSensor();
		this.setWeapon(weapon);
		this.createHealthBar();

		}

	/**
	 * Assign tower variables
	 *
	 * @param range					The range of the tower in meters (The
	 *                              size of the tower sensor entity)
	 *
	 * @param diam					The diameter of the tower base in meters
	 *
	 * @param maxHealth             The max health for the fire engine instance
	 * 	 	                  		and represents the maximum amount of
	 * 	 	                  		damage the fire engine can take before death
	 *
	 * @param textureFPDisengaged	The file path for the texture for when
	 *                              the tower does not have a target in
	 *                              range
	 *
	 * @param textureFPEngaged      The file path for the texture for when
	 * 	                            the tower does have a target in range
	 */
	private void defineStats(
		float range,
		float diam,
		int maxHealth,
		String textureFPDisengaged,
		String textureFPEngaged)
		{
		this.range = range;
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
		this.disengagedTowerTextureFP = textureFPDisengaged;
		this.engagedTowerTextureFP = textureFPEngaged;
		this.isActive = false;
		this.isAlive = true;
		this.healthBarPos = this.position.cpy().add(0, diam * 1.5f);
		this.healthBarSize = new Vector2(diam * 2, 0.1f);

		this.explosionOnDeath = new Explosion(this.screen,
											  this.sizeDims.x * 1.5f,
											  Kroy.EXPLOSION_TEX,
											  50,
											  25);
		}

	/**
	 * Creates a circle entity with no sprite and sets it to be a sensor,
	 * this will be what detects players coming within range
	 */
	private void createSensor()
		{
		this.towerSensor =
			new CircleGameEntity(
				this.screen,
				this.range,
				this.position,
				null,
				true,
				Kroy.CAT_TOWER_SENSOR,
				Kroy.MASK_TOWER_SENSOR, 1);
		this.towerSensor.setSensor(true);
		this.towerSensor.setUserData(this);
		}

	/**
	 * Setter for the towers weapon, also sets the weapons collision filtering
	 *
	 * @param weapon The weapon the Tower will use to spawn projectiles
	 */
	public void setWeapon(Weapon weapon)
		{
		towerWeapon = weapon;
		towerWeapon.setFixtureCategory(Kroy.CAT_PROJECTILE_ENEMY,
									   Kroy.MASK_ENEMY_PROJECTILE);
		}

	/**
	 * Creats the health bar sprite
	 */
	private void createHealthBar()
		{
		this.healthBar = this.spriteHandler.createNewSprite(Kroy.HEALTH_BAR_TEX,
															this.healthBarPos,
															this.healthBarSize.cpy(),
															2);
		}

	/**
	 * If the tower is alive and can fire, it fires a projectile towards a
	 * target
	 */
	public void fire()
		{
		if (isActive && isAlive) {
			this.towerWeapon.attack(this.position,
									this.currentTarget.getPosition());

		}
		}

	/**
	 * The amount of damage taken from being hit by a projectile
	 *
	 * @param damageTaken The amount of damage to deal to the towers health
	 */
	public void takeDamage(int damageTaken)
		{
		this.currentHealth -= damageTaken;
		if (currentHealth <= 0) {
			this.onDeath();
		}
		}

	/**
	 *  Called upon tower death
	 *  Updates the screen counter for towers alive
	 *  If the tower is the last one alive the game screen is updated to the
	 *  game won screen
	 *  Else if the tower is not the last one alive, an explosion is created
	 *  and the towers body, fixture and sprite destroyed.
	 */
	public void onDeath()
		{
		if (this.isAlive) {
			Kroy game = this.screen.getGame();
			this.screen.towerDestroyed();
			if (this.screen.allTowersDestroyed()) {
				game.setScreen(game.winScreen);
				game.winScreen.setTimer(5, game.mainMenu);
				game.createAllScreens();
			}
			else {
				this.explosionOnDeath.explode(this.position);
				this.isAlive = false;
				this.setTarget(null);
				this.screen.updatePlayerScore(1000);
				this.screen.destroyBody(this.towerSensor.getFixture().getBody());
				this.spriteHandler.destroySprite(this.healthBar);
				this.spriteHandler.destroySpriteAndBody(this.entityFixture);
			}
		}
		}

	/**
	 * Sets the target for the tower to fire at
	 *
	 * @param target the target Box2D body to attack/fire at
	 */
	public void setTarget(Body target)
		{
		this.currentTarget = target;
		if (this.isAlive) {
			if (this.currentTarget == null) {
				this.setActive(false);
				this.spriteHandler.setSpriteTexture(this.entityFixture,
													this.disengagedTowerTextureFP);
			}
			else {
				this.setActive(true);
				this.spriteHandler.setSpriteTexture(this.entityFixture,
													this.engagedTowerTextureFP);
			}
		}
		}

	/**
	 *  Sets the tower state, inactive means it can not fire, active means it
	 *  can
	 * @param active true = The tower can fire, False = The tower can not fire
	 */
	private void setActive(boolean active)
		{
		this.isActive = active;
		}

	/***
	 *The update method needs to be called every world update:The health bar
	 * is updated and any explosions caused by the death of the tower are
	 * updated to go away after a set amount of time via this
	 *
	 * @param deltaTime The time in seconds that have elapsed in world time
	 * 	                (Excludes time taken to draw, render etc) since the
	 * 	                last Gdx delta call.
	 */
	public void update(float deltaTime)
		{
		float percentHealth = this.currentHealth / this.maxHealth;
		this.spriteHandler.setSpriteSize(this.healthBarSize.cpy().scl(
			percentHealth,
			1f), this.healthBar);
		this.explosionOnDeath.update(deltaTime);
		}

		/**
		 * TEMPORARY: Remove when Patrol implemented
		 */
		public void transitionToMiniGame()
		{
			System.out.println("Transition");
			this.screen.getGame().setScreen(this.screen.getGame().getNewMinigameScreen());
		}

		/**
		 * TEMPORARY: Remove when Patrol implemented
		 *
		 * @param fixture The fixture that collided with the object instance
		 */
		public void collided(Fixture fixture)
		{
	}
}
