package enibdevlab.dwarves.controllers.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.TileMove;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.misc.ObjectDistanceToPointComparator;
import enibdevlab.dwarves.models.misc.RoomDistanceToPointComparator;
import enibdevlab.dwarves.models.misc.VectorDistanceToPointComparator;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.views.world.Tile;

/**
 * Quelques fonctions utiles pour simplifier l'utilisation du PathFinder
 * 
 * @author Clément Perreau
 */
public class PathUtils {

	/**
	 * Regarde si le personnage donné peut accéder à un tile donné, et retourne le chemin si oui
	 * @param character Personnage
	 * @param tile Tile ciblé
	 * @param game Référence vers le jeu pour l'accés aux données
	 */
	public static ArrayList<Node> pathToTile(MCharacter character, Vector2 tile, Game game){
		int tx = (int)(tile.x);
		int ty = (int)(tile.y);
		int x = (int) character.getX();
		int y = (int) character.getY();
		
		Pathfinder pathfinder = new Pathfinder(game.getLevel().getTilemap(),
				new Node(x,y),
				new Node(tx, ty));
		pathfinder.run();
		
		ArrayList<Node> path = pathfinder.getPath();
		
		return path; 
	}
	
	/**
	 * Chemin vers une pièce
	 * Retourne le chemin vers le plus proche tile accesible composant cette pièce
	 * Null si il n'y a pas de chemin
	 * @param character Personnage 
	 * @param room Pièce à atteindre
	 * @param game Référence vers le jeu
	 */
	public static ArrayList<Node> pathToRoom(MCharacter character, Room room, Game game){
		
		int x = (int) character.getX();
		int y = (int) character.getY();
		
		// On recupère tous les tiles de la pièce
		ArrayList<Vector2> tiles = room.getTiles();
		
		// On les trie par rapport à leur distance par rapport au personnage
		Collections.sort(tiles, new VectorDistanceToPointComparator(
		         		 x,y));
		
		// On renvoie le premier chemin qu'on arrive à créer
		ArrayList<Node> path;
		for(Vector2 tile:tiles){
			path = pathToTile(character, tile, game);
			if(path!=null){
				return path;
			}
		}
		
		// Pas de chemin trouvé
		return null;
	}
	
	/**
	 * Renvoie le chemin vers un slot précis
	 * @param character Personnage 
	 * @param slot Slot à atteindre
	 * @param game Référence vers le jeu
	 * @return
	 */
	public static ArrayList<Node> pathToSlot(MCharacter character, Slot slot, Game game){
		return pathToTile(character, slot.getRealPosition(), game);
	}
	
	
	/**
	 * Retourne la liste ordonnée des pièces d'un type donné, triées par rapport à leur
	 * distance à une position donnée
	 * @param roomType Type de pièce
	 * @param fromPosition Position utilisée pour trier
	 * @param game Référence vers le jeu
	 */
	public static ArrayList<Room> getSortedRoom(Class<? extends Room> roomType, Vector2 fromPosition,  Game game){
		// Liste des pièces du type demandé
		ArrayList<Room> rooms = game.getRooms().getRooms(roomType, true);
		// On trie la liste des pièces
		Collections.sort(rooms, new RoomDistanceToPointComparator(
				         (int)fromPosition.x,(int)fromPosition.y));
		return rooms;
	}
	
	
	/**
	 * Retourne la liste ordonnée des pièces opérationnelle d'un type donné, triées par rapport à leur
	 * distance à une position donnée
	 * @param roomType Type de pièce
	 * @param fromPosition Position utilisée pour trier
	 * @param game Référence vers le jeu
	 */
	public static ArrayList<Room> getSortedOperationnalRoom(Class<? extends Room> roomType, Vector2 fromPosition,  Game game){
		ArrayList<Room> rooms = getSortedRoom(roomType, fromPosition, game);
		ArrayList<Room> toRemove = new ArrayList<Room>();
		for(Room room:rooms){
			if(!room.isOperationnal()) toRemove.add(room);
		}
		rooms.removeAll(toRemove);		
		return rooms;
	}
	
	
	/**
	 * Retourne la liste triée des objets d'un type donné utilisable par un personnage d'un type donné
	 * @param objectType Type d'objet 
	 * @param characterType Type de personnage qui souhaite utilisé l'objet (Mettre null si vous souhaitez ignorer ce paramètre)
	 * @param room Pièce
	 * @param fromPosition Position par rapport à laquelle
	 * @param game Référence vers le jeu pour l'accés aux données
	 */
	public static ArrayList<GameObject> getSortedUsableObjectInRoom(Class<? extends GameObject> objectType,
														  Class<? extends MCharacter> characterType,
														  Room room,
														  Vector2 fromPosition,
														  Game game){
		
		if(characterType == null) characterType = MCharacter.class;
		
		// Récupérer la liste des objets du type donné dans la pièce
		ArrayList<GameObject> objects = room.getObjects(objectType);
		
		
		if(objects.size()<=0) return null; // Si il y a pas d'objet c'est pas la peine de continuer
		
		// Enlever tous les objets qui n'ont pas de slot dispo pour le type de personnage donné
		ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
		for(GameObject obj:objects){
			if(!obj.slotAvailable(characterType) && obj.getSlots().size() > 0){ // Un objet sans slots est toujours considéré utilisable
				toRemove.add(obj);
			}
		}
		objects.removeAll(toRemove);
		toRemove.clear();
		
		if(objects.size()<=0) return null; // Si il y a plus d'objets c'est pas la peine de continuer
		
		// Tri des objets par rapport à leur distance au perso
		Collections.sort(objects, new ObjectDistanceToPointComparator(
		         				(int)fromPosition.x,(int)fromPosition.y));
		
		return objects;
	}
	
