package enibdevlab.dwarves.controllers.script;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jme.JmePlatform;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * Script Lua avec LuaJ
 * 
 * @author Clément Perreau
 *
 */
public class LuaScript {
	
	/**
	 * Code compilé Lua
	 */
	protected Prototype luaCode;
	
	/**
	 * Contexte Lua
	 */
	protected LuaValue context;
	
	/**
	 * Nom du script 
	 */
	protected String scriptName;
	
	/**
	 * Permet de savoir si la map est packagée ou non avec le jeu
	 */
	protected boolean internal;
	
	/**
	 * Crée un nouveau script Lua à partir du fichier spécifié
	 * @param filename Nom du script LUA
	 * @param internal Fichier interne ou non 
	 */
	public LuaScript(String filename, boolean internal, String[] required){
		
		this.scriptName = filename;
		this.internal = internal;
		
		/**
		 * Création du contexte Lua en fonction de la plateforme
		 */
		ApplicationType platform = Gdx.app.getType();
		if(platform == ApplicationType.Android || platform == ApplicationType.iOS){
			// On prend la version de JLua pour Java Mobile Edition 
			context = JmePlatform.standardGlobals();
		}
		else{
			// On prend la version de JLua pour Java Standard Edition
			context = JsePlatform.standardGlobals();
		}
		
		try {
			
			FileHandle scriptFile;
			if(internal){
				scriptFile = Gdx.files.internal(LevelFile.scriptInternalLocation + "/" + filename);
			}
			else{
				scriptFile = Gdx.files.external(LevelFile.scriptExternalLocation + "/" + filename);
			}
			
			
			/**
			 * Crée la liste des libs requises
			 */
			
			for(String lib:required){
				String line = "require \""+lib+"\"";
				Prototype requirement = LuaC.compile(new ByteArrayInputStream(line.getBytes()), "script");
				LuaClosure requirementClosure = new LuaClosure(requirement, context);
				requirementClosure.call();
			}
			
			String code = scriptFile.readString("UTF-8");
			luaCode = LuaC.compile(new ByteArrayInputStream(code.getBytes()), "script");
		
			LuaClosure closure = new LuaClosure(luaCode, context);
			closure.call();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public boolean isInternal() {
		return internal;
	}
	
}
