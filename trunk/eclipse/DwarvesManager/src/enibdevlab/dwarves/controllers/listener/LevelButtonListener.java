package enibdevlab.dwarves.controllers.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.script.LevelFile;

/**
 * Listener for launch level buttons
 * @author Clément Perreau
 */
public class LevelButtonListener extends ClickListener {

	/**
	 * Level to launch
	 */
	private LevelFile level;
	
	public LevelButtonListener(LevelFile level) {
		super();
		this.level = level;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		DwarvesManager.getInstance().newGame(this.level);
	}

}
