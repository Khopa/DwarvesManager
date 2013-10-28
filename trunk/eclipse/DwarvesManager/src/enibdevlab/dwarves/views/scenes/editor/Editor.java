package enibdevlab.dwarves.views.scenes.editor;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 
 * Scène d'édition de map
 * 
 * @author Clément Perreau
 *
 */
public class Editor extends Stage {

	/**
	 * Couche affichant la map
	 */
	protected EditorLayer mapLayer;

	/**
	 * 
	 * Crée une nouvelle instance de l'éditeur de map avec
	 * une map vide de la taille désirée
	 * 
	 * @param xMapSize Taille X de la map
	 * @param yMapSize Taille Y de la map
	 */
	public Editor(int xMapSize, int yMapSize){
		this.mapLayer = new EditorLayer(xMapSize, yMapSize);
		this.addActor(this.mapLayer);
		init();
	}
	
	/**
	 * Initialisation
	 */
	private void init(){
		this.addActor(new TilesetTable(this.mapLayer.getTilemap().getTileset(0)));
	}	
	
}
