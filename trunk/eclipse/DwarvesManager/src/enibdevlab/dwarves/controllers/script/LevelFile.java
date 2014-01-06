package enibdevlab.dwarves.controllers.script;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.views.lang.Language;
import enibdevlab.dwarves.views.lang.StringManager;

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
	 * Titre du niveau
	 */
	private String title;
	
	/**
	 * Fichier interne ou non
	 */
	private Boolean internal;
	
	/**
	 * Id de texture a afficher pour presenter le niveau (voir fichier levels.png)
	 */
	private int textureID;
	
	private LevelFile(String name, String title, boolean internal, int textureID){
		this.name = name;
		this.internal = internal;
		this.title = title;
		this.textureID = textureID;
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
				String title = "unnamed";
				String name = element.getAttribute("name");
				int textureID = 0;
				try{
					textureID = Integer.parseInt(element.getAttribute("textureID"));
				}
				catch(GdxRuntimeException e){
					textureID = 0;
				}
				
				// recupere le titre dans la bonne langue
				for(Element titleElement:element.getChildrenByName("title")){
					if(titleElement.getAttribute("lang").equals(Language.toString(StringManager.getLang()))){
						title = titleElement.getText();
					}
				}
				
				levels.add(new LevelFile(name, title, true, textureID));
				System.out.println("Niveau interne : " + name + " chargé");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Chargement des scripts du repertoire utilisateur externe
		FileHandle scriptDirectory = Gdx.files.external(LevelFile.scriptExternalLocation);

		for(FileHandle file:scriptDirectory.list()){
			if(file.extension().equals("lua")){
				levels.add(new LevelFile(file.name(), file.name(), false,0));
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconID() {
		return textureID;
	}


	
	
}
