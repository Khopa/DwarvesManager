package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.cloud.Cloud;
import enibdevlab.dwarves.controllers.cloud.UploadThread;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.actors.WaitingEffect;

public class SaveGameMenu extends Stage {

	private Button      save;
	private Button      cancel;
	private TextField   filename;
	private Table       table;
	private CheckBox    saveOnCloud;
	private Label       error;
	
	private WaitingEffect effect;
	private UploadThread thread;
	
	private static final int WAITINGSERVER = 1;
	private static final int NORMAL = 0;
	private int state;
	
	private Stage previousStage;
	private Game game;
	
	public SaveGameMenu(Stage previousStage, Game game){
		super();
		state = NORMAL;
		this.previousStage = previousStage;
		this.game = game;
		init();
		build();
	}

	private void init() {
		Skin skin = DwarvesManager.getSkin();
		
		this.table = new Table(skin);
		this.filename = new TextField(game.getFilename(), skin);
		this.save = new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(8)),
                  new TextureRegionDrawable(MainMenu.buttonsImg.getTile(10)));
		this.cancel = new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(12)),
                      new TextureRegionDrawable(MainMenu.buttonsImg.getTile(14)));
		
		this.saveOnCloud = new CheckBox("Sauvegarde sur le Cloud", skin);
		this.error = new Label("", skin);
		
		this.addActor(this.table);
		
		this.cancel.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				if(state == NORMAL) DwarvesManager.getInstance().setStage(previousStage);
			}
		});
		
		this.save.addListener(new GameClickListener(game){
			public void clicked (InputEvent event, float x, float y) {
				
				if(state == WAITINGSERVER) return;
				
				game.setFilename(filename.getText());
				game.saveAsXmlElement();
				
				if(saveOnCloud.isChecked()){
					FileHandle file = Gdx.files.external("DwarvesManager/Saves/"+game.getFilename()+".xml");
					thread = new UploadThread(file,Cloud.instance.getUserName());
					thread.start();
					effect = new WaitingEffect("Sauvegarde de la partie sur serveur");
					addActor(effect);
					state = WAITINGSERVER;
				}
				else{
					DwarvesManager.getInstance().setStage(previousStage);
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
					this.build2();
				}
				else{
					error = new Label("Impossible de se connecter au serveur", DwarvesManager.getSkin());
					build();
				}
			}
		}
	}
	
	private void build2() {
		this.table.clear();
		this.table.defaults().space(10);
		this.table.add("Sauvegarde mise en ligne");
		this.table.row();
		this.table.add(cancel);
		this.table.pack();
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-table.getHeight()/2);
	}

	private void build() {
		
		this.table.clear();
		this.table.defaults().space(10);
		this.table.add("Sauvegarde de la partie");
		this.table.row();
		this.table.add(this.filename);
		this.table.row();
		
		Table buttonTable = new Table(DwarvesManager.getSkin());
		buttonTable.defaults().space(25);
		buttonTable.add(save);
		buttonTable.add(cancel);
		
		if(Cloud.instance.getUserName() != ""){
			this.table.add(saveOnCloud);
			saveOnCloud.setChecked(true);
			this.table.row();
		}
		game.log(Cloud.instance.getUserName());
		
		
		this.table.add(buttonTable);
		this.table.row();
		this.table.add(error);
		this.table.row();
		
		this.table.pack();
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-table.getHeight()/2);
		
	}
	
	
}
