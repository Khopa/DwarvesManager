package enibdevlab.dwarves.models.rooms;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.ARoom;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Une pièce
 * 
 * @author Clément Perreau
 *
 */
public abstract class Room implements IRoom, IPersistent {

	/**
	 * Liste des objets présents dans la pièce
	 */
	protected ArrayList<GameObject> objects;
	
	/**
	 * Liste des rectangles composant la pièce
	 */
	protected ArrayList<MapArea> areas;
	
	/**
	 * Lien vers la map
	 */
	protected TileMap tilemap;
	
	/**
	 * Lien vers la liste des pièces
	 */
	protected Rooms rooms;
	
	/**
	 * Lien vers la vue
	 */
	protected ARoom view;
	
	/**
	 * Pièce opérationnelle ou non
	 */
	protected boolean operationnal;
	
	/**
	 * Objets requis
	 */
	protected HashMap<Class<?>, Integer> neededObjects = new HashMap<Class<?>, Integer>();

	/**
	 * 
	 * Liste des rectangle composant la pièce
	 * 
	 * @param i Position X du tile bas gauche de la première zone qui compose la pièce
	 * @param j Position Y du tile bas gauche de la première zone qui compose la pièce
	 * @param w Taille X de la première zone qui compose la pièce
	 * @param h Taille Y de la première zone qui compose la pièce
	 * @param tilemap Map sur laquelle on ajoute la pièce
	 * 
	 */
	public Room(int i, int j, int w, int h, TileMap tilemap, Rooms rooms) {
		
		this.tilemap = tilemap;
		this.areas = new ArrayList<MapArea>();
		this.objects = new ArrayList<GameObject>();
		this.rooms = rooms;
		
		// Ajout de la première zone
		this.areas.add(new MapArea(i,j,w,h,this.tilemap));
		
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}
	
	public ArrayList<GameObject> getObjects(Class<? extends GameObject> objectType) {
		ArrayList<GameObject> output = new ArrayList<GameObject>();
		for(GameObject obj:this.objects){
			if(obj.getClass() == objectType) output.add(obj);
		}
		return output;
	}

	public void setObjects(ArrayList<GameObject> objects) {
		this.objects = objects;
	}

	public TileMap getTilemap() {
		return tilemap;
	}

	public void setTilemap(TileMap tilemap) {
		this.tilemap = tilemap;
	}

	public ArrayList<MapArea> getAreas() {
		return areas;
	}
	
	public ARoom getView() {
		return view;
	}

	public void setView(ARoom view) {
		this.view = view;
	}
	
	public boolean isOperationnal(){
		return operationnal;
	}
	
	/**
	 * Dit si la pièce intersecte une autre Room
	 * @param room Autre pièce
	 */
	public boolean intersect(Room room){
		for(MapArea area:this.getAreas()){
			for(MapArea area2:room.getAreas()){
				if(area.intersect(area2)) return true;
			}
		}
		return false;
	}

	/**
	 * Retourne la première zone qui compose la pièce (parfois utile lors d'initialisation)
	 */
	public MapArea getMainArea() {
		return this.getAreas().get(0);
	}
	
	
	public boolean tileInside(int i, int j){
		for(MapArea area:this.areas){
			if(area.pointInside(i, j)) return true;
		}
		return false;
	}
	
	
	/**
	 * Teste si l'objet donné est dans la pièce
	 */
	public boolean objectIn(GameObject object){
		// tous les tiles de l'objet doivent être dans la pièce
		boolean ok = false;
		for(Vector2 tile:object.getTiles()){
			ok = false;
			for(MapArea area:this.areas){
				if(area.pointInside((int)tile.x, (int)tile.y)){
					ok = true;
					break;
				}
			}
			if(ok==false) return false;
		}
		return true;
	}
	
	
	/**
	 * Retourne le barycentre formé par les rectangles
	 */
	public Vector2 barycenter(){
		
		// L'aire de chaque zone est utilisé comme poids ici
		int x = 0;         // Barycentre X
		int y = 0;         // Barycentre Y
		int totalArea = 0; // Aire totale
		int tmp;           // Stockage temporaire
		
		for(MapArea area:this.areas){
			tmp = area.area();
			x += area.centerX()*tmp;
			y += area.centerY()*tmp;
			totalArea+=tmp;
		}
		x /= totalArea;
		y /= totalArea;
		
		return new Vector2(x,y);
	}
	
	
	/**
	 * Fusionne tous les rectangles qui présentent des intersections
	 * de manière optimale (de façon à avoir le moins de MapArea possible
	 * sans qu'aucune MapArea ne se superpose)
	 */
	public void clean(){
		
		ArrayList<MapArea> toRemove = new ArrayList<MapArea>();
		ArrayList<MapArea> toAdd = new ArrayList<MapArea>();
		
		boolean ok = true;
		
		for(MapArea area:this.areas){
			for(MapArea area2:this.areas){
				if(area!=area2){
					if(area.intersect(area2)){
						
						// Cas 1 : area est entierement dans area2 => suppresion de area
						if(area.pointInside(area2.getI(), area2.getJ())
						&& area.pointInside(area2.getI()+area2.getW(), area2.getJ()+area2.getH())){
							toRemove.add(area2);
						}
						// Cas 2 : area2 est entierement dans area => suppresion de area
						else if(area2.pointInside(area.getI(), area.getJ())
						&& area2.pointInside(area.getI()+area.getW(), area.getJ()+area.getH())){
							toRemove.add(area);
						}
						// Cas 3 : Intersection sans contenance
						else{
						
							// On éclate la plus petite des deux zones en supprimant la partie qui fait intersection
							if(area.area()>area2.area()){
								toRemove.add(area2);
								toAdd.addAll(area2.hollow(area));
							}
							else{
								toRemove.add(area);
								toAdd.addAll(area.hollow(area2));
							}
							
						}
						ok = false;
						break;
						
					}
				}
			}
			if(ok==false) break;
		}
		
		// Nettoyage effectif
		for(MapArea area:toRemove){
			this.areas.remove(area);
		}
		
		// Reconstruction
		for(MapArea area:toAdd){
			this.areas.add(area);
		}
		
		if(ok == false){
			clean();
		}
	}
	

