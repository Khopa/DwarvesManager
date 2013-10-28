package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Craftman;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.objects.Anvil;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.rooms.Workshop;

/**
 * 
 * Action de l'artisan qui va réparer les pioches usagées ou en créer de nouvelles
 * 
 * @author Clément Perreau
 *
 */
public class GoCrafting extends DwarfAction {

	/**
	 * Atelier ou va travailler l'artisan
	 */
	protected Workshop workshop;
	
	/**
	 * Enclume que l'artisan va utiliser
	 */
	protected Anvil anvil;
	
	/**
	 * Slot de l'enclume utilisé
	 */
	protected Slot slot;
	
	/**
	 * Mouvement Sous action
	 */
	protected Movement movement;
	
	/**
	 * Sous action de travail
	 */
	protected Crafting crafting;
	
	private static int GOTOWORKSHOP = 0;
	private static int CRAFTING    = 1;
	
	
	/**
	 * Action de l'artisan qui va réparer les pioches usagées ou en créer de nouvelles
	 * @param game Référence vers le jeu
	 * @param dwarf Nain artisan
	 */
	public GoCrafting(Game game, Dwarf dwarf) {
		super(game, dwarf);
	}

	@Override
	public void entry() {
		
		state.clear();
		// Trouver un atelier qui a une enclume de libre
		ArrayList<Room> workshops = PathUtils.getSortedOperationnalRoom(Workshop.class, dwarf.getPosition(), game);
		ArrayList<GameObject> anvils = null;
		ArrayList<Node> path = null;
		for(Room room:workshops){
			anvils = room.getObjects(Anvil.class);
			for(GameObject obj:anvils){
				slot = obj.getAvailableSlot(Craftman.class);
				if(slot != null){
					path = PathUtils.pathToSlot(dwarf, slot, game);
					if(path != null){
						slot.setOccupant(dwarf);
						anvil = (Anvil)obj;
						workshop = (Workshop)(room);
						movement = new Movement(game, dwarf, path);
						this.state.add(GOTOWORKSHOP);
						return;
					}
				}
			}
		}
		
		abort(Complaint.NO_PICKAXE);
		return;
	}

	@Override
	public void doAction(float delta) {
		
		if(this.movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(GOTOWORKSHOP)){
			if(dwarf.getPosition().equals(slot.getRealPosition())){
				this.crafting = new Crafting(game, dwarf, workshop, anvil);
				this.state.clear();
				this.state.add(CRAFTING);
			}
			else if(movement == null){
				ArrayList<Node> path = PathUtils.pathToSlot(dwarf, slot, game);
				if(path != null){
					movement = new Movement(game, dwarf, path);
				}
				else{
					abort(Complaint.NO_PATH);
					slot.occupantLeave();
				}
			}
		}
		else if(state.contains(CRAFTING)){
			this.crafting.act(delta);
			if(this.crafting.isFinished()){
				this.crafting = null;
				this.state.add(GOTOWORKSHOP);
			}
		}
		else{
			entry();
		}

		
	}

	@Override
	public void finish() {
		if(slot != null) slot.occupantLeave();
	}

}
