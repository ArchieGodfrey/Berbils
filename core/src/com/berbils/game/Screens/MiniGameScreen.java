package com.berbils.game.Screens;

import static com.berbils.game.Kroy.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Entities.FireStation.FireStation;
import com.berbils.game.Entities.ProjectileSpawners.*;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.*;
import com.berbils.game.Entities.Towers.Tower;
import com.berbils.game.Handlers.GameContactListener;
import com.berbils.game.Handlers.SpriteHandler;
import com.berbils.game.Kroy;
import com.berbils.game.Scenes.HUD;
import com.berbils.game.Tools.InputManager;
import com.berbils.game.Tools.MapLoader;
import com.berbils.game.Entities.Aliens.Alien;
import java.util.ArrayList;

/**
 * NEW CLASS
 * Creates the minigame screen
 */
public class MiniGameScreen extends PlayScreen
	{

    /** The game viewport, the window the camera can be mapped to */
	private Viewport gamePort;

	private Alien alien;

	/** The game camera */
	public OrthographicCamera gameCam;
	// Set to public temporarily, can be private once more when tower loading from map is done


	/** the current index of the fire engine selected, this index relates to
	 * the button to select it on the SelectFireEngineScreen */
	public int fireEngineSelectedIndex;

	/** the number of fire engines alive in this screen instance */
	private int fireEnginesAlive;

	/** The number of towers left alive in this screen instance */
	private int towersAlive;

	/** Game instance */
	private Kroy game;

	/** The hud, displays FPS, SCORE, Fire engine current health, fire engine
	 * current water
	 */
	private HUD hud;

  // Map loading objects
	/** The MapLoader that will be used to obtain the positions of towers and
	 *  the fire station in addition to any colliders and boundaries
	 */
	private MapLoader maploader;

	/** The tiled map render */
	private OrthoCachedTiledMapRenderer renderer;

	private Array<Body> mapColliders, mapBorders;

	// Box2d variables
	/** The game world, where all of the sprites and Box2D objects are
	 * created onto     */
	private World world;

	/** The Box2D debug renderer, this displays all Box2D shape outlines
	 * regardless of whether there are textures or not
	 */
	private Box2DDebugRenderer b2dr;


	/** Pre-defined weapon types for the Play Screen */
	public Weapon basicWeapon,spokeWeapon,randomDirWeapon,baseFireEngWeapon,
		largeFireEngWeapon;
	// Game objects
	/** Array containing all fire engine instances */;
	private ArrayList<FireEngine> fireEngineArrayList = new ArrayList<>();

	/** large slower fire engine with a higher damage weapon and higher
	 * health and water capacity
	 */
	private FireEngine largeFireEngine;

	/** Standard fire engine with standard states */
	private FireEngine normalFireEngine;

	/** The player */
	private FireEngine player;

	/** Array containing all towers within this screen instance */
	private Array<Tower> towers;

	/** The fire station, the user can change which fire engine is currently
	 * selected and refresh its health and water */
	private FireStation fireStation;

	/** Pre-defined projectile types for use in PlayScreen */
	private Projectiles standardProjectile,
			slowLargeExplosiveProjectile,
			smallFastProjectile,
			waterProjectile,
			largewaterProjectile;

	/** Array List storing all pre-defined projectiles */
	private ArrayList<Projectiles> projectileList = new ArrayList<>();

	/** Array List storing all pre-defined Weapons */
	private ArrayList<Weapon> weaponList = new ArrayList<>();
	// Box2d Object Managers

	/** Array storing all bodies to be deleted on update */
	private ArrayList<Body> toBeDeleted = new ArrayList<Body>();

	/** PlayScreen input manager */
	private InputManager inputManager;

	/** PlayScreen sprite Handler */
	private SpriteHandler spriteHandler;

	/** Fire engine spawn position */
	private Vector2 fireEngSpawnPos;

	/** The players score */
	private int playerScore;


	/**
	 * TODO
	 *
	 * @param game the game instance
     * @param batch the batch to draw to
	 */
	public MiniGameScreen(Kroy game, Batch batch)
		{
		super(game, batch);
		this.game = game;
		createCamera();
		loadMap();
		createBox2DWorld();
        }
        
    /**
	 * Creates the Camera
	 *
	 * <p>This method creates a new Orthographic Camera, then assigns a Viewport and centres the
	 * camera. </P>
	 */
	private void createCamera()
    {
    this.gameCam = new OrthographicCamera();
    // Create a FitViewPort to maintain aspect ratio across screen sizes
    this.gamePort = new ExtendViewport(
            Kroy.V_WIDTH / ( Kroy.PPM * Kroy.CAMERA_SCALAR ),
            Kroy.V_HEIGHT / ( Kroy.PPM * Kroy.CAMERA_SCALAR ),
            this.gameCam);
    this.gameCam.position.set(this.gamePort.getWorldWidth(),
                         	  this.gamePort.getWorldHeight(),
                         	  0);
	}

		/**
	 * Load the map
	 *
	 * <p>Loads a Tiled Map and creates a Tiled map renderer
	 */
	private void loadMap()
		{
		maploader = new MapLoader("CityMap/Map.tmx");
		renderer = new OrthoCachedTiledMapRenderer(maploader.map, 1 / Kroy.PPM);
		}
	
	/**
	 * Creates the Box2d World
	 *
	 * <p>This method creates a new world, sets its contact listener, sets
	 * the screen instances sprite handler and loads all map colliders and
	 * the map borders. The box2D Debug renderer is also initalised here
	 * </p>
	 */
	private void createBox2DWorld()
		{
		// Create a world with 0 forces applied to it
		this.world = new World(new Vector2(0, 0), true);
		this.world.setContactListener(new GameContactListener());
		this.spriteHandler = new SpriteHandler(this, Kroy.CITY_MAP_TEX, maploader.getDims().cpy());
		this.mapColliders = maploader.getColliders(this.world);
		this.mapBorders = maploader.getBorders(this.world);
		// Render Box2d Fixtures
		this.b2dr = new Box2DDebugRenderer();
		}

	/**
   * Updates the camera every tick to follow the player
   * Also clamps the camera to prevent it viewing outside the map at which
   * point it no longer follows the player until they move away from the
   * boundaries again
   *
   * @param delta The time in seconds that have elapsed in world time
   * 	                (Excludes time taken to draw, render etc) since the
   * 	                last Gdx delta call.
   */
  public void updateCamera(float delta) {
    Vector2 mapDims = maploader.getDims().cpy().scl(PPM);
    float halfViewportWidth = this.gameCam.viewportWidth / 2;
    float halfViewPortHeight = this.gameCam.viewportHeight /2;

    Vector3 position = gameCam.position;
    Vector2 playerPos = alien.getBody().getPosition().scl(PPM);
    position.x = MathUtils.clamp(playerPos.x, halfViewportWidth, mapDims.x - halfViewportWidth);
    position.y = MathUtils.clamp(playerPos.y, halfViewPortHeight, mapDims.y - halfViewPortHeight);

    gameCam.position.set(position);
    gameCam.update();
  }


  @Override
  public void show() {
	  System.out.println("Render Minigame");
	  this.renderer.setView(this.gameCam);
	  this.alien = new Alien(this, new Vector2(1, 0.5f), 20, 100, Kroy.BASE_FIRE_ENGINE_TEX);
	  this.alien.spawn(new Vector2(200,220));
    }

  /**
   * Render To MiniGameScreen
   *
   * <p>This method draws the world, all sprites and HUD
   *
   * @param delta
   */
  @Override
  public void render(float delta)
	  {
        // MUST BE FIRST: Clear the screen each frame to stop textures blurring
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Progress the world
		this.world.step(1 / 60f, 6, 2);

		// Render the map
		this.renderer.render();

		// Draw Sprites
		this.game.batch.begin();
		this.spriteHandler.updateAndDrawAllSprites(this.game.batch);
		this.game.batch.end();

		updateCamera(delta);

		// If change false to true, the box2D debug renderer will render box2D
		// body outlines
		if(true) {
		b2dr.render(this.world, gameCam.combined.scl(PPM));
		}
	}

	/** updates the gamePort width and height if the game window gets resized */
	@Override
	public void resize(int width, int height)
		{
		gamePort.update(width, height);
		}

	/**
	 * Getter for the screens sprite handler
	 *
	 * @return	returns the screens {@link SpriteHandler}
	 */
	public SpriteHandler getSpriteHandler()
	{
	return this.spriteHandler;
	}

	/**
	 * A getter for the screen world
	 *
	 * @return returns the world
	 */
	@Override
	public World getWorld()
	{
	return this.world;
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

	/** Disposes of everything */
	@Override
	public void dispose()
		{
		}
}
