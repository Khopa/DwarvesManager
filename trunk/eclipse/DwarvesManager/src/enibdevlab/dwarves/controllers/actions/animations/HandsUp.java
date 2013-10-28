package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import enibdevlab.dwarves.views.actors.characters.ACharacter;


/**
 * 
 * Le personnage effectuant cette action bougera ses mains de haut en bas
 * 
 * @author Cl�ment Perreau
 *
 */
public class HandsUp extends TemporalAction {
	
	/**
	 * Personnage qui effectue l'action
	 */
	protected ACharacter acharacter;
	
	/**
	 * Position y des mains au d�part
	 */
	protected float y;
	
	/*
	 * Rapidit� de hochement
	 */
	protected float velocity;
	
	/*
	 * Amplitude du mouvement
	 */
	protected float amplitude;
	
	/**
	 * Fais le personnage bouger la t�te de haut en bas
	 * 
	 * @param acharacter Personnage sur lequel on effectue l'action
	 * @param velocity   Rapidit� du mouvement
	 * @param height     Amplitude du mouvement
	 * @param duration   Dur�e
	 */
	public HandsUp(ACharacter acharacter, float velocity, float height, float duration){
		super(duration);
		this.velocity = velocity;
		this.amplitude = height;
		this.acharacter = acharacter;
	}
	
	protected void begin () {
		y = this.acharacter.getLeftHand().getY();
	}

	protected void end () {
		this.acharacter.getLeftHand().setY(y);
		this.acharacter.getRightHand().setY(y);
		this.acharacter.getLeftHandItem().setY(y);
		this.acharacter.getRightHandItem().setY(y);
	}
	
	@Override
	protected void update(float percent) {
		float ny = (float) ((y+this.amplitude/2)+ this.amplitude/2*Math.sin(this.velocity * this.getTime())-this.amplitude/2);
		this.acharacter.getLeftHand().setY(ny);
		this.acharacter.getRightHand().setY(ny);
		this.acharacter.getLeftHandItem().setY(ny);
		this.acharacter.getRightHandItem().setY(ny);
	}

}