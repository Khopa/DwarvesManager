package enibdevlab.dwarves.controllers.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Classe qui permet à un personnage de réagir aux évenements
 * 
 * @author Clément Perreau
 *
 */
public class CharacterListener extends InputListener{

	/**
	 * Personnage à controller
	 */
	protected ACharacter character;
	
	public CharacterListener(ACharacter character){
		super();
		this.character = character;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		
		if(DwarvesManager.debug){
			// Clic sur un nain pour augmenter ses besoins (Clic gauche sommeil)
			// (autre clic -> soif)
			
			if(this.character.getModel() instanceof Dwarf){
				if(button == 0) ((Dwarf)(this.character.getModel())).getNeeds().addSleep(5000);
				else ((Dwarf)(this.character.getModel())).getNeeds().addThirst(5000);
			}
			
		}
		
		return false;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		super.touchUp(event, x, y, pointer, button);
	}

	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		super.touchDragged(event, x, y, pointer);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
		return super.mouseMoved(event, x, y);
	}

	@Override
	public boolean scrolled(InputEvent event, float x, float y, int amount) {
		return super.scrolled(event, x, y, amount);
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		return super.keyDown(event, keycode);
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		return super.keyUp(event, keycode);
	}

	@Override
	public boolean keyTyped(InputEvent event, char character) {
		return super.keyTyped(event, character);
	}
	

}
