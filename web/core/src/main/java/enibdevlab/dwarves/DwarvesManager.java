package enibdevlab.dwarves;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.khopa.skhopa.controllers.ConfigurationManager;

import enibdevlab.dwarves.controllers.manager.DwarvesConfigurationManager;
import enibdevlab.dwarves.controllers.manager.DwarvesScoreManager;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.audio.MusicManager;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.MainMenu;
import enibdevlab.dwarves.views.scenes.game.GameScene;

/**
 * 
 * Classe principale requise par libGdx
 * (a ici le rôle d'un directeur de scène, et contient aussi quelques objets et infos statiques utiles partout dans le jeu)
 * 
 * @author Clément Perreau
 *
 */
public class DwarvesManager extends Game {
	
	/**
	 * Objet qui permet le rendu des formes
	 */
	public static ShapeRenderer renderer;
	
	/**
	 * Objet qui permet le rendu de police
	 */
	public static BitmapFont font;
	
	/**
	 * Scene en cours
	 */
	protected Stage stage;
	
	/**
	 * Skin des GUI
	 */
	protected static Skin skin;
	
	/**
	 * Resolution
	 */
	protected static int width, height;
	
	/**
	 * Debug
	 */
	public static boolean debug = false;
	
	/**
	 * Random
	 */
	public static Random random = new Random();
	
	
	/**
	 * Timer
	 */
	public static float elapsedTime = 0f;
	public static Timer timer;
	
	protected static DwarvesManager instance;
	
	@Override
	public void create() {

		instance = this;
		timer = new Timer();
		
		renderer = new enibdevlab.dwarves.views.BatchAwareShapeRenderer();
		font = new BitmapFont();
		computeVirtualSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		System.out.println(width);
		System.out.println(height);
		
		new DwarvesConfigurationManager();
		MusicManager.init();
		StringManager.init();
		Loader.init();
		SoundManager.init();
		LevelFile.init();
		new DwarvesScoreManager();
		
		Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true);
		System.out.println(ConfigurationManager.getInstance());
		if(ConfigurationManager.getInstance().getBooleanValue("bigFont")){
			skin = new Skin(Gdx.files.internal("data/uiskinBig.json"));
		}
		else{
			skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		}
		
		this.setStage(new MainMenu(this));
		this.resize(width, height);
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.05f, .05f, .05f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getViewport().apply();
		stage.act();
		stage.draw();
		elapsedTime+=(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize (int w, int h) {
		if(w <= 0 || h <= 0) return;
		stage.getViewport().update(w, h, false);
		stage.getCamera().position.set(width/2, height/2, 0);
	}

	/**
	 * Virtual resolution: the game lays its UI out in pixels when a scene is built,
	 * so we fix the virtual height and derive the width from the window aspect ratio.
	 */
	public static void computeVirtualSize(int screenW, int screenH){
		if(screenW <= 0 || screenH <= 0){ screenW = 1280; screenH = 720; }
		float aspect = (float)screenW / (float)screenH;
		aspect = Math.max(1.0f, Math.min(aspect, 2.4f));
		height = VIRTUAL_HEIGHT;
		width = Math.round(VIRTUAL_HEIGHT * aspect);
	}

	public static final int VIRTUAL_HEIGHT = 720;

	/**
	 * Converts window (screen) coordinates to virtual stage coordinates (y up).
	 */
	public static com.badlogic.gdx.math.Vector2 screenToVirtual(float sx, float sy){
		com.badlogic.gdx.math.Vector2 v = new com.badlogic.gdx.math.Vector2(sx, sy);
		if(instance != null && instance.stage != null){
			com.badlogic.gdx.utils.viewport.Viewport vp = instance.stage.getViewport();
			// unproject with the camera centred on the virtual screen, independent of where
			// a scene may have moved its camera
			v.x = (sx - vp.getScreenX()) * width / (float) vp.getScreenWidth();
			v.y = height - (sy - (Gdx.graphics.getHeight() - vp.getScreenY() - vp.getScreenHeight())) * height / (float) vp.getScreenHeight();
		}
		else{
			v.y = height - sy;
		}
		return v;
	}

	public static com.badlogic.gdx.utils.viewport.Viewport newViewport(){
		com.badlogic.gdx.utils.viewport.FitViewport vp =
				new com.badlogic.gdx.utils.viewport.FitViewport(width, height);
		vp.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		return vp;
	}

	@Override
	public void dispose () {
		//Cloud.instance.close();
		stage.dispose();
		skin.dispose();
	}
	
	public static Skin getSkin(){
		return skin;
	}
	
	public static ShapeRenderer getRenderer() {
		return renderer;
	}

	public static void setRenderer(ShapeRenderer renderer) {
		DwarvesManager.renderer = renderer;
	}

	public static DwarvesManager getInstance() {
		return instance;
	}

	public void setStage(Stage stage){
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}
	
	public static void setSkin(Skin skin){
		DwarvesManager.skin = skin;
	}
	
	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}
	
	public static void toggleDebug(){
		debug = !debug;
	}
	
	public void newGame(LevelFile script){
		this.setStage(new GameScene(this, script));
		((GameScene)(this.stage)).connectInputProcessor();
	}
	
	public void loadGame(String path) {
		this.setStage(new GameScene(this, path));
		((GameScene)(this.stage)).connectInputProcessor();
	}

	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void pause() {
		super.pause();
		GameScene.PAUSED = true;
	}
	
	@Override
	public void resume() {
		super.resume();
		GameScene.PAUSED = false;
	}

}
