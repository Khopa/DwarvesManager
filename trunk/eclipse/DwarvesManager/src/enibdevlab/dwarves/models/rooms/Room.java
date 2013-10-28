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
 * Une pi�ce
 * 
 * @author Cl�ment Perreau
 *
 */
public abstract class Room implements IRoom, IPersistent {

	/**
	 * Liste des objets pr�sents dans la pi�ce
	 */
	protected ArrayList<GameObject> objects;
	
	/**
	 * Liste des rectangles composant la pi�ce
	 */
	protected ArrayList<MapArea> areas;
	
	/**
	 * Lien vers la map
	 */
	protected TileMap tilemap;
	
	/**
	 * Lien vers la liste des pi�ces
	 */
	protected Rooms rooms;
	
	/**
	 * Lien vers la vue
	 */
	protected ARoom view;
	
	/**
	 * Pi�ce op�rationnelle ou non
	 */
	protected boolean operationnal;
	
	/**
	 * Objets requis
	 */
	protected HashMap<Class<?>, Integer> neededObjects = new HashMap<Class<?>, Integer>();

	/**
	 * 
	 * Liste des rectangle composant la pi�ce
	 * 
	 * @param i Position X du tile bas gauche de la premi�re zone qui compose la pi�ce
	 * @param j Position Y du tile bas gauche de la premi�re zone qui compose la pi�ce
	 * @param w Taille X de la premi�re zone qui compose la pi�ce
	 * @param h Taille Y de la premi�re zone qui compose la pi�ce
	 * @param tilemap Map sur laquelle on ajoute la pi�ce
	 * 
	 */
	public Room(int i, int j, int w, int h, TileMap tilemap, Rooms rooms) {
		
		this.tilemap = tilemap;
		this.areas = new ArrayList<MapArea>();
		this.objects = new ArrayList<GameObject>();
		this.rooms = rooms;
		
		// Ajout de la premi�re zone
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
	 * Dit si la pi�ce intersecte une autre Room
	 * @param room Autre pi�ce
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
	 * Retourne la premi�re zone qui compose la pi�ce (parfois utile lors d'initialisation)
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
	 * Teste si l'objet donn� est dans la pi�ce
	 */
	public boolean objectIn(GameObject object){
		// tous les tiles de l'objet doivent �tre dans la pi�ce
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
	 * Retourne le barycentre form� par les rectangles
	 */
	public Vector2 barycenter(){
		
		// L'aire de chaque zone est utilis� comme poids ici
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
	 * Fusionne tous les rectangles qui pr�sentent des intersections
	 * de mani�re optimale (de fa�on � avoir le moins de MapArea possible
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
						
							// On �clate la plus petite des deux zones en supprimant la partie qui fait intersection
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
	 * Fusionne la pi�ce avec celle donn�e en param�tre
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
		if(!this.operationnal) this.updateOperationalState(); // Mise � jour
	}

	public void removeObject(GameObject object){
		this.objects.remove(object);
		if(this.operationnal) this.updateOperationalState(); // Mise � jour
	}
	
	/**
	 *  Verifie si tous les objets qui �tait pr�sent le sont toujours
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
		
		this.updateOperationalState(); // Mise � jour
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
	 * Enl�ve la zone donn�e de la pi�ce (Fractionne la pi�ce si besoin)
	 * @param area Zone � enlever 
	 * @return Vrai si l'op�ration entraine la destruction de la pi�ce
	 */
	public boolean removeArea(MapArea area) {
		
		
		ArrayList<MapArea> toRemove = new ArrayList<MapArea>(); // A enlever � la fin
		ArrayList<MapArea> toAdd = new ArrayList<MapArea>();    // A ajouter � la fin
		
		// Pour tous les zones qui composent la pi�ce
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
			// Pour tous les objets de la pi�ce
			for(GameObject obj:this.objects){
				if(obj.getClass().equals(objClass)){
					counter ++;
				}	
			}
			
			// Teste si on a compt� suffisament d'objet du type objClass dans la piece
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
		// Inutilisable ici, car les donn�es doivent servir � instancier un objet dont la
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
