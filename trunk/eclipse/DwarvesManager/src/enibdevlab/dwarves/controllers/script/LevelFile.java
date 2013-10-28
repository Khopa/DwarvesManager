package enibdevlab.dwarves.controllers.script;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 
 * Structure de données pour localiser les scripts des niveaux
 * -- Contient une liste statique des niveaux chargés (initialisé à partir de init)
 * 
 * @author Clément Perreau
 *
 */
public class LevelFile {

	public static final String scriptExternalLocation = "DwarvesManager/userLevels/scripts";
	public static final String scriptInternalLocation = "data/scripts";
	public static final String mapExternalLocation = "DwarvesManager/userLevels/maps";
	public static final String mapInternalLocation = "data/maps";
	
	/**
	 * Liste des niveaux
	 */
	private static ArrayList<LevelFile> levels;
	
	/**
	 * Nom du script
	 */
	private String name;
	
	/**
	 * Fichier interne ou non
	 */
	private Boolean internal;
	
	private LevelFile(String name, boolean internal){
		this.name = name;
		this.internal = internal;
	}
	
	/**
	 * Chargement de la liste des niveaux
	 */
	public static void init(){
		levels = new ArrayList<LevelFile>();
		
		System.out.println("Chargement des niveaux");
		
		// Chargement des scripts du repertoire interne
		XmlReader xmlReader = new XmlReader();
		Element levelList;
		try {
			levelList = xmlReader.parse(Gdx.files.internal(LevelFile.scriptInternalLocation + "/scripts.xml"));
			for(Element element:levelList.getChildrenByName("script")){
				levels.add(new LevelFile(element.getText(), true));
				System.out.println("Niveau interne : " + element.getText() + " chargé");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Chargement des scripts du repertoire utilisateur externe
		FileHandle scriptDirectory = Gdx.files.external(LevelFile.scriptExternalLocation);

		for(FileHandle file:scriptDirectory.list()){
			if(file.extension().equals("lua")){
				levels.add(new LevelFile(file.name(), false));
				System.out.println("Niveau externe : " + file.name() + " chargé");
			}
		}	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	public static String getExternallocation() {
		return scriptExternalLocation;
	}

	public static String getInternallocation() {
		return scriptInternalLocation;
	}

	public static ArrayList<LevelFile> getLevels() {
		return levels;
	}

	public static LevelFile getLevel(String name, boolean internal) {
		for(LevelFile lvl:getLevels()){
			if(lvl.isInternal() == internal && name.equals(lvl.getName())){
				return lvl;
			}
		}
		return null;
	}
	
	
}
