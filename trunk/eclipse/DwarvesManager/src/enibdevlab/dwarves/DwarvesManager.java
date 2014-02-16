package enibdevlab.dwarves;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
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
	public static Timer timer = new Timer();
	
	protected static DwarvesManager instance;
	
	@Override
	public void create() {

		instance = this;
		
		renderer = new ShapeRenderer();
		font = new BitmapFont();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		System.out.println(width);
		System.out.println(height);
		
		new DwarvesConfigurationManager();
		MusicManager.init();
		StringManager.init();
		Loader.init();
		SoundManager.init();
		LevelFile.init();
		new DwarvesScoreManager();
		
		Gdx.input.setCatchBackKey(true);
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
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		elapsedTime+=(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize (int w, int h) {
		stage.setViewport(width, height, false);
		stage.getCamera().position.set(width/2, height/2, 0);
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
