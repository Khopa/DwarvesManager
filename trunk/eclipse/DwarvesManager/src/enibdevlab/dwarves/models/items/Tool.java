package enibdevlab.dwarves.models.items;

import com.badlogic.gdx.math.Vector2;

public abstract class Tool extends Item {

	/**
	 * Etat de l'outil (Pour determiner si il a besoin de reparation)
	 * e [0,100]
	 * Quand les dommages sont à 100, l'outil est inutilisable et est envoyé à réparer
	 */
	protected float damage;
	
	/**
	 * Durabilité de l'outil, plus elle est elevée plus l'outil dure longtemps
	 * 
	 * Durabilité de 1.0 : 100 utilisations
	 *               2.0 : 200 utilisations ...etc
	 * 
	 */
	protected float durability;
	
	public Tool(Vector2 pos, float durability) {
		super(pos);
		this.damage = 0;
		this.durability = durability;
	}
	
	@Override
	public void onUse(){
		this.setDamage(getDamage()+1f/this.durability);
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
		if(this.damage > 100f) this.damage = 100f;
		else if(this.damage < 0f) this.damage = 0f;
	}
	
	public void repair(int speed){
		this.damage -= speed;
		if(this.damage < 0) this.damage = 0;
	}

}
