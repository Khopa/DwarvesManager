package enibdevlab.dwarves.controllers.actions;

import java.util.HashSet;

import com.badlogic.gdx.scenes.scene2d.Action;
import enibdevlab.dwarves.models.Game;


/** 
 * 
 * Action avec référence sur le jeu
 * 
 * @author Clément
 *
 */
public abstract class GameAction extends Action implements IStm{
	
	/**
	 * Référence vers le jeu
	 */
	protected Game game;
	
	/**
	 * Temps ecoulé
	 */
	protected float elapsed = 0f;
	
	/**
	 * Machine à état
	 */
	protected HashSet<Integer> state;
	
	/**
	 * Interrupteur de fin
	 */
	protected boolean finished = false;
	
	/**
	 * Vrai fin (passé dans l'action finish)
	 */
	protected boolean finished2 = false;

	/**
	 * Temps d'attente
	 */
	private float wait;

	public GameAction(Game game){
		this.game = game;
		this.state= new HashSet<Integer>();
	}
	
	@Override
	public final boolean act(float delta){
		if(finished2) return true;
		if(finished) {
			finish();
			finished2 = true;
			return true;
		}
		
		if(this.elapsed==0f) this.entry();
		
		// Gestion de temps d'attente
		if(this.wait > 0){
			wait -= delta;
		}
		else{
			this.doAction(delta);
		}
		
		this.elapsed+=delta;
		return false;
	}

	public Game getGame() {
		return game;
	}
	
	/**
	 * Indique l'action comme terminée
	 */
	public void setFinished(){
		this.finished = true;
	}
	
	/**
	 * Met l'action en pause
	 * @param waitDuration Temps de pause
	 */
	public void doWaiting(float waitDuration){
		this.wait = waitDuration;
	}
	
	/**
	 * Force la fin de la pause
	 */
	public void resume(){
		this.wait = 0;
	}
	
	public boolean isFinished(){
		return finished2;
	}
	

}
