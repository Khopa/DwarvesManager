package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import enibdevlab.dwarves.views.actors.characters.ACharacter;


/**
 * 
 * Cette action permet de faire bouger la t�te d'un personnage de haut en bas
 * 
 * @author Cl�ment Perreau
 *
 */
public class HeadUp extends TemporalAction{

	/**
	 * Personnage qui effectue l'action
	 */
	protected ACharacter acharacter;
	
	/**
	 * Position y de la t�te au d�part
	 */
	protected float y;
	
	/*
	 * Rapidit� de hochement
	 */
	protected float velocity;
	
	/*
	 * Hauteur du hochement
	 */
	protected float height;
	
	/**
	 * Fais le personnage bouger la t�te de haut en bas
	 * 
	 * @param acharacter Personnage sur lequel on effectue l'action
	 * @param velocity   Rapidit� de hochement de t�te
	 * @param height     Hauteur du hochement de t�te
	 * @param duration   Dur�e
	 */
	public HeadUp(ACharacter acharacter, float velocity, float height, float duration){
		super(duration);
		this.velocity = velocity;
		this.height = height;
		this.acharacter = acharacter;
	}
	
	protected void begin () {
		y = this.acharacter.getHead().getY();
	}

	protected void end () {
		this.acharacter.getHead().setY(y);
	}
	
	@Override
	protected void update(float percent) {
		this.acharacter.getHead().setY((float) ((y+this.height/2)+ this.height/2*Math.sin(this.velocity * this.getTime())-this.height/2));
	}

}
