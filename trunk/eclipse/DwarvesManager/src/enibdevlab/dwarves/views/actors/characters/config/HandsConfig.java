package enibdevlab.dwarves.views.actors.characters.config;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * Cette classe permet de configurer les mains des personnages
 * qu'ils soientt des nains, gobelins ou elfe
 * 
 * @author Yannick Guern
 *
 */

public class HandsConfig {
	
	/**
	 * Texture utilisé pour les mains
	 */
	protected TextureRegion handTexture;
	
	/**
	 * Permet de savoir où placer la main gauche (la position de la main droite en est déduite)
	 */
	protected Vector2 position;
	
	/**
	 * 
	 * @param handTexture texture de la main
	 * @param hand position de la main
	 */
	public HandsConfig(TextureRegion handTexture, Vector2 hand) {
		super();
		this.handTexture = handTexture;
		this.position = hand;
	}

	public TextureRegion getHandTexture() {
		return handTexture;
	}

	public Vector2 getPosition() {
		return position;
	}	

}
