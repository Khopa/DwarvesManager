package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import enibdevlab.dwarves.models.Direction;


/**
 * 
 * Objet graphique pour représenter les mains des personnages
 * 
 * @author Clément Perreau
 *
 */
public class AHand extends ACharacterPart {

	public enum HandSide{
		RIGHT,
		LEFT
	}
	
	/**
	 * Coté de la main
	 */
	protected HandSide handside;
	
	/**
	 * Image de la main (utilisé en tant que raccourci uniquement)
	 */
	protected TextureRegion img; 
	
	public AHand(ACharacter acharacter, HandSide handside) {
		super(acharacter);
		this.img = this.acharacter.getBodyConfig().getHandsConfig().getHandTexture();
		this.handside = handside;
		this.setSize(this.img.getRegionWidth(), this.img.getRegionHeight());
		this.setOrigin(0,0);
		this.setPosition(this.acharacter.getBodyConfig().getHandsConfig().getPosition().x,
				         this.acharacter.getBodyConfig().getHandsConfig().getPosition().y
				         +this.acharacter.getBodyConfig().getBodyTileset().getTileHeight()/2);
		this.adjustPosition();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		
		super.draw(batch, parentAlpha);
		this.setColor(acharacter.getColor());
		Color tmp = batch.getColor();
		batch.setColor(this.getColor());
		float x = this.getX()-this.getWidth()/2f;
		float y = this.getY()-this.getWidth()/2f;
		batch.draw(img, x, y, this.getOriginX(),this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
		batch.setColor(tmp);
		
	}
	
	/**
	 * Ajuste la position des mains en fonction 
	 */
	public float adjustPosition(){
		switch(this.handside){
			case RIGHT:
				
				if(this.acharacter.getDirection() == Direction.LEFT){
					this.setX(-acharacter.getBodyConfig().getHandsConfig().getPosition().x*this.getWidth()
							  +this.acharacter.getBodyConfig().getBodyTileset().getTileWidth()/2);
				}
				else{
					this.setX(acharacter.getBodyConfig().getHandsConfig().getPosition().x*this.getWidth()
							  +this.acharacter.getBodyConfig().getBodyTileset().getTileWidth()/2);
				}
				
			break;
			case LEFT:
				if(this.acharacter.getDirection() == Direction.RIGHT){
					this.setX(acharacter.getBodyConfig().getHandsConfig().getPosition().x*this.getWidth()
							  +this.acharacter.getBodyConfig().getBodyTileset().getTileWidth()/2);
				}
				else{
					this.setX(-acharacter.getBodyConfig().getHandsConfig().getPosition().x*this.getWidth()
							  +this.acharacter.getBodyConfig().getBodyTileset().getTileWidth()/2);
				}
			break;
		}
		return this.getX();
	}
}
