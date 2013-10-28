package enibdevlab.dwarves.models.rooms;

import com.badlogic.gdx.graphics.Color;

import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.Bed;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Dortoir
 * 
 * @author Clément Perreau
 *
 */
public class Dorm extends Room{
	
	protected static Color color = new Color(.2f,.1f,.9f,.2f);
	
	public Dorm(int i, int j, int w, int h, TileMap tilemap, Rooms rooms) {
		super(i, j, w, h, tilemap, rooms);
		
		// Définition des objets requis
		neededObjects.put(Bed.class, 1);
		
	}
	
	public Dorm(MapArea mapArea, Rooms rooms) {
		this(mapArea.getI(), mapArea.getJ(), mapArea.getW(), mapArea.getH(), mapArea.getTilemap(), rooms);
	}

	@Override
	public Color getRoomColor() {
		return Dorm.color;
	}

	@Override
	public String getRoomName() {
		return StringManager.getString("Dorm");
	}

	public Bed getAvailableBed() {
		for(GameObject object:this.objects){
			if(object instanceof Bed && object.slotAvailable()){
				return (Bed)object;
			}
		}
		return null;
	}

	public boolean haveAvailableBed() {
		for(GameObject object:this.objects){
			if(object instanceof Bed && object.slotAvailable()){
				return true;
			}
		}
		return false;
	}

	public int getIconId() {
		return 57;
	}

	@Override
	public boolean readyFor(Class<? extends MCharacter> characterType) {
		return Dwarf.class.isAssignableFrom(characterType) && this.haveAvailableBed();
	}



}
