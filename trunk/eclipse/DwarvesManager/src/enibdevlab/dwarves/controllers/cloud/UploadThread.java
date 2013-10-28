package enibdevlab.dwarves.controllers.cloud;

import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * Thread d'upload d'une sauvegarde sur le serveur
 * 
 * @author Clément Perreau
 *
 */
public class UploadThread extends CloudThread {

	
	/**
	 * Fichier
	 */
	private FileHandle file;
	
	/**
	 * @param file Fichier
	 * @param username Utilisateur
	 */
	public UploadThread(FileHandle file, String username){
		this.username = username;
		this.file = file;
	}

	@Override
	public void run() {
		success = Cloud.instance.save(file, username);
	}
	
	
	
}
