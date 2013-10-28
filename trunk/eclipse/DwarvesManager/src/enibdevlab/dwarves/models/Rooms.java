package enibdevlab.dwarves.models;

import java.util.ArrayList;

import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.actors.ARoom;

/**
 * 
 * Liste intelligente des pièces de la partie
 * 
 * @author Clément Perreau
 *
 */
public class Rooms implements IPersistent {
	
	/**
	 * Lien vers le jeu
	 */
	protected Game game;
	
	/**
	 * Liste des pièces
	 */
	protected ArrayList<Room> rooms;
	
	public Rooms(Game game){
		this.game = game;
		this.rooms = new ArrayList<Room>();
	}
	
	/**
	 * Ajoute une pièce dans la partie
	 * @param room Pièce à ajouter
	 */
	public void addRoom(Room room){
		
		System.out.println(room);
	
		ArrayList<Room> intersected = new ArrayList<Room>(); // liste des pièces intersectées
		ArrayList<Room> toRemove =   new ArrayList<Room>();  // liste des pièces détruites
		
		for(Room room2:this.rooms){
			if (room2.intersect(room)&&room2.getClass()==room.getClass()) intersected.add(room2); // construction de la liste des pièces intersectées
			else{
				if(room2.removeArea(room.getMainArea())) toRemove.add(room2);
			}
		}
		
		// Nettoyage effectif pour eviter la concurrentModif exception
		for(Room room2:toRemove){
			this.removeRoom(room2);
		}
		
		
		// TODO : Il y a encore des problèmes avec les objets
		
		
		if(intersected.size() > 0){
			
			// Fusion des pièces
			int i = 0;
			intersected.get(0).getAreas().add(room.getMainArea());
			for(Room current:intersected){
				if(i++>0){
					intersected.get(0).fusion(current);
					this.removeRoom(current);
				}
			}
			intersected.get(0).clean();
			
			// Prise en charge des objets recouverts par la nouvelle zone
			for(GameObject obj: game.getObjects().getObjects()){
				if(obj.getRoom()==null){
					if(room.objectIn(obj)){
						intersected.get(0).addObject(obj);
					}
				}
			}
			
			
			
		}
		else{
			
			this.rooms.add(room);
			this.game.getView().getGameplayLayer().addActor(new ARoom(room));
			
			// Prise en charge des objets recouverts
			for(GameObject obj: game.getObjects().getObjects()){
				if(obj.getRoom()==null){
					if(room.objectIn(obj)){
						room.addObject(obj);
					}
				}
			}
			
		}
		
	}
	
	/**
	 * Enlève une zone
	 * @param area
	 */
	public void removeArea(MapArea area){
		
		ArrayList<Room> toRemove = new ArrayList<Room>(); // Pièce détruite par l'opération
		
		for(Room room:this.rooms){
			if(room.removeArea(area)) toRemove.add(room);
		}
		
		for(Room room:toRemove){
			this.removeRoom(room);
		}
		
	}
	
	/**
	 * Supprime une pièce de la partie 
	 * @param object Pièce à ajouter
	 */
	public void removeRoom(Room room){
		this.game.getView().getGameplayLayer().removeActor(room.getView()); // La vue de l'objet est aussi retirée
		this.rooms.remove(room);
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * Retourne la liste des pièces d'un type donné
	 * @param type Type de pièce
	 * @param mustBeOperationnal Exclu les pièces non opérationnelle ou non
	 * @return Liste des pièces de type type
	 */
	public ArrayList<Room> getRooms(Class<? extends Room> type, boolean mustBeOperationnal){
		ArrayList<Room> output = new ArrayList<Room>();
		
		for(Room room : this.rooms){
			if(room.getClass() == type && (!mustBeOperationnal || room.isOperationnal())){
				output.add(room);
			}
			
		}
		
		return output;
	}

	/**
	 * Retourne la liste des pièces d'un type donné
	 * ( /!\ Retourne aussi les pièces non opérationnelles)
	 * @param type Type de pièce
	 * @return Liste des pièces de type type
	 */
	public ArrayList<Room> getRooms(Class<? extends Room> type){
		return getRooms(type, false);
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("Rooms", null);
		
		for(Room room:getRooms()){
			output.addChild(room.saveAsXmlElement());
		}
		
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		// Instanciation de toutes les pièces
		for(int i = 0; i<xmlElement.getChildCount(); i++){
			Room room = Room.fromXml(xmlElement.getChild(i), game.getLevel().getTilemap(), this);
			if(room != null){
				this.addRoom(room);
			}
		}
	}
	
}