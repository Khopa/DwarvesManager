package enibdevlab.dwarves.models.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.actors.AMapArea;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * 
 * Définit une zone de la map
 * 
 * @author Clément Perreau
 *
 */
public class MapArea implements IPersistent{
	
	/**
	 * Coordonnées du point haut gauche X
	 */
	protected int i;
	
	/**
	 * Coordonnées du point haut gauche Y
	 */
	protected int j;
	
	/**
	 * Largeur en tile
	 */
	protected int w;
	
	/**
	 * Hauteur en tile
	 */
	protected int h;
	
	/**
	 * Layer principal de la map, où sont situés les tiles
	 */
	protected TileMap tilemap;
	
	/**
	 * Vue
	 */
	protected AMapArea view;
	
	/**
	 * Crée une zone de map
	 * @param i Coordonnées du point haut gauche X
	 * @param j Coordonnées du point haut gauche Y
	 * @param w Largeur
	 * @param h Hauteur
	 */
	public MapArea(int i, int j, int w, int h, TileMap tilemap){
		
		// Force le coin bas gauche à être considéré comme origine de la zone (i,j)
		if(w<0){
			i = i+w;
			w = -w;
		}
		if(h<0){
			j = j+h;
			h = -h;
		}
		// ----
		
		this.i = i;
		this.j = j;
		this.w = w;
		this.h = h;
		
		this.tilemap = tilemap;
	}
	
	public MapArea(MapArea copy){
		this(copy.getI(), copy.getJ(), copy.getW(), copy.getH(), copy.getTilemap());
	}
	
	public TileMap getTilemap() {
		return tilemap;
	}

	public void setTilemap(TileMap tilemap) {
		this.tilemap = tilemap;
	}

	public int getI(){
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public AMapArea getView() {
		return view;
	}

	public void setView(AMapArea view) {
		this.view = view;
	}
	
	
	/**
	 * Retourne les coordonnées du coin bas gauche
	 */
	public Vector2 getBottomLeft(){
		return new Vector2(i,j);
	}
	
	/**
	 * Obtention de la différence de position entre deux zones
	 */
	public Vector2 deltaPosition(MapArea other){
		return new Vector2(i-other.getI(), j-other.getJ());
	}
	
	/**
	 * Retourne l'aire de la zone
	 */
	public int area(){
		return w*h;
	}
	
	/**
	 * Retourne la position X du centre
	 */
	public int centerX(){
		return (int) Math.floor(i+w/2);
	}
	
	/**
	 * Retourne la position Y du centre
	 */
	public int centerY(){
		return (int) Math.floor(j+h/2);
	}
	
	/**
	 * Dit si la zone intersecte une autre zone
	 * @param area Autre zone
	 */
	public boolean intersect(MapArea area){
		return (!((area.getI() >= this.getI() + this.getW()
			   || (area.getI() + area.getW() <= this.getI())
			   || (area.getJ() >= this.getJ() + this.getH())
			   || (area.getJ() + area.getH() <= this.getJ()))));
	}
	
	/**
	 * Dit si le tile (x,y) est situé à l'intérieur de la zone
	 * @param x Position X du tile
	 * @param y Position Y du tile
	 */
	public boolean pointInside(int x, int y){
		return(x >= i && x <= i+w && y >= j && y <= j + h);
	}
	
	/**
	 * Retourne la zone formée par l'intersection avec une autre zone
	 * (Il est supposée que la zone fournie forme une intersection avec celle ci)
	 * @param area Autre Zone
	 */
	public MapArea getIntersection(MapArea area){
		
		// La position bas gauche de l'intersection formée est toujours le max des deux coordonnées
		int x = Math.max(this.getI(), area.getI());
		int y = Math.max(this.getJ(), area.getJ());
		
		// La taille de l'intersection est trouvée par la différence entre les coordonnées
		// haut droite minimale et la position que l'on vient de calculer
		int w = Math.min(this.getI()+this.getW(), area.getI()+area.getW()) - x;
		int h = Math.min(this.getJ()+this.getH(), area.getJ()+area.getH()) - y;
		
		return new MapArea(x,y,w,h,tilemap);
		
	}
	
	
	/**
	 * Retourne un ensemble de MapArea crée par le retrait d'une zone (éclatement) 
	 */
	public ArrayList<MapArea> hollow(MapArea toRemove){
		
		MapArea area = this.getIntersection(toRemove); // On empeche le debordement
		ArrayList<MapArea> result = new ArrayList<MapArea>();	

		if(this.intersect(area)){  // Test rapide
			
			// Dans le pire des cas, on éclatera en 4 nouvelles zones
			int x = 0;
			int y = 0;
			int w = 0;
			int h = 0;
			
			// Zone de gauche
			x = this.i;
			y = this.j;
			w = area.getI() - this.getI();
			h = this.h;
			if(w>0 && h >0) result.add(new MapArea(x,y,w,h,tilemap));
			
			// Zone de droite
			x = area.getI() + area.getW();
			y = this.j;
			w = this.i+this.w-x;
			h = this.h;
			if(w>0 && h >0) result.add(new MapArea(x,y,w,h,tilemap));
			
			// Zone Inférieure
			x = area.getI();
			y = this.j;
			w = area.getW();
			h = area.getJ() - this.getJ();
			if(w>0 && h >0) result.add(new MapArea(x,y,w,h,tilemap));
			
			// Zone Supérieure
			x = area.getI();
			y = area.getJ() + area.getH();
			w = area.getW();
			h = this.getJ()+this.getH()-y;
			if(w>0 && h >0) result.add(new MapArea(x,y,w,h,tilemap));
			
		}
		else result.add(this);
		
		return result;
	}
	
	public String toString(){
		String str = "(";
		str += Integer.toString(i);
		str += ", ";
		str += Integer.toString(j);
		str += ", ";
		str += Integer.toString(w);
		str += ", ";
		str += Integer.toString(h);
		str += ")";
		return str;
	}

	public ArrayList<Vector2> getTiles() {
		ArrayList<Vector2> tiles = new ArrayList<Vector2>();
		
		for(int i = this.i; i < this.i+this.w;i++ ){
			for(int j = this.j; j < this.j+this.h;j++ ){
				tiles.add(new Vector2(i,j));
			}
		}
		
		return tiles;
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("MapArea", null);
		output.setAttribute("i", Integer.toString(i));
		output.setAttribute("j", Integer.toString(j));
		output.setAttribute("w", Integer.toString(w));
		output.setAttribute("h", Integer.toString(h));
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		// TODO Auto-generated method stub
		
	}

	public static MapArea fromXml(Element xmlMapArea, TileMap tilemap) {
		int i = Integer.parseInt(xmlMapArea.getAttribute("i"));
		int j = Integer.parseInt(xmlMapArea.getAttribute("j"));
		int w = Integer.parseInt(xmlMapArea.getAttribute("w"));
		int h = Integer.parseInt(xmlMapArea.getAttribute("h"));
		return new MapArea(i, j, w, h, tilemap);
	}
	
	


}
