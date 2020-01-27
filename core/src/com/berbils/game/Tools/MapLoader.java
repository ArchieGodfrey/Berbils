package com.berbils.game.Tools;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.berbils.game.Entities.ProjectileSpawners.Weapon;
import com.berbils.game.Entities.Towers.*;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

public final class MapLoader {
  public TiledMap map;
  public int PPM;

  private MapObjects colliders, spawns, towers;
  private BodyDef bodyDef;
  private FixtureDef fixtureDef;

  public static float BORDER_DEPTH = 0.25f;

  private static String MAP_OBJ_LAYER = "colliders";
  private static String MAP_SPAWN_LAYER = "spawns";
  private static String MAP_TOWER_LAYER = "towers";

  public static String ENGINE_SPAWN = "engineSpawn";
  public static Vector2 ENGINE_SPAWN_DEFAULT = new Vector2(3, 3);

  public MapLoader(String mapName) {
    this.map = new TmxMapLoader().load(mapName);
    this.PPM = map.getProperties().get("tileheight", Integer.class);
    this.colliders = this.map.getLayers().get(MAP_OBJ_LAYER).getObjects();
    this.spawns = this.map.getLayers().get(MAP_SPAWN_LAYER).getObjects();
    this.towers = this.map.getLayers().get(MAP_TOWER_LAYER).getObjects();

    // Reusable definitions wanted for the scenery objects
    this.bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    this.fixtureDef = new FixtureDef();
    fixtureDef.filter.maskBits = Kroy.MASK_SCENERY;
    fixtureDef.filter.categoryBits = Kroy.CAT_SCENERY;
    fixtureDef.friction = Kroy.SCENERY_FRICTION;
  }

  public void dispose() {
    this.map.dispose();
  }

