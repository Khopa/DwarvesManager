package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * 
 * 
 * @author Clément
 *
 */
public class AImage extends Actor {

	protected TextureRegion img;
	
	public AImage(TextureRegion img, int x, int y){
		super();
		this.img = img;
		this.setPosition(x,y);
		this.setSize(img.getRegionWidth(), img.getRegionHeight());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		Color tmp = batch.getColor();
		batch.setColor(this.getColor());
		batch.draw(img, this.getX()-this.getWidth()/2, this.getY()-this.getHeight()/2,
		this.getWidth()/2, this.getHeight()/2,this.getWidth(), this.getHeight(),
		this.getScaleX(), this.getScaleY(), this.getRotation());
		batch.setColor(tmp);
	}
	
	
}
