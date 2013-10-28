package enibdevlab.dwarves.models.objects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.Barman;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Comptoir où le barman sert les bières
 * 
 * @author Clément Perreau
 *
 */
public class Counter extends GameObject {
	
	/**
	 * Barman qui y sert
	 */
	protected Barman barman;
	
	/**
	 * Quantité de bière posée sur le comptoir
	 */
	protected int beer = 0;

	public Counter(Vector2 position) {
		super(position);
	}
	
	/**
	 * Un barman indique qu'il va utiliser ce comptoir pour sa période de travail
	 * @param barman Barman qui prend le bar
	 */
	public void reserve(Barman barman){
		this.barman = barman;
	}
	
	/**
	 * Retourne le slot reservé au barman
	 */
	public Slot getBarmanSlot(){
		for(Slot slot:this.slots){
			if(slot.getCharacterType() == Barman.class) return slot; 
		}
		return null;
	}
	
	/**
	 * 
	 */
	public void liberate(){
		this.barman = null;
	}
	
	/**
	 * Est utilisé ou non par un barman
	 */
	public boolean isUsed(){
		return (barman != null);
	}
	

	@Override
	public int getTextureId() {
		return 32;
	}

	@Override
	public int getNominalXSize() {
		return 3;
	}

	@Override
	public int getNominalYSize() {
		return 2;
	}

	@Override
	public int getIconId() {
		return 10;
	}
	
	@Override
	public int getPrice() {
		return 300;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Counter");
	}
	
	
	@Override
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition(){
		HashMap<Vector2, Class<? extends MCharacter>> slotsData = new HashMap<Vector2, Class<? extends MCharacter>>();
		
		// Slot du barman
		slotsData.put(new Vector2(1,1), Barman.class);
		
		// Slot de position des personnages qui réclament à boire
		slotsData.put(new Vector2(0,0), MCharacter.class);
		slotsData.put(new Vector2(1,0), MCharacter.class);
		slotsData.put(new Vector2(2,0), MCharacter.class);
		
		return slotsData;
	}

	public void addBeer() {
		this.beer ++;
		if(this.beer>1){
			this.beer = 1;
		}
	}
	
	public void takeBeer() {
		this.beer --;
	}
	
	public void resetBeer() {
		this.beer = 0;
	}
	
	public int getBeer() {
		return this.beer;
	}


}
