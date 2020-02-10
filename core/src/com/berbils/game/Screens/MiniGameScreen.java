package com.berbils.game.Screens;

import static com.berbils.game.Kroy.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
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
import com.berbils.game.Tools.MapLoader;
import com.berbils.game.Entities.Minigame.Alien;
import com.berbils.game.Entities.Minigame.FireEngineFront;
import com.berbils.game.Entities.Minigame.Spawner;
import java.util.ArrayList;

/**
 * NEW CLASS
 * Creates the minigame screen
 */
public class MiniGameScreen extends PlayScreen
	{

  /** The game viewport, the window the camera can be mapped to */
	private Viewport gamePort;

	/** The game camera */
	public OrthographicCamera gameCam;

	/** Game instance */
	private Kroy game;

	/** Timer to create aliens */
	Timer.Task spawnTimer;

	/** The hud, displays remaining aliens, Fire engine current health, fire engine
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

	/**alien instance spawner*/
	private Spawner spawner;

	/** Array storing all aliens currently on screen */
	private ArrayList<Alien> aliens = new ArrayList<Alien>();

	// Box2d Object Managers

	/** Array storing all aliens to be deleted on update */
	private ArrayList<Alien> aliensToBeDeleted = new ArrayList<Alien>();

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

		//Create HUD for player
		this.hud = new HUD(this.game.batch, 3);

		// Create inital entities
		createSpawner();
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
		this.maploader = new MapLoader("MinigameMap/Minigame.tmx");
		this.renderer = new OrthoCachedTiledMapRenderer(maploader.map, 1 / Kroy.PPM);
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
		// Render Box2d Fixtures
		this.b2dr = new Box2DDebugRenderer();
		}

	/**
	 * Set the camera's view of the whole game
   */
  private void updateCamera() {
    Vector2 mapDims = this.maploader.getDims().cpy().scl(PPM);	
		Vector3 position = new Vector3(mapDims.x / 2, mapDims.y / 2 - 80, 0);
		this.gameCam.position.set(position);
		this.gameCam.update();
		this.game.batch.setProjectionMatrix(this.gameCam.combined);
	}
	
	/**
	 * Creates a player which is a fire engine, the type of
	 * fire engine is the same used in the main game
	 */
	private void createPlayer() {
		switch (this.game.gameScreen.getSelectedFireEngineIndex()) {
			case 1:
				Projectiles largewaterProjectile = new SimpleBulletCircle(3f, 1f, 25, 0, Kroy.WATER_PROJECTILE_TEX, this);
				Weapon largeFireEngWeapon = new BasicProjectileSpawner(5, largewaterProjectile);
				this.player = new FireEngineFront(this, new Vector2(6f, 3f), largeFireEngWeapon, 800, 15, 200, Kroy.HEAVY_FIRE_ENGINE_TEX);
				break;
			default:
				Projectiles waterProjectile = new SimpleBulletCircle(3f, 0.75f, 25, 0, Kroy.WATER_PROJECTILE_TEX, this);
				Weapon baseFireEngWeapon = new BasicProjectileSpawner(5, waterProjectile);
				this.player = new FireEngineFront(this, new Vector2(5f, 2.5f), baseFireEngWeapon, 400, 20, 100, Kroy.BASE_FIRE_ENGINE_TEX);
				break;
		}
		//Spawn player in bottom left quarter of the map
		this.player.spawn(new Vector2((this.maploader.getDims().cpy().x / 4), 1));
	}

	/**
	 * Create a UFO to spawn in the aliens
	 */
	private void createSpawner(){
		this.spawner = new Spawner(this, new Vector2(1, 0.5f), 30, Kroy.BASE_FIRE_ENGINE_TEX);
		this.spawner.spawn(new Vector2((this.maploader.getDims().cpy().x / 4), 12));
	}

	/**
	 * Creates an alien and adds it to the list of
	 * all aliens on the screen. Ends the game when all
	 * aliens have been spawned
	 */
	private void createAlien() {
		if (this.alienTotal > 0) {
			// Stop the UFO and then randomise it's next direction
			this.spawner.randomiseTrajectory();
			// Create an alien and spawn it
			Alien alien = new Alien(this, new Vector2(1.5f, 0.75f), 5, 100, Kroy.BASE_FIRE_ENGINE_TEX);
			alien.spawn(this.spawner.getBody().getPosition());
			this.aliens.add(alien);
			this.alienTotal -= 1;
		} else {
			if (this.aliens.size() <= 0) {
				// Return to main game
				this.getGame().setScreen(game.gameScreen);
			}
		}
	}

	/**
	 * Destroys all objects queued for deletion from the world and removes
	 * them from the queue
	 */
	private void destroyObjects() {
		// Check if any aliens are dead
		for (Alien alien : this.aliens) {
			if (alien.getHealth() <= 0) {
				this.world.destroyBody(alien.getBody());
				this.aliensToBeDeleted.add(alien);
			}
		}
		this.aliens.removeAll(this.aliensToBeDeleted);
	}

	/**
	 * Scale objects based on their distance from the player
	 */
	private void scaleObjects() {
		MapProperties prop = this.maploader.map.getProperties();
    float mapHeight = prop.get("height", Integer.class);
		for (Alien alien : this.aliens) {
			float yPos = alien.getBody().getPosition().y;
			float scale = (float) (((mapHeight - yPos) == 0) ? 0 : Math.pow(1.25, ((mapHeight - yPos) - 1))) / 10;
			System.out.println(scale);
			alien.scaleEntity(scale);
		}
	}

	/**
	 * A function to store all code to move entities
	 */
	private void moveEntities() {
		// Only move UFO if not beaming aliens
		boolean allowUFOMovement = true;
	
    for (Alien alien : this.aliens) {
			// If first spawned, move directly down until correct axis
			if (alien.getBody().getPosition().y > 10) {
				alien.getBody().applyForceToCenter(0, -alien.getSpeed() * 4, true);
				allowUFOMovement = false;
			} else {
				// Move towards the player
				alien.moveTowards(this.player.getBody().getPosition());
			}
		}

		// Allow UFO to move again if all aliens beamed down
		this.spawner.move(allowUFOMovement);
	}

  @Override
  public void show() {
		System.out.println("Render Minigame");

		// Creates a random number of aliens between 1-11
		this.alienTotal = 1+ (int) (Math.random() * 10.0);
		
		//Create a single alien every 3 seconds
		this.spawnTimer = Timer.schedule(new Task() {
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
		scaleObjects();

		// Draw Sprites
		this.game.batch.begin();
		this.spriteHandler.updateAndDrawAllSprites(this.game.batch);
		this.game.batch.end();

		// Update HUD
		String[] labelNames = { "Water", "Score", "Aliens Left" };
		int[] labelValues = { this.player.currentWater, this.getPlayerScore(), this.alienTotal };
		this.hud.update(labelNames, labelValues);

		// Move alien and UFO
		moveEntities();

		// Clean up game
    destroyObjects();
		
		// update the camera
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
		this.gamePort.update(width, height);
		}

	/**
	 * A getter for the camera for the game
	 *
	 * @return returns the game camera
	 */
	public OrthographicCamera getCamera()
	{
	return this.gameCam;
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
			this.spawnTimer.cancel();
		}

	/** Disposes of everything */
	@Override
	public void dispose()
		{
		}
}
