package enibdevlab.dwarves.views;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;
import enibdevlab.dwarves.views.actors.characters.config.EyesConfig;
import enibdevlab.dwarves.views.actors.characters.config.HandsConfig;
import enibdevlab.dwarves.views.actors.characters.config.HeadConfig;
import enibdevlab.dwarves.views.actors.characters.config.MouthConfig;
import enibdevlab.dwarves.views.lang.Language;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Charge une grande partie des ressources graphiques
 * 
 * @author Clément
 *
 */
public class Loader {
	
	/**
	 * Configuration du nain
	 */
	public static BodyConfig dwarf;
	
	/**
	 * Configuration du nain barman
	 */
	public static BodyConfig barmanConfig;
	
	/**
	 * Configuration du nain artisan
	 */
	public static BodyConfig craftmanConfig;
	
	/**
	 * Configuration de l'elfe
	 */
	public static BodyConfig elf;
	
	/**
	 * Atlas des objets
	 */
	public static Tileset objectAtlas;
	
	/**
	 * Atlas de la Gui
	 */
	public static Tileset guiAtlas;
	
	/**
	 * Atlas des icones
	 */
	public static Tileset guiAtlasSmall;
	
	/**
	 * MainMenuGui
	 */
	public static Tileset mainMenuGui;
	

	/**
	 * Bouton (petite taille)
	 */
	public static Tileset mainMenuGuiSmall;
	
	/**
	 * social buttons
	 */
	public static Tileset social;
	
	/**
	 * Levels icons
	 */
	public static Tileset levelsIcons;
	
	/**
	 * Atlas des icones
	 */
	public static Tileset iconAtlas;
	
	/**
	 * Atlas des items
	 */
	public static Tileset itemAtlas;
	
	/**
	 * Atlas des plaintes (petit sprite affiché quand un nain se plaint, pour indiquer la raison)
	 */
	public static Tileset complaintAtlas;
	
	/**
	 * Référence sur les constructeurs des objets (permet de les instancier dynamiquement)
	 */
	public static Constructor<?>[] objects; 
	
	/**
	 * Package contenant les objets
	 */
	public static String objectsPackage;
	
	/**
	 * Liste des objets sous forme de String[]
	 */
	public static String[] objectsName;
	
	/**
	 * Référence sur les constructeurs des pièces (permet de les instancier dynamiquement)
	 */
	public static Constructor<?>[] rooms; 
	
	/**
	 * Package contenant les pièces
	 */
	public static String roomsPackage;
	
	/**
	 * Liste des pièces sous forme de String[]
	 */
	public static String[] roomsName;
	
	/**
	 * Référence sur les constructeurs des personnages (permet de les instancier dynamiquement)
	 */
	public static Constructor<?>[] characters; 
	
	/**
	 * Package contenant les personnages
	 */
	public static String characterPackage;
	
	/**
	 * Liste des pièces sous forme de String[]
	 */
	public static String[] charactersName;
	
	/**
	 * Répertoire de sauvegarde (A partir de la carte SD sur Android, ou des documents de la session sur un Desktop)
	 */
	public static String saveDirectory = "DwarvesManager/Saves/";
	
	// Couleurs
	public static Color AYELLOW = new Color(0f,.9f,0f,.25f); 
	public static Color ARED    = new Color(0f,.5f,.7f,.25f); 
	
