package enibdevlab.dwarves.models.rooms;

import com.badlogic.gdx.graphics.Color;

import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.characters.Barman;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.Barrel;
import enibdevlab.dwarves.models.objects.Counter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.TableObject;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.world.TileMap;

public class Tavern extends Room {

	public static Color color = new Color(.8f,.7f,.1f,.2f); 

	public Tavern(int i, int j, int w, int h, TileMap tilemap, Rooms rooms) {
		super(i, j, w, h, tilemap, rooms);
		
		// Définition des objets requis
		neededObjects.put(Barrel.class, 1);
		neededObjects.put(Counter.class, 1);
		neededObjects.put(TableObject.class, 1);
		
	}
	
	public Tavern(MapArea mapArea, Rooms rooms) {
		this(mapArea.getI(), mapArea.getJ(), mapArea.getW(), mapArea.getH(), mapArea.getTilemap(), rooms);
	}

	@Override
	public Color getRoomColor() {
		return Tavern.color;
	}

	@Override
	public String getRoomName() {
		return StringManager.getString("Tavern");
	}
	
	public int getIconId() {
		return 56;
	}
	
	/**
	 * Table libre
	 */
	public boolean hasAvailableTable() {
		return true;
	}
	
	
	/**
	 * Un comptoir est il libre pour servir dans ce bar
	 */
	public boolean hasAvailableCounter(){
		
		if(!this.isOperationnal()) return false;
		
		for(GameObject object:this.objects){
			if(object instanceof Counter){
				if(!((Counter)(object)).getBarmanSlot().isOccupied()){
					return true;
				}
			}
		}
		
		return false;
	}
	

	@Override
	public boolean readyFor(Class<? extends MCharacter> characterType) {
		
		if(!(this.isOperationnal())) return false;
		
		if(Barman.class.isAssignableFrom(characterType)){
			return true;
			// TODO : retour si il y a un comptoir de libre
		}
		else if(Dwarf.class.isAssignableFrom(characterType)){
			return (this.hasAvailableTable());
		}
		
		return false;
	}



}
