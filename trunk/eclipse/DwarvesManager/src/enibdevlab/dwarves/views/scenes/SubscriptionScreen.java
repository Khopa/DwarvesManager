package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.Gdx;
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
import enibdevlab.dwarves.controllers.cloud.SubscriptionThread;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.WaitingEffect;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Ecran de création d'un compte utilisateur DwarvesManager
 * 
 * @author Clément Perreau
 *
 */
public final class SubscriptionScreen extends Stage {

	private Table table;
	
	private Button subscription;
	private Button cancel;
	
	private TextField username;
	private TextField password;
	private TextField passwordConfirm;
	
	private Label error;
	
	private SubscriptionThread subthread;
	private WaitingEffect effect;
	
	private float timeout = 0.0f;
	
	private int state;
	private static int WAITINGSERVER = 1;
	private static int NORMAL = 0;
	
	/**
	 * Crée le formulaire d'inscription 
	 */
	public SubscriptionScreen(String name){
		super();
		init();
		build();
		state = NORMAL;
		this.username.setText(name);
		this.addActor(this.table);
	}

	private void init() {
		
		Skin skin = DwarvesManager.getSkin();
		
		this.table = new Table(skin);
		
		this.subscription   = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(21)),
				     					      new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(23)));
		this.cancel         = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(12)),
					     				      new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(14)));
	
		this.username = new TextField("", skin);
		
		this.password = new TextField("", skin);
		this.password.setPasswordMode(true);
		this.password.setPasswordCharacter('*');
		
		this.passwordConfirm = new TextField("", skin);
		this.passwordConfirm.setPasswordMode(true);
		this.passwordConfirm.setPasswordCharacter('*');
		
		this.error = new Label("", skin);
		
		this.cancel.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				if(state == NORMAL) DwarvesManager.getInstance().setStage(new LogonScreen());
			}
		});
		
		this.subscription.addListener(new GameClickListener(null){

			public void clicked (InputEvent event, float x, float y) {
				
				if(!password.getText().equals(passwordConfirm.getText())){
					error = new Label(StringManager.getString("ErrorPasswordConfirm"), DwarvesManager.getSkin());
					build();
					return;
				}
				else if(password.getText().length() < 5){
					error = new Label(StringManager.getString("ErrorPasswordTooShort"), DwarvesManager.getSkin());
					build();
					return;
				}
				
				if(state != WAITINGSERVER){
					timeout = 0;
					subthread = new SubscriptionThread(username.getText(), password.getText());
					subthread.start();
					effect = new WaitingEffect(StringManager.getString("LoggingIn"));
					addActor(effect);
					state = WAITINGSERVER;
				}
			}
		});
		
	}
	
	@Override
	public void act() {
		super.act();
		if(state == WAITINGSERVER){
			if(state == WAITINGSERVER){
				timeout += Gdx.graphics.getDeltaTime();
				if(subthread != null && !subthread.isAlive()){
					this.state = NORMAL;
					this.effect.remove();
					if(subthread.hasSuceeded()){
						this.build2();
					}
					else{
						error = new Label(StringManager.getString("ErrorServer"), DwarvesManager.getSkin());
						build();
					}
				}
				else if(timeout > 10.0f){
					this.effect.remove();
					this.state = NORMAL;
					error = new Label(StringManager.getString("ErrorTimeOut"), DwarvesManager.getSkin());
					build();
				}
			}
		}
	}
	
	
	private void build2() {
		table.clear();
		table.defaults().space(10);
		
		table.add(StringManager.getString("SubscriptionThanks"));
		table.row();
		table.add(cancel);
		
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-1f/3f*table.getHeight()/2);
	}

	private void build() {
		
		table.clear();
		table.defaults().space(10);
		
		Skin skin = DwarvesManager.getSkin();
		
		Table form = new Table(skin);
		form.defaults().space(10);
		
		form.add(StringManager.getString("UserAccount"));
		form.add(username);
		form.row();
		form.add(StringManager.getString("Password"));
		form.add(password);
		form.row();
		form.add(StringManager.getString("PasswordConfirm"));
		form.add(passwordConfirm);
		form.row();
		
		Table buttons = new Table(skin);
		buttons.defaults().space(10);
		buttons.add(subscription);
		buttons.add(cancel);
		
		table.add(form);
		table.row();
		table.add(buttons);
		table.row();
		table.add(error).center();
		table.pack();
		
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-1f/3f*table.getHeight()/2);
	}
	
	
	
	
	
	
}