  public Array<Body> getColliders(World world) {
    Array<Body> objectArray = new Array<Body>(false, this.colliders.getCount());

    // Reusable definitions wanted for the scenery objects
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.filter.maskBits = Kroy.MASK_SCENERY;
    fixtureDef.filter.categoryBits = Kroy.CAT_SCENERY;
    fixtureDef.friction = Kroy.SCENERY_FRICTION;

    System.out.println("Loading colliders from map files...");

    for (MapObject object : this.colliders) {
      PolygonShape shape = new PolygonShape();

      if (object instanceof RectangleMapObject) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        // Scale the rectangle down (libgdx treats the values as radiuses)
        shape.setAsBox(rectangle.width / 2 / PPM, rectangle.height / 2 / PPM);
        // Scale position of the rectangle down
        // libgdx origin is at the centre of the rectangle
        // tiled uses the bottom left corner hence adding the height/width
        float x = (rectangle.x + (rectangle.width / 2)) / PPM;
        float y = (rectangle.y + (rectangle.height / 2)) / PPM;
        bodyDef.position.set(x, y);
        System.out.printf("Rectangle: %s at %f, %f %n", object.getName(), x, y);
      } else if (object instanceof PolygonMapObject) {
        Polygon polygon = ((PolygonMapObject) object).getPolygon();
        // Scale the polygon's vertices down relative to it's first vertex
        float[] vertices = polygon.getVertices();

        // Don't try to initialise invalid colliders
        int numVertices = vertices.length / 2;
        if (numVertices > 8 || numVertices < 3) {
          System.out.printf("Skipping %s with %d vertices %n", object.getName(), numVertices);
          continue;
        }

        for (int i = 0; i < vertices.length; i++) {
          vertices[i] /= PPM;
        }
        shape.set(vertices);
        // Move it to the correct position
        float x = polygon.getX() / PPM;
        float y = polygon.getY() / PPM;
        bodyDef.position.set(x, y);
        System.out.printf("Polygon: %s at %f, %f %n", object.getName(), x, y);
      }

      Body body = world.createBody(bodyDef);
      fixtureDef.shape = shape;
      Fixture fixture = body.createFixture(fixtureDef);
      objectArray.add(body);
    }
    return objectArray;
  }

  public Array<Body> getBorders(World world) {
    Array<Body> objectArray = new Array<Body>(false, 4);
    // Getting map properties
    MapProperties prop = map.getProperties();
    float mapWidth = prop.get("width", Integer.class);
    float mapHeight = prop.get("height", Integer.class);

    PolygonShape borderShape = new PolygonShape();

    // Top and Bottom borders
    for (float height : new float[] {mapHeight + BORDER_DEPTH, -BORDER_DEPTH}) {
      // Divide box sizes by 2 because the setAsBox method uses radii
      borderShape.setAsBox(mapWidth / 2, BORDER_DEPTH);
      fixtureDef.shape = borderShape;
      bodyDef.position.set(mapWidth / 2, height);
      Body bottomBody = world.createBody(bodyDef);
      Fixture bottomFix = bottomBody.createFixture(fixtureDef);
      objectArray.add(bottomBody);
    }

    // Left and Right borders
    for (float x_offset : new float[] {mapWidth + BORDER_DEPTH, -BORDER_DEPTH}) {
      borderShape.setAsBox(BORDER_DEPTH, (mapHeight / 2) + (2 * BORDER_DEPTH));
      fixtureDef.shape = borderShape;
      bodyDef.position.set(x_offset, mapHeight / 2);
      Body rightBody = world.createBody(bodyDef);
      Fixture rightFix = rightBody.createFixture(fixtureDef);
      objectArray.add(rightBody);
    }

    System.out.println("Collider loading done.\n");
    return objectArray;
  }

  public Vector2 getEngineSpawn() {
    for (MapObject spawn : spawns) {
      if (spawn.getName().equals(MapLoader.ENGINE_SPAWN)) {
        Rectangle point = ((RectangleMapObject) spawn).getRectangle();
        System.out.println("Engine spawn found");
        return new Vector2(point.x / PPM, point.y / PPM);
      }
    }

    System.out.println("Engine spawn not found");
    return MapLoader.ENGINE_SPAWN_DEFAULT;
  }

  public Array<Tower> getTowers(PlayScreen screen) {
    Array<Tower> towerArr = new Array<Tower>();
    System.out.println("Loading towers...");

    for (MapObject tower : towers) {
      Ellipse towerShape = ((EllipseMapObject) tower).getEllipse();
      MapProperties props = tower.getProperties();
      int maxHealth = props.get("health", Integer.class);
      float size = props.get("size", Float.class);
      String engaged_tex = props.get("texEngaged", String.class);
      String disengaged_tex = props.get("texDisengaged", String.class);
      String weaponStr = props.get("weapon", String.class);
      Weapon weapon;
      if (weaponStr.equals("basicWeapon")) {
        weapon = screen.basicWeapon;
      } else if (weaponStr.equals("spokeWeapon")) {
        weapon = screen.spokeWeapon;
      } else if (weaponStr.equals("randomDirWeapon")) {
        weapon = screen.randomDirWeapon;
      } else if(weaponStr.equals("largeFireEngWeapon"))
      {
      	weapon = screen.largeFireEngWeapon;
	  }
      else {
        throw new IllegalArgumentException("Bad weapon type in tower definition");
      }
      int range = (int) towerShape.height / PPM;
      Vector2 pos = new Vector2(towerShape.x / PPM + (range / 2), towerShape.y / PPM + (range / 2));

      System.out.println(
          "Name: "
              + ((EllipseMapObject) tower).getName()
              + " Size: "
              + size
              + " Range: "
              + range
              + " Pos: "
              + pos.x
              + ","
              + pos.y
              + " Textures: "
              + engaged_tex
              + ","
              + disengaged_tex
              + " Weapon: "
              + weaponStr);

      towerArr.add(
          new Tower(size, range, maxHealth, pos, screen, weapon, disengaged_tex, engaged_tex));
    }

    System.out.println("Tower loading done.\n");
    return towerArr;
  }

  public Vector2 getDims() {
    MapProperties props = map.getProperties();
    return new Vector2(props.get("width", Integer.class), props.get("height", Integer.class));
  }
}
