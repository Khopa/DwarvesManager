package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.views.Loader;

/**
 * 
 * Vue des items (considéré comme une partie du corps des personnages quand ils sont en main)
 * 
 * @author Clément Perreau
 *
 */
public class AItem extends AHand {

	public AItem(ACharacter acharacter, HandSide handside) {
		super(acharacter, handside);
		this.setHeight(Loader.itemAtlas.getTileHeight());
		this.setWidth(Loader.itemAtlas.getTileWidth());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		this.adjustPosition();
		
		
		if(this.handside == HandSide.LEFT){
			if(this.acharacter.getModel().getLeftHandItem() != null){
				this.img = Loader.itemAtlas.getTile(this.acharacter.getModel().getLeftHandItem().getTextureId());
				super.draw(batch, parentAlpha);
			}
		}
		else if(this.handside == HandSide.RIGHT){
			if(this.acharacter.getModel().getRightHandItem() != null){
				this.img = Loader.itemAtlas.getTile(this.acharacter.getModel().getRightHandItem().getTextureId());
				super.draw(batch, parentAlpha);
			}
		}
	}
	
	@Override
	public float adjustPosition() {
		
		super.adjustPosition();
		
		if(this.acharacter.getDirection() == Direction.RIGHT && this.handside == HandSide.LEFT){
			this.setX(this.getX()-this.getWidth()/2);
		}
		else if(this.acharacter.getDirection() == Direction.LEFT || this.handside == HandSide.LEFT){
			this.setX(this.getX()+this.getWidth()/2);
		}
		else if(this.acharacter.getDirection() == Direction.RIGHT || this.handside == HandSide.RIGHT){
			this.setX(this.getX()-this.getWidth()/2);
		}
		
		
		
		return this.getX();
	}


}
