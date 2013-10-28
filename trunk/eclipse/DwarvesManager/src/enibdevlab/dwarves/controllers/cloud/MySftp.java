package enibdevlab.dwarves.controllers.cloud;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**
 * Gestion des accès SFTP
 * 
 * @author Yannick Guern
 *
 */
public class MySftp{
	
	/**
	 * session de connection
	 */
	private Session session;
	/**
	 * channel du sftp
	 */
	private ChannelSftp sftp;

	/**
	 * Permet de manipuler les fichier d'un serveur sftp
	 * @param hostname adresse du serveur
	 * @param username nom d'utilisateur
	 * @param password mot de passe de l'utilisateur
	 */
	public MySftp(String hostname, String username, String password) {
		
		JSch jsch = new JSch();
		
		try {
			session = jsch.getSession(username, hostname, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			
			sftp = (ChannelSftp) session.openChannel("sftp");
			
			sftp.connect();
			
			
		} catch (JSchException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		

	}
	
	
	/**
	 * permet d'uploader un fichier sur le serveur
	 * @param src chemin absolu du fichier source
	 * @param dest chemin relatif du fichier de destination
	 * @throws SftpException
	 * @throws JSchException 
	 */
	public void upload(String src, String dest){
		
		try {
			sftp.put(src, dest);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * permet de downloader un fichier du serveur verseur le client
	 * @param dest chemin absolu du fichier source
	 * @param src chemin relatif du fichier de destination
	 * @return le fichier trouver
	 * @throws SftpException
	 * @throws JSchException 
	 */
	public void download(String dest, String src){
		try {
			sftp.get(src, dest);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * vérifie si le cloud est acessible
	 * @return
	 */
	public boolean isConnected(){
			boolean connect = sftp.isConnected();
			return connect;
	}
	
	public void disconnect(){
		sftp.exit();
		session.disconnect();
	}

}
