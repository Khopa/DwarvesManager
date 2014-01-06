package enibdevlab.dwarves;

import com.badlogic.gdx.utils.Timer.Task;

import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.actors.APriceEffect;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.GameOver;
import enibdevlab.dwarves.views.scenes.Win;
import enibdevlab.dwarves.views.scenes.game.GameScene;

/**
 * 
 * Controlleur de la partie
 * 
 * @author Clément Perreau
 *
 */
public class GameThread extends Task {

	/**
	 * Durée en seconde d'une journée (pour versement salaire)
	 */
	private static final float dayDuration = 120;
	
	/**
	 * Partie à controler
	 */
	private Game game;
	
	/**
	 * Temps Ecoulé
	 */
	private float elapsedTime;
	
	/**
	 * Temps de la journée
	 */
	private float dayCounter;
	
	/**
	 * Subventions du royaume
	 */
	private int subventions;
	
	/**
	 * Instance
	 */
	private static GameThread instance;
	
	public GameThread(Game game){
		instance = this;
		subventions = 250;
		this.game = game;
	}
	

	@Override
	public void run() {
		
		if(GameScene.PAUSED) return;
		
		elapsedTime ++;
		dayCounter ++;
		
		testDefeatWin();
		
		if(dayCounter>=dayDuration){
			this.payday();
			dayCounter = 0;
		}
		
		// Lance le script Lua
		game.getLevel().getDwScript().run();
	}
	
	/**
	 * Test des conditions de defaites et de victoire
	 */
	private void testDefeatWin() {
		if(game.getBank().getMoney() < 0){
			this.cancel();
			this.end(false);
		}
		else if(game.getBank().getDiamonds() >= game.getLevel().getDiamondsObjective()){
			this.cancel();
			this.end(true);
		}
	}
	
	/**
	 * Fin de la patie
	 */
	public void end(boolean win) {
		if(win) DwarvesManager.getInstance().setStage(new Win(game));
		else DwarvesManager.getInstance().setStage(new GameOver(game));
	}

	/**
	 * Jour de paye, Versement des salaires
	 */
	private void payday() {
		int wages = game.getCharacters().getPayroll();
		
		SoundManager.play("cash");
		this.game.log("--------------------");
		this.game.log(StringManager.getString("DayEnd"));
		this.game.log(StringManager.getString("Wages") + Integer.toString(wages));
		this.game.log(StringManager.getString("Grant") + Integer.toString(subventions));
		this.game.log(StringManager.getString("Total") + Integer.toString(subventions-wages));
		
		this.game.getView().addActor(new APriceEffect(DwarvesManager.getWidth()/2, DwarvesManager.getHeight()/2, subventions-wages));
		this.game.getBank().addMoney(subventions - wages);
	}
	
	public float getElapsedTime() {
		return elapsedTime;
	}

	public float getTimeBeforeWages() {
		return dayDuration - dayCounter;
	}

	public int getSubventions() {
		return subventions;
	}
	
	public static GameThread getInstance() {
		return instance;
	}

	public void setElapsedTime(float elapsed) {
		this.elapsedTime = elapsed;
	}

	public void setTimeBeforeWages(float timeleft) {
		this.dayCounter = dayDuration - timeleft;
	}
	
}
