package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.Action;

import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Action faisant changer la direction d'un personnage
 * (L'int�r�t est de pouvoir int�grer cette action dans des s�quences)

 * @author Cl�ment Perreau
 *
 */
public class SetDirection extends Action {

	/**
	 * Personnage dont la direction doit changer
	 */
	protected ACharacter acharacter;
	
	/**
	 * Direction � donner
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
