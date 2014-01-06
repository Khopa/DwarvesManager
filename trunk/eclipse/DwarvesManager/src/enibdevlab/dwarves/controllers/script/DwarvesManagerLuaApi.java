package enibdevlab.dwarves.controllers.script;


import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.actions.animations.GUIAnimation;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Barman;
import enibdevlab.dwarves.models.characters.Craftman;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.characters.Needs;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.SpriteActor;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.Language;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.game.GameGui;
import enibdevlab.dwarves.views.widgets.ObjectButton;


/**
 * 
 * Api destinée à Lua
 * TODO : organiser l'API par fonctionnalités
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
		library.set("disableRecruitment", new disableRecruitment());
		library.set("enableRecruitment", new enableRecruitment());
		library.set("disableRoom", new disableRoom());
		library.set("enableRoom", new enableRoom());
		library.set("disableObjects", new disableObjects());
		library.set("enableObjects", new enableObjects());
		library.set("restoreGui", new restoreGUI());
		library.set("addSprite", new addSprite());
		library.set("removeSprite", new removeSprite());
		library.set("addDwarf", new addDwarf());
		library.set("blink", new blink());
		library.set("blinkDwarf", new blinkDwarf());
		library.set("blinkRoom", new blinkRoom());
		library.set("blinkObject", new blinkObject());
		library.set("dwarfCount", new dwarfCount());
		library.set("minerCount", new minerCount());
		library.set("backGameMenu", new backGameMenu());
		library.set("configure", new configure());
		library.set("flushRooms", new flushRooms());
		library.set("setSleepNeed", new setSleepNeed());
		library.set("setThirstNeed", new setBeerNeed());
		library.set("getDwarfPosition", new setBeerNeed());
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
	
	/**
	 *
	 * Fonctions GUI
	 *
	 */
	
	public static class restoreGUI extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setRecruitmentButton(true);
			GameGui.setObjectButton(true);
			GameGui.setRoomButton(true);
			new backGameMenu().call();
			return null;			
		}
	}
	
	public static class enableRecruitment extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setRecruitmentButton(true);
			new backGameMenu().call();
			return null;			
		}
	}
	
	
	public static class disableRecruitment extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setRecruitmentButton(false);
			new backGameMenu().call();
			return null;			
		}
	}
	
	public static class enableRoom extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setRoomButton(true);
			new backGameMenu().call();
			return null;			
		}
	}
	
	
	public static class disableRoom extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setRoomButton(false);
			new backGameMenu().call();
			return null;			
		}
	}
	
	/**
	 * Active le bouton d'achats d'objets
	 * @author Clément Perreau
	 */
	public static class enableObjects extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setObjectButton(true);
			new backGameMenu().call();
			return null;			
		}
	}
	
	/**
	 * Désactive le bouton d'achats d'objets
	 * @author Clément Perreau
	 */
	public static class disableObjects extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			GameGui.setObjectButton(false);
			new backGameMenu().call();
			return null;			
		}
	}
	
	/**
	 * Ajoute un sprite aux coordonnées cibles
	 * @author Clément Perreau
	 *
	 */
	public static class addSprite extends ThreeArgFunction{
		@Override
		public LuaValue call(LuaValue x, LuaValue y, LuaValue name){
			SpriteActor actor = new SpriteActor(Loader.iconAtlas.getTile(0));
			actor.setPosition(x.toint(), y.toint());
			game.getView().getScriptableOverlay().addActor(name.tojstring(), actor);
			return null;
		}
	}
	
	/**
	 * Enlève le sprite portant la clef donnée
	 * @author Clément
	 *
	 */
	public static class removeSprite extends OneArgFunction{
		@Override
		public LuaValue call(LuaValue name){
			game.getView().getScriptableOverlay().removeActor(name.tojstring());
			return null;
		}
	}
	
	
	/**
	 * Vide tous les sprites
	 * @author Clément Perreau
	 */
	public static class flush extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			return null;
		}
	}
	
	/**
	 * Ajoute le nain désiré à l'endroit ciblé
	 * @author Clément
	 *
	 */
	public static class addDwarf extends ThreeArgFunction{
		@Override
		public LuaValue call(LuaValue x, LuaValue y, LuaValue job){
			
			if(job.tojstring().equals("Miner")){
				game.getCharacters().addCharacter(new Miner(new Vector2(x.tofloat(), y.tofloat())));
			}
			else if(job.tojstring().equals("Craftman")){
				game.getCharacters().addCharacter(new Craftman(new Vector2(x.tofloat(), y.tofloat())));
			}
			else if(job.tojstring().equals("Bartender")){
				game.getCharacters().addCharacter(new Barman(new Vector2(x.tofloat(), y.tofloat())));
			}
			
			return null;
		}
	}
	
	/**
	 * Active ou désactive l'objet qui porte le nom donné
	 * @param buttons Map de boutons/constructeurs d'objets
	 * @param type Nom du type de bouton qu'on souhaite modifier
	 * @param enable Active ou désactive
	 */
	public static void configureButton(HashMap<ObjectButton, Constructor<?>> buttons, String type, boolean enable){
		for(ObjectButton button:buttons.keySet()){
			String name = buttons.get(button).getDeclaringClass().getSimpleName();
			if(name.equals(type)) button.setEnabled(enable);
		}
	}
	
	/**
	 * Configure the availability of buttons
	 * @author Clément Perreau
	 */
	public static class configure extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue type, LuaValue enable){
			DwarvesManagerLuaApi.configureButton(game.getView().getGameGui().getRecruitsButton(), type.tojstring(), enable.toboolean());
			DwarvesManagerLuaApi.configureButton(game.getView().getGameGui().getRoomsButton(), type.tojstring(), enable.toboolean());
			DwarvesManagerLuaApi.configureButton(game.getView().getGameGui().getObjectsButton(), type.tojstring(), enable.toboolean());
			return null;
		}
	}
	
	/**
	 * Fait clignoter le bouton indiqué
	 * @author Clément Perreau
	 */
	public static class blink extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue button, LuaValue mode){
			Actor buttonActor = null;
			if(button.tojstring().equals("Back")){
				buttonActor = game.getView().getGameGui().getBackButton();
			}
			else if(button.tojstring().equals("Menu")){
				buttonActor = game.getView().getGameGui().getGraphButton();
			}
			else if(button.tojstring().equals("Room")){
				buttonActor = game.getView().getGameGui().getRoomButton();
			}
			else if(button.tojstring().equals("Mine")){
				buttonActor = game.getView().getGameGui().getMineButton();
			}
			else if(button.tojstring().equals("Dwarf")){
				buttonActor = game.getView().getGameGui().getDwarfButton();
			}
			else if(button.tojstring().equals("Object")){
				buttonActor = game.getView().getGameGui().getObjectButton();
			}
			
			if(buttonActor != null){
				if(mode.toboolean()){
					buttonActor.addAction(GUIAnimation.blink());
				}
				else{
					buttonActor.clearActions();
					buttonActor.setColor(Color.WHITE);
				}
			}
			
			return null;
		}
	}
	
	
	
	public static void blink(HashMap<ObjectButton, Constructor<?>> buttons, String target, boolean enable){
		for(ObjectButton button:buttons.keySet()){
			String name = buttons.get(button).getDeclaringClass().getSimpleName();
			if(name.equals(target)){
				if(enable){
					button.addAction(GUIAnimation.blink());
				}
				else{
					button.clearActions();
					button.setColor(Color.WHITE);
				}
			}
		}
	}
	
	/**
	 * Fait clignoter un type de nain
	 * @author Clément Perreau
	 */
	public static class blinkDwarf extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue dwarfType, LuaValue mode){
			DwarvesManagerLuaApi.blink(game.getView().getGameGui().getRecruitsButton(), dwarfType.tojstring(), mode.toboolean());
			return null;
		}
	}
	
	/**
	 * Fait clignoter un type de piece
	 * @author Clément Perreau
	 */
	public static class blinkRoom extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue roomType, LuaValue mode){
			DwarvesManagerLuaApi.blink(game.getView().getGameGui().getRoomsButton(), roomType.tojstring(), mode.toboolean());
			return null;
		}
	}
	
	/**
	 * Fait clignoter un type d'objet
	 * @author Clément Perreau
	 */
	public static class blinkObject extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue objectType, LuaValue mode){
			DwarvesManagerLuaApi.blink(game.getView().getGameGui().getObjectsButton(), objectType.tojstring(), mode.toboolean());
			return null;
		}
	}
	
	/**
	 * Compte des nains
	 * @author Clément Perreau
	 */
	public static class dwarfCount extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			LuaValue value = LuaValue.valueOf(game.getCharacters().getCharacters().size());
			return value;
		}
	}
	
	
	/**
	 * Compte des mineurs
	 * @author Clément Perreau
	 */
	public static class minerCount extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			LuaValue value = LuaValue.valueOf(game.getCharacters().getMiner().size());
			return value;
		}
	}
	
	
	/**
	 * Retour au menu principal de la gui du jeu
	 * @author Clément Perreau
	 */
	public static class backGameMenu extends ZeroArgFunction{
		@Override
		public LuaValue call(){
			game.getView().getGameGui().setMainMode();  // Retour au menu principal de la gui
			game.getView().setNormalMode();             // La scene se remet dans le mode ou on est pas en train de placer de nains
			return null;
		}
	}
	
	
	/**
	 * Efface toutes les pièces
	 * @author Clément Perreau
	 */
	public static class flushRooms extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue dwarfType, LuaValue enable){
			game.getRooms().clear();
			return null;
		}
	}
	
	/**
	 * Setter pour la valeur de besoin de sommeil
	 * @author Clément Perreau
	 */
	public static class setSleepNeed extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue dwarfID, LuaValue value){
			Dwarf dwarf = (Dwarf)Game.getInstance().getCharacters().getCharacters().get(dwarfID.toint());
			dwarf.getNeeds().setSleep((int)(value.tofloat()*Needs.MAX));
			return null;
		}
	}
	
	/**
	 * Setter pour la valeur de besoin de bière
	 * @author Clément Perreau
	 */
	public static class setBeerNeed extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue dwarfID, LuaValue value){
			Dwarf dwarf = (Dwarf)Game.getInstance().getCharacters().getCharacters().get(dwarfID.toint());
			dwarf.getNeeds().setThirst((int)(value.tofloat()*Needs.MAX));
			return null;
		}
	}
	
	public static class getDwarfPosition extends TwoArgFunction{
		@Override
		public LuaValue call(LuaValue dwarfID, LuaValue value){
			Vector2 pos = Game.getInstance().getCharacters().getCharacters().get(dwarfID.toint()).getPosition();
			LuaTable table = new LuaTable();
			table.add(pos.x);
			table.add(pos.y);
			return table;
		}
	}
	
	
	
	
	
	
	
	
	
}
