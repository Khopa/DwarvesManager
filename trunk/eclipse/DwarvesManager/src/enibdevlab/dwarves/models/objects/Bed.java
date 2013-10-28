package enibdevlab.dwarves.models.objects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * 
 * Lit où dorment les nains
 * 
 * @author Clément Perreau
 *
 */
public class Bed extends GameObject {
	
	public Bed(Vector2 position) {
		super(position);
	}

	@Override
	public int getTextureId() {
		return 0;
	}

	@Override
	public int getNominalXSize() {
		return 2;
	}

	@Override
	public int getNominalYSize() {
		return 1;
	}

	@Override
	public int getIconId() {
		return 0;
	}
	
	@Override
	public int getPrice() {
		return 150;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Bed");
	}
	

	@Override
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition(){
		HashMap<Vector2, Class<? extends MCharacter>> slotsData = new HashMap<Vector2, Class<? extends MCharacter>>();
		slotsData.put(new Vector2(0,0), MCharacter.class);
		return slotsData;
	}


	
	
	
	

}
