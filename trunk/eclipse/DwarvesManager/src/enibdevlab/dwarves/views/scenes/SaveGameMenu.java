package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.models.Game;

public class SaveGameMenu extends Stage {

	private Button      save;
	private Button      cancel;
	private TextField   filename;
	private Table       table;
	private Label       error;
	
	
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
				
				DwarvesManager.getInstance().setStage(previousStage);
			}
		});
		
		
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
		
		
		this.table.add(buttonTable);
		this.table.row();
		this.table.add(error);
		this.table.row();
		
		this.table.pack();
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-table.getHeight()/2);
		
	}
	
	
}
