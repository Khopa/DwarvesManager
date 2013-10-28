package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpriteActor extends Actor {

	private TextureRegion region;
	
	public SpriteActor(TextureRegion region){
		this.region = region;
		this.setWidth(region.getRegionWidth());
		this.setHeight(region.getRegionHeight());
		
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		Color color = batch.getColor();
		batch.setColor(this.getColor());
		batch.draw(region, getX(), getY(), getWidth()/2, getHeight()/2, getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(color);
	}
	
	
	
}
