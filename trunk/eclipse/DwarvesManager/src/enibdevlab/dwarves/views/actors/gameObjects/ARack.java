package enibdevlab.dwarves.views.actors.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.ItemContainer;
import enibdevlab.dwarves.models.objects.Rotation;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.AGameObject;

public class ARack extends AGameObject {

	public ARack(GameObject model) {
		super(model);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		int w = Loader.objectAtlas.getTileWidth();
		int h = Loader.objectAtlas.getTileHeight();
		int x = (int) (this.model.getX()*w);
		int y = (int) (this.model.getY()*h);
		DwarvesManager.font.draw(batch, Integer.toString(((ItemContainer)(this.model)).getItems().size()) + "/"
				+ Integer.toString(((ItemContainer)(this.model)).getCapacity())
				,x, y);
		
		// Affichage des pioches
		
		if(this.model.getRotation() == Rotation.ONE_QUARTER || this.model.getRotation() == Rotation.THREE_QUARTER){
			x += w/3;
			for(int i = 0; i < ((ItemContainer)(this.model)).getItems().size(); i++){
				y += w/((ItemContainer)(this.model)).getCapacity();
				batch.draw(Loader.itemAtlas.getTile(0),
						   x, y, w/2, 0, w, h, .5f, .5f, 90);
			}
		}
		else{
			x -= 2*w/7;
			y += h/3;
			for(int i = 0; i < ((ItemContainer)(this.model)).getItems().size(); i++){
				x += w/((ItemContainer)(this.model)).getCapacity();
				batch.draw(Loader.itemAtlas.getTile(0),
						   x, y, 0, 0, w, h, .5f, .5f, this.getRotation());
			}
		}
		
		
	}

}
