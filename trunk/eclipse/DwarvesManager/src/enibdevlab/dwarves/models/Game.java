package enibdevlab.dwarves.models;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.controllers.script.DwarvesManagerLuaApi;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.game.GameScene;


/**
 * 
 * "Partie" du jeu
 * 
 * 
 * @author Clément Perreau
 *
 */
public final class Game implements IPersistent{
	
	/**
	 * Vue
	 */
	private GameScene view;
	
	/**
	 * Gestionnaire de personnage
	 */
	private Characters characters;
	
	/**
	 * Gestionnaire de pièces
	 */
	private Rooms rooms;
	
	/**
	 * Gestionnaire d'objets
	 */
	private Objects objects;
	
	/**
	 * Monde
	 */
	private Level level;
	
	/**
	 * Task Manager
	 */
	private TaskManager taskManager;
	
	/**
	 * Banque qui gère l'argent du joueur
	 */
	private Bank bank;
	
	/**
	 * Données xml de la partie
	 */
	private Element xml;

	/**
	 * Nom du fichier
	 */
	private String filename = "";
	
	/**
	 * instance
	 */
	private static Game instance = null;
	
	/**
	 * Constructeur, à partir d'un script Lua
	 */
	public Game(GameScene view, LevelFile script){
		
		instance = this;
		DwarvesManagerLuaApi.init(this);
		this.rooms = new Rooms(this);
		this.characters = new Characters(this);
		this.objects = new Objects(this);
		this.taskManager = new TaskManager(this);
		this.bank = new Bank();
		this.view = view;
		this.level = new Level(this, script);
		
	}


	/**
	 * Charge une partie à partir d'un fichier xml sauvegardé sur la machine
	 */
	public Game(GameScene view, String filename) {
		
		instance = this;
		DwarvesManagerLuaApi.init(this);
		
		this.view = view;
		this.setFilename(filename);
		FileHandle file = Gdx.files.external("DwarvesManager/saves/"+filename);
		XmlReader reader = new XmlReader();
		try {
			this.xml = reader.parse(file);
			this.loadFromXmlElement(this.xml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Envoie d'un evenement au script LUA
	 * @param eventID Indicatif d'evenement
	 */
	public void fireDwarfEvent(String eventID){
		fireDwarfEvent(eventID, null, null);
	}
	
	/**
	 * Envoie d'un evenement au script LUA
	 * @param eventID Indicatif d'evenement
	 */
	public void fireDwarfEvent(String eventID, Object param1){
		fireDwarfEvent(eventID, param1, null);
	}
	
	/**
	 * Envoie d'un evenement au script LUA
	 * @param eventID Indicatif d'evenement
	 */
	public void fireDwarfEvent(String eventID, Object param1, Object param2){
		getLevel().getDwScript().receiveDwarfEvent(eventID, param1, param2);
	}

	public Element getXml() {
		return xml;
	}

	public Characters getCharacters() {
		return characters;
	}

	public Rooms getRooms() {
		return rooms;
	}

	public Objects getObjects() {
		return objects;
	}

	public GameScene getView() {
		return view;
	}
	
	public Level getLevel() {
		return level;
	}

	public Bank getBank() {
		return bank;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
	
	/**
	 * Ajoute des infos dans le log qui apparait dans le jeu
	 * (Raccourci)
	 */
	public void log(String str){
		this.getView().getGameLog().log(str);
	}
	
	/**
	 * Ajoute des infos dans le log qui apparait dans le jeu
	 * (Raccourci)
	 */
	public void clearLog(){
		this.getView().getGameLog().clearLog();
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("DwarvesManagerGame", null);
		
		output.addChild(getCharacters().saveAsXmlElement());
		output.addChild(getObjects().saveAsXmlElement());
		output.addChild(getRooms().saveAsXmlElement());
		output.addChild(getLevel().saveAsXmlElement());
		output.addChild(getTaskManager().saveAsXmlElement());
		output.addChild(getBank().saveAsXmlElement());
		
		FileHandle file = Gdx.files.external(filename);
		this.filename = file.pathWithoutExtension(); 
		file = Gdx.files.external("DwarvesManager/Saves/"+this.filename+".xml");
		file.writeString(output.toString(), false);
		
		log(StringManager.getString("Saved"));
		
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		
		// Impossible de charger tout directement
		this.level = new Level(this, xmlElement.getChildByName("Level"));
		
		this.bank = new Bank(xmlElement.getChildByName("Bank"));
		
		this.rooms = new Rooms(this);
		this.characters = new Characters(this);
		this.objects = new Objects(this);
		this.taskManager = new TaskManager(this);
		
	}

	/**
	 * Seconde partie du chargement (Après l'initialisation complète des premières vues)
	 */
	public void loadObjects() {
		this.characters.loadFromXmlElement(this.xml.getChildByName("Characters"));
		this.rooms.loadFromXmlElement(this.xml.getChildByName("Rooms"));
		this.objects.loadFromXmlElement(this.xml.getChildByName("Objects"));
		this.taskManager.loadFromXmlElement(this.xml.getChildByName("TaskManager"));
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public static Game getInstance() {
		return instance;
	}
}
