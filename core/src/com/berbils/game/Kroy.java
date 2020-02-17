package com.berbils.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.berbils.game.Screens.*;

public class Kroy extends Game
	{
	public static final float BORDER_DEPTH = 1.0f;
	public static final float SCENERY_FRICTION = 0.0625f;
	public static final float PPM = 32f;
	public static final float CAMERA_SCALAR = 0.0625f;
	public static final short CAT_FRIENDLY = 0x0001;
	public static final short CAT_ENEMY = 0x0002;
	public static final short CAT_TOWER_SENSOR = 0x0004;
	public static final short CAT_SCENERY = 0x0008;
	public static final short CAT_PROJECTILE_FRIENDLY = 0x0010;
	public static final short CAT_PROJECTILE_ENEMY = 0x0020;
	public static final short CAT_COLLIDE_NOTHING = 0x0040;
	public static final short MASK_TOWER_SENSOR = CAT_FRIENDLY | CAT_ENEMY;
	public static final short MASK_ENEMY = CAT_FRIENDLY | CAT_PROJECTILE_FRIENDLY | CAT_SCENERY;
	public static final short MASK_FRIENDLY_PROJECTILE = CAT_ENEMY | CAT_SCENERY;
	public static final short MASK_ENEMY_PROJECTILE = CAT_FRIENDLY | CAT_SCENERY;
	public static final short MASK_FRIENDLY =
		CAT_ENEMY | CAT_SCENERY | CAT_PROJECTILE_ENEMY | CAT_TOWER_SENSOR | CAT_FRIENDLY;
	public static final short MASK_COLLIDE_NOTHING = 0x0000;
	public static final short MASK_UFO = 0x0025; // NEW Field @author Archie Godfrey
	public static final short MASK_SCENERY = -1;
	public static final String TUT_TOWER_ENGAGED_TEX = "TowerTextures/tutorialTowerEngaged.png";
	public static final String TUT_TOWER_DISENGAGED_TEX = "TowerTextures/tutorialTowerDisengaged.png";
	public static final String SPOKE_TOWER_ENGAGED_TEX = "TowerTextures/SpokeTowerEngaged.png";
	public static final String SPOKE_TOWER_DISENGAGED_TEX = "TowerTextures/SpokeTowerDisengaged.png";
	public static final String EXPLOSIVE_TOWER_ENGAGED_TEX =
		"TowerTextures/ExplosiveTowerEngaged.png";
	public static final String EXPLOSIVE_TOWER_DISENGAGED_TEX =
		"TowerTextures/ExplosiveTowerDisengaged.png";
	public static final String REG_PROJECTILE_TEX = "Projectiles/Regular Projectile.png";
	public static final String EXPLOSIVE_PROJECTILE_TEXTURE = "Projectiles/ExplosiveProjectile.png";
	public static final String EXPLOSION_TEX = "Projectiles/Explosion.png";
	public static final String BASE_FIRE_ENGINE_TEX = "FireEngine/fireEngineBase.png";
	public static final String ORANGE_FIRE_ENGINE_TEX = "FireEngine/OrangeFireEngine.png";
	public static final String GREEN_FIRE_ENGINE_TEX = "FireEngine/GreenFireEngine.png";
	public static final String HEAVY_FIRE_ENGINE_TEX = "FireEngine/fireEngineHeavy.png";
	public static final String OBSTACLE_TEX = "Obstacle.png";
	public static final String WATER_PROJECTILE_TEX = "Projectiles/waterProjectile.png";
	public static final String FIRESTATION_TEX = "FireStation/FireStation.png";
	public static int V_WIDTH;
	public static int V_HEIGHT;
	public static String NO_TEXTURE_TEX = "nullTexture.png";
	public static String HEALTH_BAR_TEX = "TowerTextures/HealthBar.png";
	public static String KROY_TITLE_TITLE = "ScreenTitles/KroyTitle.png";
	public static String SELECT_FIRE_ENGINE_TITLE = "ScreenTitles/SelectYourFireEngine.png";
	public static String GAME_OVER_TITLE = "ScreenTitles/Game Over.png";
	public static String FIRE_ENGINE_DESTROYED_TITLE = "ScreenTitles/FireEngineDestroyed.png";
	public static String YOUVE_WON_TITLE = "ScreenTitles/YouveWon.png";
	public static String GAME_PAUSED_TITLE = "ScreenTitles/GamePaused.png";
	public static String CITY_MAP_TEX = "CityMap/CityMap2.png";
	public static String MINIGAME_MAP_TEX = "MinigameMap/minigame.png"; //NEW
	public static String MINIGAME_UFO_TEX = "MinigameMap/ufo1.png"; //NEW
	public static String MINIGAME_ALIEN_TEX = "MinigameMap/alien.png"; //NEW
	// : Added minigame map texture
	public static String TOPDOWN_UFO_TEX = "ufo_above.png" ;//NEW
	public static AssetManager assets;
	public SpriteBatch batch;
	public PlayScreen gameScreen;
	public BasicMenu mainMenu;
	public PauseScreen pauseScreen;
	public TitleScreen fireEngineDestroyedScreen;
	public TitleScreen gameOverScreen;
	public TitleScreen winScreen;


	@Override
	public void create()
		{
		batch = new SpriteBatch();
		assets = new AssetManager();
		assets.load(Kroy.BASE_FIRE_ENGINE_TEX, Texture.class);
		assets.load(Kroy.ORANGE_FIRE_ENGINE_TEX, Texture.class);
		assets.load(Kroy.GREEN_FIRE_ENGINE_TEX, Texture.class);
		assets.load(Kroy.HEAVY_FIRE_ENGINE_TEX, Texture.class);
		assets.load(Kroy.REG_PROJECTILE_TEX, Texture.class);
		assets.load(Kroy.EXPLOSION_TEX, Texture.class);
		assets.load(Kroy.EXPLOSIVE_PROJECTILE_TEXTURE, Texture.class);
		assets.load(Kroy.TUT_TOWER_DISENGAGED_TEX, Texture.class);
		assets.load(Kroy.TUT_TOWER_ENGAGED_TEX, Texture.class);
		assets.load(Kroy.SPOKE_TOWER_DISENGAGED_TEX, Texture.class);
		assets.load(Kroy.SPOKE_TOWER_ENGAGED_TEX, Texture.class);
		assets.load(Kroy.EXPLOSIVE_TOWER_DISENGAGED_TEX, Texture.class);
		assets.load(Kroy.EXPLOSIVE_TOWER_ENGAGED_TEX, Texture.class);
		assets.load(Kroy.OBSTACLE_TEX, Texture.class);
		assets.load(Kroy.WATER_PROJECTILE_TEX, Texture.class);
		assets.load(Kroy.FIRESTATION_TEX, Texture.class);
		assets.load(Kroy.NO_TEXTURE_TEX, Texture.class);
		assets.load(Kroy.HEALTH_BAR_TEX, Texture.class);
		assets.load(Kroy.KROY_TITLE_TITLE, Texture.class);
		assets.load(Kroy.SELECT_FIRE_ENGINE_TITLE, Texture.class);
		assets.load(GAME_OVER_TITLE, Texture.class);
		assets.load(FIRE_ENGINE_DESTROYED_TITLE, Texture.class);
		assets.load(YOUVE_WON_TITLE, Texture.class);
		assets.load(GAME_PAUSED_TITLE, Texture.class);
		assets.load(CITY_MAP_TEX, Texture.class);
		assets.load(MINIGAME_MAP_TEX, Texture.class); //NEW: Added minigame map
		assets.load(MINIGAME_UFO_TEX, Texture.class); //New: Added minigame
		// ufo texture
		assets.load(MINIGAME_ALIEN_TEX, Texture.class); //New: Added minigame
		// alien
		assets.load(TOPDOWN_UFO_TEX, Texture.class); //NEW: Added UFO texture
		// texture asset
		assets.finishLoading();
		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();
		this.createAllScreens();
		setScreen(this.mainMenu);
		}

	public void createAllScreens()
		{
		this.gameScreen = new PlayScreen(this);
		this.mainMenu = new MenuScreen(this, batch);
		this.fireEngineDestroyedScreen = new TitleScreen(this,
														 batch,
														 Kroy.FIRE_ENGINE_DESTROYED_TITLE);
		this.gameOverScreen = new TitleScreen(this,
											  batch,
											  Kroy.GAME_OVER_TITLE);
		this.winScreen = new TitleScreen(this, batch, Kroy.YOUVE_WON_TITLE);
		this.pauseScreen = new PauseScreen(this, batch);
		}

	/**
	 * NEW Method @author Archie Godfrey
	 * Rather than dispose entites individually, dispose of the whole minigame and
	 * create a new instance everytime we enter
	 * 
	 * @return A fresh version of the minigame
	 */
	public MiniGameScreen getNewMinigameScreen() {
		return new MiniGameScreen(this, batch);
	}

	@Override
	public void dispose()
		{
		batch.dispose();
		assets.dispose();
		}

	@Override
	public void render()
		{
		super.render();
		}
	}
