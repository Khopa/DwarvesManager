package enibdevlab.dwarves.views.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Bouton représentant un objet
 * @author Clément Perreau
 *
 */
public class ObjectButton extends ImageButton {
	
	/**
	 * Atlas de texture de l'icone
	 */
	private Tileset tileset;
	
	/**
	 * Position dans l'atlas de l'icone
	 */
	private int textureId;
	
	/**
	 * Nom de l'objet
	 */
	private String name;
	
	/**
	 * Prix à afficher éventuellement
	 */
	private int price = 0;
	
	public ObjectButton(Drawable imageUp, Drawable imageDown, Tileset atlas, int textureId){
		super(imageUp, imageDown, imageDown);
		this.tileset = atlas;
		this.textureId = textureId;
	}
	
	public ObjectButton(Drawable imageUp, Drawable imageDown, Tileset atlas, int textureId, int price, String name){
		this(imageUp, imageDown, atlas, textureId);
		this.name = name;
		this.price = price;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(this.tileset.getTile(this.textureId), this.getX()+this.getWidth()/4, this.getY()+this.getHeight()/4);
		
		if(name != null){
			DwarvesManager.font.setColor(Color.WHITE);
			TextBounds bounds = DwarvesManager.font.getBounds(name);			
			DwarvesManager.font.draw(batch, name,
					this.getX()+ this.getWidth()/2 - bounds.width/2, this.getY()+this.getHeight()-bounds.height);
		}
		
		if(price > 0){
			DwarvesManager.font.setColor(Color.WHITE);
			TextBounds bounds = DwarvesManager.font.getBounds(Integer.toString(price));
			int y = (int) (this.getY());
			int x = (int) (this.getX()+this.getWidth()/2);
			x -= Loader.guiAtlasSmall.getTileWidth()/2;
			x -= bounds.width/2;
			batch.draw(Loader.guiAtlasSmall.getTile(0), x,y);
			x += Loader.guiAtlasSmall.getTileWidth()/2 + 5;
			y += bounds.height*2.2;
			DwarvesManager.font.draw(batch, Integer.toString(price),x,y);
		}
	}
	
}
