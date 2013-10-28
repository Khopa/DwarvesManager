package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Group;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.listener.CharacterListener;
import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Primitives;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;

/**
 * 
 * Vue utilisée pour représenter un personnage (comme un nain, un elfe ou un gobelin)
 * Fonctionne comme un groupe d'actor : Corps, Mains, Arme/Outil
 * 
 * @author Clément Perreau
 *
 */
public class ACharacter extends Group{

	/**
	 * Reference vers le modele
	 */
	protected MCharacter model;
	
	/**
	 * Direction du personnage (surtout utilisée pour le dessin)
	 */
	protected Direction direction = Direction.BOTTOM;
	
	/**
	 * Objet representant le corps
	 */
	protected ABody body;
	
	/*
	 * Objet représentant la tête
	 */
	protected AHead head;
	
	/**
	 * Objet representant la main gauche
	 */
	protected AHand leftHand;
	
	/**
	 * Objet representant la main droite
	 */
	protected AHand rightHand;
	
	/**
	 * Objet representant l'objet tenu en main gauche
	 */
	protected AItem leftHandItem;
	
	/**
	 * Objet representant l'objet tenu en main droite
	 */
	protected AItem rightHandItem;
	
	/**
	 * Configuration du modèle 2d
	 */
	protected BodyConfig bodyConfig;
	
	/**
	 * Plainte si le nain se plaint
	 */
	protected int complaint = -1;
	
