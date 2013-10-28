package enibdevlab.dwarves.views.world;

import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;


/**
 * Tile destinée à être affiché
 * 
 * @author Clément Perreau
 */
public class Tile implements IPersistent{

	/**
	 * Position X (sur la map)
	 */
	protected int x;
	
	/**
	 * Position Y (sur la map)
	 */
	protected int y;
	
	/**
	 * Position de sprite associé sur le tileset
	 */
	protected int id;
	
	/**
	 * Id du tileset
	 */
	protected int tilesetId;
	
	/**
	 * TileMapLayer associé
	 */
	protected TilemapLayer layer;

	/**
	 * Crée un tile à l'endroit indiqué
	 * @param x Position X sur la map
	 * @param y Position Y sur la map
	 * @param id Id du sprite à afficher (par rapport au tileset de la map qui est composée de ce tile)
	 * @param tilesetId Id du tileset
	 */
	public Tile(int x, int y, int id, int tilesetId, TilemapLayer layer) {
		super();
		this.x = x;
		this.y = y;
		this.id = id;
		this.tilesetId = tilesetId;
		this.layer = layer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTilesetId() {
		return tilesetId;
	}
	
	public void setTilesetId(int id) {
		this.tilesetId = id;
	}
	
	public boolean isBlocking(){
		return this.layer.getTileMap().isTileBlocking(this.x, this.y);
	}
	
	public Tile bottom(){
		return layer.getTile(x, y-1);
	}
	
	public Tile top(){
		return layer.getTile(x, y+1);
	}
	
	public Tile right(){
		return layer.getTile(x+1, y);
	}
	
	public Tile left(){
		return layer.getTile(x-1, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isMinable() {
		return this.layer.getTileMap().getTileset(tilesetId).isMinable(id);
	}

	public int getTileProperty(String propertyName) {
		return this.layer.getTileMap().getTileset(tilesetId).getTileProperty(this.id, propertyName);
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("tile", null);
		int gid = 1;
		int i = this.tilesetId-1;
		while(i>=0){
			gid += layer.getTileMap().getTileset(i).getTileCount();
		}
		gid += id;
		output.setAttribute("gid", Integer.toString(gid));
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
	}
	
}
