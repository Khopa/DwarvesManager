package enibdevlab.dwarves.controllers.actions;

import enibdevlab.dwarves.controllers.actions.GameAction;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;

/**
 * Action effectuée par les personnages
 * @author Clément Perreau
 */
public abstract class CharacterAction extends GameAction {

	/**
	 * Référence vers le nain acteur
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
