package enibdevlab.dwarves.views.scenes.editor;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 
 * Sc�ne d'�dition de map
 * 
 * @author Cl�ment Perreau
 *
 */
public class Editor extends Stage {

	/**
	 * Couche affichant la map
	 */
	protected EditorLayer mapLayer;

	/**
	 * 
	 * Cr�e une nouvelle instance de l'�diteur de map avec
	 * une map vide de la taille d�sir�e
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
