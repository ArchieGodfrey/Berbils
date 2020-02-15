package com.berbils.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Kroy;
import com.berbils.game.Utils;

import java.util.ArrayList;

/**
 * An abstract class used to create menu screens consisting of any number of
 * buttons a title texture
 */
public abstract class BasicMenu implements Screen
	{

	/** The list of buttons that need to be drawn */
	protected ArrayList<TextButton> menuButtons;

	/** The window the user can see through */
	private Viewport viewport;

	/** The stage, where the buttons are displayed */
	protected Stage stage;

	/** The amount of padding at the top of each button */
	private float padding;

	/** The title texture */
	private Texture title;

	/** the titleSize in pixels*/
	private Vector2 titleSize;

	/** the titlePosition in pixels */
	private Vector2 titlePos;

	/** The spritebatch */
	private SpriteBatch spriteBatch;

	/**
	 * Creates the viewport,stage and calculates padding value for each item
	 * in the screen.Also calculates the title position and size and
	 * generates each menu option as a button and stores it in the
	 * menuButtons list.
	 *
	 *
	 * @param spriteBatch 	spriteBatch
	 *
	 * @param titlePath	  	The file path of the texture to be used as a title
	 *
	 * @param menuOptions	A string array of menu options (An array of the
	 *                         text that will appear on buttons)
	 */
	public BasicMenu(SpriteBatch spriteBatch, String titlePath, String[] menuOptions)
		{
		/** 
		 * NEW Lines @author Archie Godfrey 
		 * Moved createMenuOptions into Utils
		 * so it can be used throughout the game
		 * */
		this.menuButtons = Utils.createMenuOptions(menuOptions);
		this.spriteBatch = spriteBatch;
		this.viewport = new FitViewport(Kroy.V_WIDTH, Kroy.V_HEIGHT);
		this.stage = new Stage(viewport, spriteBatch);
		this.title = Kroy.assets.get(titlePath);
		int noOfItemRows = menuOptions.length + 1;
		this.padding = Kroy.V_HEIGHT / noOfItemRows / 2;
		this.titleSize = new Vector2(Kroy.V_WIDTH / 2,
									 Kroy.V_HEIGHT / noOfItemRows);
		this.titlePos = new Vector2(Kroy.V_WIDTH / 4,
									Kroy.V_HEIGHT - this.titleSize.y);
		}

	/**
	 * Removes the button at the index passed
	 *
	 * @param indexToRemove index of menu Text button to remove
	 */
	public void removeButton(int indexToRemove)
		{
		this.menuButtons.set(indexToRemove, null);
		this.show();
		}

	/**
	 * This method creates the Menu buttons on the screen
 	 */
	@Override
	public void show()
		{
		this.stage = new Stage(viewport, spriteBatch);
		Gdx.input.setInputProcessor(this.stage);
		/** NEW Line @author Archie Godfrey */
		stage.addActor(Utils.createTable(this.menuButtons, this.titleSize, this.padding));
		}

	/***
	 * Renders the title and menu Buttons
	 *
	 * @param delta The time in seconds that have elapsed in world time
	 * 	  	                (Excludes time taken to draw, render etc) since the
	 * 	  	                last Gdx delta call.
	 */
	@Override
	public void render(float delta)
		{
		Utils.clearScreen();
		stage.act();
		stage.draw();
		this.spriteBatch.begin();
		this.spriteBatch.draw(this.title,
							  this.titlePos.x,
							  this.titlePos.y,
							  this.titleSize.x,
							  this.titleSize.y);
		this.spriteBatch.end();
		}

	/**
	 * Resizes the title and buttons if the window is resized
	 *
	 * @param width new width of the window
	 * @param height new height of the window
	 */
	@Override
	public void resize(int width, int height)
		{
		stage.getViewport().update(width, height);
		}

	@Override
	public void pause()
		{
		}

	@Override
	public void resume()
		{
		}

	@Override
	public void hide()
		{
		}

	@Override
	public void dispose()
		{
		}
	}
