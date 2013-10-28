package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.views.Loader;


public class APriceEffect extends Actor {

	/**
	 * Prix payé
	 */
	protected String price;
	
	protected TextBounds txtbnd;
	

	public APriceEffect(int x, int y, int price){
		super();
		if(price > 0){
			this.price = "+" + Integer.toString(price);
			this.setColor(Color.GREEN);
		}
		else{
			this.price = Integer.toString(price);
			this.setColor(Color.RED);
		}
		txtbnd = DwarvesManager.font.getBounds(Integer.toString(price));
		this.setPosition(x, y);
		this.setWidth(Loader.guiAtlasSmall.getTileWidth()+txtbnd.width);
		this.setHeight(Loader.guiAtlasSmall.getTileHeight()+txtbnd.height);
		this.addAction(Animation.dissapear());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		batch.setColor(1f, 1f, 1f, this.getColor().a);
		batch.draw(Loader.guiAtlasSmall.getTile(0),
				   getX(), getY(),
				   0, 0,
				   getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		DwarvesManager.font.setColor(this.getColor());
		DwarvesManager.font.draw(batch, price, getX()+Loader.guiAtlasSmall.getTileWidth()+txtbnd.width/2, getY()+txtbnd.height+Loader.guiAtlasSmall.getTileHeight()/2);
		
	}
}
