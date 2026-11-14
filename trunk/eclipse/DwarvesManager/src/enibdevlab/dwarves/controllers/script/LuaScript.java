package enibdevlab.dwarves.controllers.script;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.MathLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 *
 * Script Lua avec LuaJ
 *
 * Web port: the original built JsePlatform/JmePlatform globals and loaded the
 * game API through `require "<java class name>"`, which relies on reflection.
 * Here we build the globals by hand from the pure-Java libraries only (no
 * luajava, no coroutine/io/os libs, which need threads or a filesystem), and
 * install the game API library directly.
 *
 * @author Clément Perreau
 *
 */
public class LuaScript {

	/**
	 * Contexte Lua
	 */
	protected Globals context;

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
	 * @param libraries Librairies Java à installer dans le contexte
	 */
	public LuaScript(String filename, boolean internal, LuaValue[] libraries){

		this.scriptName = filename;
		this.internal = internal;

		context = new Globals();
		context.load(new BaseLib());
		context.load(new PackageLib());
		context.load(new TableLib());
		context.load(new StringLib());
		context.load(new MathLib());
		LoadState.install(context);
		LuaC.install(context);

		for(LuaValue lib : libraries){
			context.load(lib);
		}

		FileHandle scriptFile;
		if(internal){
			scriptFile = Gdx.files.internal(LevelFile.scriptInternalLocation + "/" + filename);
		}
		else{
			scriptFile = Gdx.files.local(LevelFile.scriptExternalLocation + "/" + filename);
		}

		String code = scriptFile.readString("UTF-8");
		context.load(code, "script").call();
	}

	public boolean isInternal() {
		return internal;
	}

}
