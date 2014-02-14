package enibdevlab.dwarves.controllers.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Rotation;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.lang.StringManager;
import enibdevlab.dwarves.views.scenes.game.GameMenu;
import enibdevlab.dwarves.views.scenes.game.GameScene;
import enibdevlab.dwarves.views.scenes.game.GameState;
import enibdevlab.dwarves.views.scenes.game.GameplayLayer;

/**
 * 
 * Classe qui gère les events de la scene GameplayScreen
 * NB : Cette classe n'est pas vraiment un listener, mais elle me permet de
 * délocaliser à peu près proprement des controlleurs qui aurait été dans
 * les vues sinon
 * 
 * @author Clément Perreau
 *
 */
public class GameplayScreenListener implements InputProcessor {

	/**
	 * Position de la souris sur l'écran (pas pris en charge sur Android)
	 */
	@SuppressWarnings("unused")
	private float mouseX = 0;
	
	/**
	 * Position de la souris sur l'écran (pas pris en charge sur Android)
	 */
	@SuppressWarnings("unused")
	private float mouseY = 0;
	
	/**
	 * Permet de gérer l'événement drag du doigt ou de la souris
	 */
	private float dragX = 0;
	
	/**
	 * Permet de gérer l'événement drag du doigt ou de la souris
	 */
	private float dragY = 0;
	
	/**
	 * GameplayScreen associé
	 */
	private GameScene scene;
	
	/**
	 * Zone de definition
	 */
	private MapArea definitionArea;
	
	/**
	 * Point de depart de la zone de definition
	 */
	private Vector2 defineStartPoint;
	
	/**
	 * Clic Droit
	 */
	@SuppressWarnings("unused")
	private boolean rightClick; 
	
	/**
	 * Clic gauche
	 */
	@SuppressWarnings("unused")
	private boolean leftClick; 
	
	/**
	 * Clic central
	 */
	private boolean centralClick; 
	
	/**
	 * Type de pièce en train d'être placé
	 */
	protected Constructor<?> roomConstructor;
	
	/**
	 * Type de nains en train d'être placé
	 */
	protected Constructor<?> dwarfConstructor;
	
	/**
	 * Activé ou désactivé
	 */
	protected boolean enabled = true;
	
	/**
	 * Classe qui gère les events de la scene GameplayScreen
	 * @param scene GameplayScreen associé
	 */
	public GameplayScreenListener(GameScene scene){
		super();
		this.scene = scene;
	}
	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		if(!enabled) return false;
		
		dragX = 0;
		dragY = 0;
		
		Vector2 tile = this.scene.getGameplayLayer().getTilemap().getClickedTile(screenX, screenY);
		
		if(scene.getGameState() == GameState.NORMAL){
			
		}
		else if(scene.getGameState() == GameState.PLACING_ROOM){
			if(button == 0 && definitionArea != null) addRoom(); // Ajoute la zone de définition en tant que nouvelle pièce
			else if(button == 1) removeArea(); // Spécifique PC : clic droit = suppresion
		}
		else if(scene.getGameState() == GameState.REMOVING_ROOM){
			removeArea();
		}
		else if(scene.getGameState() == GameState.PLACING_OBJECT){
			
			if(button == 0) this.placeObject(tile.x, tile.y);
			else if(button == 1) this.removeObject((int)tile.x, (int)tile.y);
			// Possibilité de supprimer d'un simple clic droit sur PC 
		
		}
		else if(scene.getGameState() == GameState.REMOVING_OBJECT){
			
		}
		else if(scene.getGameState() == GameState.PLACING_DWARF){
			
		}
		else if(scene.getGameState() == GameState.REMOVING_DWARF){
			
		}
		else if(scene.getGameState() == GameState.DEFINING_AREA_TO_MINE){
			if(definitionArea != null){
				MapArea copy = new MapArea(definitionArea);
				if(button == 0) scene.getGame().getTaskManager().addArea(copy);
				if(button == 1) scene.getGame().getTaskManager().removeArea(copy);
				definitionArea.setW(0);
				definitionArea.setH(0);
				definitionArea = null;
			}
		}
		else if(scene.getGameState() == GameState.REMOVING_AREA_TO_MINE){
			if(definitionArea != null){
				MapArea copy = new MapArea(definitionArea);
				scene.getGame().getTaskManager().removeArea(copy);
				definitionArea.setW(0);
				definitionArea.setH(0);
				definitionArea = null;
			}
		}
		
