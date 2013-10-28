package enibdevlab.dwarves.views.lang;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class StringManager {

	/**
	 * User language
	 */
	private static Language lang;
	
	/**
	 * Whether or not the language has already been defined by the user
	 */
	private static boolean langDefined = false;
	
	/**
	 * Dictionnary <key, content>
	 */
	private static HashMap<String, String> stringDictionnary;
	
	/**
	 * Dictionnaries directory
	 */
	private static final String dictionnariesDirectory = "data/translation/";
	
	/**
	 * Load existing lang configuration if any
	 * Fill up dictionnary
	 */
	public static void init(){
		lang = Language.fromString(Locale.getDefault().getLanguage());
		loadDictionnary();
	}
	
	/**
	 * Loading the dictionnary
	 */
	private static void loadDictionnary(){
		stringDictionnary = new HashMap<String, String>();
		
		String filepath = dictionnariesDirectory + "lang-" + Language.toString(lang) + ".xml";
		XmlReader xml = new XmlReader();
		FileHandle file = Gdx.files.internal(filepath);
		
		try {
			Element data = xml.parse(file);
			System.out.println("Loading dictionnary " + filepath);
			for(Element current:data.getChildrenByName("entry")){
				String key = current.getAttribute("key");
				String content = current.getAttribute("content");
				stringDictionnary.put(key, content);
				System.out.println(key + " : " + content);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	// ------------ Getters ----- Setters ------------------- \\
	
	public static String getString(String key){
		if(stringDictionnary.containsKey(key)) return stringDictionnary.get(key);
		else return "String <" + key + "> not found";
	}

	public static Language getLang() {
		return lang;
	}

	public static void setLang(Language lang) {
		StringManager.lang = lang;
	}


	public static boolean isLangDefined() {
		return langDefined;
	}
	
}