	/**
	 * Construit la vue du modèle de personnage donné
	 * @param mCharacter Modele
	 */
	public ACharacter(MCharacter mCharacter){
		super();
		this.model = mCharacter;
		this.model.setView(this);
		this.bodyConfig = mCharacter.getBodyConfig();
		
		this.setScale(.5f);
		
		int w = (int) (this.bodyConfig.getBodyTileset().getTileWidth()*this.getScaleX());
		int h = (int) (this.bodyConfig.getBodyTileset().getTileHeight()*this.getScaleY());;
		
		/*int w2 = (int) this.model.getGame().getLevel().getTileXSize();
	    int h2 = (int) this.model.getGame().getLevel().getTileYSize();*/
		
		// RX position du tile
		// Il faut décaler pour centrer le perso dessus
		this.setPosition(this.model.getRX(),
						 this.model.getRY());
		
		//this.setOrigin(w/2,h/2); // Origine pour avoir une bonne rotation

		this.setSize(w,h);
		this.addListener(new CharacterListener(this));
		
		this.initParts();
	}
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.model.act(delta);
	}
	
	/**
	 * Initialise le personnage en créant toutes les parties graphiques de celui ci
	 */
	private void initParts(){
		this.body = new ABody(this);
		this.rightHand = new AHand(this, AHand.HandSide.RIGHT);
		this.leftHand = new AHand(this, AHand.HandSide.LEFT);
		this.head = new AHead(this, bodyConfig.getHeadConfig().getHeadTileset());
		this.rightHandItem = new AItem(this, AHand.HandSide.RIGHT);
		this.leftHandItem = new AItem(this, AHand.HandSide.LEFT);
		this.addActor(this.body);
		this.addActor(this.head);
		this.addActor(this.rightHand);
		this.addActor(this.leftHand);
		this.addActor(this.rightHandItem);
		this.addActor(this.leftHandItem);
	}		
	
	public ABody getBody(){
		return this.body;
	}
	
	public AHand getLeftHand(){
		return this.leftHand;
	}
	
	public AHand getRightHand(){
		return this.rightHand;
	}
	
	public AItem getLeftHandItem() {
		return leftHandItem;
	}


	public AItem getRightHandItem() {
		return rightHandItem;
	}


	public AHead getHead(){
		return this.head;
	}
	
	public BodyConfig getBodyConfig() {
		return bodyConfig;
	}

	@Override
	public void draw(SpriteBatch batch, float delta){
		
		batch.end();
		Primitives.prepareRenderer(batch);
		if(DwarvesManager.debug) Primitives.rect((int)this.getX(), (int)this.getY(), (int)this.getWidth(), (int)this.getHeight(), Color.GREEN);
		Primitives.gauge((int)this.getX(), (int)(this.getY()+this.getHeight()),
				         (int)this.getWidth(), 8, 2,
				         ((float)((Dwarf)(this.model)).getNeeds().getSleep())/((float)((Dwarf)(this.model)).getNeeds().getMaxValue()),
				         Color.YELLOW, Color.BLACK);
		Primitives.gauge((int)this.getX(), (int)(this.getY()+this.getHeight()+8),
		         (int)this.getWidth(), 8, 2,
		         ((float)((Dwarf)(this.model)).getNeeds().getThirst())/((float)((Dwarf)(this.model)).getNeeds().getMaxValue()),
		         Color.CYAN, Color.BLACK);
		batch.begin();
		
		String name = this.model.getFirstName() + " " + this.model.getName();
		TextBounds txtbnd = DwarvesManager.font.getBounds(name);
		
		DwarvesManager.font.setColor(Color.RED);
		
		if(this.model instanceof Dwarf){
			if(((Dwarf)(this.model)).getState().contains(Dwarf.RESTING)){
				DwarvesManager.font.setColor(Color.RED);
			}
			else if(((Dwarf)(this.model)).getState().contains(Dwarf.WORKING)){
				DwarvesManager.font.setColor(Color.GREEN);
			}
		}
		
		DwarvesManager.font.draw(batch, name, this.getX()-txtbnd.width/2+this.getWidth()/2, this.getY()+5f/3f*this.getHeight());
		
		if(DwarvesManager.debug && this.model instanceof Miner){
			
			if(((Miner) this.model).getToMine() != null){
				batch.end();
				Primitives.prepareRenderer(batch);
				Primitives.line((int)(getX()+getWidth()/2), (int)(getY()+getHeight()/2),
						        (int)(((Miner)(this.model)).getToMine().x*this.model.getGame().getLevel().getTileXSize()+this.model.getGame().getLevel().getTileXSize()/2f),
						        (int)(((Miner)(this.model)).getToMine().y*this.model.getGame().getLevel().getTileYSize()+this.model.getGame().getLevel().getTileYSize()/2f)
						        , 1, Color.ORANGE);
				batch.begin();
			}
		}
		
		if(complaint >= 0){
			batch.draw(Loader.complaintAtlas.getTile(complaint),
					  getX()+getWidth(), getY()+getHeight(),
					  0, 0,
					  Loader.complaintAtlas.getTileWidth(),
					  Loader.complaintAtlas.getTileHeight(),
					  1,
					  1,
					  this.getRotation());
		}
		
		
		// Dessin des différentes parties du corps
		super.draw(batch, delta);
		
	}
	
	
	public Direction getDirection(){
		return this.direction;
	}
	
	
	/**
	 * Change la direction et réorganise l'ordre d'affichage des différentes parties
	 * du corps en conséquence.
	 * @param direction Nouvelle direction
	 */
	public void setDirection(Direction direction){
		
		if(this.direction == direction) return;
				
		this.direction = direction;
		this.clear();
		
		switch(direction){
			case BOTTOM:
				this.clear();
				this.addActor(this.body);
				this.addActor(this.head);
				this.addActor(this.rightHand);
				this.addActor(this.leftHand);
				this.addActor(this.rightHandItem);
				this.addActor(this.leftHandItem);
				break;
			case LEFT:
				this.clear();
				this.addActor(this.rightHandItem);
				this.addActor(this.body);
				this.addActor(this.head);
				this.addActor(this.leftHand);
				this.addActor(this.leftHandItem);
				break;
			case RIGHT:
				this.clear();
				this.addActor(this.leftHandItem);
				this.addActor(this.body);
				this.addActor(this.head);
				this.addActor(this.rightHand);
				this.addActor(this.rightHandItem);
				break;
			case TOP:
				this.clear();
				this.addActor(this.rightHandItem);
				this.addActor(this.leftHandItem);
				this.addActor(this.rightHand);
				this.addActor(this.leftHand);
				this.addActor(this.body);
				this.addActor(this.head);
				break;
			default:
				break;
		}
	}

	public MCharacter getModel() {
		return this.model;
	}


	public void setComplaint(int complaint) {
		this.complaint = complaint;
	}
	
	
	

}