	/**
	 * Retourne le plus proche objet d'un type donné qui a un slot libre pour le type de personnage
	 * du personnage qui est utilisé comme référentiel de position s'il en existe un dans la pièce donnée
	 * NB : Ca renvoie l'objet le plus proche, pas forcément l'objet qui a le slot le plus proche
	 * Donc sur des courtes distance ça peut sembler ne pas fonctionner parfaitement
	 * @param objectType Type d'objet 
	 * @param characterType Type de personnage qui souhaite utilisé l'objet (Mettre null si vous souhaitez ignorer ce paramètre)
	 * @param room Pièce
	 * @param fromPosition Position par rapport à laquelle
	 * @param game Référence vers le jeu pour l'accés aux données
	 */
	public static GameObject getNearestUsableObjectInRoom(Class<? extends GameObject> objectType,
														   Class<? extends MCharacter> characterType,
														   Room room,
														   Vector2 fromPosition,
														   Game game){
		ArrayList<GameObject> objects = getSortedUsableObjectInRoom(objectType, characterType, room, fromPosition, game);
		if(objects == null) return null;
		else return objects.get(0);
	}
	
	/**
	 * Crée et ajoute la séquence d'action correspondant au chemin donné aux actions du personnage donné
	 * @param path Chemin
	 * @param character Personnage concerné
	 * @param game Référence vers le jeu pour l'accés aux données
	 * @return Séquence d'action correspondant au chemin donné
	 */
	public static ArrayList<TileMove> buildPath(List<Node> path, MCharacter character, Game game, float moveDuration){

		ArrayList<TileMove> output = new ArrayList<TileMove>();
		
		int x,y;
		
		if(path == null) return null;
		
		for(Node node:path){
			x = (int) (node.getPos().x*game.getLevel().getTileXSize());
			y = (int) (node.getPos().y*game.getLevel().getTileYSize());
			output.add(new TileMove(game, character, new Vector2(x,y), moveDuration));
		}
		
		return output;
	}
	
	
	/**
	 * Recherche un chemin vers un tile adjacent au tile donné
	 */
	public static ArrayList<Node> pathToAdjacentTile(Game game, MCharacter character, Vector2 tile){
		
		ArrayList<Node> path = new ArrayList<Node>();
		
		// On récupère le tile correspondant sur la map et les tiles adjacents
		Tile tmp = game.getLevel().getTilemap().getTile(0, (int)tile.x, (int)tile.y);
		Tile[] neighbours = {tmp.bottom(), tmp.top(), tmp.right(), tmp.left()}; // Tiles voisins
		ArrayList<Vector2> valid = new ArrayList<Vector2>(); // Liste des coordonnées de tiles valides
		
		// On élimine les tiles qui sont bloquants
		for(Tile adjacentTile:neighbours){
			
			// Il faut faire gaffe parce que un tile peut être null si on est sur un bord de map
			if(adjacentTile == null) continue; 
			
			// Crée une position valide si le tile est non bloquant
			if(!adjacentTile.isBlocking()){
				valid.add(new Vector2(adjacentTile.getX(), adjacentTile.getY()));
			}
		}
		
		// On retourne le premier chemin qu'on arrive à créer
		for(Vector2 destination: valid){
			path = pathToTile(character, destination, game);
			if(path != null) return path;
		}
		
		return null;
	}
	
	
	
	
	
	
	
}
