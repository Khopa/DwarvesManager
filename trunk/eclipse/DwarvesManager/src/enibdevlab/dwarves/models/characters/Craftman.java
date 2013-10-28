package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.DwarfAction;
import enibdevlab.dwarves.controllers.actions.GoCrafting;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;
import enibdevlab.dwarves.views.lang.StringManager;

public class Craftman extends Dwarf {

	public Craftman(Vector2 position) {
		super(position);
	}

	@Override
	public BodyConfig getBodyConfig() {
		return Loader.craftmanConfig;
	}

	@Override
	public int getIconId() {
		return 26;
	}

	@Override
	public String getJobName() {
		return StringManager.getString("Bartender");
	}

	@Override
	public DwarfAction getWorkingAction() {
		return new GoCrafting(getGame(), this);
	}

	@Override
	public int getGoldenHello() {
		return 450;
	}

	@Override
	public int getSalary() {
		return 250;
	}

	@Override
	public int getGoldenParachute() {
		return 250;
	}
	
}
