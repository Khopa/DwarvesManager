package enibdevlab.dwarves.models.objects;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.Entity;
import enibdevlab.dwarves.models.Objects;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.world.Tile;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Objet plaçable
 * 
 * @author Clément Perreau
 *
 */
public abstract class GameObject extends Entity implements IGameObject, IPersistent {

	/**
	 * Lien vers la vue
	 */
	protected AGameObject view;
	
	/**
	 * Rotation de l'objet
	 */
	protected Rotation rotation;
	
	/**
	 * Liste des coordonnées de tile par dessus lequels l'objet est.
	 */
	protected ArrayList<Vector2> tileList;
	
	/**
	 * Eventuelle pièce dans laquelle l'objet est placé
	 */
	protected Room room;
	
	/**
	 * Liste des slots de l'objet
	 */
	protected ArrayList<Slot> slots;
	
	/**
	 * Constructeur
	 */
	public GameObject(Vector2 position){
		super(position);
		this.setRotation(Rotation.ZERO); 
		this.initializeSlot();
	}

	
	/**
	 * Initialise les slots de l'objet s'il en a
	 */
	private void initializeSlot() {
		this.slots = new ArrayList<Slot>();
		HashMap<Vector2, Class<? extends MCharacter>> slotsData = this.getSlotsPosition();
		if (slotsData == null) return;
		for(Vector2 vector: slotsData.keySet()){
			this.slots.add(new Slot((int)vector.x, (int)vector.y, this, slotsData.get(vector)));
		}
	}
	
	/**
	 * Dit si un slot est dispo pour le type de personnage donné
	 */
	public boolean slotAvailable(Class<? extends MCharacter> characterType){
		for(Slot slot:this.slots){
			if(!(slot.isOccupied()) && slot.getCharacterType().isAssignableFrom(characterType)) return true;
		}
		return false;
	}
	
	public boolean slotAvailable(){
		return slotAvailable(MCharacter.class);
	}
	
	/**
	 * Retourne un slot disponible pour le type de personnage donné
	 */
	public Slot getAvailableSlot(Class<? extends MCharacter> characterType){
		for(Slot slot:this.slots){
			if(!(slot.isOccupied()) && slot.getCharacterType().isAssignableFrom(characterType)) return slot;
		}
		return null;
	}
	
	/**
	 * Retourne un slot disponible pour tout type de personnage
	 */
	public Slot getAvailableSlot(){
		return getAvailableSlot(MCharacter.class);
	}

	/**
	 * Dit si l'objet est en collision avec la map
	 */
	public boolean collide(TileMap tilemap){
		if(this.tileList==null) this.updatetileList();
		try{
			for(Vector2 tile:this.tileList){
				
				Tile tmp = tilemap.getTile(0, (int)tile.x, (int)tile.y);
				
				if(tmp == null) return true;
				
				if(tmp.isBlocking()){
					return true;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){ // Cas ou l'on sort de la map
			return true;
		}
		return false;
	}
	
	/**
	 * Collision avec un ensemble d'objets
	 * @param objects Conteneur d'objets
	 */
	public boolean collide(Objects objects){
		for(GameObject obj:objects.getObjects()){
			// TODO : optimiser avec un calcul de distance rapide ici
			if(this!=obj){
				for(Vector2 tile:this.tileList){
					for(Vector2 tile2:obj.getTiles()){
						if(tile.x==tile2.x&&tile.y==tile2.y) return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Collision avec map et objet
	 * @param tilemap Map (seul le calque 0 est testé)
	 * @param objects Conteneur d'objets
	 */
	public boolean collide(TileMap tilemap, Objects objects){
		return (this.collide(tilemap)||this.collide(objects));
	}
	
	public ArrayList<Vector2> getTiles() {
		return tileList;
	}

	/**
	 * Calcule et retourne la liste des coordonnées des tiles par dessus lequel l'objet est posé
	 */
	public ArrayList<Vector2> updatetileList(){
		this.tileList = new ArrayList<Vector2>();
		int xsize = this.getXSize();
		int ysize = this.getYSize();
		int x = (int) this.getX();
		int y = (int) this.getY();
		for(int i = 0; i < xsize; i++){
			for(int j = 0; j < ysize; j++){
				tileList.add(new Vector2(x+i,y+j));
			}
		}
		return tileList;
	}
	
	
	/**
	 * Retourne la taille X occupée par l'objet en tenant compte de la rotation
	 */
	public int getXSize(){
		if(rotation == Rotation.ZERO || rotation == Rotation.TWO_QUARTER){
			return this.getNominalXSize();
		}
		else{
			return this.getNominalYSize();
		}
	}
	
	/**
	 * Retourne la taille Y occupée par l'objet en tenant compte de la rotation
	 */
	public int getYSize(){
		if(rotation == Rotation.ZERO || rotation == Rotation.TWO_QUARTER){
			return this.getNominalYSize();
		}
		else{
			return this.getNominalXSize();
		}
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
		this.updatetileList();
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.updatetileList();
	}

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		this.updatetileList();
	}

	@Override
	public void setPosition(Vector2 position) {
		super.setPosition(position);
	}
	
	public AGameObject getView() {
		return view;
	}

	public void setView(AGameObject view) {
		this.view = view;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	@Override
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition(){
		return null; // De base, il n'y a pas de slots
	}


	public ArrayList<Slot> getSlots() {
		return slots;
	}
	
	@Override
	public Class<? extends AGameObject> getViewType() {
		return AGameObject.class;
	}
	
	@Override
	public Element saveAsXmlElement() {
		Element output = new Element(this.getClass().getName(), null);
		
		Element position = new Element("position", null);
		position.setAttribute("x", Float.toString(this.position.x));
		position.setAttribute("y", Float.toString(this.position.y));
		
		output.setAttribute("rotation", Integer.toString(Rotation.toInt(getRotation())));
		output.addChild(position);
		
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		// Inutilisable ici, car les données doivent servir à instancier un objet dont la
		// classe peut varier
	}
	
	
	public static GameObject fromXml(Element xmlElement){
		Element position = xmlElement.getChildByName("position");
		float x = Float.parseFloat(position.getAttribute("x"));
		float y = Float.parseFloat(position.getAttribute("y"));
		GameObject obj = Loader.instanciateObjectToPlace(xmlElement.getName(), new Vector2(x,y), false);
		if(obj!=null){
			obj.setRotation(Rotation.fromInt(Integer.parseInt(xmlElement.getAttribute("rotation"))));
		}
		return obj;
	}
	
}
