package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;


import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Barman;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.objects.Counter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.rooms.Tavern;

/**
 * 
 * Action de travail du barman
 * 
 * @author Clément Perreau
 *
 */
public class GoServing extends DwarfAction {

	/**
	 * Barman (Dwarf casté)
	 */
	protected Barman barman;
	
	/**
	 * Taverne où le barman va servir
	 */
	protected Tavern tavern;
	
	/**
	 * Comptoir où le barman va servir
	 */
	protected Counter counter;
	
	/**
	 * Sous action de déplacement
	 */
	protected Movement movement;
	
	/**
	 * Sous action de servir
	 */
	protected Serving serving;
	
	public static int SEARCHINGBAR = 0;    // Etat où le nain recherche une taverne où servir
	public static int GOINGTOBAR = 1;      // Etat où le nain rejoint sa taverne et son comptoir
	public static int SERVING = 2;         // Etat où le nain part servir (sous action serving)
	
	public GoServing(Game game, Dwarf dwarf) {
		super(game, dwarf);
		this.barman = (Barman)dwarf;
	}

	@Override
	public void entry() {
		this.state.clear();
		this.state.add(SEARCHINGBAR);
		
		// I) Listes des tavernes opérationnelles triees par rapport à leur distance par rapport au barman
		ArrayList<Room> taverns = PathUtils.getSortedOperationnalRoom(Tavern.class, dwarf.getPosition(), game);
		
		// II) Vérification diverses
		// a) Verifier que un comptoir est dispo, si c'est le cas on le réserve
		
		
		// On regarde pour toutes les pièces tant qu'on trouve pas
		boolean found = false;
		for(Room tavern:taverns){
			
			ArrayList<GameObject> objects = tavern.getObjects(Counter.class);
			// Pour tous les comptoirs de la taverne
			for(GameObject obj:objects){
				Counter cnt = ((Counter)obj);
				Slot barmanSlot = cnt.getBarmanSlot();
				
				// Si ce comptoir n'est pas occupé ou reservé
				if(!barmanSlot.isOccupied() && !cnt.isUsed()){
					cnt.reserve(this.barman); // On reserve le comptoir pour que les autres barman ne l'utilise pas
					this.counter = cnt;
					this.tavern = (Tavern) tavern;
					found = true; 
					break; // Et stop
				}
			}
			if(found) break;
		}
		
		if(!found){
			abort(Complaint.NO_BEER);
			return;
		}
		
		// III) Validation, création du mouvement vers le bar et le comptoir
		
		if(this.tavern != null && this.counter != null){
			ArrayList<Node> pathToCounter = PathUtils.pathToSlot(dwarf, counter.getBarmanSlot(), game);
			this.movement = new Movement(game, dwarf, pathToCounter);
			this.state.clear();
			this.state.add(GOINGTOBAR); // Màj de la stm
		}
		
		
		
	}

	@Override
	public void doAction(float delta) {
		
		if(movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(GOINGTOBAR)){
			// Si on est arrivé dans le bar à la position du comptoir
			if(this.dwarf.getPosition().equals(counter.getBarmanSlot().getRealPosition())){
				this.serve();
			}
		}
		else if(state.contains(SERVING)){
			if(serving != null){
				serving.act(delta);
				if(serving.isFinished()){
					serving = null;
					this.state.clear();
					this.state.add(GOINGTOBAR);
				}
			}
		}
		else{
			this.entry();
		}
		
	}

	/**
	 * Action qu'effectue le nain quand il est au bar
	 */
	private void serve() {
		
		// On vérifie que le joueur n'a pas supprimé la taverne ou le comptoir et que la taverne est toujours opérationnelle
		if(!game.getObjects().getObjects().contains(this.counter) ||
		   !game.getRooms().getRooms().contains(this.tavern) ||
		   !tavern.isOperationnal()){
			abort(Complaint.TROLL);
			this.state.clear();
			return;
		}
		
		// Il regarde si un nain réclame à boire au comptoir
		boolean dwarfWaitingforBeer = false;
		for(Slot slot:this.counter.getSlots()){
			if(slot!=counter.getBarmanSlot() && slot.isOccupied()){
				dwarfWaitingforBeer = true;
				break;
			}
		}
		
		if(dwarfWaitingforBeer){
			// Passage en mode serving
			this.serving = new Serving(game, barman, tavern, counter);
			this.state.clear();
			this.state.add(SERVING);
		}// Sinon petite animation et attente
		else{
			this.counter.resetBeer();
			this.dwarf.getView().addAction(Animation.replenishing(this.dwarf.getView()));
			this.doWaiting(3f);
		}
	}

	@Override
	public void finish() {
		// On libère le comptoir (il n'est plus considéré reservé
		if(this.counter != null){
			counter.liberate();
			counter.resetBeer(); // On ne sert plus !
		}
	}

}
