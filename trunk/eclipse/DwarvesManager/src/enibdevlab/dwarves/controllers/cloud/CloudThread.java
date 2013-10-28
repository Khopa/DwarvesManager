package enibdevlab.dwarves.controllers.cloud;

/**
 * 
 * Thread d'action li� au Cloud
 * 
 * @author Cl�ment Perreau
 *
 */
public abstract class CloudThread extends Thread {

	/**
	 * Succ�s ou non
	 */
	protected boolean success = false;
	
	/**
	 * Nom de l'utilisateur connect�
	 */
	protected String username;
	
	/**
	 * L'op�ration a elle r�ussi ?
	 */
	public boolean hasSuceeded(){
		return success;
	}
	
}
