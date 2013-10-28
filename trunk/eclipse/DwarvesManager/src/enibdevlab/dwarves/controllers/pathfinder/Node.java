package enibdevlab.dwarves.controllers.pathfinder;

import com.badlogic.gdx.math.Vector2;

public class Node {
	
	/**
	 * cout en déplacement en distance manhattan
	 */
	protected int g;
	
	/**
	 * h vaut 10 si le déplacement est en ligne droite et 14 si c'est un déplacement en diagonale
	 */
	protected int h;
	
	/**
	 * position du node
	 */
	protected Vector2 pos;
	
	/**
	 * noeud parent qui nous à permis d'arriver à ce noeud
	 */
	protected Node parent;
	
	/**
	 * permet de savoir si le noeud est traversable
	 */
	protected boolean walkable;
	
	
	/**
	 * Créer un noeud
	 * @param x coordonnées en abscisse du noeud
	 * @param y coordonnées en ordonnée du noeud
	 */
	public Node(float x, float y) {
		this.pos = new Vector2(x,y);
	}
	
	
	
	public void setH(int h) {
		this.h = h;
	}

	public Vector2 getPos() {
		return pos;
	}



	public void setPos(Vector2 pos) {
		this.pos = pos;
	}



	public int getG() {
		return g;
	}



	public int getH() {
		return h;
	}



	public Node getParent() {
		return parent;
	}



	public boolean isWalkable() {
		return walkable;
	}

	public int getF(){
		
		return g+h;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	public void setG(int g) {
		this.g = g;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	

}
