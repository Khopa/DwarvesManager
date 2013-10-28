package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.Counter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.rooms.Tavern;


/**
 * Action d'aller boire
 * 
 * @author Clément Perreau
 * 
 */
public class GoDrinking extends DwarfAction {

	/**
	 * Taverne où le nain va aller boire
	 */
	protected Tavern tavern;
	
	/**
	 * Comptoir ou le nain va aller boire
	 */
	protected Counter counter;
	
	/**
	 * Slot que l'on va occuper
	 */
	protected Slot slot;
	
	/**
	 * Sous action de mouvement
	 */
	protected Movement movement;
	
	/**
	 * Sous action d'aller boire sa bière à une table
	 */
	protected Drinking drinking;
	
	private static int GOINGTOTAVERN = 0;
	private static int GOINGTOCOUNTER = 1;
	private static int DRINKING = 2;
	
	public GoDrinking(Game game, Dwarf dwarf) {
		super(game, dwarf);
	}

	@Override
	public void entry() {
		
		state.clear();
		
		// Objectif ici trouver une taverne opérationnelle
		ArrayList<Room> taverns = PathUtils.getSortedOperationnalRoom(Tavern.class, dwarf.getPosition(), game);
		
		// On choisit le chemin vers la première pièce qu'on arrive à rejoindre
		ArrayList<Node> path = null;
		boolean ok = false;
		for(Room tavern:taverns){
			
			// On vérifie que la taverne a des slots libres sur au moins un de ses comptoirs
			// (en ne tenant compte que des comptoirs qui ont un serveur)
			ok = false;
			for(GameObject obj:tavern.getObjects(Counter.class)){
				Counter cnt = (Counter)(obj);
				if(!cnt.isUsed()) continue;
				if(!((cnt.getAvailableSlot(Dwarf.class)) == null)){
					ok = true;
					break;
				}
			}
			if(!ok) continue;
			
			path = PathUtils.pathToRoom(dwarf, tavern, game);
			this.tavern = (Tavern) tavern;
			if(path != null) break;
		}
		
		if(path == null){
			abort(Complaint.NO_BEER);
			return;
		}
		else{ // Construction du chemin vers la pièce
			this.movement = new Movement(game, dwarf, path);
			state.add(GOINGTOTAVERN);
		}
		
	}

	@Override
	public void doAction(float delta) {
		
		if(this.movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(GOINGTOTAVERN)){
			// Si on est arrivé dans la taverne
			if(this.tavern.tileInside((int)dwarf.getX(), (int)dwarf.getY())){
				goToCounter();
			}
		}
		else if(state.contains(GOINGTOCOUNTER)){
			if(this.dwarf.getPosition().equals(slot.getRealPosition())){
				if(this.counter.getBeer() > 0){
					counter.takeBeer();
					slot.occupantLeave();
					drinking = new Drinking(game, dwarf, tavern);
					this.state.clear();
					this.state.add(DRINKING);
				}
				else{
					this.dwarf.getView().addAction(Animation.requestBeer(this.dwarf.getView()));
					this.doWaiting(1.1f);
				}
			}
		}
		else if(state.contains(DRINKING)){
			if(drinking != null){
				drinking.act(delta);
				if(drinking.isFinished()) drinking = null;
			}
			else{
				if(dwarf.getNeeds().getThirst()<=1f/10f*dwarf.getNeeds().getMaxValue()){
					finished = true;
				}
				state.clear();
			}
		}
		else{
			entry();
		}
	}

	/**
	 * Méthode qui envoie le nain vers un comptoir une fois qu'il est arrivé dans la taverne
	 */
	private void goToCounter() {
		
		// On vérifie que la taverne est toujours prête à l'emploi
		if(tavern == null){
			abort(Complaint.NO_BEER);
			return;
		}
		else if(!game.getRooms().getRooms().contains(tavern) || !tavern.isOperationnal()){
			abort(Complaint.NO_BEER);
			return;
		}
		
		// Trouver le comptoir le plus proche qui a des slots libres pour aller boire
		ArrayList<GameObject> counters = PathUtils.getSortedUsableObjectInRoom(Counter.class, Dwarf.class, tavern, dwarf.getPosition(), game);
		
		// On prend le premier comptoir qui a des slots libres et un serveur 
		Counter cnt;
		
		if(counters == null){
			abort(Complaint.NO_BEER);
			return;
		}
		
		for(GameObject obj:counters){
			cnt = (Counter)obj;
			
			// Choisir un slot
			if(cnt.isUsed()){ // Si il y a un barman
				slot = cnt.getAvailableSlot(MCharacter.class);
				if(slot != null){ // Si il y a de la place
					ArrayList<Node> path = PathUtils.pathToSlot(dwarf, slot, game);
					if(path != null){ // Si on arrive à créer un chemin vers le slot
						counter = cnt;
						movement = new Movement(game, dwarf, path);
						slot.setOccupant(dwarf); // reservation du slot
						this.state.clear();
						this.state.add(GOINGTOCOUNTER);
						return;
					}
				}
			}
			
			
		}
		
		abort(Complaint.NO_BEER);
		state.clear();
		return;
		
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
