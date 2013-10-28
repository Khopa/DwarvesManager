package enibdevlab.dwarves.views.actors.characters.config;


import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Cette classe permet de configurer les "modèles 2d" des personnage
 * Pour savoir comment placer chaque partie du corps de ceux-ci, qu'ils
 * soit des nains, gobelins ou elfe
 * 
 * @author Clément Perreau / Yannick Guern
 *
 */
public class BodyConfig {
	

	/**
	 * Tileset utilisé pour le corps (liste de sprite)
	 */
	protected Tileset bodyTileset;
	
	/**
	 * position du corps
	 */
	protected Vector2 position;
	
	/**
	 * configuration de la tête
	 */
	protected HeadConfig headConfig;
	
	/**
	 * configuration de la bouche
	 */
	protected MouthConfig mouthConfig;
	
	/**
	 * configuration des yeux
	 */
	protected EyesConfig eyesConfig;
	
	/**
	 * configuration des mains
	 */
	protected HandsConfig handsConfig;

	/**
	 * 
	 * @param bodyTileset liste des tiles composant le corps
	 * @param body position du corps
	 * @param headConfig configuration du corps
	 * @param mouthConfig configuration de la bouche
	 * @param eyesConfig configuration des yeux
	 * @param handsConfig configuration des mains
	 */
	public BodyConfig(Tileset bodyTileset, Vector2 body, HeadConfig headConfig,
			MouthConfig mouthConfig, EyesConfig eyesConfig,
			HandsConfig handsConfig) {
		super();
		this.bodyTileset = bodyTileset;
		this.position = body;
		this.headConfig = headConfig;
		this.mouthConfig = mouthConfig;
		this.eyesConfig = eyesConfig;
		this.handsConfig = handsConfig;
	}

	public Tileset getBodyTileset() {
		return bodyTileset;
	}

	public Vector2 getPosition() {
		return position;
	}

	public HeadConfig getHeadConfig() {
		return headConfig;
	}

	public MouthConfig getMouthConfig() {
		return mouthConfig;
	}

	public EyesConfig getEyesConfig() {
		return eyesConfig;
	}

	public HandsConfig getHandsConfig() {
		return handsConfig;
	}


	
	
}
