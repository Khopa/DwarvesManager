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
 * @author Cl�ment Perreau
 *
 */
public class GoServing extends DwarfAction {

	/**
	 * Barman (Dwarf cast�)
	 */
	protected Barman barman;
	
	/**
	 * Taverne o� le barman va servir
	 */
	protected Tavern tavern;
	
	/**
	 * Comptoir o� le barman va servir
	 */
	protected Counter counter;
	
	/**
	 * Sous action de d�placement
	 */
	protected Movement movement;
	
	/**
	 * Sous action de servir
	 */
	protected Serving serving;
	
	public static int SEARCHINGBAR = 0;    // Etat o� le nain recherche une taverne o� servir
	public static int GOINGTOBAR = 1;      // Etat o� le nain rejoint sa taverne et son comptoir
	public static int SERVING = 2;         // Etat o� le nain part servir (sous action serving)
	
	public GoServing(Game game, Dwarf dwarf) {
		super(game, dwarf);
		this.barman = (Barman)dwarf;
	}

	@Override
	public void entry() {
		this.state.clear();
		this.state.add(SEARCHINGBAR);
		
		// I) Listes des tavernes op�rationnelles triees par rapport � leur distance par rapport au barman
		ArrayList<Room> taverns = PathUtils.getSortedOperationnalRoom(Tavern.class, dwarf.getPosition(), game);
		
		// II) V�rification diverses
		// a) Verifier que un comptoir est dispo, si c'est le cas on le r�serve
		
		
		// On regarde pour toutes les pi�ces tant qu'on trouve pas
		boolean found = false;
		for(Room tavern:taverns){
			
			ArrayList<GameObject> objects = tavern.getObjects(Counter.class);
			// Pour tous les comptoirs de la taverne
			for(GameObject obj:objects){
				Counter cnt = ((Counter)obj);
				Slot barmanSlot = cnt.getBarmanSlot();
				
				// Si ce comptoir n'est pas occup� ou reserv�
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
		
		// III) Validation, cr�ation du mouvement vers le bar et le comptoir
		
		if(this.tavern != null && this.counter != null){
			ArrayList<Node> pathToCounter = PathUtils.pathToSlot(dwarf, counter.getBarmanSlot(), game);
			this.movement = new Movement(game, dwarf, pathToCounter);
			this.state.clear();
			this.state.add(GOINGTOBAR); // M�j de la stm
		}
		
		
		
	}

	@Override
	public void doAction(float delta) {
		
		if(movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(GOINGTOBAR)){
			// Si on est arriv� dans le bar � la position du comptoir
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
		
		// On v�rifie que le joueur n'a pas supprim� la taverne ou le comptoir et que la taverne est toujours op�rationnelle
		if(!game.getObjects().getObjects().contains(this.counter) ||
		   !game.getRooms().getRooms().contains(this.tavern) ||
		   !tavern.isOperationnal()){
			abort(Complaint.TROLL);
			this.state.clear();
			return;
		}
		
		// Il regarde si un nain r�clame � boire au comptoir
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
		// On lib�re le comptoir (il n'est plus consid�r� reserv�
		if(this.counter != null){
			counter.liberate();
			counter.resetBeer(); // On ne sert plus !
		}
	}

}
