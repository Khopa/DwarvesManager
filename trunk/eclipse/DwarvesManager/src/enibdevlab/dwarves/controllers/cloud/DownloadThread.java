package enibdevlab.dwarves.controllers.cloud;


/**
 * 
 * Thread de téléchargement d'une sauvegarde sur le serveur
 * 
 * @author Clément Perreau
 *
 */
public class DownloadThread extends CloudThread {

	/**
	 * Utilisateur
	 */
	private String username;
	
	/**
	 * Fichier
	 */
	private String file;
	
	/**
	 * @param file Fichier
	 * @param username Utilisateur
	 */
	public DownloadThread(String file, String username){
		this.username = username;
		this.file = file;
	}

	@Override
	public void run() {
		success = Cloud.instance.load(file, username);
	}
	
	
	
	
}
