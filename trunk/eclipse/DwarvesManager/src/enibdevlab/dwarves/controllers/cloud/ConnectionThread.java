package enibdevlab.dwarves.controllers.cloud;

/**
 * 
 * Thread de connection
 * 
 * @author Clément Perrreau
 *
 */
public class ConnectionThread extends CloudThread {
	
	
	/**
	 * Mot de passe rentré
	 */
	private final String password;
	
	public ConnectionThread(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void run(){
		success = Cloud.instance.connection(username, password);
	}
	

}
