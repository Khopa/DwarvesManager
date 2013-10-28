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
 * Liste intelligente des pi�ces de la partie
 * 
 * @author Cl�ment Perreau
 *
 */
public class Rooms implements IPersistent {
	
	/**
	 * Lien vers le jeu
	 */
	protected Game game;
	
	/**
	 * Liste des pi�ces
	 */
	protected ArrayList<Room> rooms;
	
	public Rooms(Game game){
		this.game = game;
		this.rooms = new ArrayList<Room>();
	}
	
	/**
	 * Ajoute une pi�ce dans la partie
	 * @param room Pi�ce � ajouter
	 */
	public void addRoom(Room room){
		
		System.out.println(room);
	
		ArrayList<Room> intersected = new ArrayList<Room>(); // liste des pi�ces intersect�es
		ArrayList<Room> toRemove =   new ArrayList<Room>();  // liste des pi�ces d�truites
		
		for(Room room2:this.rooms){
			if (room2.intersect(room)&&room2.getClass()==room.getClass()) intersected.add(room2); // construction de la liste des pi�ces intersect�es
			else{
				if(room2.removeArea(room.getMainArea())) toRemove.add(room2);
			}
		}
		
		// Nettoyage effectif pour eviter la concurrentModif exception
		for(Room room2:toRemove){
			this.removeRoom(room2);
		}
		
		
		// TODO : Il y a encore des probl�mes avec les objets
		
		
		if(intersected.size() > 0){
			
			// Fusion des pi�ces
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
	 * Enl�ve une zone
	 * @param area
	 */
	public void removeArea(MapArea area){
		
		ArrayList<Room> toRemove = new ArrayList<Room>(); // Pi�ce d�truite par l'op�ration
		
		for(Room room:this.rooms){
			if(room.removeArea(area)) toRemove.add(room);
		}
		
		for(Room room:toRemove){
			this.removeRoom(room);
		}
		
	}
	
	/**
	 * Supprime une pi�ce de la partie 
	 * @param object Pi�ce � ajouter
	 */
	public void removeRoom(Room room){
		this.game.getView().getGameplayLayer().removeActor(room.getView()); // La vue de l'objet est aussi retir�e
		this.rooms.remove(room);
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * Retourne la liste des pi�ces d'un type donn�
	 * @param type Type de pi�ce
	 * @param mustBeOperationnal Exclu les pi�ces non op�rationnelle ou non
	 * @return Liste des pi�ces de type type
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
	 * Retourne la liste des pi�ces d'un type donn�
	 * ( /!\ Retourne aussi les pi�ces non op�rationnelles)
	 * @param type Type de pi�ce
	 * @return Liste des pi�ces de type type
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
		// Instanciation de toutes les pi�ces
		for(int i = 0; i<xmlElement.getChildCount(); i++){
			Room room = Room.fromXml(xmlElement.getChild(i), game.getLevel().getTilemap(), this);
			if(room != null){
				this.addRoom(room);
			}
		}
	}
	
}