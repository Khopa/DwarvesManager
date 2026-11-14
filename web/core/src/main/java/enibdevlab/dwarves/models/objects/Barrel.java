package enibdevlab.dwarves.models.objects;



import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.lang.StringManager;

public class Barrel extends GameObject {

	public Barrel(Vector2 position) {
		super(position);
	}

	@Override
	public int getTextureId() {
		return 8;
	}

	@Override
	public int getIconId() {
		return 8;
	}
	
	@Override
	public int getNominalXSize() {
		return 1;
	}

	@Override
	public int getNominalYSize() {
		return 1;
	}

	@Override
	public int getPrice() {
		return 350;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Barrel");
	}
	

	

	
	

}
