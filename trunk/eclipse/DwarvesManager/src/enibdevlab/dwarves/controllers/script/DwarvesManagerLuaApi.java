package enibdevlab.dwarves.controllers.script;


import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.Language;
import enibdevlab.dwarves.views.lang.StringManager;


/**
 * 
 * Api destinée à Lua
 * 
 * @author Clément Perreau
 *
 */
public class DwarvesManagerLuaApi extends TwoArgFunction {

	/**
	 * Lien vers la partie
	 */
	public static Game game; 
	
	/**
	 * Initialisation de l'API
	 * @param game Partie de DwarvesManager
	 */
	public static void init(Game game){
		DwarvesManagerLuaApi.game = game;
	}
	
	public DwarvesManagerLuaApi(){}

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		library.set("addMoney", new addMoney());
		library.set("getMoney", new getMoney());
		library.set("getDiamonds", new getDiamonds());
		library.set("popup", new popup());
		library.set("moveCamera", new moveCamera());
		library.set("sound", new sound());
		library.set("log", new log());
		library.set("clearLog", new clearLog());
		library.set("getLang", new getLang());
		env.set("dwarf", library);
		return library;
	}
	
	/**
	 * Popup
	 */
	public static class popup extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue title, LuaValue text) {
			game.getView().spawnTextMessageWindow(title.tojstring(), text.tojstring());
			return null;
		}	
	}
	
	
	/**
	 * ARGENT
	 */
	public static class addMoney extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue value) {
			game.getBank().addMoney(value.toint());
			return null;
		}	
	}
	public static class getMoney extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue value) {
			return LuaValue.valueOf(game.getBank().getMoney());
		}	
	}

	public static class getDiamonds extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue value) {
			return LuaValue.valueOf(game.getBank().getDiamonds());
		}	
	}
	
	/**
	 * CAMERA
	 */
	public static class moveCamera extends ThreeArgFunction{
		@Override
		public LuaValue call(LuaValue _x, LuaValue _y, LuaValue _zoom){
			
			int x = _x.toint();
			int y = _y.toint();
			float zoom = _zoom.tofloat();
			
			game.getView().getGameplayLayer().addAction(
					Actions.scaleTo(zoom, zoom, .5f)
					);
			game.getView().getGameplayLayer().addAction(
					Actions.moveTo(-x*game.getLevel().getTileXSize()*zoom+DwarvesManager.getWidth()/2,
							   -y*game.getLevel().getTileYSize()*zoom+DwarvesManager.getHeight()/2,.5f)
							);
			return null;
		}
	}
	
	/**
	 * SON
	 */
	public static class sound extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue name){
			SoundManager.play(name.tojstring());
			return null;			
		}
	}
	
	/**
	 * LOG
	 */
	public static class log extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue name){
			game.log(name.tojstring());
			return null;			
		}
	}
	
	public static class clearLog extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			game.clearLog();
			return null;			
		}
	}
	
	public static class getLang extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			LuaString str = LuaValue.valueOf(Language.toString(StringManager.getLang()));
			return str;			
		}
	}
	
	
	
	
	
	
	
}
