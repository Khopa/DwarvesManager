package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.views.audio.MusicManager;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.game.GameScene;

/**
 * 
 * Menu des options
 * Réglages sonores uniquement pour l'instant
 * 
 * @author Clément Perreau
 *
 */
public class OptionMenu extends Stage {

	protected Table table;
	protected Button ok;
	protected Slider musicSlider;
	protected Slider audioSlider;
	
	protected Stage previousStage;
	
	public OptionMenu(Stage previous){
		super();
		GameScene.PAUSED = true;
		previousStage = previous;
		init();
		build();
	}

	private void init() {
		
		Skin skin = DwarvesManager.getSkin();
		
		this.table = new Table(skin);
		this.ok = new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(8)),
                  				  new TextureRegionDrawable(MainMenu.buttonsImg.getTile(10)));
		this.musicSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
		this.audioSlider = new Slider(0.0f, 1.0f, 0.01f, false, skin);
		
		this.addActor(this.table);
		
		this.ok.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(previousStage);
			}
		});
		
		this.musicSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MusicManager.setVolume(((Slider)(actor)).getValue());
			}
		});
		
		this.audioSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundManager.setVolume(((Slider)(actor)).getValue());
			}
		});
	}
	
	private void build() {
		
		Skin skin = DwarvesManager.getSkin();
		
		this.table.clear();
		this.table.defaults().space(10);
		
		Table leftPanel = new Table(skin);
		Table rightPanel = new Table(skin);
		Table horizontal = new Table(skin);
		
		leftPanel.defaults().space(10);
		rightPanel.defaults().space(10);
		horizontal.defaults().space(10);
		
		leftPanel.defaults().pad(10);
		rightPanel.defaults().pad(10);
		horizontal.defaults().pad(10);
		
		musicSlider.setValue(MusicManager.getVolume());
		audioSlider.setValue(SoundManager.getVolume());
		
		leftPanel.add(StringManager.getString("SoundEffects"));
		leftPanel.row();
		leftPanel.add(StringManager.getString("Music"));
		leftPanel.row();
		leftPanel.pack();
		
		rightPanel.add(this.audioSlider);
		rightPanel.row();
		rightPanel.add(this.musicSlider);
		rightPanel.row();
		rightPanel.pack();
		
		horizontal.add(leftPanel);
		horizontal.add(rightPanel);
		
		this.table.add("Options");
		this.table.row();
		this.table.add(horizontal);
		this.table.row();
		this.table.add(ok);
		this.table.pack();
		
		this.table.setPosition(DwarvesManager.getWidth()/2 - this.table.getWidth()/2,
							   DwarvesManager.getHeight()/2 - this.table.getHeight()/2);
		
	}
	
	
}
