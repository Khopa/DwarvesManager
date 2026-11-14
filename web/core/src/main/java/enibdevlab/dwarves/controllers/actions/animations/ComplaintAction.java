package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.Action;

import enibdevlab.dwarves.views.actors.characters.ACharacter;

public class ComplaintAction extends Action {

	/**
	 * Vue du personnage
	 */
	protected ACharacter character; 
	
	/**
	 * Valeur
	 */
	protected int value;
	
	public ComplaintAction(ACharacter character, int value){
		super();
		this.character = character;
		this.value = value;
	}
	
	@Override
	public boolean act(float delta) {
		character.setComplaint(value);
		return true;
	}

}
