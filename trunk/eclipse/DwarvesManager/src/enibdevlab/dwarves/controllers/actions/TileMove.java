package enibdevlab.dwarves.controllers.actions;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.controllers.actions.animations.HandsShake;
import enibdevlab.dwarves.controllers.actions.animations.HandsUp;
import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.actors.characters.ACharacter;


/**
 * 
 * Bouge le personnage d'un tile
 * 
 * @author Clément Perreau
 *
 */
public class TileMove extends CharacterAction {
	
	protected int CHECKING = 0;
	protected int MOVING = 1;
	
	/**
	 * Position Cible (En Px)
	 */
	protected Vector2 target;
	
	/**
	 * Start
	 */
	protected float start;
	
	/**
	 * Durée du mouvement pour l'interpolation
	 */
	protected float duration;
	
	/**
	 * Position de départ de l'acteur (en px)
	 */
	private float startX;
	
	/**
	 * Position de départ de l'acteur (en px)
	 */
	private float startY;
	
	/**
	 * Position finale de l'acteur (en px)
	 */
	private float endX;
	
	/**
	 * Position finale de l'acteur (en px)
	 */
	private float endY;
	
	/**
	 * Bloqué
	 */
	private boolean stuck = false;
	
	/**
	 * compteur de bloquage
	 */
	private int stuckCounter = 0;

	public TileMove(Game game, MCharacter character, Vector2 tile, float duration) {
		super(game, character);
		this.target = tile;
		this.duration = duration;
	}

	@Override
	public void entry() {
		startX = actor.getX();
		startY = actor.getY();
		endX = target.x;
		endY = target.y;
		this.state.add(CHECKING);
		
		// Met le personnage dans la bonne direction
		if(endY>startY) character.getView().setDirection(Direction.TOP);
		else if (endY<startY) character.getView().setDirection(Direction.BOTTOM);
		else if(endX>startX) character.getView().setDirection(Direction.RIGHT);
		else if (endX<startX) character.getView().setDirection(Direction.LEFT);
	}

	@Override
	public void doAction(float delta) {
		if(state.contains(CHECKING)){
			check();
		}
		else if(state.contains(MOVING)){
			float percent = start/duration; 
			if(percent >= 1.0) percent = 1.0f;
			// Déplace Effectivement le personnage
			actor.setPosition(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
			start += delta;
			if(percent >= 1.0){
				finished = true;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void onStuck(){
		this.doWaiting(.2f);
		stuckCounter ++;
		if(stuckCounter > 5) stuck = true;
		
		this.actor.addAction(Actions.parallel(
							 new HandsShake(character.getView(), 8f, 15f, .2f),
							 new HandsUp(character.getView(), 8f, 15f, .2f)));
	}
	
	public void check(){
		
		// Vérifier si un nain n'est pas déjà sur ce tile ou en train de se déplacer dessus
		
		for(MCharacter chara:this.game.getCharacters().getCharacters()){
			
			if(chara == this.character) continue;
			
			// NB : On ne gère plus les collisions entre personnages
			// (trop de problème, beaucoup de situations ou les nains se bloquaient dans les couloirs étroits)
			
			/*Vector2 nextTile = chara.getNextTile();
			if(nextTile != null){
				if(nextTile.x == target.x && nextTile.y == target.y){
					onStuck();
					return;
				}
			}
			
			if(chara.getX() != (int)(target.x) && chara.getY() != (int)(target.y))
				continue;
			else{
				onStuck();
				return;
			}*/
		}	
		
		if(!(actor==character.getView())){
			finished = true;
			stuck = true;
			return;
		}
		
		stuckCounter = 0;
		this.character.setNextTile(new Vector2(target.x, target.y));
		state.remove(CHECKING);
		state.add(MOVING);
		start = 0;
	}

	@Override
	public void finish() {
		// Mettre à jour la position du modèle en Tile
		ACharacter view = character.getView();
		this.character.setPosition(view.getX()/game.getLevel().getTileXSize(),
							       view.getY()/game.getLevel().getTileYSize());
	}

	public boolean isStuck() {
		return stuck;
	}

}
