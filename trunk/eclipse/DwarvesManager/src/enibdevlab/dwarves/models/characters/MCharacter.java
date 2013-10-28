package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.Entity;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.items.Item;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Modele de base des personnages du jeu
 * 
 * @author Clément Perreau
 *
 */
public abstract class MCharacter extends Entity implements ICharacter, IPersistent{
	
	/**
	 * Nom du personnage
	 */
	protected String name;
	
	/**
	 * Prénom du personnage
	 */
	protected String firstName;
	
	/**
	 * Vue du personnage
	 */
	protected ACharacter view;
	
	/**
	 * Slot d'objet sur lequel le personnage peut être positionné
	 */
	protected Slot slot;
	
	/**
	 * Objet tenu en main droite
	 */
	protected Item rightHandItem;
	
	/**
	 * Objet tenu en main gauche
	 */
	protected Item leftHandItem;
	
	/**
	 * Référence vers le jeu
	 */
	protected Game game;
	
	/**
	 * Prochain tile sur lequel le perso va se déplacer (Pour éviter que les personnages se superposent
	 * lorsque l'on résout les mouvements 
	 */
	protected Vector2 nextTile;

	/**
	 * Constructeur
	 * @param position Position
	 */
	public MCharacter(Vector2 position){
		this(position, "Unnammed", "Unnammed");
	}
	
	public void act(float delta){
		
	}
	
	/**
	 * Constructeur
	 * @param position Position
	 * @param name Nom
	 * @param firstName Prénom
	 */
	public MCharacter(Vector2 position, String name, String firstName){
		super(position);
		this.name = name;
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public ACharacter getView(){
		return view;
	}

	public void setView(ACharacter view) {
		this.view = view;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	
	/**
	 * Quitte le slot sur lequel il est positionné
	 */
	public void leaveSlot(){
		this.slot = null;
	}

	/**
	 * Retourne la position X en pixels
	 */
	public int getRX() {
		return (int) (this.getX()*this.game.getLevel().getTileXSize());
	}
	
	/*
	 * Retourne la position Y en pixels
	 */
	public int getRY() {
		return (int) (this.getY()*this.game.getLevel().getTileYSize());
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Vector2 getNextTile() {
		return nextTile;
	}

	public void setNextTile(Vector2 nextTile) {
		this.nextTile = nextTile;
	}

	public Item getRightHandItem() {
		return rightHandItem;
	}

	public void setRightHandItem(Item rightHandItem) {
		this.rightHandItem = rightHandItem;
	}

	public Item getLeftHandItem() {
		return leftHandItem;
	}

	public void setLeftHandItem(Item leftHandItem) {
		this.leftHandItem = leftHandItem;
	}


	
	
}
