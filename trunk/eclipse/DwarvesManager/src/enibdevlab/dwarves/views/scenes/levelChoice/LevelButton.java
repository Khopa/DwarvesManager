package enibdevlab.dwarves.views.scenes.levelChoice;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.views.actors.ButtonActor;


/**
 * 
 * Bouton de selection de niveau, avec une image et un texte
 * 
 * @author Clément Perreau
 *
 */
public class LevelButton extends ButtonActor {

	private LevelFile level;
	
	public LevelButton(TextureRegion region, LevelFile level) {
		super(region);
		this.level = level;
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha); // dessin normal du bouton
	}


	public LevelFile getLevel() {
		return level;
	}


	public void setLevel(LevelFile level) {
		this.level = level;
	}

}
