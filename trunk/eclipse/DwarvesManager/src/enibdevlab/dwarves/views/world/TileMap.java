package enibdevlab.dwarves.views.world;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.levels.Level;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.Tileset;

/**
 * 
 * Classe utilis�e pour dessiner le monde
 * 
 * @author Cl�ment Perreau
 *
 */
public class TileMap extends Actor implements IPersistent{
	
	/**
	 * Liste des tilesets utilis�s pour le rendu
	 */
	protected Tileset[] tilesets;
	
	/**
	 * Nombre de tile en X
	 */
	protected int xSize;
	
	/**
	 * Nombre de tile en Y
	 */
	protected int ySize;
	
	/**
	 * Taille des tile en X
	 */
	protected int tileWidth = 0;
	
	/**
	 * Taille des tiles en Y
	 */
	protected int tileHeight = 0;
	
	/**
	 * Liste des layers
	 */
	protected TilemapLayer[] layers;
	
	/**
	 * R�f�rence vers la classe qui contient la map
	 */
	protected Group parentGroup;
	
	public TileMap(){}
	
	/**
	 * 
	 * Cr�e une nouvelle map � partir d'un fichier .tmx
	 * 
	 * @param parent Groupe parent
	 * @param tileset Tileset utilis�
	 * @param mapFile Chemin vers le fichier de map � charger
	 */
	public TileMap(Group parent, Level level){
		super();
		this.parentGroup = parent;
		
		XmlReader xmlReader = new XmlReader();
		try {
			Element map = xmlReader.parse(level.getMapFile());
			loadFromXmlElement(map);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * 
	 * Charge une map depuis des donn�es xml
	 * 
	 * @param parent Groupe parent
	 * @param level Niveau du jeu
	 * @param xmlData Donn�es XML
	 */
	public TileMap(Group parent, Level level, Element xmlData) {
		super();
		this.parentGroup = parent;
		loadFromXmlElement(xmlData);
	}

	/**
	 * Cr�e une map vide 
	 * @param xSize Taille X de la map
	 * @param ySize Taille Y de la map
	 */
	public TileMap(Group parent, int xSize, int ySize) {
		super();
		this.parentGroup = parent;
		this.xSize = xSize;
		this.ySize = ySize;
		createEmptyMap(xSize, ySize);
	}
	
	private void createEmptyMap(int xSize, int ySize) {
		
		// Tileset par defaut
		this.tilesets = new Tileset[1];
		XmlReader tilesetLoader = new XmlReader();
		try {
			this.tilesets[0] = new Tileset(tilesetLoader.parse(Gdx.files.internal("data/maps/tileset.tsx")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.tileHeight = this.tilesets[0].getTileHeight();
		this.tileWidth  = this.tilesets[0].getTileWidth();
		
		// Calques
		this.layers = new TilemapLayer[1];
		this.layers[0] = new TilemapLayer(tilesets, this, xSize, ySize);
		
		
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("map", null);
		output.setAttribute("width", Integer.toString(xSize));
		output.setAttribute("height", Integer.toString(ySize));
		output.setAttribute("tilewidth", Integer.toString(this.getTileWidth()));
		output.setAttribute("tileheight", Integer.toString(this.getTileHeight()));
		
		// Sauvegarde des tilesets
		int firstGid = 1;
		for(Tileset tileset:tilesets){
			Element tilesetXml = new Element("tileset",null);
			tilesetXml.setAttribute("firstGid", Integer.toString(firstGid));
			tilesetXml.setAttribute("source", "tileset.tsx");
			firstGid += tileset.getTileCount();
			output.addChild(tilesetXml);
		}
		
		// Sauvegarde des calques
		for(TilemapLayer layer:layers){
			output.addChild(layer.saveAsXmlElement());
		}
		
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {

		XmlReader xmlReader = new XmlReader();
		
		// Chargement de la taille
		this.xSize = Integer.parseInt(xmlElement.getAttribute("width"));
		this.ySize = Integer.parseInt(xmlElement.getAttribute("height"));
		this.tileWidth = Integer.parseInt(xmlElement.getAttribute("tilewidth"));
		this.tileHeight = Integer.parseInt(xmlElement.getAttribute("tileheight"));
		this.setSize(xSize*tileWidth, ySize*tileHeight);
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
		
		// Chargement des Tileset
		Array<Element> tilesetXml = xmlElement.getChildrenByName("tileset");
		Element tileset;
		this.tilesets = new Tileset[tilesetXml.size];
		for(int i = 0; i < tilesetXml.size; i++){
			tileset = tilesetXml.get(i);
			if(tileset.getAttribute("source")!=null){
				try {
					this.tilesets[i] = new Tileset(xmlReader.parse(Gdx.files.internal("data/maps/"+tileset.getAttribute("source"))));
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			else this.tilesets[i] = new Tileset(tileset);
		}

		// Chargement des layers 
		Array<Element> layerXml = xmlElement.getChildrenByName("layer");
		Element layer;
		this.layers = new TilemapLayer[layerXml.size];
		for(int i = 0; i < layerXml.size; i++){
			layer = layerXml.get(i);
			this.layers[i] = new TilemapLayer(layer, this.tilesets, this, this.xSize, this.ySize, true);
		}	
		
	}
	
	/**
	 * Affiche la map � l'�cran
	 * @param batch
	 * @param parentAlpha
	 */
	public void draw(SpriteBatch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		
		batch.setColor(this.getColor());

		int XminTile = (int) (Math.floor(-this.getParent().getX()/this.getTileWidth())/this.getParent().getScaleX());
		int YminTile = (int) (Math.floor(-this.getParent().getY()/this.getTileHeight())/this.getParent().getScaleY());
		int XmaxTile = (int) ((DwarvesManager.getWidth()/(this.getTileWidth())+1)/this.getParent().getScaleX()+2);
		int YmaxTile = (int) ((DwarvesManager.getHeight()/(this.getTileHeight())+1)/this.getParent().getScaleY()+2);
		
		for(TilemapLayer layer:this.layers){
			layer.draw(this, batch, XminTile, YminTile, XmaxTile, YmaxTile);
		}
	}

	/**
	 * Retourne les tiles du layer de fond
	 */
	public Tile[][] getTiles() {
		return this.layers[0].getTiles();
	}
	
	/**
	 * Retourne les tiles du layer demand�
	 * @param id Id du layer dans la liste des layers de la tilemap
	 */
	public Tile[][] getTiles(int id) {
		return this.layers[id].getTiles();
	}
	
	/**
	 * Retourne le tile i,j du layer dont l'id est id dans la liste des layers de la tilemap
	 * @param id Id du layer sur lesquel on r�cup�re les infos
	 * @param i Position x du tile
	 * @param j Position Y du tile
	 */
	public Tile getTile(int id, int i, int j){
		return this.layers[id].getTile(i,j);
	}

	/**
	 * Retourne la taille X des tiles utilis�s 
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Retourne la taille Y des tiles utilis�s 
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Retourne le tileset dont l'id est id dans la liste des tileset de la tilemap
	 * @param id Id du tileset
	 */
	public Tileset getTileset(int id) {
		return this.tilesets[id];
	}
	
	public boolean isTileBlocking(int layer, int i, int j){
		try{
			Tile tile = this.getTile(layer, i, j);
			if(tile == null) return true;
			return this.tilesets[tile.getTilesetId()].getCollision(tile.getId());
		}
		catch(ArrayIndexOutOfBoundsException e){
			return true;
		}
	}
	
	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public boolean isTileBlocking(int i, int j){
		return isTileBlocking(0,i,j);
	}

	public Group getParentGroup() {
		return this.parentGroup;
	}
	
	/**
	 * Retourne les coordonn�es de la case sur laquelle on a cliqu�, en tenant compte du scrolling et du scaling
	 * @param sx Posiition X souris
	 * @param sy Posiition Y souris
	 */
	public Vector2 getClickedTile(int sx, int sy){
		
		// Calcul des coordonn�es � l'�cran par rapport � la r�solution de base 
		sx = (int) (sx*((float)(DwarvesManager.getWidth())/(float)(Gdx.graphics.getWidth()))); 
		sy = (int) (sy*((float)(DwarvesManager.getHeight())/(float)(Gdx.graphics.getHeight()))); 
		sy = DwarvesManager.getHeight()-sy; // car les coords sont invers�s en y pour la souris 
	 	// A partir de l� on a plus � se pr�occuper du redimensionnement, on a les coordonn�es de la fen�tre 	                         
	 	// On scrolle les coordonn�es pour correspondre � celle de la map 
	 	sx -= this.parentGroup.getX(); 
	 	sy -= this.parentGroup.getY(); 
	 	// On divise par les tailles des tiles pour obtenir les coords de la case sur laquelle on a cliqu� 
	 	sx /= (this.getTileWidth()*this.parentGroup.getScaleX()); 
	 	sy /= (this.getTileHeight()*this.parentGroup.getScaleY());
		
		return new Vector2(sx,sy);
	}
	
	/**
	 * Retourne le tile de la case sur laquelle on a cliqu�
	 * @param sx Posiition X souris
	 * @param sy Posiition Y souris
	 * @param layer Layer sur lesquel on veut r�cup�rer le tile (en g�n�ral 0)
	 */
	public Tile getClickedTile(int sx, int sy, int layer){
		Vector2 tile = this.getClickedTile(sx, sy);
		return this.getTile(layer, (int)tile.x, (int)tile.y);
	}
	
	/**
	 * Retourne les coordonn�es en pixels de l'endroit cliqu�, en tenant compte du scrolling et du scaling
	 * @param f Posiition X souris
	 * @param g Posiition Y souris
	 */
	public Vector2 getPixelCoordinate(float f, float g){
		
		// Calcul des coordonn�es � l'�cran par rapport � la r�solution de base 
		float fsx = (float) (f*((float)(DwarvesManager.getWidth())/(float)(Gdx.graphics.getWidth()))); 
		float fsy = (float) (g*((float)(DwarvesManager.getHeight())/(float)(Gdx.graphics.getHeight()))); 
		fsy = DwarvesManager.getHeight()-g; // car les coords sont invers�s en y pour la souris 
	 	// A partir de l� on a plus � se pr�occuper du redimensionnement, on a les coordonn�es de la fen�tre 	                         
	 	// On scrolle les coordonn�es pour correspondre � celle de la map 
	 	fsx -= (float)this.parentGroup.getX(); 
	 	fsy -= (float)this.parentGroup.getY(); 
	 	// On divise par les tailles des tiles pour obtenir les coords de la case sur laquelle on a cliqu� 
	 	fsx /= ((float)this.parentGroup.getScaleX()); 
	 	fsy /= ((float)this.parentGroup.getScaleY());
		
		return new Vector2(fsx,fsy);
	}
	
	/**
	 * Coordonn�es du centre
	 */
	public Vector2 getCenterCoordinate(){
		double x = DwarvesManager.getWidth()/2;
		double y = DwarvesManager.getHeight()/2;
		// On scrolle les coordonn�es pour correspondre � celle de la map 
		double fsx = x;
		double fsy = y;
	 	fsx -= (double)this.parentGroup.getX(); 
	 	fsy -= (double)this.parentGroup.getY(); 
	 	// On divise par les tailles des tiles pour obtenir les coords de la case sur laquelle on a cliqu� 
	 	fsx /= ((double)this.parentGroup.getScaleX()); 
	 	fsy /= ((double)this.parentGroup.getScaleY());
		return new Vector2((float)fsx,(float)fsy);
	}
	
}
