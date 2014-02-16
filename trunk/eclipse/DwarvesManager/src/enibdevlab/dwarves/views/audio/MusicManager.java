package enibdevlab.dwarves.views.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.khopa.skhopa.controllers.ConfigurationManager;

/**
 * 
 * Classe statique qui se charge de gérer les musiques
 * 
 * @author Clément Perreau
 *
 */
public class MusicManager {

	protected static boolean ENABLED = true;
	
	/**
	 * Musique actuelle
	 */
	protected static Music current;
	
	/**
	 * Volume de la musique (0f-1f)
	 */
	protected static float volume = .5f;
	
	/**
	 * Internal Folder location
	 */
	protected static String musicFolder = "data/music/";
	
	/**
	 * Music file extension
	 */
	protected static String format = "ogg";
	
	
	public static void init(){
		volume = ConfigurationManager.getInstance().getFloatValue("musicVolume");
	}
	
	public static void setVolume(float newValue){
		assert (newValue >=0.0 && newValue <=1.0);
		volume = newValue;
		if (current != null){
			current.setVolume(volume);
		}
	}
	
	
	public static boolean playMusic(String filename){
		boolean result = setMusic(filename);
		if(result&&ENABLED) play();
		return result;
	}
	
	public static boolean setMusic(String filename){

		if(!(current == null)) current.stop();

		current = Gdx.audio.newMusic(Gdx.files.internal(musicFolder+filename+"."+format));
		if(current == null) return false;
		
		current.setVolume(volume);
		current.setLooping(true);

		return true;
	}
	
	public static void stop(){
		if(!(current == null)) current.stop();
	}
	
	public static void pause(){
		if(!(current == null)) current.pause();
	}
	
	public static void play(){
		if(!(current == null)) current.play();
	}
	
	public static boolean isPlaying(){
		if(!(current == null)) return current.isPlaying();
		return false;
	}
	
	public static float getVolume(){
		return volume;
	}
	
	
	
	
}
