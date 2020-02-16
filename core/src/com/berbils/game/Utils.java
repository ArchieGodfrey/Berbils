package com.berbils.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class Utils
	{
	private Utils()
		{
		}

	public static void clearScreen()
		{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

	public static Drawable getColoredDrawable(int width, int height, Color color)
		{
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		TextureRegionDrawable drawable =
			new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		return drawable;
		}

	/**
	 * NEW Method @author Archie Godfrey
	 * Creates a table to present an array of buttons
	 * 
	 * @param menuButtons 	The buttons to display
	 * @param titleSize 	The padding above the table
	 * @param padding 		The padding between rows
	 * @return 				A table that can be added to a stage
	 */
	public static Table createTable(ArrayList<TextButton> menuButtons, Vector2 titleSize, float padding) {
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();
		// need to pad as the title takes up the top x amount of spae
		mainTable.pad(titleSize.y);
		for (TextButton eachButton : menuButtons) {

			//NEW: Calculate how much padding each button needs to be the set width,
			//and pad the button both left and right accordingly
			float width = eachButton.getWidth();
			float maxWidth = 196;
			
			float pad = (maxWidth-width)/2;
			eachButton.padLeft(pad);
			eachButton.padRight(pad);

			mainTable.row();
			mainTable.add(eachButton).padTop(padding);
		}
		return mainTable;
	}

	/**
	 * NEW Method @author Archie Godfrey
	 * Create an array of textbuttons with text
	 * from the given array of strings
	 * 
	 * @param menuOptions 	An array of strings containing the text to display on the buttons
	 * @return 				The menuOptions as buttons
	 */
	public static ArrayList<TextButton> createMenuOptions(String[] menuOptions) {
		ArrayList<TextButton> menuButtons = new ArrayList<>();
		
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		for (String option : menuOptions) {
			menuButtons.add(new TextButton(option, skin));
		}
		return menuButtons;
	}
	}
