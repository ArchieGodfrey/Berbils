package com.berbils.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.berbils.game.Kroy;

/**
 * Creates the Select your fire engine screen
 */
public class SelectFireEngineScreen extends BasicMenu
	{
	/** The file path to the title texture to display at the top of the screen
	 */
	private static String titlePath = Kroy.SELECT_FIRE_ENGINE_TITLE;

	/** An array containing the text that will appear on its own button */
	private static String[] menuOptions = new String[] { "Regular Fire Engine",
		"Large Fire Engine"
	};

	/**
	 * Creates the select fire engine screen and assigns functions to each of
	 * the menu buttons
	 *
	 * @param game			The game instance
	 * @param spriteBatch	The spritebatch
	 */
	public SelectFireEngineScreen(final Kroy game, SpriteBatch spriteBatch)
		{
		super(spriteBatch, titlePath, menuOptions);
		super.menuButtons.get(0).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					game.setScreen(game.gameScreen);
					game.gameScreen.selectFireEngine(0);
					Gdx.input.setInputProcessor(null);
					}
				});
		super.menuButtons.get(1).addListener(
			new ClickListener()
				{
				@Override
				public void clicked(InputEvent event, float x, float y)
					{
					game.setScreen(game.gameScreen);
					game.gameScreen.selectFireEngine(1);
					Gdx.input.setInputProcessor(null);
					}
				});
		}
	}
