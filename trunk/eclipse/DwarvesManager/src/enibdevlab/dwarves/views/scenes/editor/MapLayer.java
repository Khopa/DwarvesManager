package enibdevlab.dwarves.views.scenes.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.scenes.scene2d.Group;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Couche d'affichage de map
 * 
 * @author Clément Perreau
 *
 */
public class MapLayer extends Group{

	/**
	 * Map
	 */
	protected TileMap tilemap;

	/**
	 * Crée un layer avec une map vide
	 * @param xSize Taille X de la map
	 * @param ySize Taille Y de la map
	 */
	public MapLayer(int xSize, int ySize){
		this.tilemap = new TileMap(this, xSize, ySize);
		this.addActor(this.tilemap);
	}
	
	public MapLayer() {
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		int d = (int) (delta*1000);
		
		// Scroll clavier
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			this.setX(this.getX()+d);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			this.setX(this.getX()-d);	
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			this.setY(this.getY()+d);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			this.setY(this.getY()-d);
		}
		
		// Scroll souris
		if(Gdx.app.getType() != ApplicationType.Android){
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if(x>DwarvesManager.getWidth()-5){
				this.setX(this.getX()-d);
			}
			else if(x<5){
				this.setX(this.getX()+d);
			}
			
			if(y>DwarvesManager.getHeight()-5){
				this.setY(this.getY()+d);
			}
			else if(y<5){
				this.setY(this.getY()-d);
			}
		}
		
		this.clamp();
	}
	
	/**
	 * S'assure que l'on ne scrolle pas en dehors de l'écran
	 */
	public void clamp() {
		if (-this.getX() < -1f/3f*DwarvesManager.getWidth()) this.setX(1f/3f*DwarvesManager.getWidth());
		if (-this.getY() < -1f/3f*DwarvesManager.getWidth()) this.setY(1f/3f*DwarvesManager.getWidth());
		
		float maxX = this.getScaleX()*this.tilemap.getTileWidth()*this.tilemap.getXSize() - 2f*DwarvesManager.getWidth()/3f;
		float maxY = this.getScaleY()*this.tilemap.getTileHeight()*this.tilemap.getYSize() - 2f*DwarvesManager.getHeight()/3f;
		
		if (-this.getX() > maxX) this.setX(-maxX);
		if (-this.getY() > maxY) this.setY(-maxY);
	}

	public TileMap getTilemap() {
		return tilemap;
	}
	
}
