package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;

/**
 * 
 * Cette classe fait un peu comme la classe SequenceAction de libgdx,
 * Je voulais hériter de celle ci, car j'ai besoin lors de ma séquence
 * de vérifier si le personnage n'est pas bloqué. Or, c'était impossible
 * d'avoir accès à ces attributs là (car privés!) à partir de SequenceAction.
 * 
 * @author Clément Perreau
 *
 */
public class Movement extends CharacterAction {
	
	protected  ArrayList<Node> path;

	/**
	 * Liste des mouvements
	 */
	protected ArrayList<TileMove> moves;
	
	/**
	 * Progression
	 */
	protected int index = 0;
	
	/**
	 * Stuck
	 */
	protected boolean stuck = false;
	
	public Movement(Game game, MCharacter character, ArrayList<Node> path) {
		super(game, character);
		this.path = path;
		this.setActor(character.getView());
	}
	
	@Override
	public void entry() {
		
		// Construction du chemin
		this.moves = new ArrayList<TileMove>();
		
		if(path == null) return;
		
		for(TileMove move:PathUtils.buildPath(path, character, game)){
			this.addMove(move);
			move.setActor(this.actor);
		}
		
	}
	

	@Override
	public void doAction(float delta) {
		
		if (index >= moves.size()){
			finished = true;
			return;
		}
		
		if(moves.get(index).isStuck()){
			finished = true;
		}
		
		if (moves.get(index).act(delta)) {
			index++;
			if (index > moves.size()) finished = true;
		}
		
	}

	@Override
	public void finish() {
		this.moves.clear();
	}
	
	public void addMove(TileMove move){
		this.moves.add(move);
	}
	
	
	public static Movement random(Game game, MCharacter character){
		Vector2 destination = character.getPosition().cpy();
		int rand = DwarvesManager.random.nextInt(4);
		switch(rand){
		case 0:
			destination.add(1, 0);
			break;
		case 1:
			destination.add(-1, 0);
		case 2:
			destination.add(0, 1);
		case 3:
			destination.add(0, -1);
		}
		return new Movement(game, character, PathUtils.pathToTile(character, destination, game));
	}
	
	
	
	
	
	
}
