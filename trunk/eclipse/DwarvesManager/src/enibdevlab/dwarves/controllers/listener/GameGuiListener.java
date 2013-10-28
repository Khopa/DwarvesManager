package enibdevlab.dwarves.controllers.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.views.scenes.game.GameGui;
import enibdevlab.dwarves.views.scenes.game.GameMenu;
import enibdevlab.dwarves.views.scenes.game.GameScene;



/**
 * 
 * Listener pour la gui principale du jeu
 * 
 * @author Clément Perreau
 *
 */
public class GameGuiListener extends ChangeListener {

	/**
	 * Référence vers la vue
	 */
	protected GameGui gui;
	
	public GameGuiListener(GameGui gui){
		this.gui = gui;
	}
	
	@Override
	public void changed(ChangeEvent event, Actor actor) {
		
		GameScene scene   = ((GameScene)(event.getStage())); 
		
		if(gui.getState() == GameGui.State.MAIN){
			if(checkButton(actor, gui.getDwarfButton())){
				gui.setRecruitMode();
				scene.setPlacingDwarvesMode();
			}
			else if(checkButton(actor, gui.getRoomButton())){
				gui.setRoomMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getObjectButton())){
				gui.setObjectMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getMineButton())){
				gui.setMineMode();
				scene.setDefiningAreaToMineMode();
			}
			else if(checkButton(actor, gui.getGraphButton())){
				scene.spawnWindow(new GameMenu(scene.getGame()));
			}
		}
		else if(gui.getState() == GameGui.State.OBJECT){
			
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Passage en mode : effacement d'objet
				scene.setRemovingObjectMode();
			}
			else{
				// Alors c'est un bouton pour choisir un objet à placer
				// On passe le jeu en mode placement d'objet en instanciant l'objet associé au bouton
				Constructor<?> c = gui.getObjectsButton().get(actor);
				if(c == null) return;
				try {
					GameObject object = (GameObject) c.newInstance(new Vector2(0,0));
					scene.setPlacingObjectMode(object);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		}
		else if(gui.getState() == GameGui.State.ROOM){
			
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Passage en mode : effacement d'objet
				scene.setRemovingRoomMode();
			}
			else{
				// Alors c'est un bouton pour choisir un objet à placer
				// On passe le jeu en mode placement d'objet en instanciant l'objet associé au bouton
				Constructor<?> c = gui.getRoomsButton().get(actor);
				if(c == null) return;
				scene.getListener().setRoomType(c);
				scene.setPlacingRoomMode();
			}
			
		}
		else if(gui.getState() == GameGui.State.MINE){
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Togle le mode placement ou ajout de zone à miner
				scene.toggleMineMode();
			}
		}
		else if(gui.getState() == GameGui.State.RECRUIT){
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Togle le mode placement ou ajout de zone à miner
				scene.setRemovingDwarfMode();
			}
			else{
				// Alors c'est un bouton pour choisir un nain à placer
				// On passe le jeu en mode placement d'objet en instanciant l'objet associé au bouton
				Constructor<?> c = gui.getRecruitsButton().get(actor);
				if(c == null) return;
				scene.getListener().setDwarfConstructor(c);
				scene.setPlacingDwarvesMode();
			}
		}
		
		// Repack de la table vu que la taille des widgets peut avoir changé
		// gui.repack(); // en fait c'est moche
	
	}
	
	/**
	 * Verifie si l'acteur est le bouton et si le bouton est checké
	 */
	private static boolean checkButton(Actor actor, Button button){
		return (actor == button);
	}

}
