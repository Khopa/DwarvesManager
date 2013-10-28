package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Cette classe permet de représenter le corps des personnages
 * 
 * @author Clément Perreau
 *
 */
public class ABody extends ACharacterPart {

	/**
	 * Liste d'images pour les différentes directions (Utilisé ici comme raccourci uniquement)
	 */
	protected Tileset tileset;
	
	public ABody(ACharacter acharacter){
		super(acharacter);
		this.tileset = acharacter.bodyConfig.getBodyTileset();
		this.setSize(this.tileset.getTileWidth(), this.tileset.getTileHeight());
		this.setOrigin(0,0);
		this.setPosition(0, 0);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		this.setColor(acharacter.getColor());
		Color tmp = batch.getColor();
		batch.setColor(this.getColor());
		batch.draw(this.tileset.getTile(this.acharacter.direction.toInt()),
				   this.getX(), this.getY(),
	               this.getOriginX(),this.getOriginY(),this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
		batch.setColor(tmp);
	}
	
	
}
