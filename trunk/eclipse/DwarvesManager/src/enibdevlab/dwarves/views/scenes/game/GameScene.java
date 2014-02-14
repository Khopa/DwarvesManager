package enibdevlab.dwarves.views.scenes.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.listener.GameplayGestureListener;
import enibdevlab.dwarves.controllers.listener.GameplayScreenListener;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.audio.MusicManager;

/**
 * 
 * Scene créant l'affichage du jeu
 * 
 * @author Clément Perreau
 *
 */
public class GameScene extends Stage{

	
	/**
	 * Référence vers le modèle de la partie
	 */
	protected Game game;
	
	/**
	 * Référence vers le jeu
	 */
	protected DwarvesManager gdxGame;
	
	/**
	 * Couche de gameplay
	 */
	private GameplayLayer gameplayLayer;	
	
	/**
	 * Couche d'overlay scriptable
	 */
	private ScriptableOverlay scriptableOverlay;
	
	/**
	 * Gestionnaire d'events
	 */
	protected GameplayScreenListener listener;
	
	/**
	 * Gestionnaire d'event android
	 */
	private GameplayGestureListener gestureListener;
	
	/**
	 * Fenetre de Gui Principale
	 */
	protected GameGui gameGui;
	
	/**
	 * Infobar (argent)
	 */
	protected InfoBar infobar;
	
	/**
	 * Vue de Simulation
	 */
	protected Window window;
	
	/**
	 * Etat du jeu
	 */
	protected GameState gameState;
	
	/**
	 * Log
	 */
	protected GameLog gameLog;
	
	/**
	 * PAUSE
	 */
	public static boolean PAUSED = false;
	
	/**
	 * Mode cinématique
	 */
	@SuppressWarnings("unused")
	private boolean cinematicMode = false;
	// TODO
	
	/**
	 * 
	 * Création d'une nouvelle partie
	 * 
	 * @param gdxGame Référence vers le jeu
	 */
	public GameScene(DwarvesManager gdxGame, LevelFile script){
		PAUSED = false;
		this.gdxGame = gdxGame;
		this.setGame(new Game(this, script));
		this.listener = new GameplayScreenListener(this);
		this.setGameplayLayer(new GameplayLayer(this.getGame().getLevel()));
		this.addActor(this.getGameplayLayer());
		this.init();
		
		this.getGame().getLevel().init();
		this.getGame().getLevel().setup();
	}
	
	/**
	 * 
	 * Chargement d'une partie
	 * 
	 * @param gdxGame Référence vers le jeu
	 */
	public GameScene(DwarvesManager gdxGame, String path){
		PAUSED = false;
		this.gdxGame = gdxGame;
		this.setGame(new Game(this, path));
		this.listener = new GameplayScreenListener(this);
		this.setGameplayLayer(new GameplayLayer(this.game.getLevel(),this.game.getXml().getChildByName("Level").getChildByName("map")));
		this.addActor(this.getGameplayLayer());
		this.init();
		this.game.loadObjects();
		
		// Transition
		this.addAction(Actions.color(new Color(0,0,0,0)));
		this.addAction(Actions.color(new Color(1,1,1,1), 1f));
		
		this.getGame().getLevel().setup();
	}
	
	/**
	 * Création d'une nouvelle partie avec un niveau aléatoire
	 */
	public GameScene(DwarvesManager gdxGame){
		PAUSED = false;
		this.gdxGame = gdxGame;
		this.setGame(new Game(this));
		this.listener = new GameplayScreenListener(this);
		
		this.init();
		
		// 
	}
	
	
	/**
	 * 
	 * Initialise les différentes composantes du jeu
	 * 
	 */
	public void init(){
	

		// Fenetre de GUI
		this.gameGui = new GameGui(DwarvesManager.getSkin(), this.game);
		this.addActor(this.gameGui);
		// Infobar
		this.infobar = new InfoBar(this);
		this.addActor(infobar);
		// Overlayer
		this.scriptableOverlay = new ScriptableOverlay();
		this.addActor(this.scriptableOverlay);
		// Log
		this.gameLog = new GameLog();
		this.addActor(gameLog);
		// 
		
		this.gameState = GameState.NORMAL;
		MusicManager.playMusic(this.game.getLevel().getMusicFileName());
	}
	
	public GameState getGameState() {
		return gameState;
	}

	private void setGameState(GameState gameState) {
		this.gameState = gameState;
		this.getGameplayLayer().getObjectLayer().removeObjectToPlace();
		if(gameState != GameState.PLACING_OBJECT) this.getGameplayLayer().getObjectLayer().removeObjectToPlace();
		this.listener.switchMode();
	}
	
	public void setNormalMode(){
		this.setGameState(GameState.NORMAL);
	}
	
	public void setRemovingObjectMode(){
		this.setGameState(GameState.REMOVING_OBJECT);
	}
	
