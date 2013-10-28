package enibdevlab.dwarves.models.rooms;

import com.badlogic.gdx.graphics.Color;

import enibdevlab.dwarves.models.characters.MCharacter;

public interface IRoom {

	/**
	 * Cette fonction doit vérifier si la pièce en question est opérationnelle ou non
	 */
	public boolean updateOperationalState();
	
	/**
	 *  Couleur de la pièce (info pour la vue)
	 */
	public Color getRoomColor();
	
	/**
	 * Nom de la pièce
	 */
	public String getRoomName();
	
	/**
	 * Utilisable par un type de nain
	 */
	public boolean readyFor(Class<? extends MCharacter> characterType);
	
	/**
	 * Icone
	 */
	public int getIconId();
	
}
