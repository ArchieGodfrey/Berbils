package com.berbils.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

/**
 * Creates the Heads up Display for the user
 */
public class HUD implements Disposable
	{
	public Stage stage;
	Label waterLabel;
	Label healthLabel;
	Label scoreLabel;
	Label FPSLabel;
	Label ScoreLabel;
	private Viewport viewport;
	private Integer health;
	private Integer water;
	private Integer score;
	private Integer FPS;
	private FireEngine player;
	private PlayScreen screen;

	/**
	 *Creates the viewport, labels and stage for the HUD
	 *
	 * @param sb		spriteBatch
	 *
	 * @param player	The player
	 *
	 * @param screen	The screen that the player is on
	 */
	public HUD(SpriteBatch sb, FireEngine player, PlayScreen screen)
		{
		health = player.currentHealth;
		water = player.currentWater;
		this.screen = screen;
		viewport = new FitViewport(Kroy.V_WIDTH,
								   Kroy.V_HEIGHT,
								   new OrthographicCamera());
		stage = new Stage(viewport, sb);
		Table table = new Table();
		table.top();
		table.setFillParent(true); // Table is size of stage
		healthLabel =
			new Label(
				String.format("%s %03d", "Health: ", health),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		waterLabel =
			new Label(
				String.format("%s %3d", "Water: ", water),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		FPSLabel = new Label(String.format("%s %3d", "FPS: ", FPS),
							 new Label.LabelStyle(new BitmapFont(),
												  Color.WHITE));

		ScoreLabel = new Label(String.format("%s %4d", "Score: ", score),
							 new Label.LabelStyle(new BitmapFont(),
												  Color.WHITE));
		table.add(waterLabel).padTop(10).expandX();
		table.add(FPSLabel).padTop(10).expandX();
		table.add(ScoreLabel).padTop(10).expandX();
		table.add(healthLabel).padTop(10).expandX();
		stage.addActor(table);
		}

	/**
	 *  Updates all the labels
	 */
	public void update()
		{
		this.updateHealth(player.currentHealth);
		this.updateWater(player.currentWater);
		this.updateFPS(Gdx.graphics.getFramesPerSecond());
		this.updateScore(this.screen.getPlayerScore());
		}

	/**
	 * Updates the health label
	 * @param health new player health value
	 */
	private void updateHealth(Integer health)
		{
		healthLabel.setText(String.format("%s %3d", "Health: ", health));
		}

	/**
	 * Updates the water label
	 * @param water	player's fire engine current water value
	 */
	private void updateWater(Integer water)
		{
		waterLabel.setText(String.format("%s %3d", "Water: ", water));
		}

	/**
	 * Updates the fps label
	 * @param fps the fps value
	 */
	private void updateFPS(Integer fps)
		{
		FPSLabel.setText(String.format("%s %3d", "FPS: ", fps));
		}

	/**
	 * Updates the score label
	 * @param score	the new score value
	 */
	private void updateScore(int score)
		{
		ScoreLabel.setText(String.format("%s %3d", "Score: ", score));
		}

	/**
	 * Sets the player to be the new player passedin
	 *
	 * @param player new FireEngine instance the player is using
	 */
	public void setPlayer(FireEngine player)
		{
		this.player = player;
		}

	@Override
	public void dispose()
		{
		stage.dispose();
		}
	}
