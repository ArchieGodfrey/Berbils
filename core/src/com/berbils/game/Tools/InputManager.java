package com.berbils.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.berbils.game.Entities.FireEngines.FireEngine;
import com.berbils.game.Kroy;

public class InputManager {
  private Vector2 mousePos = new Vector2();
  private OrthographicCamera camera;

  public InputManager(OrthographicCamera cam){this.camera = cam;}


  /**
   * Gets user input from keyboard
   *
   * @param delta
   * @param player
   */
  public void handlePlayerInput(FireEngine player, float delta, Kroy game) {
    int torque = 0;

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      player
          .getBody()
          .applyForce(
              new Vector2(
                  player.speed * MathUtils.cos(player.getBody().getAngle()),
                  player.speed * MathUtils.sin(player.getBody().getAngle())),
              player.getBody().getWorldCenter(),
              true);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      player
          .getBody()
          .applyForce(
              new Vector2(
                  -player.speed * MathUtils.cos(player.getBody().getAngle()),
                  -player.speed * MathUtils.sin(player.getBody().getAngle())),
              player.getBody().getWorldCenter(),
              true);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      torque -= 3;
      player.getBody().setAngularVelocity(torque);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      torque += 3;
      player.getBody().setAngularVelocity(torque);
    }
    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
    {

      game.setScreen(game.pauseScreen);
    }

    if (Gdx.input.isTouched()) {
      Vector3 mousePosInWorld = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).scl(1/ Kroy.PPM);
      mousePos.x = mousePosInWorld.x;
      mousePos.y = mousePosInWorld.y;

      if (player.currentWater > 0) {
        player.fire(mousePos);
        // hud.updateWater(player.water);
      }
    }
  }
}
