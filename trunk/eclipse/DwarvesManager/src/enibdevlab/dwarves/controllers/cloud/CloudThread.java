package enibdevlab.dwarves.controllers.cloud;

/**
 * 
 * Thread d'action lié au Cloud
 * 
 * @author Clément Perreau
 *
 */
public abstract class CloudThread extends Thread {

	/**
	 * Succès ou non
	 */
	protected boolean success = false;
	
	/**
	 * Nom de l'utilisateur connecté
	 */
	protected String username;
	
	/**
	 * L'opération a elle réussi ?
	 */
	public boolean hasSuceeded(){
		return success;
	}
	
}
