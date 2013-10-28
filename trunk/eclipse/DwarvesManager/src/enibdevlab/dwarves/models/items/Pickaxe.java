package enibdevlab.dwarves.models.items;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.lang.StringManager;

public class Pickaxe extends Tool {

	public Pickaxe(Vector2 pos) {
		super(pos, .2f);
	}

	@Override
	public String getName() {
		return StringManager.getString("Pickaxe");
	}

	@Override
	public int getTextureId() {
		return 0;
	}
	
}