		if(button==1) rightClick = false;
		else if(button==0) leftClick = false;
		else if(button==2) centralClick = false;
		
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		if(!enabled) return false;
		
		Vector2 tile = this.scene.getGameplayLayer().getTilemap().getClickedTile(screenX, screenY);
		if(scene.getGameState() == GameState.PLACING_ROOM
		   || scene.getGameState() == GameState.REMOVING_ROOM
		   || scene.getGameState() == GameState.DEFINING_AREA_TO_MINE
		   || scene.getGameState() == GameState.REMOVING_AREA_TO_MINE){
			
			if(!centralClick){
				defineArea((int)tile.x, (int)tile.y);
			}
		}
		
		/**
		 * Scroll Spécifique PC/Web
		 */
		if(centralClick){
			if (dragX != 0 && dragY != 0){
				this.scene.getGameplayLayer().addAction(Actions.moveBy(-(dragX-screenX), (dragY-screenY)));
			}
			dragX = screenX;
			dragY = screenY;
			this.scene.getGameplayLayer().clamp();
		}
		
		/*
		 * Scroll spécifique Android
		 */
		if(Gdx.app.getType()==ApplicationType.Android){
			if(scene.getGameState() == GameState.NORMAL){
				if (dragX != 0 && dragY != 0){
					this.scene.getGameplayLayer().addAction(Actions.moveBy(-(dragX-screenX), (dragY-screenY)));
				}
				dragX = screenX;
				dragY = screenY;
				this.scene.getGameplayLayer().clamp();
			}
			
			
			// Sur Android, on place les objets en faisant glisser le doigt sur l'écran
			if(scene.getGameState()==GameState.PLACING_OBJECT){
				// Mise à jour de la position de l'objet qu'on est en train de placer
				// On le colore aussi en rouge si il y a collision avec le decor ou un autre objet
				GameObject obj = this.scene.getGameplayLayer().getObjectLayer().getObjectToPlace().getModel();
				obj.setPosition(tile.x, tile.y);
				if(obj.collide(scene.getGameplayLayer().getTilemap(), scene.getGame().getObjects())){
					obj.getView().setColor(Color.RED);
				}
				else obj.getView().setColor(Color.WHITE);
			}
			
		}
		
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		
		if(!enabled) return false;
		
		mouseX = screenX;
		mouseY = screenY;
	
		
		if(scene.getGameState() == GameState.PLACING_OBJECT){
			Vector2 tile = this.scene.getGameplayLayer().getTilemap().getClickedTile(screenX, screenY);
			GameObject obj = this.scene.getGameplayLayer().getObjectLayer().getObjectToPlace().getModel();
			obj.setPosition(tile.x, tile.y);
			if(obj.collide(scene.getGameplayLayer().getTilemap(), scene.getGame().getObjects())){
				obj.getView().setColor(Color.RED);
			}
			else obj.getView().setColor(Color.WHITE);
		}
		
