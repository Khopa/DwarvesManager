package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.khopa.skhopa.controllers.ConfigurationManager;

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
	protected CheckBox bigFontCheckbox;
	protected CheckBox particleEffect;
	
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
		
		this.bigFontCheckbox = new CheckBox(" Enable Big Font", skin);
		this.bigFontCheckbox.setSize(DwarvesManager.getWidth()/7, DwarvesManager.getHeight()/7);
		if(ConfigurationManager.getInstance().getBooleanValue("bigFont")){
			this.bigFontCheckbox.setChecked(true);
		}
		
		this.particleEffect = new CheckBox(" Enable Particles", skin);
		this.particleEffect.setSize(DwarvesManager.getWidth()/7, DwarvesManager.getHeight()/7);
		if(ConfigurationManager.getInstance().getBooleanValue("particle")){
			this.particleEffect.setChecked(true);
		}
		
		this.addActor(this.table);
		
		this.ok.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(previousStage);
				ConfigurationManager.getInstance().save();
			}
		});
		
		this.musicSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MusicManager.setVolume(((Slider)(actor)).getValue());
				ConfigurationManager.getInstance().setValue("musicVolume", ((Slider)(actor)).getValue());
			}
		});
		
		this.audioSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundManager.setVolume(((Slider)(actor)).getValue());
				ConfigurationManager.getInstance().setValue("sfxVolume", ((Slider)(actor)).getValue());
			}
		});
		
		this.bigFontCheckbox.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox checkbox = (CheckBox) actor;
				if(checkbox.isChecked()){
					DwarvesManager.setSkin(new Skin(Gdx.files.internal("data/uiskinBig.json")));
					ConfigurationManager.getInstance().setValue("bigFont", true);
				}
				else{
					DwarvesManager.setSkin(new Skin(Gdx.files.internal("data/uiskin.json")));
					ConfigurationManager.getInstance().setValue("bigFont", false);
				}
			}
		});
		
		this.particleEffect.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox checkbox = (CheckBox) actor;
				if(checkbox.isChecked()){
					ConfigurationManager.getInstance().setValue("particle", true);
				}
				else{
					ConfigurationManager.getInstance().setValue("particle", false);
				}
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
		this.table.add(this.bigFontCheckbox);
		this.table.row();
		this.table.add(this.particleEffect);
		this.table.row();
		this.table.add(ok);
		this.table.pack();
		
		this.table.setPosition(DwarvesManager.getWidth()/2 - this.table.getWidth()/2,
							   DwarvesManager.getHeight()/2 - this.table.getHeight()/2);
		
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK){ // Appuie du bouton retour Android => retour menu précédent
			DwarvesManager.getInstance().setStage(previousStage);
		}
		return super.keyDown(keyCode);
	}
	
	
}
