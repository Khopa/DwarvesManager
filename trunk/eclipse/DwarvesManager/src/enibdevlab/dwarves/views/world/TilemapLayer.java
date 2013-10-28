package enibdevlab.dwarves.views.world;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Une couche de tiles pour les tilemap
 * (Pour cr�er de jolis d�cors, il faut souvent les superposer)
 * 
 * @author Cl�ment Perreau
 *
 */
public class TilemapLayer implements IPersistent{
	
	/**
	 * Alpha blending activ� ou non
	 */
	protected boolean alphaBlending = false;
	
	/**
	 * Liste des tiles
	 */
	protected Tile[][] tiles;
	
	/**
	 * Liste des tilesets utilis� pour le rendu
	 */
	protected Tileset[] tilesets;
	
	/**
	 * TileMap
	 */
	protected TileMap tilemap;
	
	/**
	 * Nombre de tile en X
	 */
	protected int xSize;
	
	/**
	 * Nombre de tile en Y
	 */
	protected int ySize;
	
	/**
	 * Debug font (pour afficher du texte sur certains blocs pour voir si les propri�t�s sont bien charg�es)
	 */
	protected BitmapFont bmpfnt = new BitmapFont();
	
	/**
	 * Construit un calque de tiles
	 * @param xmlData Donn�es XML utilis�es pour charger la map
	 * @param tilesets Liste des tilesets utilis�s
	 * @param xSize Nombre de tile en X de la map
	 * @param ySize Nombre de tile en Y de la map
	 * @param alphaBlending Optimisation ou non par d�sactivation de la transparence des tiles
	 */
	public TilemapLayer(Element xmlData, Tileset[] tilesets, TileMap tilemap, int xSize, int ySize, boolean alphaBlending){
		this.tilesets = tilesets;
		this.tilemap = tilemap;
		this.xSize = xSize;
		this.ySize = ySize;
		this.alphaBlending = alphaBlending;
		this.loadFromXmlElement(xmlData);
	}
	
	/**
	 * Construit un calque de tiles
	 * @param xmlData Donn�es XML utilis�es pour charger la map
	 * @param tilesets Liste des tilesets utilis�s
	 * @param xSize Nombre de tile en X de la map
	 * @param ySize Nombre de tile en Y de la map
	 */
	public TilemapLayer(Element xmlData, Tileset[] tilesets, TileMap tilemap, int xSize, int ySize){
		this(xmlData, tilesets, tilemap, xSize, ySize, false);
	}
	
	/**
	 * Construit un calque de Tile vide
	 *
	 * @param tilesets Liste des tilesets utilis�s
	 * @param tilemap Map
	 * @param xSize Nombre de tile en X de la map
	 * @param ySize Nombre de tile en Y de la map
	 */
	public TilemapLayer(Tileset[] tilesets, TileMap tilemap, int xSize, int ySize){
		this.tilesets = tilesets;
		this.tilemap = tilemap;
		this.xSize = xSize;
		this.ySize = ySize;
		this.alphaBlending = false;
		this.createEmptyLayer();
	}
	
	private void createEmptyLayer() {
		this.tiles = new Tile[xSize][ySize];
		// On cr�e la liste des tiles � partir de l'arbre xml
		int i = 0;
		int j = 0;
		for(int k = 0; k < xSize*ySize; k++){
			// On cr�e le tile
			this.tiles[i][ySize - j - 1] = new Tile(i,ySize-j-1, 0, 0, this);
			if (++i >= xSize){
				j ++;
				i = 0;
			}
			if (j>=ySize){
				break;
			}
		}
	}