	public static void init(){
		
		// Chargement du nain (Le nain de base est le mineur)
		
		Tileset dwarfHandsTileset = new Tileset("data/tileset/hands.png", 32, 32);
		Tileset dwarfBodyTileset = new Tileset("data/tileset/dwarf/dwarf_body.png", 128, 128);
		Tileset dwarfHeadTileset = new Tileset("data/tileset/dwarf/dwarf_head.png", 64, 128);
		Tileset dwarfEyesTileset = new Tileset("data/tileset/dwarf/dwarf_eyes.png", 16, 8);
		Tileset dwarfMouthTileset  = new Tileset("data/tileset/dwarf/dwarf_mouth.png", 16, 8);
		
		EyesConfig dwarfEyes = new EyesConfig(dwarfEyesTileset, new Vector2(13,26), 3,1);
		HeadConfig dwarfHead = new HeadConfig(dwarfHeadTileset, new Vector2(0,0));
		HandsConfig dwarfHand = new HandsConfig(dwarfHandsTileset.getTile(0), new Vector2(5f/4f, -40));
		MouthConfig dwarfMouth = new MouthConfig(dwarfMouthTileset, new Vector2(-1,6), new Vector2(20,0));

		dwarf = new BodyConfig(dwarfBodyTileset, new Vector2(0,0), dwarfHead, dwarfMouth, dwarfEyes, dwarfHand);
	
		// Barman
		Tileset barmanBodyTileset = new Tileset("data/tileset/dwarf/barman_body.png", 128, 128);
		Tileset barmanHeadTileset = new Tileset("data/tileset/dwarf/barman_head.png", 64, 128);
		HeadConfig barmanHead = new HeadConfig(barmanHeadTileset, new Vector2(0,0));
		barmanConfig = new BodyConfig(barmanBodyTileset, new Vector2(0,0), barmanHead, dwarfMouth, dwarfEyes, dwarfHand);
	
		// Artisan
		Tileset craftmanBodyTileset = new Tileset("data/tileset/dwarf/craftsman_body.png", 128, 128);
		Tileset craftmanHeadTileset = new Tileset("data/tileset/dwarf/craftsman_head.png", 64, 128);	
		HeadConfig craftmanHead = new HeadConfig(craftmanHeadTileset, new Vector2(0,0));
		craftmanConfig = new BodyConfig(craftmanBodyTileset, new Vector2(0,0), craftmanHead, dwarfMouth, dwarfEyes, dwarfHand);
		
		// Chargement de l'elfe
		
		Tileset elfBodyTileset = new Tileset("data/tileset/elf/elf_body.png", 128, 128);
		Tileset elfHeadTileset = new Tileset("data/tileset/elf/elf_head.png", 64, 128);
		Tileset elfEyesTileset = new Tileset("data/tileset/elf/elf_eyes.png", 16, 8);
		Tileset elfMouthTileset  = new Tileset("data/tileset/elf/elf_mouth.png", 16, 8);
		
		EyesConfig elfEyes = new EyesConfig(elfEyesTileset, new Vector2(11,45), 3,1);
		HeadConfig elfHead = new HeadConfig(elfHeadTileset, new Vector2(0,53));
		HandsConfig elfHand = new HandsConfig(dwarfHandsTileset.getTile(1), new Vector2(5f/4f, 15));
		MouthConfig elfMouth = new MouthConfig(elfMouthTileset, new Vector2(-1,15), new Vector2(20,0));
		
		elf = new BodyConfig(elfBodyTileset, new Vector2(0,0), elfHead, elfMouth, elfEyes, elfHand);
		
		// CHARGEMENT DES TEXTURES DE LA GUI
		guiAtlas = new Tileset("data/sprites/guiAtlas.png", 128, 128);
		guiAtlasSmall = new Tileset("data/sprites/guiSmallAtlas.png", 32, 32);
		iconAtlas = new Tileset("data/sprites/iconAtlas.png", 64, 64);
		
		// CHARGEMENT DES OBJETS
	
		// Chargement des textures des objets :
		objectAtlas = new Tileset("data/sprites/objectAtlas.png", 64, 64);
		
		
		String lang = Language.toString(StringManager.getLang());
		String guiSmall = "data/sprites/mainMenuGuiSmall"+"-"+lang+".png";
		String gui =  "data/sprites/mainMenuGui"+"-"+lang+".png";
		
		mainMenuGuiSmall = new Tileset(guiSmall, 256, 64);
		if(DwarvesManager.getWidth()>800 && DwarvesManager.getHeight() > 600){
			mainMenuGui = new Tileset(gui, 512, 128);
		}
		else{
			mainMenuGui = mainMenuGuiSmall;
		}
		
		// chargement des icones de niveaux
		levelsIcons = new Tileset("data/sprites/levels.png", 512, 512);
		
		//chargement des bouttons sociaux
		social = new Tileset("data/sprites/social.png", 64, 64);
		
		// Texture des items
		itemAtlas = new Tileset("data/sprites/itemAtlas.png", 64, 64);
		
		// Texture des plaintes
		complaintAtlas = new Tileset("data/sprites/complaintAtlas.png", 32, 32);
		
		// Chargement des objets par introspection
		objectsName = new String[]{"Bed", "TableObject", "Barrel", "Anvil", "Rack", "Counter"};
		objectsPackage = "enibdevlab.dwarves.models.objects.";
		// On sait que tous les objets ne prennent qu'un Vector2 en parametre de construction
		Class<?>[] parameters = {Vector2.class};
		
		objects = introspectionLoading(objectsName, objectsPackage, parameters);

		// CHARGEMENT DES PIECES
		
		// Chargement des objets par introspection
		roomsName = new String[]{"Dorm", "Tavern", "Workshop"};
		roomsPackage = "enibdevlab.dwarves.models.rooms.";
		// On connait les paramètres des rooms (un mapArea)
		parameters = new Class[]{MapArea.class, Rooms.class};
		
		rooms = introspectionLoading(roomsName, roomsPackage, parameters);
		
		// CHARGEMENT DES PIECES
		
		// Chargement des objets par introspection
		charactersName = new String[]{"Miner", "Barman", "Craftman"};
		characterPackage = "enibdevlab.dwarves.models.characters.";
		// On connait les paramètres des rooms (un mapArea)
		parameters = new Class[]{Vector2.class};
		
		characters = introspectionLoading(charactersName, characterPackage, parameters);
		
	}
	
	
	/**
	 * Charge une liste de constructeur
	 * @param names Liste des noms des classes recherchées
	 * @param packageName Nom du package dans lequel on recherche
	 * @param parameters Liste des classes formant les paramètres du constructeur cherché
	 * @return
	 */
	private static Constructor<?>[] introspectionLoading(
			String[] names, String packageName, @SuppressWarnings("rawtypes") Class[] parameters){
		
		Constructor<?>[] constructors = new Constructor<?>[names.length];
		int i = 0;
		for(String s:names){
			try {
				Class<?> classe = Class.forName(packageName + s);
				try {
					constructors[i] = classe.getConstructor(parameters);
					i++;
					System.out.println("Chargement de " + s + ".class");
				} catch (NoSuchMethodException e) {
					System.out.println("Constructeur non trouvé pour : " + s);
					e.printStackTrace();
				} catch (SecurityException e) {
					System.out.println("Security exception pour : " + s);
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Impossible de charger la classe " + s);
				e.printStackTrace();
			}
		}
		return constructors;
	}

	/**
	 * Instancie un GameObject à partir du nom de la classe
	 * @param objectName Nom de la classe à instancier
	 */		
	public static GameObject instanciateObjectToPlace(String objectName, Vector2 position, boolean usePackageName) {
		if(!usePackageName) objectName = objectName.substring(objectsPackage.length());
		for(Constructor<?> constructor:objects){
			if(constructor.getName().substring(objectsPackage.length()).equals(objectName)){
				try {
					return (GameObject)(constructor.newInstance(position));
				} catch (InstantiationException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return null;
	}
	
	/**
	 * Instancie une pièce à partir du nom du type de pièce
	 */
	public static Room instantiateRoomToPlace(String roomName, MapArea area, boolean usePackageName, Rooms gameRooms){
		if(!usePackageName) roomName = roomName.substring(roomsPackage.length());
		for(Constructor<?> constructor:rooms){
			if(constructor.getName().substring(roomsPackage.length()).equals(roomName)){
				try {
					return (Room)(constructor.newInstance(area,gameRooms));
				} catch (InstantiationException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return null;
	}
	
	/**
	 * Instancie un personnage à partir du nom de sa classe
	 * @param characterName Nom de la classe de personnage (par exemple "Miner")
	 * @param position Position du personnage
	 */
	public static MCharacter instantiateCharacterToPlace(String characterName, Vector2 position, boolean usePackageName){
		if(!usePackageName) characterName = characterName.substring(characterPackage.length());
		for(Constructor<?> constructor:characters){
			if(constructor.getName().substring(characterPackage.length()).equals(characterName)){
				try {
					return (MCharacter)(constructor.newInstance(position));
				} catch (InstantiationException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return null;
	}
	
	/**
	 * Retourne un nom aléatoire pour un nain
	 */
	public static String randomName(){
		FileHandle file = Gdx.files.internal("data/dwarf.name");
		String str = file.readString();
		String[] names = str.split("\n");
		str = names[DwarvesManager.random.nextInt(names.length-1)];
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * Retourne un nom aléatoire pour un nain
	 */
	public static String randomFirstName(){
		FileHandle file = Gdx.files.internal("data/dwarf_en.name");
		String str = file.readString();
		String[] names = str.split("\n");
		str = names[DwarvesManager.random.nextInt(names.length-1)];
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * Retourne un nom aléatoire pour un nain
	 */
	public static String randomLastName(){
		FileHandle file = Gdx.files.internal("data/dwarf_en_last.name");
		String str = file.readString();
		String[] names = str.split("\n");
		str = names[DwarvesManager.random.nextInt(names.length-1)];
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * Charge les noms des sauvegardes
	 */
	public static String[] getSavegames(){
		ArrayList<String> saves = new ArrayList<String>();
		FileHandle directoryChecker = Gdx.files.external(saveDirectory);
		for(FileHandle child:directoryChecker.list()){
			if(child.extension().equals("xml")){
				saves.add(child.name());
			}
		}
		String[] output = new String[saves.size()];
		int i = 0;
		for(String file:saves){
			output[i++] = file;
		}
		return output;
	}
	

	
	
}
