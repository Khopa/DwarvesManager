package enibdevlab.dwarves.models.objects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.Craftman;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.lang.StringManager;


/**
 * 
 * Enclume où travaille l'artisan
 * 
 * @author Clément Perreau
 *
 */
public class Anvil extends GameObject {

	public Anvil(Vector2 position) {
		super(position);
	}

	@Override
	public int getTextureId() {
		return 16;
	}
	
	@Override
	public int getIconId() {
		return 9;
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
	public int getPrice() {
		return 500;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Anvil");
	}
	
	@Override
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition() {
		HashMap<Vector2, Class<? extends MCharacter>> slotsData = new HashMap<Vector2, Class<? extends MCharacter>>();
		slotsData.put(new Vector2(1,0), Craftman.class);
		return slotsData;
	}

}
