package com.berbils.game.Screens;

import static com.berbils.game.Kroy.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Entities.ProjectileSpawners.*;
import com.berbils.game.Entities.ProjectileSpawners.ProjectileTypes.*;
import com.berbils.game.Handlers.GameContactListener;
import com.berbils.game.Handlers.SpriteHandler;
import com.berbils.game.Kroy;
import com.berbils.game.Scenes.HUD;
import com.berbils.game.Tools.InputManager;
import com.berbils.game.Tools.MapLoader;
import com.berbils.game.Entities.Minigame.Alien;
import com.berbils.game.Entities.Minigame.FireEngineFront;
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

	// Game objects

	/** The player */
	private FireEngineFront player;

	/** Array storing all aliens currently on screen */
	private ArrayList<Alien> aliens = new ArrayList<Alien>();

	// Box2d Object Managers

	/** Array storing all bodies to be deleted on update */
	private ArrayList<Body> toBeDeleted = new ArrayList<Body>();

	/** PlayScreen sprite Handler */
	private SpriteHandler spriteHandler;

	/** The number of aliens in this round */
	private int alienTotal;


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
		// NEED TO UPDATE
		createPlayer();
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
            Kroy.V_WIDTH,
            Kroy.V_HEIGHT,
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
		maploader = new MapLoader("MinigameMap/Minigame.tmx");
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
		this.spriteHandler = new SpriteHandler(this, Kroy.MINIGAME_MAP_TEX, maploader.getDims().cpy());
		this.mapColliders = maploader.getColliders(this.world);
		this.mapBorders = maploader.getBorders(this.world);
		// Render Box2d Fixtures
		this.b2dr = new Box2DDebugRenderer();
		}

	/**
	 * Set the camera's view of the whole game. Only needs
	 * to be set once as it does not move.
   */
  private void updateCamera() {
    Vector2 mapDims = this.maploader.getDims().cpy().scl(PPM);	
		Vector3 position = new Vector3(mapDims.x / 2, mapDims.y / 2, 0);
    this.gameCam.position.set(position);
		this.gameCam.update();
		this.game.batch.setProjectionMatrix(this.gameCam.combined);
		this.renderer.setView(this.gameCam);
	}
	
	/**
	 * Creates a player which is a fire engine, the type of
	 * fire engine is the same used in the main game
	 */
	private void createPlayer() {
		switch (this.game.gameScreen.getSelectedFireEngineIndex()) {
			case 1:
				Projectiles largewaterProjectile = new SimpleBulletCircle(4f, 0.4f, 20, 3.5f, Kroy.WATER_PROJECTILE_TEX, this);
				Weapon largeFireEngWeapon = new BasicProjectileSpawner( 10, largewaterProjectile);
				this.player = new FireEngineFront(this, new Vector2(6f, 3f), largeFireEngWeapon, 800, 15, 200, Kroy.HEAVY_FIRE_ENGINE_TEX);
				break;
			default:
				Projectiles waterProjectile = new SimpleBulletCircle(5f, 0.25f, 10, 3, Kroy.WATER_PROJECTILE_TEX, this);
				Weapon baseFireEngWeapon = new BasicProjectileSpawner( 20, waterProjectile);
				this.player = new FireEngineFront(this, new Vector2(5f, 2.5f), baseFireEngWeapon, 400, 20, 100, Kroy.BASE_FIRE_ENGINE_TEX);
				break;
		}
		//Spawn player in bottom left quarter of the map
		this.player.spawn(new Vector2((this.maploader.getDims().cpy().x / 4), 0));
	}

	/**
	 * Creates an alien and adds it to the list of
	 * all aliens on the screen. Ends the game when all
	 * aliens have been spawned
	 */
	private void createAlien() {
		if (this.alienTotal > 0) {
			Alien alien = new Alien(this, new Vector2(1, 0.5f), 5, 100, Kroy.BASE_FIRE_ENGINE_TEX);
			alien.spawn(new Vector2((int) (Math.random() * 10.0),10));
			this.aliens.add(alien);
			this.alienTotal -= 1;
		} else {
			// Return to main game
			this.getGame().setScreen(game.gameScreen);
		}
	}

	/**
	 * Adds the body to an array that on each update will be iterated
	 * through, destroying each body inside the array
	 *
	 * @param toDestroy body to remove from the world
	 */
	public void destroyBody(Body toDestroy)
		{
    	this.toBeDeleted.add(toDestroy);
  		}

	/**
	 * Destroys all objects queued for deletion from the world and removes
	 * them from the queue
	 */
	private void destroyObjects() {
    for (int i = 0; i < this.toBeDeleted.size(); i++) {
      this.world.destroyBody(this.toBeDeleted.get(i));
      this.toBeDeleted.remove(i);
    }
	}

	/**
	 * Scale objects based on their distance from the player
	 */
	private void scaleObjects() {
		MapProperties prop = this.maploader.map.getProperties();
    float mapHeight = prop.get("height", Integer.class);
		for (Alien alien : this.aliens) {
			float yPos = alien.getBody().getPosition().y;
			float scale = (mapHeight - yPos) * (1 / Kroy.PPM);
			alien.scaleEntity(scale);
		}
	}

  @Override
  public void show() {
		System.out.println("Render Minigame");

		// Creates a random number of aliens between 1-11
		this.alienTotal = 1+ (int) (Math.random() * 10.0);
		
		//Create a single alien every 3 seconds
		Timer.schedule(new Task() {
			@Override
			public void run() {
				createAlien();
			}
		}, 1, 3 );
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

		// Get player input
		if (Gdx.input.isTouched()) {
      Vector3 mousePosInWorld = this.gameCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).scl(1/ Kroy.PPM);
      if (player.currentWater > 0) {
        player.fire(new Vector2(mousePosInWorld.x, mousePosInWorld.y));
      }
    }

		// Progress the world
		this.world.step(1 / 60f, 6, 2);

		//Scale objects CURRENTLY NOT WORKING
		//scaleObjects();

		// Draw Sprites
		this.game.batch.begin();
		this.spriteHandler.updateAndDrawAllSprites(this.game.batch);
		this.game.batch.end();

		// Clean up game
    destroyObjects();

    for (Alien alien : this.aliens) {
			alien.moveTowards(this.player.getBody().getPosition());
		}

		// Render the map and update the camera
		this.renderer.render();
		updateCamera();

		// If change false to true, the box2D debug renderer will render box2D
		// body outlines
		if(true) {
			b2dr.render(this.world, this.gameCam.combined.scl(PPM));
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
