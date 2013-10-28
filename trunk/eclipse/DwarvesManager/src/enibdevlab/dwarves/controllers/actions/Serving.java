package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.items.Beer;
import enibdevlab.dwarves.models.objects.Barrel;
import enibdevlab.dwarves.models.objects.Counter;
import enibdevlab.dwarves.models.rooms.Tavern;

/**
 * 
 * Action de servir une bière
 * 
 * @author Clément Perreau
 *
 */
public class Serving extends DwarfAction {

	/**
	 * Taverne
	 */
	protected Tavern tavern;
	
	/**
	 * Comptoir
	 */
	protected Counter counter;
	
	/**
	 * Fut de bière
	 */
	protected Barrel barrel;
	
	/**
	 * Sous action de mouvement
	 */
	protected Movement movement;
	
	/**
	 * Position gardée en mémoire pour connaitre les destinations précises
	 */
	protected Vector2 tile;
	
	/**
	 * Chemin gardé en mémoire (car il s'agit d'un aller retour)
	 */
	protected ArrayList<Node> path;
	
	protected static int GOINGTOBARREL = 0;
	protected static int GOINGTOCOUNTER = 1;
	
	/**
	 * Action de servir une bière
	 * @param game Référence vers le jeu
	 * @param dwarf Nain
	 * @param tavern Taverne
	 */
	public Serving(Game game, Dwarf dwarf, Tavern tavern, Counter counter) {
		super(game, dwarf);
		this.tavern = tavern;
		this.counter = counter;
	}

	@Override
	public void entry() {
		
		this.state.clear();
		
		// Trouver le barrel le plus proche
		this.barrel = (Barrel) PathUtils.getNearestUsableObjectInRoom(Barrel.class, null, tavern, dwarf.getPosition(), game);
		
		// Construction du chemin vers le barrel
		if(this.barrel != null){
			path = PathUtils.pathToAdjacentTile(game, dwarf, this.barrel.getPosition());
			
			if(path != null){
				this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
				this.movement = new Movement(game, dwarf, path);
				this.state.add(GOINGTOBARREL);
			}

		}
		
	}

	@Override
	public void doAction(float delta) {
			
		if(this.movement != null){
			this.movement.act(delta);
			if(this.movement.isFinished()) this.movement = null;
		}
		
		if(this.state.contains(GOINGTOBARREL)){
			if(dwarf.getPosition().equals(tile)){

				// Animation ou le barman pioche sa bière
				dwarf.setRightHandItem(new Beer(new Vector2()));
				dwarf.getView().addAction(Animation.replenishing(dwarf.getView()));
				this.doWaiting(1.1f);
				
				// Nouvel état et Retour
				this.state.clear();
				this.state.add(GOINGTOCOUNTER);
				
				Collections.reverse(path);
				
				this.movement = new Movement(game, dwarf, path);
			}
		}
		else if(this.state.contains(GOINGTOCOUNTER)){
			if(dwarf.getPosition().equals(this.counter.getBarmanSlot().getRealPosition())){
				dwarf.getView().addAction(Animation.replenishing(dwarf.getView()));
				this.doWaiting(1.1f);
				this.finished = true;
			}
		}
		
	}

	@Override
	public void finish() {
		dwarf.setRightHandItem(null);
		if(this.path != null) this.counter.addBeer(); // Si on avait trouvé un chemin alors c'est bon
	}

}
