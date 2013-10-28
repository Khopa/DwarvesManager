package enibdevlab.dwarves.views;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 
 * Classe utilis�e pour stocker des suites d'images
 * D�coupe une image "Texture" contenant plusieurs sprites
 * en plusieurs "TextureRegion"
 * 
 * @author Cl�ment Perreau
 *
 */
public class Tileset implements Disposable{
	
	/**
	 * Taille des tiles
	 */
	protected int [] tileSize;

	/**
	 * Image
	 */
	private Texture imgdata;
	
	/**
	 * Liste des tiles
	 */
	protected TextureRegion[] data;
	
	/**
	 * Liste des collisions
	 */
	protected Boolean[] collisionData;
	
	/**
	 * Taille d'une ligne en tiles
	 */
	protected int lineSize;
	
	/**
	 * Propri�t�s des tiles
	 */
	protected ArrayList<HashMap<String, Integer>> properties;
	
	/**
	 * Cr�e un tileset � partir du fichier sp�cifi�, en utilisant XSize et YSize pour le d�coupage
	 * 
	 * @param path        Chemin de l'image
	 * @param tileXSize   Taille X de chaque tile
	 * @param tileYSize   Taille Y de chaque tile
	 */
	public Tileset(String path, int tileXSize, int tileYSize){
		imgdata = new Texture(path);
		tileSize = new int[2];
		tileSize[0] = tileXSize;
		tileSize[1] = tileYSize; 
		System.out.println("Loading : " + path);
		this.prepareData();
	}
	
	/**
	 * Charge un tileset � partir de donn�es Xml
	 * @param xmlData Donn�es Xml
	 */
	public Tileset(Element xmlData){
		
		// Cr�ation du tileset � partir de l'image
		imgdata = new Texture("data/maps/" + xmlData.getChildByName("image").getAttribute("source"));
		tileSize = new int[2];
		tileSize[0] = Integer.parseInt(xmlData.getAttribute("tilewidth"));
		tileSize[1] = Integer.parseInt(xmlData.getAttribute("tileheight"));
		this.prepareData();
		
		// Pr�paration des donn�es de collisions
		Array<Element> tilesXml = xmlData.getChildrenByName("tile");
		int id; // temporaire pour stocker l'id du tile en cours de traitement
		int tmp; // variable temporaire pour convertir ce qu'il y a dans l'xml en bool
		Element tile;
		for(int i = 0; i < tilesXml.size; i++){
			tile = tilesXml.get(i);
			id = Integer.parseInt(tile.getAttribute("id"));
			tmp = Integer.parseInt(tile.getChildByName("properties")
					               .getChildByName("property").getAttribute("value"));
			
			for(Element property:tile.getChildByName("properties").getChildrenByName("property")){
				properties.get(id).put(property.getAttribute("name"), Integer.parseInt(property.getAttribute("value")));
			}
			
			if(tmp==0){
				collisionData[id] = false;
			}
			else{
				collisionData[id] = true;
			}
		}
		
		System.out.println(properties);
		
	}
	
	/*
	 * Decoupe le tileset en petite images
	 */
	private void prepareData(){
		
		int width = this.imgdata.getWidth();
		int height = this.imgdata.getHeight();
		
		lineSize = width/tileSize[0];
		
		// Allocation M�moire
		data = new TextureRegion[width/tileSize[0]*height/tileSize[1]];
		collisionData = new Boolean[width/tileSize[0]*height/tileSize[1]];
		properties = new ArrayList<HashMap<String,Integer>>();
		for(int i = 0; i < width/tileSize[0]*height/tileSize[1]; i++){
			properties.add(new HashMap<String, Integer>());
		}
		
		// Creation � partir de l'image
		for(int i = 0; i < width/tileSize[0]; i++){
			for(int j = 0; j < height/tileSize[1]; j++){
				//System.out.println(j*height/tileSize[1] + i);
				data[j*width/tileSize[0] + i] = new TextureRegion(this.imgdata, i*tileSize[0],  j*tileSize[1],  tileSize[0], tileSize[1]);
				collisionData[j*width/tileSize[0] + i] = false;
			}
		}
	
	}
	
	/**
	 * Retourne un tile en fonction de l'index
	 * @param id index du tile
	 * @return
	 */
	public TextureRegion getTile(int id){
		return this.data[id];
	}
	
	/**
	 * Donne le nombre de tile
	 * @return
	 */
	public int getTileCount(){
		return this.data.length;
	}

	public int getTileHeight() {
		return tileSize[1];
	}
	
	public int getTileWidth() {
		return tileSize[0];
	}
	
	public void dispose(){
		this.data = null;
		this.imgdata.dispose();
	}

	public boolean getCollision(int id) {
		
		if(this.properties.get(id).containsKey("collide")){
			if(this.properties.get(id).get("collide") == 1){
				return true;
			}
		}
		
		return false;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	public boolean isMinable(int id) {
		
		if(this.properties.get(id).containsKey("minable")){
			if(this.properties.get(id).get("minable") == 0){
				return false;
			}
			else{
				return true;
			}
		}
		if(this.properties.get(id).containsKey("collide")){
			if(this.properties.get(id).get("collide") == 1){
				return true;
			}
		}
		
		return false;
	}
	
	public int getTileProperty(int tileId, String propertyName){
		if(this.properties.get(tileId).containsKey(propertyName)){
			return this.properties.get(tileId).get(propertyName);
		}
		return 0;
	}
	
	
	public Texture getImgdata() {
		return imgdata;
	}

	
}
