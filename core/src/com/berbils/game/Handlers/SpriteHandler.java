package com.berbils.game.Handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.berbils.game.Entities.EntityTypes.Entity;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;
import com.berbils.game.Screens.MiniGameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * handles all the sprites created within a particular world/screen
 *
 * <P>Split into two categories, fixture associated sprites and sprite-only
 * sprites</P>
 *
 * <p> Fixture associated sprites are constantly updated according to its
 * associated fixture and fixture body, whereas sprite-only sprites are only
 * changed externally and on call
 * </p>
 *
 * <p> The fixture associated sprites are "attached" to an Entity class and
 * are drawn before sprite-only sprites regardless of sprite layer. The
 * sprite-only sprites are just a sprite with no Box2D or Entity properties
 * and are drawn after all fixture associated sprites.</p>
 *
 * <P> The sprite layers work internally so if the sprite layer no = 4,
 * fixture associated will have its own four layers and sprite-only will have
 * its own four layers</P>
 */
public class SpriteHandler
	{
	/** The screen the sprites will be created in/on */
	private PlayScreen screen;

	/** The number of sprite layers for sprites on their own and fixture
	 * associated sprites. The higher the number the more specific you can
	 * get sprites to draw over or under other sprites
	 */
	private int noOfSpriteLayers;

	/** A store of all the fixture associated Sprites in an ArrayList where
	 * each hashmap within that arrayList represents a different layer.The
	 * hashmap then contains all the sprites
	 */
	private ArrayList<HashMap<Fixture, Sprite>> fixtureSpriteLayers;

	/** A store of all the sprites in an ArrayList where each hashmap within
	 * that arrayList represents a differnt layer.The hashmap then contains
	 * all the sprites
	 */
	private ArrayList<ArrayList<Sprite>> spriteLayers;

	/** The background(world) texture, drawn before everything else */
	private Sprite worldMap;

	/**
	 * Creates the ArrayLists for the sprites depdenent upon the number of
	 * layers specified and creates the world map sprite. The world map
	 * sprite is the background, drawn before everything else.
	 *
	 * @param screen 				The screen the sprites will be created in/on
	 *
	 * @param mapTextureFilePath	The file path to the background texture
	 *                              for the world
	 *
	 * @param mapSize				The size of the world in meters
	 */
	public SpriteHandler(PlayScreen screen, String mapTextureFilePath, Vector2 mapSize)
		{
		this.fixtureSpriteLayers = new ArrayList<HashMap<Fixture, Sprite>>();
		this.spriteLayers = new ArrayList<ArrayList<Sprite>>();
		this.noOfSpriteLayers = 4;
		this.screen = screen;
		for (int i = 0; i < noOfSpriteLayers; i++) {
			this.fixtureSpriteLayers.add(new HashMap<Fixture, Sprite>());
			this.spriteLayers.add(new ArrayList<Sprite>());
		}

		this.createWorldMapSprite(mapTextureFilePath, mapSize);


		}

	/**
	 * Creates the world background, it is always drawn before everything else
	 *
	 * @param mapTextureFilePath	The file path to the background texture
	 *                              for the world
	 *
	 * @param mapSize				The size of the world in meters
	 */
	private void createWorldMapSprite(String mapTextureFilePath,
									  Vector2 mapSize)
		{
		this.worldMap = new Sprite(Kroy.assets.get(mapTextureFilePath,
												   Texture.class));
		mapSize.scl(Kroy.PPM);
		this.worldMap.setSize(mapSize.x, mapSize.y);

		}

	/**
	 * Creates a fixture associated sprite, all fixtures passed must be an
	 * Entity type.
	 *
	 * @param associatedFixture		The fixture the spite is "attached" to
	 *
	 * @param spriteTexture			The sprite texture
	 *
	 * @param spriteLayer			The layer the sprite will be drawn on
	 *                              within the fixture associated sprites
	 *                              layer groupings
	 *
	 * @return						Returns the sprite created
	 */
	public Sprite createNewSprite(Fixture associatedFixture, Texture spriteTexture, int spriteLayer)
		{
		Body fixtureBody = associatedFixture.getBody();
		Sprite newSprite = new Sprite(spriteTexture);
		Object fixBodyUserData = fixtureBody.getUserData();
		if (fixBodyUserData instanceof Entity) {
			this.fixtureSpriteLayers.get(spriteLayer).put(associatedFixture,
														  newSprite);
			this.updateSprite(associatedFixture, newSprite);
		}
		else {
			throw new IllegalArgumentException(
				"Sprite User Data not an Entity Object");
		}
		return newSprite;
		}

	/**
	 *
	 * Updates the fixture-associated sprite's rotation, size and position
	 *
	 * @param currentFixture	Fixture associated with the sprite
	 *
	 * @param currentSprite		The sprite associated with the fixture
	 */
	private void updateSprite(Fixture currentFixture, Sprite currentSprite)
		{
		this.updateSpriteRot(currentFixture.getBody().getAngle(),
							 currentSprite);
		this.updateSpritePos(currentFixture.getBody().getPosition(),
							 currentSprite);
		if (currentFixture.getUserData() instanceof Entity) {
			Vector2 size =
				( (Entity) currentFixture.getUserData() ).getSizeDims();
			this.updateSpriteSizes(size, currentSprite);
		}
		else {
			throw new IllegalArgumentException(
				"Sprites user data is not an Entity object");
		}
		}

	/**
	 * Updates a sprites rotation
	 *
	 * @param angle		new rotation angle
	 *
	 * @param sprite	sprite for rotation to apply to
	 */
	private void updateSpriteRot(float angle, Sprite sprite)
		{
		sprite.setOriginCenter();
		sprite.setRotation((float) Math.toDegrees(angle));
		}

	/**
	 * Updates a sprites position
	 *
	 * @param newSpritePos		new position in meters
	 *
	 * @param currentSprite		sprite for new position to apply to
	 */
	private void updateSpritePos(Vector2 newSpritePos, Sprite currentSprite)
		{
		newSpritePos.scl(Kroy.PPM);
		// In Libgdx position setting sets it from the bottom left corner
		newSpritePos.sub(currentSprite.getWidth() / 2,
						 currentSprite.getHeight() / 2);
		currentSprite.setPosition(newSpritePos.x, newSpritePos.y);
		}


	/**
	 * Updates a sprites size
	 *
	 * @param size				new sprite size in meters
	 *
	 * @param sprite			sprite for new size to apply to
	 */
	private void updateSpriteSizes(Vector2 size, Sprite sprite)
		{
		size.scl(Kroy.PPM);
		sprite.setSize(size.x, size.y);
		}

	/**
	 * Creates a sprite-only sprite
	 *
	 * @param textureFP		The file path to the textue for the sprit e
	 *
	 * @param spritePos		The position in meters where the sprite should be drawn
	 *
	 * @param spriteSize	The size in meteres of the sprite
	 *
	 * @param spriteLayer			The layer the sprite will be drawn on
	 *                              within the sprite-only sprites
	 *                              layer groupings
	 *
	 * @return				Returns the created sprite
	 */
	public Sprite createNewSprite(String textureFP, Vector2 spritePos, Vector2 spriteSize, int spriteLayer)
		{
		Sprite newSprite = new Sprite(Kroy.assets.get(textureFP,
													  Texture.class));
		this.spriteLayers.get(spriteLayer).add(newSprite);
		this.updateSpriteSizes(spriteSize, newSprite);
		this.updateSpritePos(spritePos, newSprite);
		return newSprite;
		}

	/**
	 *Goes through every fixture associated sprite first, updates them draws
	 * them.Then it goes through every sprite-only sprite and draws them
	 * .Both on the correct layer
	 *
	 * @param batch spriteBatch
	 */
	public void updateAndDrawAllSprites(SpriteBatch batch)
		{
		this.worldMap.draw(batch);
		for (HashMap<Fixture, Sprite> spriteLayer : this.fixtureSpriteLayers) {
			for (Map.Entry<Fixture, Sprite> entry : spriteLayer.entrySet()) {
				Sprite currentSprite = entry.getValue();
				this.updateSprite(entry.getKey(), currentSprite);
				currentSprite.draw(batch);
			}
		}
		for (ArrayList<Sprite> spriteLayer : this.spriteLayers) {
			for (Sprite sprite : spriteLayer) {
				sprite.draw(batch);
			}
		}
		}

	/** Destroy a fixture-associated sprite, removing it from being drawn or
	 * updated and destroying its body and fixture
	 *
	 * @param spriteFixture sprite to be destroyed/deleted
	 */
	public void destroySpriteAndBody(Fixture spriteFixture)
		{
		for (HashMap<Fixture, Sprite> layer : this.fixtureSpriteLayers) {
			if (layer.containsKey(spriteFixture)) {
				layer.get(spriteFixture).setSize(0, 0);
				layer.remove(spriteFixture);
				this.screen.destroyBody(spriteFixture.getBody());
			}
		}
		}

	/** Set a fixture-associated sprites texture to a new texture
	 *
	 * @param fixture			The fixture of the fixture associated-sprite
	 *
	 * @param textureFilePath	The file path to the new texture for the sprite
	 */
	public void setSpriteTexture(Fixture fixture, String textureFilePath)
		{
		for (HashMap<Fixture, Sprite> layer : this.fixtureSpriteLayers) {
			if (layer.containsKey(fixture)) {
				layer.get(fixture).setTexture(Kroy.assets.get(textureFilePath,
															  Texture.class));
			}
		}
		}

	/**
	 * Set a sprites size
	 *
	 * @param newSize	The new size of the sprite in meters
	 *
	 * @param sprite	The sprite to have its size updated
	 */
	public void setSpriteSize(Vector2 newSize, Sprite sprite)
		{
		this.updateSpriteSizes(newSize, sprite);
		}

	/**
	 * Destroys a sprite-only sprite, removing it so it will not be drawn
	 *
 	 * @param sprite the sprite to destroy
	 */
	public void destroySprite(Sprite sprite)
		{
		for (ArrayList<Sprite> spriteLayer : this.spriteLayers) {
			spriteLayer.remove(sprite);
		}
		}

	/**
	 * Destroys a fixture-associated sprite' sprite, removing it so it will not
	 * be drawn or updated
	 *
	 * @param fixture the fixture of the fixture-associated sprite whose
	 *                   sprite will be destroyed
	 */
	public void destroySprite(Fixture fixture)
		{
		for (HashMap<Fixture, Sprite> layer : this.fixtureSpriteLayers) {
			if(layer.containsKey(fixture)) {
				layer.remove(fixture);
			}
		}
		}
	}
