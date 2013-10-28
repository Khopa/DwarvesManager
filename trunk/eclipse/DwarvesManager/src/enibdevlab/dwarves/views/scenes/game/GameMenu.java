package enibdevlab.dwarves.views.scenes.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.LoadGameMenu;
import enibdevlab.dwarves.views.scenes.MainMenu;
import enibdevlab.dwarves.views.scenes.OptionMenu;
import enibdevlab.dwarves.views.scenes.SaveGameMenu;


/**
 * 
 * Menu de pause du jeu
 * 
 * @author Clément Perreau
 *
 */
public class GameMenu extends Window {

	private Button resume;
	private Button save;
	private Button load;
	private Button restart;
	private Button options;
	private Button exit;
	
	protected Game game;
	
	public GameMenu(Game game) {
		super("Menu", DwarvesManager.getSkin());
		
		this.game = game;
		
		init();
		build();
		pack();
	}
	
	private void init() {
		Skin skin = DwarvesManager.getSkin();
		
		resume  = new TextButton(StringManager.getString("Resume"),  skin);
		save    = new TextButton(StringManager.getString("Save"),  skin);
		load    = new TextButton(StringManager.getString("LoadGame"),  skin);
		restart = new TextButton(StringManager.getString("Restart"),  skin);
		options = new TextButton(StringManager.getString("Options"),  skin);
		exit    = new TextButton(StringManager.getString("Quit"),  skin);
		
		resume.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				GameScene.PAUSED = false;
				event.getListenerActor().getParent().remove();
			}
		});
		
		save.addListener(new GameClickListener(game){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new SaveGameMenu(DwarvesManager.getInstance().getStage(), game));
			}
		});
		
		load.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new LoadGameMenu(DwarvesManager.getInstance().getStage()));
			}
		});
		
		restart.addListener(new GameClickListener(game){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().newGame(game.getLevel().getScriptFile());
			}
		});
		
		options.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new OptionMenu(DwarvesManager.getInstance().getStage()));
			}
		});
		
		exit.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new MainMenu(DwarvesManager.getInstance()));
			}
		});
	}

	private void build() {		
		this.defaults().space(10);
		this.pad(25);
		
		this.add(resume);
		this.row();
		this.add(save);
		this.row();
		this.add(load);
		this.row();
		this.add(restart);
		this.row();
		this.add(options);
		this.row();
		this.add(exit);
		this.row();
		this.pack();
		
		this.setPosition(DwarvesManager.getWidth()/2-this.getWidth()/2, DwarvesManager.getHeight()/2-this.getHeight()/2);
		
	}
	
}