	/**
	 * 
	 * Affichage du calque de tile
	 * 
	 * @param tilemap Map associ�
	 * @param batch Batch sur lesquel on dessine
	 * @param XminTile Premier tile en X � dessiner
	 * @param YminTile Premier tile en Y � dessiner
	 * @param XmaxTile Dernier tile en X � dessiner
	 * @param YmaxTile Dernier tile en Y � dessiner
	 */
	public void draw(TileMap tilemap, SpriteBatch batch, int XminTile, int YminTile, int XmaxTile, int YmaxTile){
		
		if(!this.alphaBlending) batch.disableBlending();
		
		int tileId;
		Tileset tileset;
		
		for(int i = XminTile ; i< XminTile+XmaxTile; i++){
			for(int j = YminTile; j < YminTile+YmaxTile; j++){
				try{
					tileId = this.tiles[i][j].getId();
					tileset = tilemap.getTileset(this.tiles[i][j].getTilesetId());
					if (tileId < 0) continue; // Si le bloc est vide
					batch.draw(tileset.getTile(tileId),
							   tilemap.getX() + i * tileset.getTileWidth(),
							   tilemap.getY() + j * tileset.getTileHeight(),
							   tilemap.getOriginX(), tilemap.getOriginY(),
							   tilemap.getTileWidth(), 
							   tilemap.getTileHeight(),
							   tilemap.getScaleX(),
							   tilemap.getScaleY(),
							   tilemap.getRotation());
					
					// Test seulement (affiche C si le tile doit �tre bloquant)
					/*if (tileset.getCollision(tileId)){
						bmpfnt.draw(batch, "C",
								    tilemap.getX() + (i+1f/2f) * tileset.getTileWidth(),
								    tilemap.getY() + (j+1f/2f) * tileset.getTileHeight());
					}*/
					
					
				}
				catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
		
		if(!this.alphaBlending) batch.enableBlending();
		
	}
	
	/**
	 * Retourne tous les tiles
	 */
	public Tile[][] getTiles(){
		return this.tiles;
	}

	/**
	 * Retourne le tile dont les coordonn�es sont i,j
	 * @param i Position X
	 * @param j Position Y
	 * @return 
	 */
	public Tile getTile(int i, int j) {
		try{
			return this.tiles[i][j];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	public TileMap getTileMap(){
		return this.tilemap;
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("layer", null);
		output.setAttribute("width", Integer.toString(xSize));
		output.setAttribute("height", Integer.toString(ySize));
		Element data = new Element("data", null);
		
		int i = 0;
		int j = 0;
		for(int k = 0; k < xSize*ySize; k++){
			data.addChild(this.getTile(i,ySize-j-1).saveAsXmlElement());
			if (++i >= xSize){
				j ++;
				i = 0;
			}
			if (j>=ySize){
				break;
			}
		}
		
		output.addChild(data);
		return output;
	}
	
	@Override
	public void loadFromXmlElement(Element xmlData){
		this.tiles = new Tile[xSize][ySize];
		Element data = xmlData.getChildByName("data");
		Element tile;
		int i = 0; // utilis� pour positionner les tiles
		int j = 0; // utilis� pour positionner les tiles
		int tileId; // utilis� pour connaitre l'id et le tileset du tile
		
		int tilesetCount; // utilis� pour compter les tiles des tilesets
		int ti; // utilis� pour compter et trouver le bon tileset
		
		// On cr�e la liste des tiles � partir de l'arbre xml
		for(int k = 0; k < data.getChildCount(); k++){
			tile = data.getChild(k);
			
			// On r�cup�re l'id du bon tileset du tile � partir de l'id qu'on lui a donn�
			tileId = tile.getInt("gid")-1;
			ti = 0;
			tilesetCount = tilesets[ti].getTileCount();
			while(tileId > tilesetCount){
				tilesetCount += tilesets[ti].getTileCount();
				ti ++;
			}
			tileId -= (tilesetCount-tilesets[ti].getTileCount());

			// On cr�e le tile
			this.tiles[i][ySize - j - 1] = new Tile(i,ySize-j-1,tileId, ti, this);
			
			if (++i >= xSize){
				j ++;
				i = 0;
			}
			if (j>=ySize){
				break;
			}
		}
	}
	
	
	


}
