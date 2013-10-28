package enibdevlab.dwarves.controllers.actions;

import java.util.HashSet;

import com.badlogic.gdx.scenes.scene2d.Action;
import enibdevlab.dwarves.models.Game;


/** 
 * 
 * Action avec r�f�rence sur le jeu
 * 
 * @author Cl�ment
 *
 */
public abstract class GameAction extends Action implements IStm{
	
	/**
	 * R�f�rence vers le jeu
	 */
	protected Game game;
	
	/**
	 * Temps ecoul�
	 */
	protected float elapsed = 0f;
	
	/**
	 * Machine � �tat
	 */
	protected HashSet<Integer> state;
	
	/**
	 * Interrupteur de fin
	 */
	protected boolean finished = false;
	
	/**
	 * Vrai fin (pass� dans l'action finish)
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
	 * Indique l'action comme termin�e
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
