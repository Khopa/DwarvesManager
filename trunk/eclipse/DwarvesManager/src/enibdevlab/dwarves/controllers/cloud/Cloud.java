package enibdevlab.dwarves.controllers.cloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Gestion des sauvegardes d'un joueur sur un cloud
 * 
 * @author Yannick Guern
 *
 */
public class Cloud {
	
	/**
	 * Instance unique
	 */
	public static Cloud instance;
	
	/**
	 * gestion des accès sftp sur le serveur du cloud
	 */
	protected  MySftp sftp;
	
	/**
	 * nom de l'utilisateur
	 */
	protected String userName="";
	
	/**
	 * Instancie un cloud
	 */
	public Cloud(){
		
		instance = this;
		
		/**XmlReader reader = new XmlReader();
		FileHandle file = Gdx.files.internal("data/sftpConf.xml");
		try {
			Element tree = reader.parse(file);
			String host = tree.getChildByName("host").getText();
			String user = tree.getChildByName("user").getText();
			String pass = tree.getChildByName("pass").getText();
			
			//sftp = new MySftp(host, user, pass);
			
			System.out.println(host);
			
		} catch (IOException e) {
			e.printStackTrace();
		}**/

	}
	
	
	public static void init(){
		new Cloud();
	}
	
	/**
	 * parse les données reçu en http
	 * @param param paramètre de service
	 * @return l'http brute
	 * @throws Exception
	 */
	protected  Vector<String> urlParse(String param)throws Exception{
		
		Vector<String> lines = new Vector<String>();

		   OutputStreamWriter writer = null;
		   try {
		     //encodage des paramètres de la requête
		      String parameters = new String();
		      
		      
		      String[]params = param.trim().split("&");
		      
		      for(String param2 : params){
		    	  String[] val = param2.split("=");
		    	  parameters += URLEncoder.encode(val[0].trim(), "UTF-8") + "=" +URLEncoder.encode(val[1].trim(), "UTF-8");
		    	  if(!param2.equals(params[params.length-1])) parameters += "&";
		      }
		      
		 
		      //création de la connection
		      URL url = new URL("http://rezid.org/dwarves/index.php");
		      URLConnection conn = url.openConnection();
		      conn.setDoOutput(true);
		 
		      //envoi de la requête
		      writer = new OutputStreamWriter(conn.getOutputStream());
		      writer.write(parameters);
		      writer.flush();
		 
		      //lecture de la réponse
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				
				while((inputLine = in.readLine()) != null){
					lines.add(inputLine);
				}
				in.close();
		   }catch (Exception e) {
		      e.printStackTrace();
		   }finally{
		      try{writer.close();}catch(Exception e){}
		   }
		   
		   return lines;
	}
	
	
	/**
	 * récupère l'identifiant de l'utilisateur
	 * @param user
	 * @return retourne son id si l'utilisateur existe, 0 sinon
	 * @throws Exception
	 */
	protected  int getIdUser(String user) throws Exception{
		Vector<String> lines = urlParse("service=id&name="+user);
		
		int id = Integer.parseInt(lines.get(0).trim());
		
		if(id != 0){
			return id;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * récupère le mot de passe 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	protected  String getPassUser(String user) throws Exception{
		Vector<String> lines = urlParse("service=pass&id="+getIdUser(user));
		
		if(lines.get(0).trim().equals("0") ){
			return "";
		}
		else{
			return lines.get(0).trim();
		}
	}
	
	/**
	 * applique l'algorithme de cryptage sha1 sur le mot de passe en clair
	 * @param plaintext mot de passe en clair
	 * @return le mot de passe crypté
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
    protected  String hash(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    	crypt.reset();
    	crypt.update(plaintext.getBytes());
    	return new BigInteger(1, crypt.digest()).toString(16);
    }
	
	/**
	 * sauvegarde un fichier dans le cloud
	 * @param file nom de la sauvegarde
	 * @param user nom du propriètaire de la sauvegarde
	 * @return
	 * @throws Exception
	 */
	public boolean save(FileHandle file, String user)  {
		
		if(!sftp.isConnected()) return false;
		
		int id = 0;
		try {
			id = getIdUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(id != 0){
			sftp.upload(file.file().getAbsoluteFile().toString(), "saves/"+Integer.toString(id)+"/"+file.name());	
		}
		
		return true;
	}
	
	/**
	 * authentification de l'utilisateur
	 * @param user nom de l'utilisateur voulant se connecter
	 * @return retourne faux, si l'utilisateur n'existe pas sinon retourne vrai
	 * @throws Exception
	 */
	public  boolean connection(String user, String password){
		//Si l'utilisateur existe et a le bon mot de passe
		try {
			if(getIdUser(user)!=0 && hash(password).endsWith(getPassUser(user))){
				this.userName=user;
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return false;
	}
	
	
	/**
	 * sauvegarde une partie contenue dans le cloud
	 * @param file
	 * @param user
	 * @return retourne faux si le cloud est innacessible
	 * @throws Exception
	 */
	public boolean load(String file, String user){
		
		if(!sftp.isConnected()) return false;
		
		int id = 0;
		try {
			id = getIdUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		String src = "saves/"+Integer.toString(id)+"/"+file;
		String dest =  Gdx.files.external("DwarvesManager/Saves/"+file).file().getAbsoluteFile().toString();
		try {
			sftp.download(dest, src);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	
	/**
	 * enregistre un utilisateur sur le cloud
	 * @param name nom choisi par l'utilisateur
	 * @return vrai si l'inscription s'est correctement déroulée, faux s'il y a eu une erreur
	 * @throws Exception
	 */
	public  boolean suscribe(String name, String password){
		
		Vector<String> lines = new Vector<String>();
		try {
			lines = urlParse("service=subscribe&name="+name+"&password="+hash(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(lines.isEmpty()) return false;
		
		if(Integer.parseInt(lines.get(0).trim()) == 1){
			return false;
		}
		else{
			this.userName=name;
			return true;
		}
	}
	
	/**
	 * récupère la liste des savegarde associée à l'utilisateur sur le cloud
	 * @param user
	 * @return retourne la liste des sauvegardes de l'utilisateur
	 * @throws Exception
	 */
	public Vector<String> getSaves(String user){
		
		Vector<String> lines = new Vector<String>();
		try {
			lines = urlParse("service=listsaves&id="+Integer.toString(getIdUser(user)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(lines.isEmpty()) return lines;
		
		lines= new Vector<String>(Arrays.asList(lines.get(0).split("<br />")));
		
		return lines;
	}
	
	
	/**
	 * supprime une sauvegarde
	 * @param user utilisateur concerné
	 * @param save titre de la sauvegarde
	 * @return retourne en vrai si la sauvegarde est bien sauvegardée
	 */
	public boolean delSave(String user, String save){
		Vector<String> lines = new Vector<String>();
		try {
			lines = urlParse("service=delsave&save="+save+"&id="+Integer.toString(getIdUser(user)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(lines.get(0));
		
		if(lines.isEmpty()) return false;
		
		if(Integer.parseInt(lines.get(0).trim()) == 1){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * ajoute un record
	 * @param level nom du level
	 * @param user nom de l'utilisateur
	 * @param time temps réalisé au format hh:mm:ss
	 * @return
	 */
	public boolean addRecord(String level, String user, String time){
		
		Vector<String> lines = new Vector<String>();
		try {
			lines = urlParse("service=addrecord&level="+level+"&user="+user+"&time="+time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		lines= new Vector<String>(Arrays.asList(lines.get(0).split("<br />")));
		
		if(lines.isEmpty()) return false;
		
		if(Integer.parseInt(lines.get(0).trim()) != 1){
			return false;
		}
		else{
			return true;
		}
		
		
	}
	
	
	
	/**
	 * records du niveau
	 * @param level niveau considéré
	 * @return retourne les n premiers records de niveau sous la forme user=>time
	 */
	public ArrayList<ArrayList<String>> getRecords(String level, int n){
		
		Vector<String> lines = new Vector<String>();
		try {
			lines = urlParse("service=getrecord&level="+level+"&max="+n);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
		
		if(lines.isEmpty()) return records;
		
		String[]recordStr = lines.get(0).split("&"); 

		for(String record:recordStr){
			String[] data = record.split(",");
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(data[0]);
			tmp.add(data[1]);
			
			records.add(tmp);
		}
		
		return records;
	}
	
	/**
	 * permet de vérifier si le cloud est en ligne
	 * @return
	 */
	public boolean isOnline(){
		return sftp.isConnected();
	}
	
	/**
	 * retourne le nom de l'utilisateur connecté
	 * @return s'il n'est pas connecté renvoie une chaîne vide
	 */
	public String getUserName(){
		return userName;
	}
	
	/**
	 * permet de fermé le cloud
	 */
	public void close(){
		sftp.disconnect();
	}
	
	
	
	/**
	 * test divers et variés
	 */
	public void test(){
		try {
			
			System.out.println(delSave("Noa", "Niveau3.xml"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}