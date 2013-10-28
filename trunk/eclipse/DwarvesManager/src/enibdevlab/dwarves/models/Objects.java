package enibdevlab.dwarves.models;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.views.actors.AGameObject;

/**
 * 
 * Liste intelligente des objets
 * 
 * @author Cl�ment Perreau
 * 
 */
public class Objects implements IPersistent {

	/**
	 * Lien vers le jeu
	 */
	protected Game game;
	
	/**
	 * Liste des objets
	 */
	protected ArrayList<GameObject> objects;
	
	/**
	 * Constructeur
	 */
	public Objects(Game game){
		this.game = game;
		this.objects = new ArrayList<GameObject>();
	}
	
	
	/**
	 * Ajoute un objet dans la partie
	 * @param object Objet � ajouter
	 */
	public void addObject(GameObject object){
		this.objects.add(object);
		
		try {
			AGameObject view = object.getViewType().getConstructor(GameObject.class).newInstance(object);
			this.game.getView().getGameplayLayer().getObjectLayer().addActor(view);
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
		
		// Quand on ajoute un objet, il faut v�rifier les pi�ces
		// si l'objet est plac� dans une pi�ce, l'objet doit �tre mis au courant, et la pi�ce aussi
		for(Room room:game.getRooms().getRooms()){
			if(room.objectIn(object)){
				room.addObject(object);
				break;
			}
		}
		
		
	}
	
	/**
	 * Supprime un objet de la partie 
	 * @param object Objet � enlever
	 */
	public void removeObject(GameObject object){
		this.objects.remove(object);
		this.game.getView().getGameplayLayer().getObjectLayer().removeActor(object.getView()); // Retrait de la vue correspondante
		// Si l'objet �tait dans une pi�ce, il faut que la pi�ce soit mise au courant
		if(object.getRoom()!=null){
			object.getRoom().removeObject(object);
			object.setRoom(null);
		}
	}

	

	public ArrayList<GameObject> getObjects() {
		return this.objects;
	}


	/**
	 * Enleve l'objet situ� au tile X,Y
	 * @param x Position X du tile
	 * @param y Position Y du tile
	 * @return l'objet supprim�
	 */
	public GameObject removeAtTile(int x, int y) {
		
		GameObject toRemove = null;
		
		for(GameObject obj: this.objects){
			for(Vector2 vec:obj.getTiles()){
				if((int)vec.x == x && (int)vec.y == y){
					toRemove=obj;
					break;
				}
			}
			if(toRemove!=null) break;
		}
		
		if(toRemove != null){
			this.removeObject(toRemove);
		}
		
		return toRemove;
	}


	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("Objects", null);
		for(GameObject obj:this.objects){
			output.addChild(obj.saveAsXmlElement());
		}
		return output;
	}


	@Override
	public void loadFromXmlElement(Element xmlElement) {
		
		// Instanciation de toutes les pi�ces
		for(int i = 0; i<xmlElement.getChildCount(); i++){
			GameObject obj = GameObject.fromXml(xmlElement.getChild(i));
			if(obj != null){
				this.addObject(obj);
			}
		}
		
	}
	
	
	
	
}
