package com.berbils.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Kroy;
import com.berbils.game.Utils;

/**
 *  Creates a title screen which consists of a single large texture in the
 *  middle of the screen. Has the ability to display indefinitely or update
 *  onto a different screen after a set amount of time
 */
public class TitleScreen implements Screen
	{

	/** The size of the window the user can see through */
	private Viewport viewport;

	/** The game instance */
	private Game game;

	/** The title texture */
	private Texture title;

	/** the titleSize in pixels*/
	private Vector2 titleSize;

	/** the titlePosition in pixels */
	private Vector2 titlePos;

	/** The spritebatch */
	private SpriteBatch spriteBatch;

	/** How long the screen has been displayed for */
	private float timeSinceInitShow;

	/** How long the screen should be displayed for */
	private float timeToShow;

	/** Whether the screen can only be display for a set amount of time or
	 * indefinitely */
	private boolean timeEnabled;

	/** The next screen to change to */
	private Screen nextScreen;

	/**
	 * Creates the viewport,stage and calculates padding value.Also calculates
	 * the title position and size
	 *
	 *
	 * @param spriteBatch 	spriteBatch
	 *
	 * @param titlePath	  	The file path of the texture to be used as a title
	 *
	 * @param titlePath		The file path to the texture for the title that
	 *                         will be displaed
	 */
	public TitleScreen(Game game, SpriteBatch spriteBatch, String titlePath)
		{
		this.game = game;
		this.viewport = new FitViewport(Kroy.V_WIDTH, Kroy.V_HEIGHT);
		this.title = Kroy.assets.get(titlePath);
		this.titleSize = new Vector2(Kroy.V_WIDTH / 2, Kroy.V_HEIGHT / 2);
		this.titlePos = new Vector2(Kroy.V_WIDTH / 4, Kroy.V_HEIGHT / 4);
		this.spriteBatch = spriteBatch;
		}

	/**
	 * This method resets the timeSinceInitShow variable back to zero on
	 * initial screen show
	 */
	@Override
	public void show()
		{
		this.timeSinceInitShow = 0;
		}

	/***
	 * Renders the title
	 *
	 * @param delta The time in seconds that have elapsed in world time
	 * 	  	                (Excludes time taken to draw, render etc) since the
	 * 	  	                last Gdx delta call.
	 */
	@Override
	public void render(float delta)
		{
		Utils.clearScreen();
		this.spriteBatch.begin();
		this.spriteBatch.draw(this.title,
							  this.titlePos.x,
							  this.titlePos.y,
							  this.titleSize.x,
							  this.titleSize.y);
		this.spriteBatch.end();
		if (timeEnabled)
			this.timeSinceInitShow += delta;
		if (this.timeSinceInitShow > this.timeToShow) {
			this.game.setScreen(this.nextScreen);
		}
		}


	@Override
	public void resize(int width, int height)
		{
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

	/**
	 *
	 * This methods creates a timer that allows the screen to only display
	 * for a set amount of time before changing to a different once
	 *
	 * @param timeToShow how long in seconds the screen will be displayed for
	 *
	 * @param toChangeTo The screen that will be displaed to the user after
	 *                   the length of time determined by timeToShow
	 */
	public void setTimer(float timeToShow, Screen toChangeTo)
		{
		this.timeEnabled = true;
		this.timeToShow = timeToShow;
		this.nextScreen = toChangeTo;
		}
	}
