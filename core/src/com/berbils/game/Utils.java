package com.berbils.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	}
