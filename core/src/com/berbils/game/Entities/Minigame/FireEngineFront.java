package com.berbils.game.Entities.Minigame;

import com.badlogic.gdx.math.Vector2;
import com.berbils.game.Entities.EntityTypes.BoxGameEntity;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.MiniGameScreen;
import com.berbils.game.Screens.PlayScreen;

/**
 * A class used to generate Box Entitys and a sprite with in built resources
 * used as limitations such as ammo for firing projectiles or health
 * <P>Contains all methods required to be used by a player </P>
 *
 */
public class FireEngineFront extends FireEngine
	{

	/**
		Variable to keep track of the current minigame instance
	*/
	private MiniGameScreen miniGameScreen;
	/**
	 * This constructor assigns required variables and sets up the weapon class
	 * instance for use.It only creates a fixture and body definition, no
	 * sprite, body or fixture generation.
	 *
	 * @param screen 			The Screen the entity object is located and will be
	 *               			created
	 *
	 * @param dimensions 		The size dimensions of the entity in meters
	 *
	 * @param weapon 			The weapon instance that the fire engine will use to
	 *               			fire projectiles
	 *
	 * @param maxWater 			The max "ammo" resource for the weapon , it is
	 *                      	consumed each time the fire engine instance
	 *                      	fires and is the value the water resource is
	 *                      	reset to
	 *
	 * @param speed 			The speed of the fire engine, how fast it
	 *                          will move
	 *
	 * @param maxHealth			The max health for the fire engine instance
	 *                          and represents the maximum amount of
	 *                          damage the fire engine can take before death
	 *
	 * @param textureFilePath  The file path to the sprite texture
	 *                         Note - If passd as null no sprite will be
	 *                         created
	 */
	public FireEngineFront(
		MiniGameScreen screen,
		Vector2 dimensions,
		Weapon weapon,
		int maxWater,
		float speed,
		int maxHealth,
		String textureFilePath)
		{
		super(screen, dimensions, weapon, maxWater, speed, maxHealth, textureFilePath);
		this.miniGameScreen = screen;
		}

	/**
	 * Method called to represent fire engine death, updates scores, the
	 * current screen shown and what state the game will be in after the
	 * screens shown.
	 */
	@Override
	public void onDeath()
		{
		if (this.getAlive()) {
			this.setAlive(false);
			Kroy game = this.screen.getGame();
			game.gameScreen.updatePlayerScore(-200);
			this.miniGameScreen.cancelTimer();
			game.setScreen(game.gameScreen);
		}
		}

	/**
	 * This method sets the body position, then creates the body, fixture and
	 * creates an attached sprite
	 *
	 * @param spawnPos The position in meters where the fire engine should be
	 *                   created with the center of the fire engine being at
	 *                   this position.
	 */
	@Override
	public void spawn(Vector2 spawnPos)
		{
		super.spawn(spawnPos);
        // Rotate to face aliens
        this.getBody().setAngularVelocity(16);
		}
	}
