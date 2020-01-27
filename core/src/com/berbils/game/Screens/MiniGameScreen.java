package com.berbils.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.berbils.game.Kroy;

/**
 * NEW CLASS
 * Creates the minigame screen
 */
public class MiniGameScreen extends PlayScreen
	{

    /** The game viewport, the window the camera can be mapped to */
	private Viewport gamePort;

	/**
	 * TODO
	 *
	 * @param game the game instance
     * @param batch the batch to draw to
	 */
	public MiniGameScreen(Kroy game, Batch batch)
		{
        super(game, batch);
        createCamera();
        }
        
    /**
	 * Creates the Camera
	 *
	 * <p>This method creates a new Orthographic Camera, then assigns a Viewport and centres the
	 * camera. </P>
	 */
	private void createCamera()
    {
    gameCam = new OrthographicCamera();
    // Create a FitViewPort to maintain aspect ratio across screen sizes
    gamePort = new ExtendViewport(
            Kroy.V_WIDTH / ( Kroy.PPM * Kroy.CAMERA_SCALAR ),
            Kroy.V_HEIGHT / ( Kroy.PPM * Kroy.CAMERA_SCALAR ),
            gameCam);
    gameCam.position.set(gamePort.getWorldWidth(),
                         gamePort.getWorldHeight(),
                         0);
    }


  @Override
  public void show() {
      System.out.println("Render Minigame");
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
	}

	/** updates the gamePort width and height if the game window gets resized */
	@Override
	public void resize(int width, int height)
		{
		gamePort.update(width, height);
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
