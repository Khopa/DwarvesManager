package enibdevlab.dwarves.views.scenes.game;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.listener.GameGuiListener;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Primitives;
import enibdevlab.dwarves.views.widgets.ObjectButton;


/**
 * 
 * Fenêtre principale de Gui dans le jeu
 * 
 * @author Clément Perreau
 *
 */
public class GameGui extends Table {

	
	/**
	 *  Activation des menus (utile pour activer les fonctionnalitées au fur et à mesure
	 *  dans le tuto
	 */
	private static boolean RECRUITMENT_ENABLED = false;
	private static boolean OBJECTS_ENABLED = false;
	private static boolean ROOM_ENABLED = false;
	
	protected static GameGui instance;
	
	public enum State{
		MAIN,
		OBJECT,
		ROOM,
		RECRUIT,
		MINE
	}
	
	/**
	 * Skin de la Gui
	 */
	protected Skin skin;
	
	protected State state;
	protected Game game;
	
	protected ImageButton dwarfButton;
	protected ImageButton roomButton;
	protected ObjectButton objectButton;
	protected ImageButton mineButton;
	protected ImageButton graphButton;
	
	protected ObjectButton backButton;
	protected ObjectButton eraseButton;
	
	protected HashMap<ObjectButton, Constructor<?>> objectsButton;
	protected HashMap<ObjectButton, Constructor<?>> roomsButton;
	protected HashMap<ObjectButton, Constructor<?>> recruitsButton;
	
	protected ScrollPane scroll;
	protected Table container;
	
	/**
	 * Crée la fenêtre de Gui principale 
	 * @param skin Skin de la Gui
	 */
	public GameGui(Skin skin, Game game) {
		
		super(skin);
		
		this.skin = skin;
		this.game = game;
		
		this.initWidget();
		this.setMainMode();
		
		this.addListener(new GameGuiListener(this));
		
		instance = this;
		
	}
	