		return false;
	}
	
	public boolean scrolled(int amount) {
		
		if(!enabled) return false;
		
		GameplayLayer gameplay = this.scene.getGameplayLayer();
		float o_amount = gameplay.getScaleX();
		float r_amount = (float) (gameplay.getScaleX() - amount/10f);
		
		if (r_amount > 3f){
			r_amount = 3f;
		}
		else if (r_amount < 0.3f){
			r_amount = 0.3f;
		}
		else{
			
			// Système de zoom
			// TODO : à perfectionner
			double d_amount = (double)(o_amount-r_amount);
			
			// On recupère la case centrale de l'écran
			// Vector2 center  = gameplay.getTilemap().getCenterCoordinate();
			// On effectue le scaling
			gameplay.setScale(r_amount);
			// gameplay.clamp();
			// On recupère la nouvelle case centrale de l'écran
			// Vector2 center2  = gameplay.getTilemap().getCenterCoordinate();
			// On calcule la différence
			// Vector2 diff    = new Vector2(center.x-center2.x, center.y-center2.y);
			Vector2 realPos = new Vector2(gameplay.getX()/o_amount, gameplay.getY()/o_amount);
			Vector2 newPos = new Vector2((float)(realPos.x*r_amount), (float)(realPos.y*r_amount));
			Vector2 diff   = new Vector2((float)(d_amount*(DwarvesManager.getWidth())), (float)(d_amount*DwarvesManager.getHeight()));
			System.out.println(newPos);
			// TODO : Il manque sans doute une valeur de scaling sur le décalage, je verrai ça plus tard si j'ai le temps
			// décalage pour centrer le zoom
			gameplay.setPosition(newPos.x+diff.x, newPos.y+diff.y);
			//gameplay.setPosition(gameplay.getX()-diff.x, gameplay.getY()-diff.y);
			gameplay.clamp();
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(!enabled) return false;
		
		if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE || keycode == Input.Keys.BACK){
			scene.getGameGui().setMainMode();
			scene.setNormalMode();
		}
		
		if(scene.getGameState() == GameState.PLACING_OBJECT){
			if(keycode == Input.Keys.R){
				rotateObject();
			}
		}
		
		if(keycode == Input.Keys.S || keycode == Input.Keys.F5){   // SAUVEGARDE RAPIDE
			String tmp = scene.getGame().getFilename();
			scene.getGame().setFilename("fast");
			scene.getGame().log(StringManager.getString("FastSave"));
			scene.getGame().saveAsXmlElement();
			scene.getGame().setFilename(tmp);
		}
		else if(keycode == Input.Keys.F9){                        // CHARGEMENT RAPIDE
			for(String saves:Loader.getSavegames()){
				if(saves.equals("fast.xml")){
					DwarvesManager.getInstance().loadGame("fast.xml");
					return false;
				}
			}
		}
		else if(keycode == Input.Keys.D){
			DwarvesManager.toggleDebug();
		}
		else if(keycode == Input.Keys.F10){
			scene.spawnWindow(new GameMenu(scene.getGame()));
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(!enabled) return false;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(!enabled) return false;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		if(!enabled) return false;
		
		this.scene.unfocus(this.scene.getGameGui());
		
		Vector2 tile = this.scene.getGameplayLayer().getTilemap().getClickedTile(screenX, screenY);
		
		if(scene.getGameState() == GameState.NORMAL){
			
		}
		else if(scene.getGameState() == GameState.PLACING_ROOM || scene.getGameState() == GameState.REMOVING_ROOM){
			if(button != 2) this.beginDefinitionArea(tile.x,tile.y);
		}
		else if(scene.getGameState() == GameState.PLACING_OBJECT){
			
		}
		else if(scene.getGameState() == GameState.REMOVING_OBJECT){
			this.removeObject((int)tile.x, (int)tile.y);
		}
		else if(scene.getGameState() == GameState.PLACING_DWARF){
			if (button == 1) this.removeDwarf((int)tile.x, (int)tile.y);
			else if (button != 2) this.addDwarf((int)tile.x, (int)tile.y);
		}
		else if(scene.getGameState() == GameState.REMOVING_DWARF){
			if (button != 2) this.removeDwarf((int)tile.x, (int)tile.y);
		}
		else if(scene.getGameState() == GameState.DEFINING_AREA_TO_MINE){
			if(button != 2) this.beginDefinitionArea(tile.x,tile.y);
		}
		else if(scene.getGameState() == GameState.REMOVING_AREA_TO_MINE){
			if(button != 2) this.beginDefinitionArea(tile.x,tile.y);
		}
	
		if(button == 1){
			rightClick = true;
		}
		else if(button==0){
			leftClick = true;
		}
		else if(button==2) centralClick = true;

		return false;
	}

	public void switchMode() {
		
		GameState state = this.scene.getGameState();
		
		if(state == GameState.NORMAL){
			this.definitionArea = null;
		}
		else if(state == GameState.PLACING_ROOM){
			
		}
		else if(state == GameState.REMOVING_ROOM){
			
		}
		else if(state == GameState.PLACING_OBJECT){
			
		}
		else if(state == GameState.REMOVING_OBJECT){
			
		}
		else if(state == GameState.PLACING_DWARF){
			
		}
		else if(state == GameState.REMOVING_DWARF){
			
		}
		
	}
	
	/**
	 * --------------------------------------------------
	 */
	
	/**
	 * Place l'objet à placer
	 */
	private void placeObject(float x, float y){	
		// Place l'objet à placer
		GameObject obj = this.scene.getGameplayLayer().getObjectLayer().getObjectToPlace().getModel();
		if(!(obj.collide(scene.getGameplayLayer().getTilemap(), scene.getGame().getObjects()))){
			
			// Recrée un objet du même type que le précédent
			// NB : c'est un peu sale, je sais
			// TODO : clean up
			GameObject newobj;
			try {
				
				newobj = obj.getClass().getConstructor(new Class<?>[] {Vector2.class}).newInstance(new Vector2(x, y));
				
				if(this.scene.getGame().getBank().removeMoney(newobj.getPrice())){
					this.scene.getGame().getObjects().addObject(obj);
					newobj.setRotation(obj.getRotation());
					this.scene.getGameplayLayer().getObjectLayer().setObjectToPlace
					(new AGameObject(newobj));
					SoundManager.play("cash");
					scene.getGameplayLayer().priceEffect((int)(x*scene.getGame().getLevel().getTileXSize()),
							 (int)(y*scene.getGame().getLevel().getTileYSize()),
							 -newobj.getPrice());
				}
				else SoundManager.play("Menu_cancel");
				
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Rotation de l'objet à placer
	 */
	protected void rotateObject(){
		GameObject obj = this.scene.getGameplayLayer().getObjectLayer().getObjectToPlace().getModel();
		obj.setRotation(Rotation.rotateClockwise(obj.getRotation()));
		if(obj.collide(scene.getGameplayLayer().getTilemap(), scene.getGame().getObjects())){
			obj.getView().setColor(Color.RED); // Si l'objet est en collision, il est affiché en rouge
		}
		else obj.getView().setColor(Color.WHITE);
	}
	
	/**
	 * Commence le dessin d'une zone de définition
	 */
	private void beginDefinitionArea(float x, float y){		
		defineStartPoint = new Vector2(x, y);
		this.definitionArea = this.scene.getGameplayLayer().getDefinitionArea().getModel();
		this.definitionArea.setI((int)x);
		this.definitionArea.setJ((int)y+1);
		this.scene.getGameplayLayer().getDefinitionArea().setModel(definitionArea);
		this.defineArea((int)x, (int)y);
	}
	
	/**
	 * Ajout de la zone de définition en tant que pièce
	 */
	private void addRoom(){
		if(definitionArea == null) return;
		
		MapArea copy = new MapArea(definitionArea);
		
		if(copy.getH() != 0 && copy.getW() != 0){
			Room room;
			try {
				room = (Room) roomConstructor.newInstance(copy, scene.getGame().getRooms());
				scene.getGame().getRooms().addRoom(room);
				Game.getInstance().fireDwarfEvent("roomPlaced", room.getTiles().size());
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
		}
		definitionArea.setW(0);
		definitionArea.setH(0);
		definitionArea = null;
		SoundManager.play("placeRoom");
	}
	
	
	/**
	 * Recrutement d'un nain
	 */
	private void addDwarf(int x, int y){
		
		if(scene.getGameplayLayer().getTilemap().isTileBlocking(x, y)) return;
		
		if(dwarfConstructor != null){
			MCharacter character;
			try {
				character = (MCharacter) dwarfConstructor.newInstance(new Vector2(x,y));
				
				Game.getInstance().fireDwarfEvent(character.getClass().getSimpleName()+"Recruited");
				
				if(this.scene.getGame().getBank().removeMoney(character.getGoldenHello())){
					scene.getGame().getCharacters().addCharacter(character);
					scene.getGameplayLayer().priceEffect((int)(x*scene.getGame().getLevel().getTileXSize()),
														 (int)(y*scene.getGame().getLevel().getTileYSize()),
														 -character.getGoldenHello());
					SoundManager.play("cash");
				}
				else{
					SoundManager.play("Menu_cancel");
				}
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Renvoye le nain situé à la position x,y
	 * @param x Position X
	 * @param y Position Y
	 */
	private void removeDwarf(int x, int y) {
		MCharacter toRemove = null;
		Vector2 pos = new Vector2(x,y);
		for(MCharacter character: this.scene.getGame().getCharacters().getCharacters()){
			if(character.getPosition().equals(pos)){
				this.scene.getGame().getBank().removeMoney(character.getGoldenParachute());
				SoundManager.play("cash");
				scene.getGameplayLayer().priceEffect((int)(x*scene.getGame().getLevel().getTileXSize()),
						 (int)(y*scene.getGame().getLevel().getTileYSize()),
						 -character.getGoldenParachute());
				toRemove = character;
				break;
			}
		}
		if(toRemove != null) this.scene.getGame().getCharacters().removeCharacter(toRemove);
	}
	
	/**
	 * Defini la zone de definition (s'assure aussi de la màj de la vue associée)
	 */
	private void defineArea(int x, int y){
		if(!(definitionArea != null)) return;
		
		
		int m_x = (int)defineStartPoint.x;
		if(x<(int)defineStartPoint.x) m_x = (int)defineStartPoint.x+1;
		
		int m_y = (int)defineStartPoint.y;
		if(y<(int)defineStartPoint.y) m_y = (int)defineStartPoint.y+1;
		
		int m_w = (int)x-(int)(defineStartPoint.x);
		int m_h = (int)y-(int)(defineStartPoint.y);
		
		if(m_w < 0) m_w--;
		else m_w++;
		
		if(m_h < 0) m_h--;
		else m_h++;
		
		this.definitionArea = new MapArea(m_x,
										  m_y,
										  m_w,
										  m_h,
										  definitionArea.getTilemap());
		
		//System.out.println(this.definitionArea);
		
		this.scene.getGameplayLayer().getDefinitionArea().setModel(definitionArea);
	}

	public Constructor<?> getRoomType() {
		return roomConstructor;
	}

	public void setRoomType(Constructor<?> c) {
		this.roomConstructor = c;
	}
	
	public Constructor<?> getDwarfConstructor() {
		return dwarfConstructor;
	}

	public void setDwarfConstructor(Constructor<?> dwarfConstructor) {
		this.dwarfConstructor = dwarfConstructor;
	}

	/**
	 * Enleve un morceau de piece
	 */
	public void removeArea(){
		if(definitionArea == null) return;
		MapArea copy = new MapArea(definitionArea);
		System.out.println("REMOVING :");
		this.scene.getGame().getRooms().removeArea(copy);
		definitionArea.setW(0);
		definitionArea.setH(0);
		definitionArea = null;
		SoundManager.play("removeRoom");
	}
	
	/**
	 * Enleve un objet
	 * @param y Tile X
	 * @param x Tile Y
	 */
	public void removeObject(int x, int y){
		GameObject removed = this.scene.getGame().getObjects().removeAtTile(x,y);
		if(removed != null){
			this.scene.getGame().getBank().addMoney(removed.getPrice());
			SoundManager.play("cash");
		}
		
	}
	
	/**
	 * Désactive le listener
	 */
	public void disable(){
		this.enabled = false;
	}
	
	/**
	 * Active
	 */
	public void enable(){
		this.enabled = true;
	}
	
	
	
	
	
	

}
