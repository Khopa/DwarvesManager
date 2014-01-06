package enibdevlab.dwarves.controllers.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import enibdevlab.dwarves.models.Game;
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
				Game.getInstance().fireDwarfEvent("hitDwarfButton");
			}
			else if(checkButton(actor, gui.getRoomButton())){
				gui.setRoomMode();
				scene.setNormalMode();
				Game.getInstance().fireDwarfEvent("hitRoomButton");
			}
			else if(checkButton(actor, gui.getObjectButton())){
				gui.setObjectMode();
				scene.setNormalMode();
				Game.getInstance().fireDwarfEvent("hitObjectButton");
			}
			else if(checkButton(actor, gui.getMineButton())){
				gui.setMineMode();
				scene.setDefiningAreaToMineMode();
				Game.getInstance().fireDwarfEvent("hitMineButton");
			}
			else if(checkButton(actor, gui.getGraphButton())){
				scene.getGame().fireDwarfEvent("popupClosed");
				scene.spawnWindow(new GameMenu(scene.getGame()));
				Game.getInstance().fireDwarfEvent("hitMenuButton");
			}
		}
		else if(gui.getState() == GameGui.State.OBJECT){
			
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
				Game.getInstance().fireDwarfEvent("hitBackButton");
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Passage en mode : effacement d'objet
				scene.setRemovingObjectMode();
				Game.getInstance().fireDwarfEvent("hitEraseButton");
			}
			else{
				// Alors c'est un bouton pour choisir un objet à placer
				// On passe le jeu en mode placement d'objet en instanciant l'objet associé au bouton
				Constructor<?> c = gui.getObjectsButton().get(actor);
				if(c == null) return;
				try {
					GameObject object = (GameObject) c.newInstance(new Vector2(0,0));
					scene.setPlacingObjectMode(object);
					Game.getInstance().fireDwarfEvent("placingObject"+object.getClass().getSimpleName());
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
				Game.getInstance().fireDwarfEvent("hitBackButton");
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Passage en mode : effacement d'objet
				scene.setRemovingRoomMode();
				Game.getInstance().fireDwarfEvent("hitEraseButton");
			}
			else{
				// Alors c'est un bouton pour choisir un objet à placer
				// On passe le jeu en mode placement de pièce en donnant le type de pièce à placer
				Constructor<?> c = gui.getRoomsButton().get(actor);
				if(c == null) return;
				scene.getListener().setRoomType(c);
				scene.setPlacingRoomMode();
				Game.getInstance().fireDwarfEvent("placingRoom"+c.getDeclaringClass().getSimpleName());
			}
			
		}
		else if(gui.getState() == GameGui.State.MINE){
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
				Game.getInstance().fireDwarfEvent("hitBackButton");
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Togle le mode placement ou ajout de zone à miner
				scene.toggleMineMode();
				Game.getInstance().fireDwarfEvent("hitEraseButton");
			}
		}
		else if(gui.getState() == GameGui.State.RECRUIT){
			if(checkButton(actor, gui.getBackButton())){
				// Retour au menu d'avant
				gui.setMainMode();
				scene.setNormalMode();
				Game.getInstance().fireDwarfEvent("hitBackButton");
			}
			else if(checkButton(actor, gui.getEraseButton())){
				// Togle le mode placement ou ajout de zone à miner
				scene.setRemovingDwarfMode();
				Game.getInstance().fireDwarfEvent("hitEraseButton");
			}
			else{
				// Alors c'est un bouton pour choisir un nain à placer
				// On passe le jeu en mode placement d'objet en instanciant l'objet associé au bouton
				Constructor<?> c = gui.getRecruitsButton().get(actor);
				if(c == null) return;
				scene.getListener().setDwarfConstructor(c);
				scene.setPlacingDwarvesMode();
				Game.getInstance().fireDwarfEvent("placingDwarf");
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
