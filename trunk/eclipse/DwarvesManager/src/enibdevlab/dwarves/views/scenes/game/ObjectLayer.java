package enibdevlab.dwarves.views.scenes.game;


import com.badlogic.gdx.scenes.scene2d.Group;

import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.world.TileMap;


/**
 * 
 * Couche d'objets
 * Couche qui regroupe tous les objets de la partie Objets
 * 
 * @author Cl�ment Perreau
 *
 */
public class ObjectLayer extends Group {

	/**
	 * Map sur laquelle se d�roule l'action.
	 * On en a besoin pour positionner les diff�rents objets correctement et
	 * r�cup�rer la valeur de scrolling (position de la map)
	 * NB : En fait non
	 */
	protected TileMap tilemap;
	
	/**
	 * Objet � placer (apparait quand le joueur d�cide de placer un objet)
	 */
	protected AGameObject objectToPlace;
	
	public ObjectLayer(TileMap tilemap){
		this.tilemap = tilemap;
	}

	public AGameObject getObjectToPlace() {
		return objectToPlace;
	}

	public void setObjectToPlace(AGameObject objectToPlace) {
		this.removeObjectToPlace(); // enleve l'ancien objet
		this.objectToPlace = objectToPlace;
		this.addActor(objectToPlace);
	}

	public void removeObjectToPlace() {
		if(this.objectToPlace != null) objectToPlace.remove();
	}

}
