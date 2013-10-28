package enibdevlab.dwarves.controllers.actions;

import enibdevlab.dwarves.controllers.actions.GameAction;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;

/**
 * Action effectu�e par les personnages
 * @author Cl�ment Perreau
 */
public abstract class CharacterAction extends GameAction {

	/**
	 * R�f�rence vers le nain acteur
	 */
	protected MCharacter character;
	
	public CharacterAction(Game game, MCharacter character){
		super(game);
		this.character = character;
	}

	public MCharacter getDwarf() {
		return character;
	}
	

}
