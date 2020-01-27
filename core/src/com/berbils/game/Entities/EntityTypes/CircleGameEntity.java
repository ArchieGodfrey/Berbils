package com.berbils.game.Entities.EntityTypes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.berbils.game.Screens.PlayScreen;


/**
 * 		Creates a Box2D circular body and fixture definition
 *
 *<P>
 * 		Also has the ability to auto generate sprites and the body and fixture
 *</P>
 *
 */
public class CircleGameEntity extends Entity
	{

	/**
	 * Constructs a box2D Body definition and fixture
	 *
	 * @param screen          The screen containing the world where the Box2D object
	 *                        will be created
	 *
	 * @param diam            The diameter of the circle body in meters to be
	 *                        created
	 *
	 * @param textureFilePath The file path to the texture for the sprite,
	 *
	 *                        NOTE - If this is null a sprite will
	 *                        not be created
	 *
	 * @param isStatic        Used to set the Box2D body type to either Static (
	 *                        true) or Dynamic (false)
	 *
	 * @param spriteLayer     The layer the sprite will be drawn on, 0 being the
	 *                        bottom layer
	 */
	public CircleGameEntity(
		PlayScreen screen, float diam, String textureFilePath, boolean isStatic, int spriteLayer)
		{
		super(
			screen,
			new Vector2(diam, diam),
			new Vector2(0, 0),
			textureFilePath,
			isStatic,
			spriteLayer
			);

		this.defineShape();
		super.createFixtureDefinition();
		super.createBox2Definition();
		}

	/**
	 * Constructs a box2D Body, fixture and potentially a sprite (if texture
	 * not null) with the given
	 * variables
	 *
	 * @param screen          The screen containing the world where the Box2D object
	 *                        will be created
	 *
	 * @param diam            The diameter of the circle body in meters to be
	 *                        created
	 *
	 * @param pos             The position in meters that the body, fixture and
	 *                        sprite will be created at where the origin is the centre of the body
	 * @param textureFilePath The file path to the texture for the sprite,
	 *
	 *                        NOTE - If this is null a sprite will
	 *                        not be created
	 *
	 * @param isStatic        Used to set the Box2D body type to either Static (
	 *                        true) or Dynamic (false)
	 *
	 * @param catBits         The category bits used for Box2D collision filtering
	 * @param maskBits        The mask bits used for Box2D collision filtering
	 * @param spriteLayer     The layer the sprite will be drawn on, 0 being the
	 *                        bottom layer
	 */
	public CircleGameEntity(
		PlayScreen screen,
		float diam,
		Vector2 pos,
		String textureFilePath,
		boolean isStatic,
		short catBits,
		short maskBits, int spriteLayer)
		{
		super(screen,
			  new Vector2(diam, diam),
			  pos,
			  textureFilePath,
			  isStatic,
			  spriteLayer
			 );

		this.catBits = catBits;
		this.maskBits = maskBits;
		this.defineBox2DEntity();
		}

	/**
	 * Creates a box shape, body, fixture and sprite
	 * according to already defined parameters from within entity
	 */
	protected void defineBox2DEntity()
		{
		super.createBox2DBody();
		this.defineShape();
		super.createFixture();
		super.setUserData(this);
		super.createSprite();
		}

	/**
	 * Creates a circle Box2D shape and sets the entityShape to it
	 */
	private void defineShape()
		{
		CircleShape shape = new CircleShape();
		// Halved as sizeDims is the diameter
		shape.setRadius(this.sizeDims. x / 2);
		super.setShape(shape);
		}


	/***
	 * Getter for the size dimensions of the entity
	 * The size dimensions will be the same as the diameter as its a circle
	 * object
	 *
	 *
	 * @return A copy of the entity size dimensions
	 */
	@Override
	public Vector2 getSizeDims()
		{
		return this.sizeDims.cpy();
		}


	}
