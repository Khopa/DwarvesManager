package enibdevlab.dwarves.views.actors.characters.config;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.Tileset;


/**
 * 
 * Cette classe permet de configurer la tête des personnages
 * qu'ils soientt des nains, gobelins ou elfe
 * 
 * @author Yannick Guern
 *
 */

public class HeadConfig {
	
	/**
	 * Tileset utilisé pour la tête (liste de sprite)
	 */
	protected Tileset headTileset;
	
	/**
	 * Position de la tête
	 */
	protected Vector2 position;

	/**
	 * 
	 * @param headTileset liste des tile de la tête (de face, de derrière, de profil)
	 * @param head position de la tête
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
