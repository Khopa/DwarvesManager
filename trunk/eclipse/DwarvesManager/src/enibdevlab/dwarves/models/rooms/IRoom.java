package enibdevlab.dwarves.models.rooms;

import com.badlogic.gdx.graphics.Color;

import enibdevlab.dwarves.models.characters.MCharacter;

public interface IRoom {

	/**
	 * Cette fonction doit v�rifier si la pi�ce en question est op�rationnelle ou non
	 */
	public boolean updateOperationalState();
	
	/**
	 *  Couleur de la pi�ce (info pour la vue)
	 */
	public Color getRoomColor();
	
	/**
	 * Nom de la pi�ce
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
