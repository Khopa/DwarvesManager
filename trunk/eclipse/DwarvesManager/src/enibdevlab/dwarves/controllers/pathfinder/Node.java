package enibdevlab.dwarves.controllers.pathfinder;

import com.badlogic.gdx.math.Vector2;

public class Node {
	
	/**
	 * cout en d�placement en distance manhattan
	 */
	protected int g;
	
	/**
	 * h vaut 10 si le d�placement est en ligne droite et 14 si c'est un d�placement en diagonale
	 */
	protected int h;
	
	/**
	 * position du node
	 */
	protected Vector2 pos;
	
	/**
	 * noeud parent qui nous � permis d'arriver � ce noeud
	 */
	protected Node parent;
	
	/**
	 * permet de savoir si le noeud est traversable
	 */
	protected boolean walkable;
	
	
	/**
	 * Cr�er un noeud
	 * @param x coordonn�es en abscisse du noeud
	 * @param y coordonn�es en ordonn�e du noeud
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
