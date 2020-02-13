package com.berbils.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.berbils.game.Kroy;

/**
 * Creates the pause menu screen
 */
public class PauseScreen extends BasicMenu
	{
	/** The file path to the title texture to display at the top of the screen
	 */
	private static String titlePath = Kroy.GAME_PAUSED_TITLE;

	/** An array containing the text that will appear on its own button */
	private static String[] menuOptions = new String[] { "Resume", "menu",
		"												Quit" };
	/**
	 * Creates the pause menu and assigns functions to each of the menu buttons
	 *
	 * @param game			The game instance
	 * @param spriteBatch	The spritebatch
	 */
	public PauseScreen(final Kroy game, SpriteBatch spriteBatch)
		{
		super(spriteBatch, titlePath, menuOptions);

		super.menuButtons.get(0).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					game.setScreen(game.gameScreen);
					}
				});
		super.menuButtons.get(1).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
						game.createAllScreens();
						game.setScreen(game.mainMenu);
					}
				}
		);
		super.menuButtons.get(2).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					System.out.println("Exit Game");
					Gdx.app.exit();
					}
				});
		}

		/**
		 * NEW Method @author Archie Godfrey
		 * 
		 * Update the pause screen to return to a specified screen
		 * rather than defaulting to the main game
		 * 
		 * @param game 		The game instance
		 * @param screen	The screen instance to return to
		 */
		public void returnToScreen(final Kroy game, final PlayScreen screen) {
			super.menuButtons.get(0).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					game.setScreen(screen);
					}
				});
		}
	}
