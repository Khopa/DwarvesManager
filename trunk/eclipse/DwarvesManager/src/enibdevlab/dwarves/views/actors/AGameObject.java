package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Primitives;
import enibdevlab.dwarves.models.objects.Rotation;

public class AGameObject extends Actor {

	/**
	 * Lien vers le modèle
	 */
	protected GameObject model;
	
	/**
	 * Textures de l'objet
	 */
	protected TextureRegion[][] textures;
	

	public AGameObject(GameObject model){
		this.setModel(model);
	}
	
	/**
	 * Recupère la liste des textures requises
	 * Appelé à l'initialisation uniquement
	 */
	private void getTextures(){
		
		int id = 0;
		int originID = this.model.getTextureId();
		
		int xsize = model.getNominalXSize();
		int ysize = model.getNominalYSize();
		int lineSize = Loader.objectAtlas.getLineSize();
		
		textures = new TextureRegion[xsize][ysize];
		
		for(int i = 0; i<xsize;i++){
			for(int j = 0; j<ysize;j++){
				id = originID+i-j*lineSize;
				System.out.println(id);
				textures[i][j] = Loader.objectAtlas.getTile(id);
			}
		}
	}
	
	public GameObject getModel() {
		return model;
	}
	
	public void setModel(GameObject model) {
		if(this.model != null) this.model.setView(null);
		this.model = model;
		this.model.setView(this);
		this.getTextures(); 
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		
		Color memory = batch.getColor();
		
		batch.setColor(this.getColor());
		
		switch(Rotation.toInt(model.getRotation())){
			case(0):
				draw0(batch, parentAlpha);
				break;
			case(90):
				draw90(batch, parentAlpha);
				break;
			case(180):
				draw180(batch, parentAlpha);
				break;
			case(270):
				draw270(batch, parentAlpha);
				break;
			default:
				draw0(batch, parentAlpha);
		}
		
		// Affichage des slots
		int w = Loader.objectAtlas.getTileWidth();
		int h = Loader.objectAtlas.getTileHeight();
		if(DwarvesManager.debug){
			batch.end();
			Primitives.prepareRenderer(batch);
			for(Slot slot:this.model.getSlots()){
				Primitives.rect((int)(this.model.getX()*w+slot.getX()*w+1), (int)(this.model.getY()*h+slot.getY()*h+1), w-1, h-1, Color.ORANGE);
				if(slot.isOccupied()){
					Primitives.line((int)(this.model.getX()*w+slot.getX()*w),
									 (int)(this.model.getY()*h+slot.getY()*h),
									 (int)(this.model.getX()*w+slot.getX()*w+w),
									 (int)(this.model.getY()*h+slot.getY()*h+h),
									  1,
									  Color.MAGENTA);
				}
			}
			batch.begin();
			
		}
		
		
		
		batch.setColor(memory);
		
	}

	
	private void draw270(SpriteBatch batch, float parentAlpha) {
		
		int xsize = model.getNominalXSize();
		int ysize = model.getNominalYSize();
		int w = Loader.objectAtlas.getTileWidth();
		int h = Loader.objectAtlas.getTileHeight();
		
		int x = (int) model.getX();
		int y = (int) model.getY();
		
		// Affichage du contour
		if(DwarvesManager.debug){
			batch.end();
			Primitives.prepareRenderer(batch);
			Primitives.rect(x*w, y*h, w*ysize, h*xsize);
			batch.begin();
		}
		
		for(int i = 0; i<xsize;i++){
			for(int j = 0; j<ysize;j++){
				batch.draw(textures[i][j],
						  (x+j)*w,
						  (y+xsize-i)*h,
						   0,
						   0,
						   Loader.objectAtlas.getTileWidth(),
						   Loader.objectAtlas.getTileHeight(), 1, 1, Rotation.toInt(model.getRotation()));
			}
		}
	}

	private void draw180(SpriteBatch batch, float parentAlpha) {
		
		int xsize = model.getNominalXSize();
		int ysize = model.getNominalYSize();
		int w = Loader.objectAtlas.getTileWidth();
		int h = Loader.objectAtlas.getTileHeight();

		int x = (int) model.getX();
		int y = (int) model.getY();
		
		// Affichage du contour
		if(DwarvesManager.debug){
			batch.end();
			Primitives.prepareRenderer(batch);
			Primitives.rect(x*w, y*h, w*xsize, h*ysize);
			batch.begin();
		}
		
		for(int i = 0; i<xsize;i++){
			for(int j = 0; j<ysize;j++){
				batch.draw(textures[i][j],
						  (x+xsize-i)*w,
						  (y+ysize-j)*h,
						   0,
						   0,
						   Loader.objectAtlas.getTileWidth(),
						   Loader.objectAtlas.getTileHeight(), 1, 1, Rotation.toInt(model.getRotation()));
			}
		}
	}


	private void draw90(SpriteBatch batch, float parentAlpha) {
		
		int xsize = model.getNominalXSize();
		int ysize = model.getNominalYSize();
		int w = Loader.objectAtlas.getTileWidth();
		int h = Loader.objectAtlas.getTileHeight();
		
		int x = (int) model.getX();
		int y = (int) model.getY();
		
		// Affichage du contour
		if(DwarvesManager.debug){
			batch.end();
			Primitives.prepareRenderer(batch);
			Primitives.rect(x*w, y*h, w*ysize, h*xsize);
			batch.begin();
		}
		
		for(int i = 0; i<xsize;i++){
			for(int j = 0; j<ysize;j++){
				batch.draw(textures[i][j],
						  (x-j+ysize)*w,
						  (y+i)*h,
						   0,
						   0,
						   Loader.objectAtlas.getTileWidth(),
						   Loader.objectAtlas.getTileHeight(), 1, 1, Rotation.toInt(model.getRotation()));
			}
		}
	}

	private void draw0(SpriteBatch batch, float parentAlpha) {

			int xsize = model.getNominalXSize();
			int ysize = model.getNominalYSize();
			int w = Loader.objectAtlas.getTileWidth();
			int h = Loader.objectAtlas.getTileHeight();
			
			int x = (int) model.getX();
			int y = (int) model.getY();
			
			// Affichage du contour
			if(DwarvesManager.debug){
				batch.end();
				Primitives.prepareRenderer(batch);
				Primitives.rect(x*w, y*h, w*xsize, h*ysize);
				batch.begin();
			}
			
			for(int i = 0; i<xsize;i++){
				for(int j = 0; j<ysize;j++){
					batch.draw(textures[i][j],
							  (x+i)*w,
							  (y+j)*h,
							   0,
							   0,
							   Loader.objectAtlas.getTileWidth(),
							   Loader.objectAtlas.getTileHeight(), 1, 1, Rotation.toInt(model.getRotation()));
				}
			}
		
	}
	
}
