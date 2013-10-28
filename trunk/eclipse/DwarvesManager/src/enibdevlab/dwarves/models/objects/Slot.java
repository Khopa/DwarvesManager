package enibdevlab.dwarves.models.objects;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.MCharacter;

/**
 * Partie d'un objet où un personnage peut prendre place
 * @author Clément Perreau
 */

public class Slot {
	
	/**
	 * Objet auquel appartient le slot
	 */
	protected GameObject gameObject;
	
	/**
	 * Position X du slot par rapport à l'objet
	 */
	protected int x;
	
	/**
	 * Position Y du slot par rapport à l'objet 
	 */
	protected int y;
	
	/**
	 * Type de personnage pouvant être accueilli
	 */
	protected Class<? extends MCharacter> characterType;
	
	/**
	 * Occupant du slot
	 */
	protected MCharacter occupant;
	
	public Slot(int x, int y, GameObject gameObject, Class<? extends MCharacter> characterType){
		this.x = x;
		this.y = y;
		this.gameObject = gameObject;
		this.characterType = characterType;
	}
	
	
	public boolean isOccupied(){
		return(occupant!=null);
	}


	public int getX() {
		
		if(gameObject.rotation == Rotation.ZERO){
			return x;
		}
		else if(gameObject.rotation == Rotation.ONE_QUARTER){
			return gameObject.getNominalYSize()-y-1;		
		}
		else if(gameObject.rotation == Rotation.TWO_QUARTER){
			return gameObject.getNominalXSize()-x-1;		
		}
		else if(gameObject.rotation == Rotation.THREE_QUARTER){
			return y;	
		}
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		if(gameObject.rotation == Rotation.ZERO){
			return y;
		}
		else if(gameObject.rotation == Rotation.ONE_QUARTER){
			return x;	
		}
		else if(gameObject.rotation == Rotation.TWO_QUARTER){
			return gameObject.getNominalYSize()-y-1;
		}
		else if(gameObject.rotation == Rotation.THREE_QUARTER){
			return gameObject.getNominalXSize()-x-1;
		}
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public MCharacter getOccupant() {
		return occupant;
	}

	public void setOccupant(MCharacter occupant) {
		occupant.setSlot(this);
		this.occupant = occupant;
	}
	
	/**
	 * Fait partir l'occupant
	 */
	public void occupantLeave(){
		if (occupant != null){
			this.occupant.leaveSlot();
			this.occupant = null;
		}
	}

	public Class<? extends MCharacter> getCharacterType() {
		return characterType;
	}


	public GameObject getGameObject() {
		return gameObject;
	}


	public Vector2 getRealPosition() {
		return this.gameObject.getPosition().cpy().add(this.getX(), this.getY());
	}
	
	

}
