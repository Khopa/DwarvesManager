package enibdevlab.dwarves.views.scenes.levelChoice;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.listener.LevelButtonListener;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.lang.StringManager;

public class LevelChoice extends Stage {

	// GUI elements
	
	/**
	 * Liste des boutons de niveaux
	 */
	private List<LevelButton> levelsButtons;
	
	/**
	 * Bouton pour revenir en arriere
	 */
	private Button cancel;
	
	private Group buttonGroup;
	
	/**
	 * Conteneur
	 */
	
	private Stage previousStage;
	
	private Label chooseALevel;
	
	// Controller related
	private float dragX = -1;
	private float dragY = -1;
	private int maxPosition;
	private int minPosition;
	
	public LevelChoice(){
		this.previousStage = DwarvesManager.getInstance().getStage();
		initWidgets();
		build();
	}

	private void initWidgets() {
		
		// Init levels buttons
		levelsButtons = new ArrayList<LevelButton>();
		List<LevelFile> levels = LevelFile.getLevels();
		for(LevelFile level:levels){
			LevelButton btn = new LevelButton(Loader.levelsIcons.getTile(level.getIconID()), level);
			levelsButtons.add(btn);
			btn.addListener(new LevelButtonListener(level));
		}
		
		// Init others buttons
		cancel = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGui.getTile(12)),
                 new TextureRegionDrawable(Loader.mainMenuGui.getTile(14)));
		cancel.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(previousStage);
			}
		});
		
		buttonGroup = new Group();
		
		chooseALevel = new Label(StringManager.getString("Level"), DwarvesManager.getSkin());
	}
	
	private void build() {
		int ref = DwarvesManager.getWidth()>DwarvesManager.getHeight() ? DwarvesManager.getHeight():DwarvesManager.getWidth();
		float scaling = (float) ((0.5f*ref)/((float)(levelsButtons.get(0).getWidth()))); // Un bouton prend la moitie de l'ecran
		float size = levelsButtons.get(0).getWidth()*scaling;
		int i = 0;
		for(LevelButton btn:levelsButtons){
			btn.setScale(scaling);
			btn.setX(size*i);
			btn.setY((DwarvesManager.getHeight()/2)-((btn.getHeight()/2)*scaling));
			buttonGroup.addActor(btn);
			
			Label title = new Label(btn.getLevel().getTitle(), DwarvesManager.getSkin());
			title.setY(btn.getY()-title.getHeight());
			title.setX(size*i+((btn.getWidth()/2)*scaling)-title.getWidth()/2);
			buttonGroup.addActor(title);
			
			i++;
		}
		
		chooseALevel.setPosition(DwarvesManager.getWidth()/2-chooseALevel.getWidth()/2, DwarvesManager.getHeight()-size/3);
		maxPosition = 0;
		minPosition = (int) (-size*levelsButtons.size()+DwarvesManager.getWidth());
		cancel.setX(DwarvesManager.getWidth()/2-cancel.getWidth()/2);
		this.addActor(buttonGroup);
		this.addActor(cancel);
		this.addActor(chooseALevel);
	}
	
	/*@Override
	public void act() {
		super.act();
		for(Actor act:this.getActors()){
			act.remove();
		}
		build();
	}*/
	
	
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		do{
			if(dragX == -1 || dragY == -1) break;
			buttonGroup.addAction(Actions.moveBy(-1.25f*(dragX-screenX), 0));
		}while(false);
		
		if(buttonGroup.getX() > maxPosition) buttonGroup.setX(maxPosition);
		else if(buttonGroup.getX() < minPosition) buttonGroup.setX(minPosition);
		
		dragX = screenX;
		dragY = screenX;
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		dragX = -1;
		dragY = -1;
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK){ // Appuie du bouton retour Android => retour menu précédent
			DwarvesManager.getInstance().setStage(previousStage);
		}
		return super.keyDown(keyCode);
	}
	
	@Override
	public boolean scrolled(int amount) {
		buttonGroup.addAction(Actions.moveBy(amount*DwarvesManager.getWidth()/6f, 0));
		if(buttonGroup.getX() > maxPosition) buttonGroup.setX(maxPosition);
		else if(buttonGroup.getX() < minPosition) buttonGroup.setX(minPosition);
		return super.scrolled(amount);
	}
	

	

	
	
}
