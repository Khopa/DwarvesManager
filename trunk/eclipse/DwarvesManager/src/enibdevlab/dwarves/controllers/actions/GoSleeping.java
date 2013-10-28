package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.controllers.pathfinder.Pathfinder;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.objects.Bed;
import enibdevlab.dwarves.models.rooms.Dorm;
import enibdevlab.dwarves.models.rooms.Room;

public class GoSleeping extends DwarfAction {
	
	/**
	 * Dortoir cible
	 */
	protected Dorm dorm;
	
	/**
	 * Liste de tous les dortoirs utilisable
	 */
	protected ArrayList<Room> dorms;
	
	/**
	 * Lit Cible
	 */
	protected Bed bed;
	
	/**
	 * Sous Action de mouvement
	 */
	protected Movement movement;
	
	/**
	 * Sous Action de sommeil
	 */
	protected Sleeping sleeping;
	
	private static int GOINGTODORM = 1;
	private static int GOINGTOBED = 2;
	private static int SLEEPING = 3;
	
	public GoSleeping(Game game, Dwarf dwarf) {
		super(game, dwarf);
	}

	@Override
	public void entry() {
		
		ArrayList<Node> path = null;
		
		int x = (int) dwarf.getX();
		int y = (int) dwarf.getY();

		// 1) On recupere une liste de dortoirs operationnels triés par rapport à leur distance au personnage
		dorms = PathUtils.getSortedRoom(Dorm.class, new Vector2(x,y), game);
		ArrayList<Room> toRemove = new ArrayList<Room>();
	
		// 2) Exclusion des pièces dont aucun lit n'est libre
		for(Room r:dorms){
			if (!((Dorm)(r)).haveAvailableBed()){
				toRemove.add(r);
			}
		}
		for(Room r: toRemove){
			dorms.remove(r);
		}
		toRemove.clear();
		
		/* 
		 * 3) Pour tous les dortoirs, on cherche si il existe un chemin. Dès qu'un
		 *  chemin est trouvé, on le garde (comme les pièces sont triées par rapport
		 *  à leur distance Manhattan vis à vis du personnage, il y a de bonne chance
		 *  que ce soit la plus proche
		 *  On pourrait calculer sinon la longueur que renvoie le Pathfinder pour chaque pièce
		 *  Mais ce serait vraiment peu efficace en terme de temps de calcul 
		 *  (C'est déjà pas terrible vu que c'est du java :D)
		 */
		Pathfinder pathfinder;
		for(Room dorm:dorms){
			pathfinder = new Pathfinder(game.getLevel().getTilemap(),
										new Node(x,y),
										new Node(dorm.barycenter().x, dorm.barycenter().y));
			pathfinder.run();
			path = PathUtils.pathToRoom(dwarf, dorm, game);
			
			this.dorm = (Dorm) dorm;
			if(path!=null) break;			
		}
		
		if(path == null){
			abort(Complaint.NO_BED); // Aucun dortoir accesible
			return;
		}
		
		
		movement = new Movement(game, dwarf, path);
		
		state.add(GOINGTODORM); // Confirmation du nouvel état
	}

	@Override
	public void doAction(float delta) {
		
		if(movement != null){
			movement.act(delta);
			if(movement.finished) movement = null;
		}
		
		if(sleeping != null){
			sleeping.act(delta);
			if(sleeping.isFinished()){
				sleeping = null;
				this.finished = true;
				return;
			}
		}
		
		if(state.contains(GOINGTODORM)){ // Si le nain va vers le dortoir cible
			if(this.dorm.tileInside((int)dwarf.getX(), (int)dwarf.getY())){ // Si il est dans le dortoir cible
				goToBed();
			}
		}
		else if(state.contains(GOINGTOBED)){ // Si le nain va au lit
			if(bed == null || !game.getObjects().getObjects().contains(bed) || !bed.slotAvailable()) goToBed();
			else if(dwarf.getX() == bed.getX() && dwarf.getY() == bed.getY() && bed.slotAvailable()){ // Si le nain est sur le lit et que le lit cible est libre
				movement = null;
				sleeping = new Sleeping(game, dwarf, bed);
				this.state.clear();
				this.state.add(SLEEPING);
			}
			else if(!bed.slotAvailable() || movement == null){
				goToBed(); // Recherche d'un nouveau lit
			}
		}
		else if(state.isEmpty()){
			this.entry();
		}
	
	}

	@Override
	public void finish() {
		if(bed != null){
			this.dwarf.getView().addAction(new Sleeping(game, dwarf, bed));
		}
	}
	
	
	private void goToBed(){
		movement = null;
		
		// Existe il un lit non ocuupé ?
		if(!dorm.haveAvailableBed()) abort(Complaint.NO_BED); // sinon stop !
		
		// Récuperer le plus proche lit libre
		int x = (int) dwarf.getX();
		int y = (int) dwarf.getY();
		bed = (Bed) PathUtils.getNearestUsableObjectInRoom(Bed.class, dwarf.getClass(), dorm, new Vector2(x,y), game);
		
		if(bed==null){
			abort(Complaint.NO_BED);
			return;
		}

		// Créer une sous action pour aller vers le lit libre
		ArrayList<Node> path = PathUtils.pathToTile(dwarf, bed.getPosition(), game);
		
		movement = new Movement(game, dwarf, path);
		
		state.remove(GOINGTODORM);
		state.add(GOINGTOBED); // Confirmation du nouvel état
	}

}
