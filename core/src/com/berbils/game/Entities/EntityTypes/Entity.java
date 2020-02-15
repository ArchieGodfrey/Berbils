package com.berbils.game.Entities.EntityTypes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.berbils.game.Handlers.SpriteHandler;
import com.berbils.game.Kroy;
import com.berbils.game.Screens.PlayScreen;

/***
 * An abstract class designed to extend from any entity based class
 * Ensures all entities have the required methods
 */
public abstract class Entity
	{
	/**The position the variable is at in meters*/
	protected Vector2 position;

	/**The size dimensions of the entity in meters */
	protected Vector2 sizeDims;

	/**The Screen the entity object is located and will be created */
	protected PlayScreen screen;
	/**
	 * The world attached the body would be created on and is attached to
	 * the screen
	 * */
	protected World world;

	/** The entities Box2D body */
	protected Body entityBody;

	/** The Entities Box2D current body definition */
	protected BodyDef entityBodyDefinition;

	/** The Entities Box2D fixture */
	protected Fixture entityFixture;

	/** The Entities Box2D current fixture definition */
	protected FixtureDef entityFixtureDefinition;
	/** A boolean stating whether the Box2D body is static (For true) or
	 * Dynamic (For false)
	 */
	protected boolean isStatic;

	/** The object attached to body and fixture userData */
	protected Object userData;

	/** The shape of the entity*/
	protected Shape entityShape;

	/** The spritehandler attached to the screen */
	protected SpriteHandler spriteHandler;

	/** The entities sprite */
	protected Sprite entitySprite;

	/** The draw layer the sprite will be drawn on, used to determine draw
	 * order. 0 Is the bottom layer
	 */
	protected int spriteLayer;

	/** The category bits used for Box2D collision filtering */
	protected short catBits;

	/** The mask bits used for Box2D collision filtering */
	protected short maskBits;

	/** The entities texture, set to null if the entity doesn't need a sprite */
	private Texture entityTexture;

	/**
	 * A constructor defining all entity properties
	 *
	 * @param screen          The Screen the entity object is located and will
	 *                        be created
	 *
	 * @param sizeDims 		  The size dimensions of the entity in meters
	 *
	 * @param pos 			  The position the variable is at in meters
	 *
	 * @param textureFilePath The file path to the texture for the sprite,
	 *
	 * 	                      NOTE - If this is null a sprite will
	 * 	                      not be created
	 *
	 * @param isStatic 		  A boolean stating whether the Box2D body is
	 *                        static (For true) or Dynamic (For false)
	 *
	 * @param spriteLayer     The draw layer the sprite will be drawn on, used
	 *                        to determine draw order. 0 Is the bottom layer
	 */
	protected Entity(
		PlayScreen screen, Vector2 sizeDims, Vector2 pos, String textureFilePath, boolean isStatic, int spriteLayer)
		{
		this.screen = screen;
		this.position = pos;
		this.sizeDims = sizeDims;
		this.world = screen.getWorld();
		this.isStatic = isStatic;
		this.spriteHandler = this.screen.getSpriteHandler();
		this.spriteLayer = spriteLayer;

		// Null tells the entity a sprite doesn't need to be created
		if(textureFilePath == null)
		{
			this.entityTexture = null;
		}
		else {
			this.entityTexture = Kroy.assets.get(textureFilePath, Texture.class);
		}

		}

	/**
	 *  Method for creating The Box2D body definition
	 */
	protected void createBox2Definition()
		{
		this.entityBodyDefinition = new BodyDef();
		this.entityBodyDefinition.position.set(this.position);

		// Defines the Box2D body type
		if (this.isStatic) {
			this.entityBodyDefinition.type = BodyDef.BodyType.StaticBody;
		}
		else {
			this.entityBodyDefinition.type = BodyDef.BodyType.DynamicBody;
		}
		}

	/**
	 *  Method for creating Box2D body based upon entity body definition
	 */
	protected void createBox2DBody()
		{
		this.createBox2Definition();
		this.entityBody = world.createBody(this.entityBodyDefinition);
		}
	/** Create a body using the Entities already defined body definition */
	public void createBodyCopy()
		{
		this.entityBody = this.world.createBody(this.entityBodyDefinition);
		}

	/**
	 * method for creating Box2D fixture definition
	 */
	protected void createFixtureDefinition()
		{
		this.entityFixtureDefinition = new FixtureDef();
		this.entityFixtureDefinition.shape = this.entityShape;
		this.setFixtureCategory(catBits, maskBits);
		}

	/**
	 * Method for creating Box2D fixture based upon entity fixture definition
	 */
	protected void createFixture()
		{
		this.createFixtureDefinition();
		this.entityFixture = this.entityBody.createFixture(this.entityFixtureDefinition);
		}

	/** Create a fixture using the Entities already defined fixture
	 * definition
	 */
	public void createFixtureCopy()
		{
		this.entityFixture = this.entityBody.createFixture(this.entityFixtureDefinition);
		}



	/**
	 * Creates a sprite attached to the entitity
	 * Note - will not create a sprite if its texture is null
	 */
	public void createSprite()
		{
		if (this.entityTexture != null) {
			this.entitySprite = this.spriteHandler.createNewSprite(this.entityFixture,
																   this.entityTexture,
																   this.spriteLayer);
		}
		}

	/** All entity subclasses need a method for creating atleast a Box2D
	 * body and fixture definition
	 */
	protected abstract void defineBox2DEntity();

	/**
	 * Getter for the fixture currently assigned to the entity
	 *
	 * @return The fixture attached to the entity
	 */
	public Fixture getFixture()
		{
		return this.entityFixture;
		}

	/** All entity subclasses need a method for getting their dimensions
	 * @return returns the size dimensions of the entity in meters
	 * */
	public abstract Vector2 getSizeDims();



	/**
	 *  Sets the angular dampening for all future Box2d bodies created
	 * @param angleDamp The amount of angular dampening to apply to a body
	 */
	protected void setBodyDefAngularDampening(float angleDamp)
		{
		this.entityBodyDefinition.angularDamping = angleDamp;
		}


	/**
	 *  Sets the linear dampening for all future Box2d bodies created
	 * @param linDamp The amount of angular dampening to apply to a body
	 */
	protected void setBodyDefLinearDampening(float linDamp)
		{
		this.entityBodyDefinition.linearDamping = linDamp;
		}

	/**
	 * Sets fixture definition category bits and mask bits
	 * Note - Used for collision filtering
	 *
	 * @param catBits The category bits used for Box2D collision filtering
	 * @param maskBits The mask bits used for Box2D collision filtering
	 */
	public void setFixtureCategory(short catBits, short maskBits)
		{
		this.entityFixtureDefinition.filter.categoryBits = catBits;
		this.entityFixtureDefinition.filter.maskBits = maskBits;
		}

	/**
	 * NEW METHOD @author Archie Godfrey
	 * Sets the position of the entity
	 *
	 * @param newPosition The new position the entity should be at
	 */
	public void setPosition(Vector2 newPosition)
		{
		this.position = newPosition;
		}


	/**Stores the userData to be used for all future body and fixture
	 * creations
	 *
	 * <p> The user data should be an instance of the final subclass
	 *     extending Entity</p>
	 *
	 * @param userData The userData you wish all future body and fixtures to
	 *                    have, should be an instance of the final subclass
	 *                    extending Entity
	 */
	public void storeUserData(Object userData)
		{
		this.userData = userData;
		}

	/**
	 *Gets the body attached to this Entity instance
	 *
	 * @return Returns this entities body
	 */
	public Body getBody()
		{
		return this.entityBody;
		}


	/**
	 *Sets the currently assigned entity fixture definition to be a sensor so
	 * all future fixtures created would have the sensor property you define
	 *(Generates collision callbacks but without colliding with any Box2D
	 * object)
	 *
	 * @param val Boolean for whether the entity's fixture definition is a
	 *               sensor
	 */
	public void setFixtureDefSensor(Boolean val)
		{
		this.entityFixtureDefinition.isSensor = val;
		}

	/**
	 * Set the position that all future bodies will be created at
	 *
	 * @param pos The position in meters all future bodies will spawn at
	 */
	public void setSpawnPosition(Vector2 pos)
		{
		this.entityBodyDefinition.position.set(pos);
		}

	/**
	 *Sets the currently assigned entity fixture to be a sensor(Generates
	 * collision callbacks but without colliding with any object)
	 *
	 * @param val Boolean for whether the entity's fixture is a sensor
	 */
	public void setSensor(boolean val)
		{
		this.entityFixture.setSensor(val);
		}

	/**
	 * Setter for the class variable entityShape
	 * Sets the shape for the Bodies created by this entity
	 *
	 * @param shape The entity shape
	 */
	public void setShape(Shape shape)
		{
		this.entityShape = shape;
		}


	/**
	 * Sets the user data of the spawned fixture and body curently attached to
	 * this entity
	 *
	 * @param userData The userData you wish the currentbody and
	 *                    fixtures to have, should be an instance of the final
	 *                    subclass extending Entity
	 */
	public void setUserData(Object userData)
		{
		this.entityFixture.setUserData(userData);
		this.entityBody.setUserData(userData);
		}


	}
