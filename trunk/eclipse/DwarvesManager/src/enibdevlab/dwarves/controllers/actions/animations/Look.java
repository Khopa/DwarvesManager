package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Permet de controler la direction du regard d'un personnage
 * (ça les rend plus attachant) :3
 * 
 * @author Clément Perreau
 *
 */
public class Look extends TemporalAction {

	public static char TOP = 0x1;
	public static char BOTTOM = 0x2;
	public static char RIGHT = 0x4;
	public static char LEFT = 0x8;
	
	/**
	 * Personnage qui effectue l'action
	 */
	protected ACharacter acharacter;
	
	/**
	 * Position de depart des pupilles du personnage
	 */
	protected Vector2 startPosition;
	
	/**
	 * Position voulue à la fin
	 */
	protected Vector2 endPosition;
	
	/**
	 * Position binaire
	 */
	protected int position;
	
	/**
	 * Chemin à effectuer
	 */
	protected Vector2 path;
	
	/**
	 * 
	 * Fais le personnage regarder dans la direction indiquée
	 * Pour régler la position, voici les flags binaires à utiliser :
	 * Look.TOP, Look.BOTTOM, Look.RIGHT, Look.LEFT
	 * (TOP et RIGHT sont prioritaire)
	 * Mettre 0 permet de recentrer le regard
	 * 
	 * @param acharacter Personnage
	 * @param position Position
	 * @param duration Durée du mouvement
	 */
	public Look(ACharacter acharacter, int position, float duration){
		super(duration);
		this.acharacter = acharacter;
		this.position = position;
	}
	
	public void begin(){
		
		startPosition = this.acharacter.getHead().getPupilPosition().cpy();
		endPosition = new Vector2();
		
		if ((this.position&Look.TOP)>0){
			endPosition.set(0, acharacter.getBodyConfig().getEyesConfig().getyRadius());
		}
		else if ((this.position&Look.BOTTOM)>0){
			endPosition.set(0, -acharacter.getBodyConfig().getEyesConfig().getyRadius());
		}
		
		if ((this.position&Look.RIGHT)>0){
			endPosition.set(acharacter.getBodyConfig().getEyesConfig().getxRadius(), endPosition.y);
		}
		else if ((this.position&Look.LEFT)>0){
			endPosition.set(-acharacter.getBodyConfig().getEyesConfig().getxRadius(), endPosition.y);
		}
		
		path = endPosition.sub(startPosition).cpy();
		endPosition.add(startPosition);
		
	}
	
	public void end(){
		this.acharacter.getHead().setPupilPosition(this.endPosition);
	}
	
	@Override
	protected void update(float percent) {
		this.acharacter.getHead().getPupilPosition().set(this.startPosition.x + percent*this.path.x,
											             this.startPosition.y + percent*this.path.y);
	}

}