	public void setPlacingRoomMode(){
		this.setGameState(GameState.PLACING_ROOM);
	}
	
	public void setDefiningAreaToMineMode(){
		this.setGameState(GameState.DEFINING_AREA_TO_MINE);
	}
	
	public void setRemovingAreaToMineMode(){
		this.setGameState(GameState.REMOVING_AREA_TO_MINE);
	}
	
	public void setPlacingDwarvesMode(){
		this.setGameState(GameState.PLACING_DWARF);
	}
	
	public void setRemovingDwarfMode() {
		this.setGameState(GameState.REMOVING_DWARF);
	}
	
	public void toggleMineMode() {
		if(this.gameState == GameState.DEFINING_AREA_TO_MINE) this.setRemovingAreaToMineMode();
		else this.setDefiningAreaToMineMode();
	}
	
	public void setPlacingObjectMode(GameObject object){
		this.setGameState(GameState.PLACING_OBJECT);
		this.getGameplayLayer().getObjectLayer().setObjectToPlace(new AGameObject(object));
	}
	
	public void setRemovingRoomMode() {
		this.setGameState(GameState.REMOVING_ROOM);
	}
	

	public GameplayScreenListener getListener() {
		return listener;
	}

	public GameGui getGameGui() {
		return gameGui;
	}

	public GameLog getGameLog() {
		return gameLog;
	}

	public GameplayLayer getGameplayLayer() {
		return gameplayLayer;
	}

	public void setGameplayLayer(GameplayLayer gameplayLayer) {
		this.gameplayLayer = gameplayLayer;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public ScriptableOverlay getScriptableOverlay() {
		return scriptableOverlay;
	}

	public Window getVisualisation() {
		return window;
	}
	
	public void spawnWindow(Window window){
		if(this.window != null){
			this.window.remove();
		}
		this.window = window;
		this.addActor(window);
	}
	
	public void spawnTextMessageWindow(String title, String text){
		this.spawnWindow(new TextMessageWindow(title, text));
	}
	
	
	/**
	 * Active le mode cinématique (16:9) + désactive les GUI
	 */
	public void enableCinematicMode(){
		cinematicMode = true;
		this.gameGui.setVisible(false);
		this.infobar.setVisible(false);
		this.gameLog.setVisible(false);
		if(this.window != null) this.window.remove();
	}
	
	/**
	 * Désactive le mode cinématique
	 */
	public void disableCinematicMode(){
		cinematicMode = true;
		this.gameGui.setVisible(false);
		this.infobar.setVisible(false);
		this.gameLog.setVisible(false);
		if(this.window != null) this.window.remove();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean tmp = super.touchDown(screenX, screenY, pointer, button);
		if (!tmp) listener.touchDown(screenX, screenY, pointer, button);
		return tmp;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean tmp = super.touchDragged(screenX, screenY, pointer);
		if (!tmp) listener.touchDragged(screenX, screenY, pointer);
		return tmp;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean tmp = super.touchUp(screenX, screenY, pointer, button);
		if(!tmp) listener.touchUp(screenX, screenY, pointer, button);
		return tmp;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean tmp = super.mouseMoved(screenX, screenY);
		if(!tmp) listener.mouseMoved(screenX, screenY);
		return tmp;
	}

	@Override
	public boolean scrolled(int amount) {
		boolean tmp = super.scrolled(amount);
		if(!tmp) listener.scrolled(amount);
		return tmp;
	}

	@Override
	public boolean keyDown(int keyCode) {
		boolean tmp = super.keyDown(keyCode);
		if(!tmp) listener.keyDown(keyCode);
		return tmp;
	}

	@Override
	public boolean keyUp(int keyCode) {
		boolean tmp = super.keyUp(keyCode);
		if(!tmp) listener.keyUp(keyCode);
		return tmp;
	}

	@Override
	public boolean keyTyped(char character) {
		boolean tmp = super.keyTyped(character);
		if(!tmp) listener.keyTyped(character);
		return tmp;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void act() {
		if(!PAUSED){
			super.act();
		}
		
	}

	/**
	 * Connecte les bons gestionnaire d'event à libgdx
	 */
	public void connectInputProcessor() {
		this.listener = new GameplayScreenListener(this);
		this.gestureListener = new GameplayGestureListener(this);
		
		if(Gdx.app.getType() == ApplicationType.Android){
			// Si on est sous Android on ajoute le gestionnaire d'evenements tactiles
			InputMultiplexer multiplexer = new InputMultiplexer();
			multiplexer.addProcessor(new GestureDetector(gestureListener));
			multiplexer.addProcessor(this);
			Gdx.input.setInputProcessor(multiplexer);
			System.out.println("Gestionnaire d'event connecté");
		}
	}

	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.game.update();
	}



	


	
}