	/**
	 * Initialise tous les widgets
	 */
	private void initWidget(){
		
		container = new Table();
		scroll = new ScrollPane(container, skin);
		scroll.setScrollingDisabled(true, false);
		scroll.setHeight(DwarvesManager.getHeight());
		
		dwarfButton   = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										 new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										 Loader.iconAtlas, 37);
		graphButton   = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										 new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										 Loader.iconAtlas, 39);
		roomButton    = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										 new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										 Loader.iconAtlas, 36);
		objectButton  = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										 new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										 Loader.iconAtlas, 32);
		mineButton    = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
									     new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
									     Loader.iconAtlas, 35);
		eraseButton  = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
					   					new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
					   					Loader.iconAtlas, 33);
		backButton   = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
					   					new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
					   					Loader.iconAtlas, 34);
		
		// Création des boutons pour chaque objets
		objectsButton = new HashMap<ObjectButton, Constructor<?>>();
		for(Constructor<?> c: Loader.objects){
			
			int i;
			int price;
			String name;
			try {
				GameObject instance = (GameObject) c.newInstance(new Vector2(0,0));
				i = instance.getIconId();
				name = instance.getName();
				price = instance.getPrice();
			} catch (InstantiationException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				continue;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
			
			objectsButton.put(new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
							  				   new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
							  				   Loader.iconAtlas, i, price, name),
							  c);
			System.out.println(c);
		}
		
		// Création des boutons pour chaque pièces
		roomsButton = new HashMap<ObjectButton, Constructor<?>>();
		for(Constructor<?> c: Loader.rooms){
			
			int i = getRoomTextureId(c);
			System.out.println(i);
			roomsButton.put(new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
							new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),Loader.iconAtlas, i,
							 0, getRoomName(c)),
							c);
		}
		
		// Création des boutons pour chaque nain
		recruitsButton = new HashMap<ObjectButton, Constructor<?>>();
		for(Constructor<?> c:Loader.characters){
			
			int i;
			int price;
			String name;
			try {
				MCharacter instance = (MCharacter) c.newInstance(new Vector2(0,0));
				i = instance.getIconId();
				name = instance.getJobName();
				price = instance.getGoldenHello();
			} catch (InstantiationException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				continue;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
			recruitsButton.put(new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
							new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),Loader.iconAtlas, i,
							price, name),
							c);
		}
		
	}

	private String getRoomName(Constructor<?> c) {
		Room room;
		try {
			room = (Room) c.newInstance(new MapArea(0,0,0,0,this.game.getLevel().getTilemap()), this.game.getRooms());
			return room.getRoomName();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Sale
	 * @return
	 */
	private int getRoomTextureId(Constructor<?> c){
		Room room;
		try {
			room = (Room) c.newInstance(new MapArea(0,0,0,0,this.game.getLevel().getTilemap()), this.game.getRooms());
			return room.getIconId();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Packe et met à la bonne position
	 */
	public void repack(){
		this.pack();
		this.setX(DwarvesManager.getWidth()-this.getWidth());
		this.setY(DwarvesManager.getHeight()-this.getHeight()-2);
	}
	
	/**
	 * Crée le rendu pour le menu principal
	 */
	public void setMainMode(){
		this.clear();
		container.clear();
		
		this.state = State.MAIN;
		
		container.defaults().padRight(25);
		container.add(graphButton);
		container.row();
		if(RECRUITMENT_ENABLED){
			container.add(dwarfButton);
			container.row();
		}
		if(ROOM_ENABLED){
			container.add(roomButton);
			container.row();
		}
		if(OBJECTS_ENABLED){
			container.add(objectButton);
			container.row();
		}
		container.add(mineButton);
		container.row();
		container.pack();
		
		this.defaults().space(10);
		this.add(scroll).height(DwarvesManager.getHeight());
		
		this.repack();
	}
	
	/**
	 * Crée le rendu pour le sous menu des objets
	 */
	public void setObjectMode(){
		this.clear();
		container.clear();
		
		this.state = State.OBJECT;

		container.defaults().padRight(25);
		container.add(backButton);
		container.row();
		container.add(eraseButton);
		container.row();
		
		for(ImageButton imgbtn:this.objectsButton.keySet()){
			container.add(imgbtn);
			container.row();
		}
		
		this.defaults().space(10);
		this.add(scroll).height(DwarvesManager.getHeight());
		
		this.repack();
	}
	
	/**
	 * Crée le menu pour le sous menu des pièces
	 */
	public void setRoomMode(){
		this.clear();
		container.clear();
		
		this.state = State.ROOM;
		
		container.defaults().padRight(25);
		container.add(backButton);
		container.row();
		container.add(eraseButton);
		container.row();
		
		for(ImageButton imgbtn:this.roomsButton.keySet()){
			container.add(imgbtn);
			container.row();
		}
		
		this.defaults().space(10);
		this.add(scroll).height(DwarvesManager.getHeight());
		
		this.repack();
	}
	
	public void setMineMode() {
		this.clear();
		container.clear();
		
		this.state = State.MINE;
		
		container.defaults().padRight(25);
		container.add(backButton);
		container.row();
		container.add(eraseButton);
		container.row();
		
		this.defaults().space(10);
		this.add(scroll).height(DwarvesManager.getHeight());
		
		this.repack();
	}
	
	/**
	 * Crée le rendu pour le sous menu de recrutement
	 */
	public void setRecruitMode() {
		this.clear();
		container.clear();
		
		this.state = State.RECRUIT;
		
		container.defaults().padRight(25);
		container.add(backButton);
		container.row();
		container.add(eraseButton);
		container.row();
		
		for(ImageButton imgbtn:this.recruitsButton.keySet()){
			container.add(imgbtn);
			container.row();
		}
		
		this.defaults().space(10);
		this.add(scroll).height(DwarvesManager.getHeight());
		
		this.repack();
	}

	public Skin getSkin() {
		return skin;
	}

	public ImageButton getGraphButton() {
		return graphButton;
	}

	public Button getDwarfButton() {
		return dwarfButton;
	}

	public Button getRoomButton() {
		return roomButton;
	}

	public Button getObjectButton() {
		return objectButton;
	}

	public Button getMineButton() {
		return mineButton;
	}


	public State getState() {
		return state;
	}

	public ImageButton getBackButton() {
		return backButton;
	}

	public ImageButton getEraseButton() {
		return eraseButton;
	}

	public HashMap<ObjectButton, Constructor<?>> getObjectsButton() {
		return objectsButton;
	}

	public HashMap<ObjectButton, Constructor<?>> getRoomsButton() {
		return roomsButton;
	}

	public HashMap<ObjectButton, Constructor<?>> getRecruitsButton() {
		return recruitsButton;
	}

	public ScrollPane getScroll() {
		return scroll;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		/**
		 * Dessin d'un fond
		 */
		Primitives.prepareRenderer(batch);
		Primitives.filledRect((int)this.getX(), (int)this.getY()-5,DwarvesManager.getWidth(), (int) this.getHeight()+7, Color.BLACK);
		Primitives.filledRect((int)this.getX(), (int)this.getY()-6,DwarvesManager.getWidth(), 2, Color.GRAY);
		
		super.draw(batch, parentAlpha);
		GameGui.drawDebug(getStage());
	}
	
	
	
	/**
	 * Active/Désactive le bouton de recrutement
	 */
	public static void setRecruitmentButton(boolean enabled){
		RECRUITMENT_ENABLED = enabled;
		instance.setMainMode();
	}
	
	/**
	 * Active/Désactive le bouton d'achat d'objet
	 */
	public static void setObjectButton(boolean enabled){
		OBJECTS_ENABLED = enabled;
		instance.setMainMode();
	}
	
	/**
	 * Active/Désactive le bouton des pièces
	 */
	public static void setRoomButton(boolean enabled){
		ROOM_ENABLED = enabled;
		instance.setMainMode();
	}
	


	

}
