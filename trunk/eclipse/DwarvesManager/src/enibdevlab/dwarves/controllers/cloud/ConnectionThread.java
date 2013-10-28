package enibdevlab.dwarves.controllers.cloud;

/**
 * 
 * Thread de connection
 * 
 * @author Cl�ment Perrreau
 *
 */
public class ConnectionThread extends CloudThread {
	
	
	/**
	 * Mot de passe rentr�
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
