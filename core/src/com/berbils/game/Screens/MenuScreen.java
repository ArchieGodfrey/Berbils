package com.berbils.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.berbils.game.Kroy;

/*** Creates the Main Menu Screen
 *
 */
public class MenuScreen extends BasicMenu
	{

	/** The file path to the title texture to display at the top of the screen
	 */
	private static String titlePath = Kroy.KROY_TITLE_TITLE;
	/** An array containing the text that will appear on its own button */
	private static String[] menuOptions = new String[] { "New Game", "Quit" };

	/**
	 * Creates the menu and assigns functions to each of the menu buttons
	 *
	 * @param game			The game instance
	 * @param spriteBatch	The spritebatch
	 */
	public MenuScreen(final Kroy game, SpriteBatch spriteBatch)
		{
		super(spriteBatch, titlePath, menuOptions);
		super.menuButtons.get(0).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					game.gameScreen.selectFireEngine(0);
					game.setScreen(game.gameScreen);
					}
				});
		super.menuButtons.get(1).addListener(
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
	}
