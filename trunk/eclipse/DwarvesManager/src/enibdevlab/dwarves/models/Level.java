package enibdevlab.dwarves.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.GameThread;
import enibdevlab.dwarves.controllers.script.DwarvesManagerScript;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Un niveau du jeu
 * 
 * @author Clément Perreau
 *
 */
public class Level implements IPersistent {
	
	/**
	 * Nom du niveau
	 */
	protected String name; 
	
	/**
	 * 'Map' du monde
	 */
	protected TileMap tilemap;

	/**
	 * Jeu
	 */
	protected Game game;
	
	/**
	 * Script
	 */
	protected DwarvesManagerScript script;
	
	/**
	 * Fichier de script
	 */
	protected LevelFile scriptFile;

	private GameThread thread;
	
	/**
	 * Constructeur
	 * @param path Chemin du fichier à charger
	 */
	public Level(Game game, LevelFile script){
		this.game = game;
		this.scriptFile = script;
		this.script = new DwarvesManagerScript(script);
		this.thread = new GameThread(game);
		DwarvesManager.timer.scheduleTask(this.thread, 1, 1);
	}

	public Level(Game game, Element xmlElement) {
		this.game = game;
		this.name = xmlElement.getName();
		this.loadFromXmlElement(xmlElement);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TileMap getTilemap() {
		return tilemap;
	}

	public void setTilemap(TileMap tilemap) {
		this.tilemap = tilemap;
	}

	public Game getGame() {
		return game;
	}

	public float getTileXSize() {
		return this.tilemap.getTileWidth();
	}
	
	public float getTileYSize() {
		return this.tilemap.getTileHeight();
	}

	public DwarvesManagerScript getDwScript() {
		return script;
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("Level", null);
		output.setAttribute("name", script.getLevelName());
		output.setAttribute("elapsedTime", Float.toString(thread.getElapsedTime()));
		output.setAttribute("timeBeforeWages", Float.toString(thread.getTimeBeforeWages()));
		Element scriptElement = new Element("Script", null);
		scriptElement.setAttribute("name", scriptFile.getName());
		int location = 0;
		if(scriptFile.isInternal()) location = 1;
		scriptElement.setAttribute("internal", Integer.toString(location));
		scriptElement.setAttribute("progression", Integer.toString(script.getProgression()));
		output.addChild(scriptElement);
		output.addChild(this.tilemap.saveAsXmlElement());
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		this.name = xmlElement.getAttribute("name");
		Element scriptElement = xmlElement.getChildByName("Script");
		String scriptFilename = scriptElement.getAttribute("name");
		int location = Integer.parseInt(scriptElement.getAttribute("internal"));
		int progression = Integer.parseInt(scriptElement.getAttribute("progression"));
		boolean internal = false;
		if(location == 1) internal = true; 
		this.scriptFile = LevelFile.getLevel(scriptFilename, internal);
		this.script = new DwarvesManagerScript(scriptFile);
		this.thread = new GameThread(game);
		DwarvesManager.timer.scheduleTask(this.thread, 1, 1);
		this.script.setProgression(progression);
		this.thread.setElapsedTime(Float.parseFloat(xmlElement.getAttribute("elapsedTime")));
		this.thread.setTimeBeforeWages(Float.parseFloat(xmlElement.getAttribute("timeBeforeWages")));
	}
	

	public FileHandle getMapFile() {
		FileHandle file = null;
		String mapName = script.getMapName();
		if(script.isInternal()){
			file = Gdx.files.internal(LevelFile.mapInternalLocation + "/" + mapName);
		}
		else{
			file = Gdx.files.external(LevelFile.mapExternalLocation + "/" + mapName);
		}
		return file;
	}
	
	public void init(){
		script.init();
	}
	
	public void setup(){
		script.setup();
	}
	
	public float getElapsedTime(){
		return this.thread.getElapsedTime();
	}
	
	public String getFormattedElapsedTime(boolean forceHour) {
		float elapsedTime = getElapsedTime();
		int seconds = (int) elapsedTime;
		int minutes = (int) Math.floor(elapsedTime/60);
		
		seconds-= minutes*60;
		int hours = minutes/60;
		minutes -= hours*60;
		if(hours > 0 || forceHour == true){
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		else return String.format("%02d:%02d", minutes, seconds);
		
	}
	
	public String getFormattedElapsedTime(){
		return getFormattedElapsedTime(false);
	}
	
	/**
	 * Temps avant les prochains salaires formaté
	 */
	public String getFormattedWageTimer() {
		float timeleft = this.thread.getTimeBeforeWages();
		int seconds = (int) (timeleft);
		int minutes = (int) Math.floor((timeleft)/60);
		
		seconds-= minutes*60;
		int hours = minutes/60;
		minutes -= hours*60;
		if(hours > 0){
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		else return String.format("%02d:%02d", minutes, seconds);
	}

	public int getDiamondsObjective() {
		return script.getDiamondsObjective();
	}
	
	public LevelFile getNextLevel(){
		String name = this.script.getNextLevelName();
		return LevelFile.getLevel(name, scriptFile.isInternal());
	}

	public String getLevelName() {
		return script.getLevelName();
	}

	public String getMusicFileName() {
		return script.getMusicFileName();
	}

	public LevelFile getScriptFile() {
		return this.scriptFile;
	}
	
}
