package enibdevlab.dwarves.views.scenes.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Level;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.AMapArea;
import enibdevlab.dwarves.views.actors.APriceEffect;
import enibdevlab.dwarves.views.scenes.editor.MapLayer;
import enibdevlab.dwarves.views.world.TileMap;

/**
 * Couche de Gameplay du jeu
 * C'est toute la partie "scrollable" du jeu (La map + personnages + objets)
 * + affichage des zones à miner
 * 
 * @author Clément Perreau
 *
 */
public class GameplayLayer extends MapLayer{
	
	/**
	 * Couche d'objets
	 */
	protected ObjectLayer objectLayer;
	
	/**
	 * Couche de personnages
	 */
	protected CharacterLayer characterLayer;
	
	/**
	 * Zone de definition
	 */
	protected AMapArea definitionArea;
	
	/**
	 * Initialisation
	 */
	protected boolean initialized;

	/**
	 * Modèle
	 */
	protected Level level;

	public GameplayLayer(Level level){
		super();
		this.level = level;
		this.tilemap = new TileMap(this, level);
		this.init();
	}
	;
	public GameplayLayer(Level level, Element xmlData) {
		super();
		this.level = level;
		this.tilemap = new TileMap(this, level, xmlData);
		this.init();
	}
	
	public void init(){
		this.level.setTilemap(this.tilemap);
		this.objectLayer = new ObjectLayer(this.tilemap);
		this.characterLayer = new CharacterLayer();
		this.definitionArea = new AMapArea(new MapArea(0, 0, 0, 0, tilemap));
		this.addActor(this.tilemap);
		this.addActor(this.objectLayer);
		this.addActor(this.characterLayer);
		this.addActor(this.definitionArea);
		initialized = true;
	}
	
	@Override
	public void addActor(Actor actor){
		if(this.initialized) this.addActorAfter(tilemap, actor);
		else super.addActor(actor);
	}
	
	/**
	 * Cette fonction est appelé quand de l'argent est depensé ou gagné pour faire apparaitre un joli effet
	 */
	public void priceEffect(int x, int y, int price){
		this.addActorAfter(definitionArea, new APriceEffect(x, y, price));
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		/**
		 * Dessin des tiles à miner ici
		 */
		
		// Je n'utilise pas mes primitives ici : il vaut mieux travailler à plus bas niveau
		// car le besoin d'optimisation est assez important. (Je veux éviter de faire
		// des begin inutiles sachant qu'on peut avoir des centaines de "filledRectangle"
		// à afficher) )
		batch.end();
		Gdx.gl.glEnable(GL10.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		DwarvesManager.renderer.begin(ShapeType.FilledRectangle);
		
		int w = (int) level.getTileXSize();
		int h = (int) level.getTileYSize();
		
		// Dessin des tiles accesibles
		DwarvesManager.renderer.setColor(Loader.AYELLOW);
		for(Vector2 tile:this.level.getGame().getTaskManager().getAccesibleTile()){
			DwarvesManager.renderer.filledRect(tile.x*w, tile.y*h, w, h);
		}
		
		// Dessin des tiles innacessibles
		DwarvesManager.renderer.setColor(Loader.ARED);
		for(Vector2 tile:this.level.getGame().getTaskManager().getInaccesibleTile()){
			DwarvesManager.renderer.filledRect(tile.x*w, tile.y*h, w, h);
		}
		
		// Debug pour connaitre les tiles assignés aux nains
		if(DwarvesManager.debug){
			DwarvesManager.renderer.setColor(Color.BLUE);
			for(Vector2 tile:this.level.getGame().getTaskManager().getAssignedTile()){
				DwarvesManager.renderer.filledRect(tile.x*w, tile.y*h, w, h);
			}
		}
		
		DwarvesManager.renderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		batch.begin();
	}
	
	/**
	 * Retourne la map du jeu
	 */
	public TileMap getMap(){
		return this.tilemap;
	}
	
	/**
	 * Retourne le calque des objets
	 */
	public ObjectLayer getObjectLayer(){
		return this.objectLayer;
	}
	
	public CharacterLayer getCharacterLayer() {
		return characterLayer;
	}
	/**
	 * Calque de la map
	 */
	public TileMap getTilemap() {
		return tilemap;
	}

	public AMapArea getDefinitionArea() {
		return definitionArea;
	}

	public void setDefinitionArea(AMapArea definitionArea) {
		this.definitionArea = definitionArea;
	}
}
