package enibdevlab.dwarves.controllers.cloud;

/**
 * Thread d'inscription
 * @author Clément Perreau
 */
public class SubscriptionThread extends CloudThread {
	
	/**
	 * Mot de passe rentré
	 */
	private final String password;
	
	public SubscriptionThread(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void run() {
		success = Cloud.instance.suscribe(username, password);
	}
}
