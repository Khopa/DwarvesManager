package enibdevlab.dwarves.views.scenes.game;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;


/**
 * 
 * This layer is used to insert additionnal sprites in the game using scripts
 * 
 * @author Clément Perreau
 *
 */
public class ScriptableOverlay extends Group {

	/**
	 * Using scripts, user may add sprites in this map
	 */
	private HashMap<String, Actor> spriteMap;
	
	public ScriptableOverlay(){
		spriteMap = new HashMap<String, Actor>();
	}
	
	/**
	 * Add an actor to the overlayer
	 * @param key Actor's key
	 * @param actor Actor to add
	 */
	public void addActor(String key, Actor actor){
		if(spriteMap.containsKey(key)){
			spriteMap.get(key).remove();
		}
		spriteMap.put(key, actor);
		super.addActor(actor);
		System.out.println("Added " + key);
	}
	
	/**
	 * Remove the actor using the key 'key' if any
	 * @param key Key identifier
	 */
	public void removeActor(String key){
		if(spriteMap.containsKey(key)){
			spriteMap.get(key).remove();
			spriteMap.remove(key);
		}
	}
	
	/**
	 * Get the Actor identified by the key
	 * @param key Key identifier
	 * @return The actor identified by the key
	 */
	public Actor getActor(String key){
		return spriteMap.get(key);
	}
	
	
	
}
