package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.Action;

import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Action faisant changer la direction d'un personnage
 * (L'intérêt est de pouvoir intégrer cette action dans des séquences)

 * @author Clément Perreau
 *
 */
public class SetDirection extends Action {

	/**
	 * Personnage dont la direction doit changer
	 */
	protected ACharacter acharacter;
	
	/**
	 * Direction à donner
	 */
	protected Direction direction;
	
	/**
	 * Change la direction d'un personnage
	 * @param acharacter Personnage dont la direction doit changer
	 * @param direction  Nouvelle Direction
	 */
	public SetDirection(ACharacter acharacter, Direction direction){
		this.acharacter = acharacter;
		this.direction = direction;
	}
	
	@Override
	public boolean act(float delta) {
		acharacter.setDirection(this.direction);
		return true;
	}

}
