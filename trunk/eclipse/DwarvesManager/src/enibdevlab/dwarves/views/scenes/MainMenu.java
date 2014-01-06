package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Tileset;
import enibdevlab.dwarves.views.actors.AImage;
import enibdevlab.dwarves.views.actors.ButtonActor;
import enibdevlab.dwarves.views.audio.MusicManager;
import enibdevlab.dwarves.views.scenes.levelChoice.LevelChoice;

/**
 * 
 * Scene destinée à afficher le menu principal du jeu
 * 
 * @author Clément Perreau
 *
 */
public class MainMenu extends Stage {

	protected DwarvesManager game;
	protected static Tileset buttonsImg = Loader.mainMenuGui;
	
	/**
	 * Constructeur
	 * @param game   Reference vers le jeu
	 * @param width  Taille X
	 * @param height Taille Y
	 */
	public MainMenu(DwarvesManager game){
		super(DwarvesManager.getWidth(),DwarvesManager.getHeight(), false);
		this.game = game;
		init();
		
		// Transition
		this.addAction(Actions.color(new Color(0,0,0,0)));
		this.addAction(Actions.color(new Color(1,1,1,1), 1f));
		
		MusicManager.stop(); // stop musique
	}
	
	/**
	 * Fonction appelée à l'initialisation
	 * Dispose les widgets de manière à créer le menu
	 */
	private void init(){
		
		// Titre
		TextureRegion title = new TextureRegion(new Texture("data/sprites/title.png"));
		float sx = (0.7f*Gdx.graphics.getWidth())/title.getRegionWidth();
		float sy = (0.25f*Gdx.graphics.getHeight())/title.getRegionHeight();
		AImage titleText = new AImage(title, (int)(Gdx.graphics.getWidth()/2), (int) (.9f*Gdx.graphics.getHeight()));
		titleText.setScale(sx, sy);
		this.addActor(titleText);
		
		// Logo
		TextureRegion khopaGameText =  new TextureRegion(new Texture("data/sprites/logo.png"));
		sx = (0.2f*Gdx.graphics.getWidth())/khopaGameText.getRegionWidth();
		sy = (0.1f*Gdx.graphics.getHeight())/khopaGameText.getRegionHeight();
		AImage khopaGames = new AImage(khopaGameText, (int)(Gdx.graphics.getWidth()-sx*khopaGameText.getRegionWidth()/2),
				                              (int)(sy*khopaGameText.getRegionHeight()/2));
		khopaGames.setScale(sx,sy);
		this.addActor(khopaGames);
		
		// Creation du menu
		
		// Créations des boutons
		ButtonActor newGame      =      new ButtonActor(buttonsImg.getTile(0));
		ButtonActor continueGame =      new ButtonActor(buttonsImg.getTile(2));
		ButtonActor choiceLevel  =      new ButtonActor(buttonsImg.getTile(16));
		ButtonActor options      =      new ButtonActor(buttonsImg.getTile(4));
		ButtonActor quit         =      new ButtonActor(buttonsImg.getTile(6));
		
		sx =(0.35f*Gdx.graphics.getWidth())/buttonsImg.getTileWidth();
		sy =(0.15f*Gdx.graphics.getHeight())/buttonsImg.getTileHeight();
		
		this.put(newGame, sx, sy, 0.65f);
		this.put(continueGame, sx, sy, 0.50f);
		this.put(choiceLevel, sx, sy, 0.35f);
		this.put(options, sx, sy, 0.2f);
		this.put(quit, sx, sy, 0.05f);
		
		/* Ajout des actions des boutons */
		
		// Jouer
		newGame.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				this.dw.newGame(LevelFile.getLevel("tuto1.lua", true));
			}
		});
		
		// Continuer
		continueGame.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				this.dw.setStage(new LoadGameMenu(DwarvesManager.getInstance().getStage()));
			}
		});
		
		// Choix du niveau
		choiceLevel.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				this.dw.setStage(new LevelChoice());
				//this.dw.setStage(new LoadGameMenu(DwarvesManager.getInstance().getStage(), false));
			}
		});
		
		// Options
		options.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				this.dw.setStage(new OptionMenu(DwarvesManager.getInstance().getStage()));
			}
		});
		
		// Quitter
		quit.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				System.exit(0);
			}
		});
			
	}	
	
	@Override
	public void act() {
		super.act();
	}
	
	private void put(Actor actor, float sx, float sy, float yPercent){
		actor.setScale(sx, sy);
		actor.setPosition(Gdx.graphics.getWidth()/2-actor.getWidth()/2, Gdx.graphics.getHeight()*yPercent);
		this.addActor(actor);
	}
}