	/**
	 * Fusionne la pièce avec celle donnée en paramètre
	 */
	public void fusion(Room current) {
		for(MapArea area:current.getAreas()){
			if (!(this.areas.contains(area))) this.areas.add(area);
		}
		for(GameObject obj:current.getObjects()) this.addObject(obj);
	}
	
	public void addObject(GameObject object){
		this.objects.add(object);
		object.setRoom(this);
		if(!this.operationnal) this.updateOperationalState(); // Mise à jour
	}

	public void removeObject(GameObject object){
		this.objects.remove(object);
		if(this.operationnal) this.updateOperationalState(); // Mise à jour
	}
	
	/**
	 *  Verifie si tous les objets qui était présent le sont toujours
	 */
	public void updateObjects(){
		
		ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
		
		for(GameObject obj:this.objects){
			if(!(this.objectIn(obj))){
				toRemove.add(obj);
			}
		}
		
		// Nettoyage Effectif
		for(GameObject obj:toRemove){
			this.objects.remove(obj);
		}
		
		this.updateOperationalState(); // Mise à jour
	}
	
	
	/**
	 * Recalcule la liste des objets
	 */
	public void rebuildObjectList(){
		
		// TODO
		
	}
	
	
	public ArrayList<Vector2> getTiles(){
		ArrayList<Vector2> tiles = new ArrayList<Vector2>();
		for(MapArea area:this.areas){
			tiles.addAll(area.getTiles());
		}
		return tiles;
	}
	
	
	
	/**
	 * Enlève la zone donnée de la pièce (Fractionne la pièce si besoin)
	 * @param area Zone à enlever 
	 * @return Vrai si l'opération entraine la destruction de la pièce
	 */
	public boolean removeArea(MapArea area) {
		
		
		ArrayList<MapArea> toRemove = new ArrayList<MapArea>(); // A enlever à la fin
		ArrayList<MapArea> toAdd = new ArrayList<MapArea>();    // A ajouter à la fin
		
		// Pour tous les zones qui composent la pièce
		for(MapArea roomArea:areas){
			if(roomArea.intersect(area)){
				// On enleve l'intersection
				toRemove.add(roomArea);
				toAdd.addAll(roomArea.hollow(area));
			}
		}
		
		// Nettoyage effectif
		for(MapArea roomArea:toRemove){
			this.areas.remove(roomArea);
		}
		
		// Reconstruction
		for(MapArea roomArea:toAdd){
			this.areas.add(roomArea);
		}
		
		// Controle des objets
		this.updateObjects();
		
		if(this.areas.size() == 0){
			return true;
		}
		
		return false;
		
		
	}
	
	public int getX(){
		return (int) (this.barycenter().x*this.tilemap.getTileWidth());
	}

	public int getY(){
		return (int) (this.barycenter().y*this.tilemap.getTileWidth());
	}

	@Override
	public boolean updateOperationalState() {
		
		operationnal = false;
		int counter;
		
		// Pour chaque type d'objet requis
		for(Class<?> objClass : this.neededObjects.keySet()){
			counter = 0;
			// Pour tous les objets de la pièce
			for(GameObject obj:this.objects){
				if(obj.getClass().equals(objClass)){
					counter ++;
				}	
			}
			
			// Teste si on a compté suffisament d'objet du type objClass dans la piece
			if(counter < this.neededObjects.get(objClass)){
				return false;
			}
			
		}
		
		operationnal = true;
		return operationnal;
	}
	
	@Override
	public Element saveAsXmlElement() {
		Element output = new Element(this.getClass().getName(), null);
		for(MapArea area:areas){
			output.addChild(area.saveAsXmlElement());
		}
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		// Inutilisable ici, car les données doivent servir à instancier un objet dont la
		// classe peut varier
	}
	
	public static Room fromXml(Element xmlElement, TileMap tilemap, Rooms rooms){
		ArrayList<MapArea> areas = new ArrayList<MapArea>();
		for(Element xmlMapArea:xmlElement.getChildrenByName("MapArea")){
			MapArea area = MapArea.fromXml(xmlMapArea, tilemap);
			if(area!=null) areas.add(area);
		}
		Room output = (Room) Loader.instantiateRoomToPlace(xmlElement.getName(),areas.remove(0), false, rooms);
		if(!areas.isEmpty())output.areas.addAll(areas);
		return output;
	}
	
	
}
