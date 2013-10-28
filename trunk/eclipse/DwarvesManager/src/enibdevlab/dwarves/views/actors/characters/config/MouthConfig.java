package enibdevlab.dwarves.views.actors.characters.config;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.Tileset;


/**
 * 
 * Cette classe permet de configurer la bouche des personnages
 * qu'ils soientt des nains, gobelins ou elfe
 * 
 * @author Yannick Guern
 *
 */


public class MouthConfig {
	
	/**
	 * position de la bouche
	 */
	protected Vector2 position;
	
	/*
	 * décalage à appliquer lorsque le personnage est dirigé vers le coté
	 */
	protected Vector2 sidePosition;
	
	/**
	 * Tileset de la bouche
	 */
	protected Tileset mouthTileset;
	

	/**
	 * 
	 * Crée une configuration pour la bouche d'un modèle
	 * 
	 * @param mouth    tileset de la bouche
	 * @param position position de la bouche 
	 * @param sidePosition décalage de position à appliquer lorsque le personnage est dirigé vers le coté
	 */
	public MouthConfig(Tileset mouth, Vector2 position, Vector2 sidePosition) {
		this.position = position;
		this.sidePosition = sidePosition;
		this.mouthTileset = mouth;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getSidePosition() {
		return sidePosition;
	}

	public Tileset getMouthTileset() {
		return mouthTileset;
	}
	
}
