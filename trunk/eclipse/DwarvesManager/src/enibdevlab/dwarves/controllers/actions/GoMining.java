package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.rooms.Workshop;

/**
 * 
 * Action de travail du mineur
 * 
 * @author Clément Perreau
 *
 */
public class GoMining extends DwarfAction {

	private static int WAITING = 0;          // Attente d'ordre de la part du task manager
	private static int GOINGTOTILE = 1;      // En chemin vers le tile à miner
	private static int MINING = 2;           // En train de miner le tile
	private static int SEARCHINGPICKAXE = 3; // Recherche d'une pioche
	
	/**
	 * Mouvement
	 */
	protected Movement movement;
	
	/**
	 * Minage
	 */
	protected Mining mining;
	
	/**
	 * Mineur
	 */
	protected Miner miner;
	
	/**
	 * Tile auquel le nain doit aller pour miner (ou position mémoire pour la recherche de pioche)
	 */
	protected Vector2 tile;
	
	/**
	 * Ratelier dans lequel on va chercher une pioche
	 */
	protected Rack rack;
	
	/**
	 * Atelier dans lequel on va chercher une pioche 
	 */
	protected Workshop workshop;
	
	/**
	 * 
	 * @param game
	 * @param dwarf
	 */
	public GoMining(Game game, Dwarf dwarf) {
		super(game, dwarf);
		this.miner = (Miner)dwarf;
	}

	@Override
	public void entry() {
		
		// Si on a pas de pioche on va aller en chercher une
		if(!dwarf.isCarryingPickaxe()){
			// Recherche d'un atelier avec un ratelier qui contient au moins une pioche en état
			
			miner.stopWorking(); // On signale au task manager qu'on ne mine plus (libère le tile à miner)
			ArrayList<Room> workshops = PathUtils.getSortedOperationnalRoom(Workshop.class, dwarf.getPosition(), game);
			
			if(workshops != null){
				for(Room room:workshops){
					ArrayList<GameObject> racks = PathUtils.getSortedUsableObjectInRoom(Rack.class,
												  null, room, dwarf.getPosition(), game);
					if(racks != null){
						for(GameObject obj:racks){
							Rack rck = (Rack)obj;
							if(rck.getNewPickaxe() != null){
								ArrayList<Node> path = PathUtils.pathToAdjacentTile(game, dwarf, rck.getPosition());
								this.movement = new Movement(game, dwarf, path);
								this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
								this.rack = rck;
								this.workshop = (Workshop)room;
								state.clear();
								state.add(SEARCHINGPICKAXE);
								return;
							}
						}
					}
					
				}
			}
			abort(Complaint.NO_PICKAXE);
			return;
		}
		
		// Si un tile est déjà assigné : pas de problème
		if(this.miner.getToMine()!=null){
			
			// Trouver un chemin vers un tile adjacent au tile à miner
			ArrayList<Node> path = PathUtils.pathToAdjacentTile(game, dwarf, miner.getToMine());
			
			// Crée le mouvement si le chemin existe
			if(path != null){
				movement = new Movement(game, dwarf, path);
				this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
				state.clear();
				state.add(GOINGTOTILE);
			}
			else{
				// Le mineur refuse de miner ce tile ce qui force le taskManager à lui en trouver un autre
				miner.deny();
				// Si il n'y a pas de chemin, demander un tile différent au TaskManager
				state.clear();
				state.add(WAITING);
			}
			
		}
		else if(!state.contains(WAITING)){
			// Sinon le nain se tourne les pouces et vagabonde et demande une tache au task manager
			game.getTaskManager().assignTask(miner);
			if(this.movement == null){
				this.movement = Movement.random(game, dwarf);
			}
			state.clear();
			state.add(WAITING);
		}
		
	}

	@Override
	public void doAction(float delta) {
		
		if(this.movement != null){
			this.movement.act(delta);
			if(this.movement.isFinished()){
				this.movement = null;
				if(state.contains(WAITING)){
					state.remove(WAITING);
				}
			}
		}
		
		if(state.contains(MINING)){
			
			if(!dwarf.isCarryingPickaxe()){
				state.clear();
				movement = null;
			}
			
			mining.act(delta);
			if(mining.isFinished()){
				state.clear();
			}
		}
		else if (state.contains(GOINGTOTILE)){
			if(this.dwarf.getPosition().equals(this.tile)){
				// Commencer à miner
				mining = new Mining(game, miner, tile);
				state.clear();
				state.add(MINING);
			}
		}
		else if(state.contains(SEARCHINGPICKAXE)){
			if(dwarf.getPosition().equals(tile)){
				takePickaxe();
			}
		}
		else {
			entry();
		}
		
	}

	/**
	 * Prendre une pioche dans le ratelier
	 */
	private void takePickaxe() {
		// On vérifie que le ratelier et l'atelier existe toujours
		if(rack == null || workshop == null){
			abort(Complaint.NO_PICKAXE);
			return;
		} // Le joueur a t'il supprimé ces éléments ?
		else if(!game.getObjects().getObjects().contains(rack) || !game.getRooms().getRooms().contains(workshop)){
			abort(Complaint.TROLL);
			return;
		}
		
		// On vérifie qu'il y a bien une pioche
		Pickaxe pkx = rack.getNewPickaxe();
		if(pkx != null){
			this.dwarf.setRightHandItem(pkx);
			this.state.clear();
			rack.getItems().remove(pkx);
			return;
		}
		else{ // sinon retour état de départ
			abort(Complaint.NO_PICKAXE);
			return;
		}
	
	}

	@Override
	public void finish() {
		miner.stopWorking();
	}
	
}
