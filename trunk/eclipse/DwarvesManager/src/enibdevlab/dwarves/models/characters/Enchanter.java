package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.DwarfAction;
import enibdevlab.dwarves.controllers.actions.GoEnchanting;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;
import enibdevlab.dwarves.views.lang.StringManager;

public class Enchanter extends Dwarf {

	public Enchanter(Vector2 position) {
		super(position);
	}

	@Override
	public BodyConfig getBodyConfig() {
		return Loader.enchanterConfig;
	}

	@Override
	public int getGoldenHello() {
		return 2000;
	}

	@Override
	public int getSalary() {
		return 500;
	}

	@Override
	public int getGoldenParachute() {
		return 500;
	}

	@Override
	public int getIconId() {
		return 27;
	}

	@Override
	public String getJobName() {
		return StringManager.getString("Enchanter");
	}

	@Override
	public DwarfAction getWorkingAction() {
		return new GoEnchanting(game, this);
	}

}
