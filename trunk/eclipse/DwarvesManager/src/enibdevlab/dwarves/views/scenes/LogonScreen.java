package enibdevlab.dwarves.views.scenes;

import java.io.IOException;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.cloud.ConnectionThread;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.WaitingEffect;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Ecran de connection au jeu
 * Compte et Mot de passe
 * 
 * @author Clément Perreau
 *
 */
public final class LogonScreen extends Stage{

	private Table table;
	
	private Button connect;
	private Button offline;
	private Button subscribe;
	private Button quit;
	private Label error;
	
	private CheckBox rememberMe;
	private WaitingEffect effect;
	
	private TextField account;
	private TextField password;
	
	private int state;
	private float timeout;
	
	private ConnectionThread connection;
	
	private static final int WAITINGSERVER = 0;
	private static final int NORMAL = 1;
		
	public LogonScreen(){
		super();
		state = NORMAL;
		init();
		build();
		this.addActor(this.table);
		remember();
	}

	/**
	 * Charge le nom d'utilisateur si enregistré
	 */
	private void remember() {
		XmlReader xml = new XmlReader();
		FileHandle file = Gdx.files.external("userConfig.xml");
		if(file.exists()){
			try {
				Element data = xml.parse(file);
				this.account.setText(data.getChildByName("user").getText());
				this.rememberMe.setChecked(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			
		}
	}
	
	/**
	 * Enregistre le nom d'utilisateur si se souvenir de moi est coché lors d'une connection
	 */
	private void rememberForNextTime(){
		FileHandle file = Gdx.files.external("userConfig.xml");
		Element xml = new Element("cfg", null);
		Element user = new Element("user", null);
		user.setText(account.getText());
		xml.addChild(user);
		file.writeString(xml.toString(), false);
	}

	private void init() {
		
		Skin skin = DwarvesManager.getSkin();
		
		this.table = new Table(skin);
		
		this.connect   = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(17)),
                		                 new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(19)));
		this.offline   = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(20)),
				   					     new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(22)));
		this.quit      = new ImageButton(new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(6)),
				   				         new TextureRegionDrawable(Loader.mainMenuGuiSmall.getTile(7)));
		this.subscribe = new TextButton(StringManager.getString("Subscribe"), skin);
		
		this.account = new TextField("", skin);
		
		this.password = new TextField("", skin);
		this.password.setPasswordMode(true);
		this.password.setPasswordCharacter('*');
		
		this.rememberMe = new CheckBox(StringManager.getString("RememberMe"), skin);
	
		this.error = new Label("", skin);
		
		this.offline.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new LogoScreen(DwarvesManager.getInstance()));
			}
		});
		
		this.connect.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				
				if(state!=WAITINGSERVER){
					timeout = 0;
					connection = new ConnectionThread(account.getText(), password.getText());
					connection.start();
					effect = new WaitingEffect(StringManager.getString("LoggingIn"));
					addActor(effect);
					state = WAITINGSERVER;
				}
			
			}
		});
		
		this.subscribe.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				DwarvesManager.getInstance().setStage(new SubscriptionScreen(account.getText()));
			}
		});
		
		this.quit.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				System.exit(0);
			}
		});
		
	}
	
	
	@Override
	public void act() {
		super.act();
		if(state == WAITINGSERVER){
			timeout += Gdx.graphics.getDeltaTime();
			if(connection != null && !connection.isAlive()){
				this.state = NORMAL;
				this.effect.remove();
				if(connection.hasSuceeded()){
					if(rememberMe.isChecked()) rememberForNextTime();
					DwarvesManager.getInstance().setStage(new LogoScreen(DwarvesManager.getInstance()));
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
	
	private void build() {
		
		Skin skin = DwarvesManager.getSkin();
		
		this.table.clear();
		this.table.defaults().space(10);
		
		Table textfieldTable = new Table(skin);
		
		textfieldTable.defaults().space(10);
		
		textfieldTable.add(StringManager.getString("UserAccount"));
		textfieldTable.add(account);
		textfieldTable.add(rememberMe);
		textfieldTable.row();
		textfieldTable.add(StringManager.getString("Password"));
		textfieldTable.add(password);
		textfieldTable.add(subscribe);
		
		Table buttonTable = new Table(skin);
		buttonTable.defaults().space(10);
		buttonTable.add(connect);
		buttonTable.add(offline);
		buttonTable.add(quit).fillX().center();
		
		table.add(textfieldTable).center().height(DwarvesManager.getHeight()/3);
		table.row();
		table.add(buttonTable).center();
		table.row();
		table.add(error).center();
		table.pack();
		
		this.table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-1f/3f*table.getHeight()/2);
		
	}

	
	
}
