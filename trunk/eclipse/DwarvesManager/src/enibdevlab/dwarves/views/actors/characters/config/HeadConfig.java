package enibdevlab.dwarves.views.actors.characters.config;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.Tileset;


/**
 * 
 * Cette classe permet de configurer la t�te des personnages
 * qu'ils soientt des nains, gobelins ou elfe
 * 
 * @author Yannick Guern
 *
 */

public class HeadConfig {
	
	/**
	 * Tileset utilis� pour la t�te (liste de sprite)
	 */
	protected Tileset headTileset;
	
	/**
	 * Position de la t�te
	 */
	protected Vector2 position;

	/**
	 * 
	 * @param headTileset liste des tile de la t�te (de face, de derri�re, de profil)
	 * @param head position de la t�te
	 */
	public HeadConfig(Tileset headTileset, Vector2 head) {
		super();
		this.headTileset = headTileset;
		this.position = head;
	}

	public Tileset getHeadTileset() {
		return headTileset;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	
	

}
