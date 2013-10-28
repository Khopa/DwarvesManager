package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.items.Beer;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.models.objects.TableObject;
import enibdevlab.dwarves.models.rooms.Tavern;


/**
 * Action d'aller boire à une table après avoir pris une bière
 * 
 * @author Clément Perreau
 *
 */
public class Drinking extends DwarfAction {

	/**
	 * Taverne où le nain est venu boire sa bière
	 */
	protected Tavern tavern;
	
	/**
	 * Table où le nain va aller boire
	 */
	protected TableObject tableObject;
	
	/**
	 * Slot de la table utilisé
	 */
	protected Slot slot;
	
	/**
	 * Sous action de mouvement
	 */
	protected Movement movement;
	
	
	private static int GOINGTOTABLE = 1;
	private static int DRINKINGBEER = 2;
	
	/**
	 * 
	 * Action d'aller boire à une table après avoir pris une bière
	 * 
	 * @param game Réference vers le jeu
	 * @param dwarf Nain buveur
	 * @param tavern Taverne
	 */
	public Drinking(Game game, Dwarf dwarf, Tavern tavern) {
		super(game, dwarf);
		this.tavern = tavern;
	}

	@Override
	public void entry() {
		
		if(elapsed==0) this.dwarf.setLeftHandItem(new Beer(new Vector2()));
		
		state.clear();
		
		// Recherche d'une table
		tableObject = (TableObject) PathUtils.getNearestUsableObjectInRoom(TableObject.class, null, tavern, dwarf.getPosition(), game);
		
		// Selection d'un slot et construction d'un chemin
		if(tableObject != null){
			slot = tableObject.getAvailableSlot(); // On récupère un slot libre de la table
			if(slot!=null){
				ArrayList<Node> path = PathUtils.pathToSlot(dwarf, slot, game);
				if(path != null){
					movement = new Movement(game, dwarf, path);
					this.state.add(GOINGTOTABLE);
					slot.setOccupant(dwarf);
					return;
				}
			}
		}
		
		abort(Complaint.NO_PATH);
		return;
		
	}

	@Override
	public void doAction(float delta) {
		
		if(this.movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(GOINGTOTABLE)){
			if(this.dwarf.getPosition().equals(slot.getRealPosition())){
				this.state.clear();
				this.state.add(DRINKINGBEER);
			}
		}
		else if(state.contains(DRINKINGBEER)){
			
			Beer beer = (Beer) dwarf.getLeftHandItem();
			
			if(!beer.isEmpty()){
				beer.onUse();
				dwarf.getNeeds().setThirst((int) (dwarf.getNeeds().getThirst() - dwarf.getNeeds().getMaxValue()*1f/7f));
				dwarf.getView().addAction(Animation.drinking(dwarf.getView()));
				this.doWaiting(1.5f);
			}
			else{
				slot.occupantLeave();
				finished = true;
			}
			
		}
		
		
		
	}

	@Override
	public void finish() {
		this.dwarf.setLeftHandItem(null);
	}

}
