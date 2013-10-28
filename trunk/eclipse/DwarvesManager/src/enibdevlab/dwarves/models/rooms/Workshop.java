package enibdevlab.dwarves.models.rooms;

import com.badlogic.gdx.graphics.Color;

import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.Anvil;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.world.TileMap;

public class Workshop extends Room {

	public static Color color = new Color(.1f,.9f,.1f,.2f); 
	
	public Workshop(int i, int j, int w, int h, TileMap tilemap, Rooms rooms) {
		super(i, j, w, h, tilemap, rooms);
		
		// Définition des objets requis
		neededObjects.put(Rack.class, 1);
		neededObjects.put(Anvil.class, 1);
	}
	
	public Workshop(MapArea mapArea, Rooms rooms) {
		this(mapArea.getI(), mapArea.getJ(), mapArea.getW(), mapArea.getH(), mapArea.getTilemap(), rooms);
	}

	@Override
	public Color getRoomColor() {
		return Workshop.color;
	}

	@Override
	public String getRoomName() {
		return StringManager.getString("Workshop");
	}

	@Override
	public boolean readyFor(Class<? extends MCharacter> characterType) {
		return true;
	}

	@Override
	public int getIconId() {
		return 58;
	}

}
