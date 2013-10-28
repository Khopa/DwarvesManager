package enibdevlab.dwarves.views.scenes;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.cloud.Cloud;
import enibdevlab.dwarves.controllers.cloud.DownloadThread;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.WaitingEffect;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Menu de chargement d'une partie
 * 
 * @author Clément Perreau
 *
 */
public class LoadGameMenu extends Stage {

	/**
	 * Jeu
	 */
	protected DwarvesManager game;
	
	/**
	 * Widgets
	 */
	private Button ok;
	private Button cancel;
	protected Table table;
	protected Table scrolltable;
	protected ScrollPane scroll;
	protected List list;
	private Label error;
	
	private WaitingEffect effect;
	private DownloadThread thread;
	private String toLoad;
	
	private int state;
	
	private static final int WAITINGSERVER = 0;
	private static final int NORMAL = 1;
	
	// Charge on une partie
	protected boolean load = false;
	
	/**
	 * Scène précédente
	 */
	protected Stage previousStage;
	
	public LoadGameMenu(Stage previousStage, boolean load){
		super();
		this.load = load;
		this.previousStage = previousStage;
		this.game = DwarvesManager.getInstance();
		init();
		build();
		
		// Transition
		this.addAction(Actions.color(new Color(0,0,0,0)));
		this.addAction(Actions.color(new Color(1,1,1,1), 1f));
	}
	
	public LoadGameMenu(Stage previousStage){
		this(previousStage, true);
	}

	private void init(){
		Skin skin = DwarvesManager.getSkin();
		
		scrolltable = new Table(skin);
		scroll = new ScrollPane(scrolltable, skin);
		scroll.setScrollingDisabled(false, false);
		scroll.setHeight(DwarvesManager.getHeight()/2);
		scroll.setWidth(DwarvesManager.getWidth()*2f/3f);
		
		this.table = new Table(skin);
		
		if(load){
			
			if(Cloud.instance.getUserName()!=""){
				Vector<String> vect = Cloud.instance.getSaves(Cloud.instance.getUserName());
				String[] tmp = new String[vect.size()];
				int i = 0;
				for(String str:vect){
					tmp[i] = str;
					i++;
				}
				this.list = new List(tmp, skin);
			}
			else{
				this.list = new List(Loader.getSavegames(), skin);
			}
		}
		else{
			String[] str = new String[LevelFile.getLevels().size()];
			int i = 0;
			for(LevelFile level:LevelFile.getLevels()){
				str[i++] = level.getName();
			}
			this.list = new List(str, skin);
		}
		
		this.ok = new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(8)),
                  new TextureRegionDrawable(MainMenu.buttonsImg.getTile(10)));
		this.cancel = new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(12)),
                      new TextureRegionDrawable(MainMenu.buttonsImg.getTile(14)));
		
		this.error = new Label("", skin);
		this.addActor(this.table);
		
		
		this.cancel.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(previousStage);
			}
		});
		
		this.ok.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				
				if(Cloud.instance.getUserName()!="" && load){
					toLoad = list.getSelection();
					thread = new DownloadThread(toLoad,Cloud.instance.getUserName());
					thread.start();
					effect = new WaitingEffect("Telechargement de la partie sur serveur");
					addActor(effect);
					state = WAITINGSERVER;
				}
				else{
					if(list.getSelection()==null) return;
					if(load) DwarvesManager.getInstance().loadGame(list.getSelection());
					else{
						String selection = list.getSelection();
						for(LevelFile level:LevelFile.getLevels()){
							if(level.getName().equals(selection)){
								DwarvesManager.getInstance().newGame(level);
								return;
							}
						}
					}
				}
				
				
				
			}
		});
	}
	
	
	@Override
	public void act() {
		super.act();
		if(state == WAITINGSERVER){
			if(thread != null && !thread.isAlive()){
				this.state = NORMAL;
				this.effect.remove();
				if(thread.hasSuceeded()){
					DwarvesManager.getInstance().loadGame(toLoad);
				}
				else{
					error.setText("Impossible de se connecter au serveur");
					build();
				}
			}
		}
	}
	
	private void build() {
		
		this.scrolltable.clear();
		this.scrolltable.defaults().pad(25);
		this.scrolltable.add(this.list);
		this.scrolltable.pack();
		
		this.table.clear();
		this.table.defaults().space(10);
		
		if(load) this.table.add(StringManager.getString("LoadGame"));
		else  this.table.add(StringManager.getString("Level"));

		this.table.row();
		this.table.add(this.scroll).width(DwarvesManager.getWidth()/3).height(DwarvesManager.getHeight()/2);
		this.table.row();
		
		Table buttonTable = new Table(DwarvesManager.getSkin());
		buttonTable.defaults().space(25);
		buttonTable.add(ok);
		buttonTable.add(cancel);

		this.table.add(buttonTable);
		this.table.row();
		this.table.add(error);
		
		this.table.pack();
		
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-table.getHeight()/2);
	}
	
	
}
