package enibdevlab.dwarves.models.objects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Table de taverne
 * 
 * @author Clément Perreau
 *
 */
public class TableObject extends GameObject {
	
	public TableObject(Vector2 position) {
		super(position);
	}

	@Override
	public int getTextureId() {
		return 18;
	}

	@Override
	public int getNominalXSize() {
		return 4;
	}

	@Override
	public int getNominalYSize() {
		return 3;
	}
	
	@Override
	public int getPrice() {
		return 350;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Table");
	}
	
	
	@Override
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition(){
		HashMap<Vector2, Class<? extends MCharacter>> slotsData = new HashMap<Vector2, Class<? extends MCharacter>>();
		slotsData.put(new Vector2(0,0), MCharacter.class);
		slotsData.put(new Vector2(3,0), MCharacter.class);
		slotsData.put(new Vector2(0,1), MCharacter.class);
		slotsData.put(new Vector2(3,1), MCharacter.class);
		slotsData.put(new Vector2(0,2), MCharacter.class);
		slotsData.put(new Vector2(3,2), MCharacter.class);
		return slotsData;
	}

	@Override
	public int getIconId() {
		return 2;
	}


	

}
