package enibdevlab.dwarves.views.scenes.game;


import com.badlogic.gdx.scenes.scene2d.Group;

import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.world.TileMap;


/**
 * 
 * Couche d'objets
 * Couche qui regroupe tous les objets de la partie Objets
 * 
 * @author Clément Perreau
 *
 */
public class ObjectLayer extends Group {

	/**
	 * Map sur laquelle se déroule l'action.
	 * On en a besoin pour positionner les différents objets correctement et
	 * récupérer la valeur de scrolling (position de la map)
	 * NB : En fait non
	 */
	protected TileMap tilemap;
	
	/**
	 * Objet à placer (apparait quand le joueur décide de placer un objet)
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
