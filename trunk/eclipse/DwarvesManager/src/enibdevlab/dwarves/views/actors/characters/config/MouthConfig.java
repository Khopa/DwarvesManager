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
	 * d�calage � appliquer lorsque le personnage est dirig� vers le cot�
	 */
	protected Vector2 sidePosition;
	
	/**
	 * Tileset de la bouche
	 */
	protected Tileset mouthTileset;
	

	/**
	 * 
	 * Cr�e une configuration pour la bouche d'un mod�le
	 * 
	 * @param mouth    tileset de la bouche
	 * @param position position de la bouche 
	 * @param sidePosition d�calage de position � appliquer lorsque le personnage est dirig� vers le cot�
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
