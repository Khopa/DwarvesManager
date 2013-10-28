package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * 
 * Vue de base pour les objets étants des parties d'un personnage
 * 
 * @author Clément Perreau
 *
 */
public abstract class ACharacterPart extends Actor {

	/**
	 * Réference vers le personnage complet
	 */
	protected ACharacter acharacter;
	
	public ACharacterPart(ACharacter acharacter){
		this.acharacter = acharacter;
		this.setX(0);
		this.setY(0);
	}
	
	
	
}
