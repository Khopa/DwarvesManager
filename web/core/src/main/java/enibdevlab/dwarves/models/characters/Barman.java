package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.DwarfAction;
import enibdevlab.dwarves.controllers.actions.GoServing;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;
import enibdevlab.dwarves.views.lang.StringManager;

public class Barman extends Dwarf{

	public Barman(Vector2 position) {
		super(position);
	}

	@Override
	public String getJobName() {
		return StringManager.getString("Bartender");
	}

	@Override
	public BodyConfig getBodyConfig() {
		return Loader.barmanConfig;
	}

	@Override
	public int getIconId() {
		return 24;
	}

	@Override
	public DwarfAction getWorkingAction() {
		return new GoServing(this.getGame(), this);
	}

	@Override
	public int getGoldenHello() {
		return 350;
	}

	@Override
	public int getSalary() {
		return 80;
	}

	@Override
	public int getGoldenParachute() {
		return 150;
	}


}
