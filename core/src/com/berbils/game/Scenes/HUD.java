package com.berbils.game.Scenes;

import java.util.ArrayList;

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
import com.berbils.game.Kroy;

/**
 * Creates the Heads up Display for the user
 */
public class HUD implements Disposable
	{
	private Stage stage;
	private Viewport viewport;
	private ArrayList<Label> labels;

	/**
	 * UPDATED @author Archie Godfrey
	 * Changed paramaters to be more self contained
	 * 
	 * Creates the viewport, labels and stage for the HUD
	 *
	 * @param sb		spriteBatch
	 *
	 * @param numberOfLabels The number of labels to be drawn to the screen
	 */
	public HUD(SpriteBatch sb, int numberOfLabels)
		{
		this.viewport = new FitViewport(Kroy.V_WIDTH,
								   Kroy.V_HEIGHT,
								   new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		Table table = new Table();
		table.top();
		table.setFillParent(true); // Table is size of stage

		this.labels = new ArrayList<Label>();

		// Create predefined number of labels and add them to the stage and list
		for (int i = 0; i < numberOfLabels; i++) {
			Label label = new Label(String.format("%s %03d", "Temp: ", i), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			this.labels.add(label);
			table.add(label).padTop(10).expandX();
		}
		stage.addActor(table);
		}

	/**
	 * NEW Method @author Archie Godfrey
	 * Updates all the labels with names and values given by two equal length arrays
	 * 
	 * @param labelNames	The name to be displayed for each label
	 * @param labelValues	The value to be displayed for each label
	 */
	public void update(String[] labelNames, int[] labelValues)
		{
			if (labelNames.length == this.labels.size() && labelValues.length == this.labels.size()) {
				for (int i = 0; i < this.labels.size(); i++) {
					this.labels.get(i).setText(String.format("%s %s %3d", labelNames[i], ": ", labelValues[i]));
				}
				this.stage.draw();
			} else {
				System.out.println("Not enough label names or values supplied");
			}
		}

	/**
	 * NEW Method @author Archie Godfrey
	 * Get the stage the HUD is using
	 * 
	 * @return the stage the HUD is using
	 */
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void dispose()
		{
		this.stage.dispose();
		}
	}
