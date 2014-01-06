package enibdevlab.dwarves.controllers.script;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

/**
 * 
 * Script Lua pour Dwarves Manager
 * 
 * @author Clément Perreau
 *
 */
public class DwarvesManagerScript extends LuaScript{

	/**
	 * Fonction d'initialisation de la partie
	 */
	protected LuaFunction luaInit;
	
	/**
	 * Fonction de setup de la partie
	 */
	protected LuaFunction luaSetup;
	
	/**
	 * Boucle principale
	 */
	protected LuaFunction luaRun;
	
	/**
	 * Fonction evenement
	 */
	protected LuaFunction luaEvent;
	
	/**
	 * Api
	 */
	protected LuaValue api;
	
	
	/**
	 * Constructeur d'un script Dwarves Manager
	 * @param filename Fichier de script Dwarves Manager
	 */
	public DwarvesManagerScript(LevelFile script) {
		super(script.getName(), script.isInternal(), new String[]{"enibdevlab.dwarves.controllers.script.DwarvesManagerLuaApi"});
		load();
	}
	
	/**
	 * Recherche dans le script les fonctions souhaitée
	 */
	private void load() {
		luaInit  = (LuaFunction) context.get("init");
		luaSetup = (LuaFunction) context.get("setup");
		luaRun = (LuaFunction) context.get("run");
		
		try{
			luaEvent = (LuaFunction) context.get("onEvent");
		}
		catch(ClassCastException e){
			luaEvent = null;
		}
		
	}
	
	public void receiveDwarfEvent(String name, Object param1, Object param2){
		System.out.print("DwarfEvent : " + name + " ");
		System.out.print(param1);
		System.out.print(" ");
		System.out.println(param2);
		
		String str1 = "null";
		String str2 = "null";
		if(param1!=null) str1 = param1.toString();
		if(param2!=null) str2 = param2.toString();
		
		if(luaEvent != null){
			luaEvent.call(LuaString.valueOf(name), LuaString.valueOf(str1), LuaString.valueOf(str2));
		}
	}
	
	public String getMapName(){
		return context.get("map").tojstring();
	}
	
	public void init(){
		luaInit.call();
	}

	public void setup(){
		luaSetup.call();
	}
	
	public void run(){
		luaRun.call();
	}

	public int getDiamondsObjective() {
		return context.get("objective").toint();
	}

	public String getNextLevelName() {
		return context.get("nextLevel").tojstring();
	}

	public String getLevelName() {
		return context.get("levelName").tojstring();
	}

	public int getProgression() {
		return context.get("progression").toint();
	}

	public void setProgression(int progression) {
		context.set("progression", progression);
	}

	public String getMusicFileName() {
		return "data/music/" + context.get("music").tojstring();
	}

}
