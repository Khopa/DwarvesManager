package enibdevlab.dwarves.views.actors.characters.config;

import com.badlogic.gdx.math.Vector2;
import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Cette classe permet de configurer les yeux des personnages
 * qu'ils soientt des nains, gobelins ou elfe
 * 
 * @author Yannick Guern / Clément Perreau
 *
 */
public class EyesConfig {

	/**
	 * position de l'oeil ET de la pupille quand elle est au centre
	 * 
	 * NB : @Yannick Il y a pas besoin de faire une position pour la pupille toute seule, les
	 * sprites sont sur le même tileset et conçus pour être positionné ensemble, c'est pas pour rien
	 * 
	 */
	protected Vector2 position;
	
	/**
	 * Taille de l'oeil en x 
	 * Décalage à appliquer à la pupille quand elle est à droite de l'oeil
	 * On en déduit aussi le décalage pour les regards vers la gauche
	 */
	protected float xRadius;
	
	/**
	 * Taille de l'oeil en y
	 * Décalage à appliquer à la pupille quand elle est en haut de l'oeil
	 * On en déduit aussi le décalage pour les regards vers la gauche
	 */
	protected float yRadius;
	
	/**
	 * Tileset utilisé pour les yeux
	 */
	protected Tileset eyesTileset;

	/**
	 * Construit une configuration pour les yeux
	 * @param eyesTileset Tileset des yeux
	 * @param position Position de l'oeil
	 * @param xRadius Taille de l'oeil en x (utilisé pour limiter les mouvements de la pupille)
	 * @param yRadius Taille de l'oeil en y (utilisé pour limiter les mouvements de la pupille)
	 */
	public EyesConfig(Tileset eyesTileset, Vector2 position, float xRadius, float yRadius) {
		super();
		this.position = position;
		this.eyesTileset = eyesTileset;
		this.xRadius = xRadius;
		this.yRadius = yRadius;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Tileset getEyesTileset() {
		return eyesTileset;
	}
	
	public float getxRadius() {
		return xRadius;
	}

	public float getyRadius() {
		return yRadius;
	}
	
	
	

}
