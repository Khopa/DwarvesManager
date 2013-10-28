package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import enibdevlab.dwarves.views.actors.characters.ACharacter;


/**
 * 
 * Le personnage effectuant cette action bougera ses mains sur l'axe x
 * 
 * @author Cl�ment Perreau
 *
 */
public class HandsShake extends TemporalAction {
	
	/**
	 * Personnage qui effectue l'action
	 */
	protected ACharacter acharacter;
	
	/*
	 * Rapidit� de hochement
	 */
	protected float velocity;
	
	/*
	 * Amplitude du mouvement
	 */
	protected float amplitude;
	
	/**
	 * Permet de savoir si les mains vont dans le m�me sens (1) ou non (-1)
	 */
	protected int synchrone;
	
	/**
	 * Le personnage effectuant cette action bougera ses mains sur l'axe x
	 * 
	 * @param acharacter Personnage sur lequel on effectue l'action
	 * @param velocity   Rapidit� de hochement
	 * @param amplitude  Amplitude du mouvement
	 * @param duration   Dur�e
	 * @param synchrone  Dit si les mains bougent dans le m�me sens ou non
	 */
	
	public HandsShake(ACharacter acharacter, float velocity, float amplitude, float duration, boolean synchrone){
		super(duration);
		this.velocity = velocity;
		this.amplitude = amplitude;
		this.acharacter = acharacter;
		if(synchrone) this.synchrone = 1;
		else this.synchrone = -1;
	}
	
	/**
	 * Le personnage effectuant cette action bougera ses mains sur l'axe x
	 * 
	 * @param acharacter Personnage sur lequel on effectue l'action
	 * @param velocity   Rapidit� de hochement
	 * @param amplitude  Amplitude du mouvement
	 * @param duration   Dur�e
	 */
	public HandsShake(ACharacter acharacter, float velocity, float amplitude, float duration){
		this(acharacter, velocity, amplitude, duration, true);
	}
	
	protected void begin () {
	}

	protected void end () {
		this.acharacter.getLeftHand().setX(this.acharacter.getLeftHand().adjustPosition());
		this.acharacter.getRightHand().setX(this.acharacter.getRightHand().adjustPosition());
	}
	
	@Override
	protected void update(float percent) {
		
		this.acharacter.getLeftHand().setX(
				(float) ((this.acharacter.getLeftHand().adjustPosition()
						  +this.amplitude/2)
						  +this.amplitude/2*Math.sin(this.velocity * this.getTime())-this.amplitude/2));
		
		this.acharacter.getRightHand().setX(
				(float) ((this.acharacter.getRightHand().adjustPosition()
						  +this.amplitude/2)
						  +this.synchrone*this.amplitude/2*Math.sin(this.velocity * this.getTime())-this.amplitude/2));
	}

}