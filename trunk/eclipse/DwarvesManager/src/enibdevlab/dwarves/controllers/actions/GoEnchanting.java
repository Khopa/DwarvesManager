package enibdevlab.dwarves.controllers.actions;

import java.util.Collections;
import java.util.List;

import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.items.Item;
import enibdevlab.dwarves.models.items.Tool;
import enibdevlab.dwarves.models.misc.ObjectDistanceToPointComparator;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.rooms.Workshop;

/**
 * 
 * Action de l'enchanteur
 * 
 * Consiste à trouver un ratelier rempli (e.g non vide) d'outils
 * Il enchante ensuite les outils
 * 
 * @author Clément Perreau
 *
 */
public class GoEnchanting extends DwarfAction {

	/**
	 * Etats
	 */
	protected static int WAITING = 0;
	protected static int GOINGTORACK = 1;
	protected static int ENCHANTING = 2;
	
	/**
	 * Ratelier cible
	 */
	protected Rack currentTarget;
	
	/**
	 * Mouvement Sous action
	 */
	protected Movement movement;
	
	/**
	 * Sous action d'enchantement
	 */
	protected Enchanting enchanting = null;

	/**
	 * 
	 * @param game
	 * @param dwarf
	 */
	public GoEnchanting(Game game, Dwarf dwarf) {
		super(game, dwarf);
	}

	@Override
	public void entry() {
		
		// Recherche d'un ratelier cible
		if(currentTarget == null){
			this.state.clear();
			for(Room room:PathUtils.getSortedOperationnalRoom(Workshop.class, dwarf.getPosition(), game)){
				List<GameObject> racks = room.getObjects(Rack.class);
				if(!racks.isEmpty()){
					Collections.sort(racks, new ObjectDistanceToPointComparator((int)dwarf.getX(),(int)dwarf.getY()));
					
					for(GameObject rack:racks){
						if(isRackWorthy((Rack) rack)){
							currentTarget = (Rack) rack;
							break;
						}
					}
					
					if(currentTarget!=null){
						this.state.clear();
						this.state.add(GOINGTORACK); // ratelier trouve
						movement = new Movement(game, dwarf, PathUtils.pathToAdjacentTile(game, dwarf, currentTarget.getPosition()));
						return;
					}
				}
			}
			
			// Si ratelier non trouve
			abort(Complaint.NO_PICKAXE);
			this.state.clear();
			this.state.add(WAITING);
			
		}
		
	}

	@Override
	public void doAction(float delta) {
		if(state.contains(WAITING)){
			entry();
		}
		else if(state.contains(GOINGTORACK)){ 
			if(this.movement != null){
				movement.act(delta);
				if(movement.isFinished()){
					movement = null;
					this.state.clear();
					if(isRackWorthy(currentTarget)){ // Si les pioches ont ete prises entre temps
						entry();
					}
					enchanting = new Enchanting(game, dwarf, currentTarget);
					this.state.add(ENCHANTING);
				}
			}
		}
		else if(state.contains(ENCHANTING)){
			enchanting.act(delta);
			if(enchanting.isFinished()){
				state.clear();
				currentTarget = null;
				entry();
			}
		}
		
		
	}

	@Override
	public void finish() {
		
	}
	
	/**
	 * Verifier si le ratelier ciblé vaut le coup d'être enchanté (non vide, et pioche non enchantées)
	 */
	private boolean isRackWorthy(Rack rack){
		
		if(rack.getItems().size()<=0) return false; // Vide
		
		for(Item item:rack.getItems()){
			Tool tool = (Tool) item;
			if(!tool.isEnchanted()){ // l'outil est il non enchanté ?
				System.out.println("Non enchantée");
				return true; // Si oui, on peut y aller
			}
		}
		
		return false;
	}
	
	

}
