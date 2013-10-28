package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.views.actors.characters.ACharacter;


/**
 * 
 * Action pour controler les yeux du personnage (ouvert fermé)
 * 
 * @author Clément Perreau
 *
 */
public class Eyes extends Action {

	/**
	 * Perso sur lequel on effectue l'actions
	 */
	protected ACharacter acharacter;
	
	/**
	 * Permet de savoir si on ferme ou on ouvre les yeux
	 */
	protected boolean close;
	
	public Eyes(ACharacter acharacter, boolean close){
		super();
		this.close = close;
		this.acharacter = acharacter;
	}
	
	@Override
	public boolean act(float delta) {
		this.acharacter.getHead().setBlink(this.close);
		return true;
	}
	
	
	// Actions dérivées
	
	/**
	 * 
	 * Fait clignoter des yeux le personnage qui effectue cette action
	 * 
	 * @param acharacter Personnage qui effectue l'action
	 * @param duration Durée du clignotement
	 * @return Sequence d'actions qui simule le clignotement
	 */
	public static Action blinkAction(ACharacter acharacter, float duration){
		return Actions.sequence(new Eyes(acharacter, true),
				                Actions.delay(duration),
				                new Eyes(acharacter, false));
	}


}
