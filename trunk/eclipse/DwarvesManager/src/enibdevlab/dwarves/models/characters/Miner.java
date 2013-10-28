package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.DwarfAction;
import enibdevlab.dwarves.controllers.actions.GoMining;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;
import enibdevlab.dwarves.views.lang.StringManager;

public class Miner extends Dwarf {
	
	/**
	 * Tile assigné à miner
	 */
	protected Vector2 toMine;

	public Miner(Vector2 position) {
		super(position);
		this.setRightHandItem(new Pickaxe(position));
	}

	@Override
	public String getJobName() {
		return StringManager.getString("Miner");
	}

	@Override
	public int getIconId() {
		return 25;
	}

	@Override
	public DwarfAction getWorkingAction() {
		return new GoMining(game, this);
	}

	public Vector2 getToMine() {
		return toMine;
	}
	
	@Override
	public BodyConfig getBodyConfig() {
		return Loader.dwarf;
	}

	public void setToMine(Vector2 toMine) {
		this.toMine = toMine;
	}

	/**
	 * Le mineur indique qu'il ne peut miner un tile (sans doute pas de chemin)
	 */
	public void deny() {
		game.getTaskManager().tileRefused(this.toMine);
		game.getTaskManager().assignTask(this);
	}
	
	/**
	 * Le mineur indique qu'il a fini
	 */
	public void tileMined() {
		game.getLevel().getTilemap().getTile(0, (int)toMine.x, (int)toMine.y).setId(0);
		game.getTaskManager().tileMined(toMine);
		game.getTaskManager().assignTask(this);
	}

	/**
	 * Le nain arrête le travail pour aller se reposer
	 */
	public void stopWorking() {
		game.getTaskManager().tileLeft(toMine);
		this.toMine = null;
	}

	@Override
	public int getGoldenHello() {
		return 150;
	}

	@Override
	public int getSalary() {
		return 50;
	}

	@Override
	public int getGoldenParachute() {
		return 100;
	}


}
