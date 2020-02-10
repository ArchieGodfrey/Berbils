package com.berbils.game.Entities.EntityTypes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.berbils.game.Screens.PlayScreen;

/**
 * 		Creates a Box2D rectangular body and fixture definition *
 *<P>
 *		Also has the ability to auto generate sprites and the body and fixture
 *</P>
 *
 *
 */
public class BoxGameEntity extends Entity
	{

	/***
	 * Constructs a box2D Body definition and fixture
	 *
	 * @param screen The screen containing the world where the Box2D object
	 * 				 will be created
	 *
	 * @param sizeDims The size of the body,fixture and sprite in meters
	 *
	 * @param textureFilePath The file path to the texture for the sprite,
	 * 	                      NOTE - If this is null a sprite will
	 * 	                      not be created
	 *
	 * @param isStatic Used to set the Box2D body type to either Static (
	 *                 true) or Dynamic (false)
	 *
	 * @param spriteLayer The layer the sprite will be drawn on, 0 being the
	 * 	                  bottom layer
	 */
	public BoxGameEntity(
		PlayScreen screen, Vector2 sizeDims, String textureFilePath, boolean isStatic, int spriteLayer)
		{
		super(screen,
			  sizeDims,
			  //Nothing will be created at the initial position but the
			  // entity class requires it so it is set to an arbitrary vector
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
	 * @param sizeDims        The size of the body,fixture and sprite in meters
	 * @param pos             The position in meters that the body, fixture and
	 *                        sprite will be created at where the origin is the centre of the body
	 * @param textureFilePath The file path to the texture for the sprite,
	 *                        NOTE - If this is null a sprite will
	 *                        not be created
	 * @param isStatic        Used to set the Box2D body type to either Static (
	 *                        true) or Dynamic (false)
	 * @param catBits         The category bits used for Box2D collision filtering
	 * @param maskBits        The mask bits used for Box2D collision filtering
	 * @param spriteLayer     The layer the sprite will be drawn on, 0 being the
	 *                        bottom layer
	 */
	public BoxGameEntity(
		PlayScreen screen,
		Vector2 sizeDims,
		Vector2 pos,
		String textureFilePath,
		boolean isStatic,
		short catBits,
		short maskBits,
		int spriteLayer)
		{
		super(screen,
			  sizeDims,
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
	 * according to already defined parameters within Entity
	 */
	protected void defineBox2DEntity()
		{
		this.defineShape();
		super.createBox2DBody();
		super.createFixture();
		super.setUserData(this);
		super.createSprite();
		}

	/**
	 * Creates a rectangular box2D shape and sets the entityShape to the
	 * newly generated shape
	 */
	private void defineShape()
		{
		PolygonShape shape = new PolygonShape();
		// Divided by two as setAsBox using half width and half height
		shape.setAsBox(this.sizeDims.x / 2, this.sizeDims.y / 2);
		super.setShape(shape);
		}

	/***
	 * Getter for the size dimensions of the entity
	 *
	 * @return A copy of the entity size dimensions
	 */
	@Override
	public Vector2 getSizeDims() { return this.sizeDims.cpy(); }

	/***
	 * NEW METHOD @author Archie Godfrey
	 * Setter for changing the size of an entity
	 */
	public void setScale(Vector2 scale) {
		if (this.sizeDims != scale) {
			this.sizeDims = scale;
		}
	}

	}

