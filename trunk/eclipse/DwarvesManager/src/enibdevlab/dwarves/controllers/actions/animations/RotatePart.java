package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import enibdevlab.dwarves.views.actors.characters.ACharacter;
import enibdevlab.dwarves.views.actors.characters.ACharacterPart;


/**
 * 
 * Le personnage effectuant cette action bougera ses mains de haut en bas
 * 
 * @author Clément Perreau
 *
 */
public class RotatePart extends TemporalAction {
	
	/**
	 * Personnage qui effectue l'action
	 */
	protected ACharacter acharacter;
	
	/**
	 * Part du corps
	 */
	protected ACharacterPart part;
	
	/*
	 * Vitesse Angulaire
	 */
	protected float velocity;
	
	/*
	 * Amplitude du mouvement
	 */
	protected float angle;
	
	/**
	 * Angle de départ
	 */
	protected float startAngle = 0;
	
	/**
	 * Fais le personnage bouger la tête de haut en bas
	 * 
	 * @param acharacter Personnage sur lequel on effectue l'action
	 * @param velocity   Rapidité du mouvement
	 * @param height     Amplitude du mouvement
	 * @param duration   Durée
	 */
	public RotatePart(ACharacter acharacter, ACharacterPart part, float velocity, float angle, float duration){
		super(duration);
		this.velocity = velocity;
		this.angle = angle;
		this.acharacter = acharacter;
		this.part = part;
	}
	
	protected void begin () {
		startAngle = part.getRotation();
	}

	protected void end () {
		part.setRotation(startAngle);
	}
	
	@Override
	protected void update(float percent) {
		float theta = (float) ((startAngle+this.angle/2)+ this.angle/2*Math.sin(this.velocity * this.getTime())-this.angle/2);
		part.setRotation(theta);
	}

}