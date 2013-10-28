package enibdevlab.dwarves.models;

import com.badlogic.gdx.math.Vector2;

/**
 * Objet qui a une position dans le monde
 * @author Clément Perreau
 */
public abstract class Entity {

	/**
	 * Position de l'objet dans le monde
	 */
	protected Vector2 position;
	
	/**
	 * Crée une entité à la position donnée
	 * @param position Position de l'entité
	 */
	public Entity(Vector2 position){
		this.position = position;
	}
	
	/**
	 * Crée une entité à la position (0,0)
	 */
	public Entity(){
		this(new Vector2());
	}
	
	/* Acceseurs */
	
	public float getX(){
		return this.position.x;
	}
	
	public float getY(){
		return this.position.y;
	}
	
	public Vector2 getPosition(){
		return this.position;
	}
	
	/* Modifieurs */
	
	public void setPosition(float x, float y){
		this.position.set(x,y);
	}
	
	public void setPosition(int x, int y){
		this.position.set(x,y);
	}
	
	public void setPosition(Vector2 position){
		this.position.set(position);
	}
	
}