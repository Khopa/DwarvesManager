package enibdevlab.dwarves.controllers.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import enibdevlab.dwarves.views.world.TileMap;


/**
 * 
 * Classe qui permet de calculer les chemins qu'empruntent les personnages
 * 
 * @author Yannick Guern
 *
 */
public class Pathfinder implements Runnable{
	
	
	/**
	 * Mouvements diagonaux autorisés ou non
	 */
	private static boolean ALLOW_DIAGONALE_MOVEMENT= false;
	
	/**
	 * en cas de mouvement droit
	 */
	private static int STRAIGHT_MOVEMENT_COST  = 10;
	
	/**
	 * en cas de mouvement diagonal
	 */
	private static int DIAGONAL_MOVEMENT_COST = 14;
	
	/**
	 * noeud de départ
	 */
	protected Node beginNode;
	
	/**
	 * noeud d'arrivé
	 */
	protected Node goalNode;
	
	/**
	 * noeud courant
	 */
	protected Node current;
	
	/**
	 * Liste ouverte contient l'ensemble des solutions testables
	 */
	protected ArrayList<Node> openList = new ArrayList<Node>();
	
	/**
	 * liste des noeuds déterminés comme solution du chemin
	 */
	protected ArrayList<Node> closeList =  new ArrayList<Node>();
	
	/**
	 * liste des noeuds déterminés comme solution du chemin
	 */
	protected ArrayList<Node> path;
	
	/**
	 * tableau bidimensionnel contenant le monde
	 */
	protected TileMap map;
	
	
	/**
	 * 
	 * Crée un pathfinder
	 * 
	 * @param map Map sur lasquelle on souhaite effectuer la recherche
	 * @param begin Noeud de départ
	 * @param goal Noeud d'arrivée
	 */
	public Pathfinder(TileMap map, Node beginNode, Node goalNode) {
		this.beginNode = beginNode;
		this.goalNode = goalNode;
		this.map = map;
	}
	
	
	public ArrayList<Node> aStar()
	{
		//on ajoute le noeud de départ à liste ouverte
		openList.add(this.beginNode);
		
		while(!openList.isEmpty())
		{
			//on ajoute le noeud ayant le plus faible des F de la liste ouverte à la liste fermée
			
			current = findMinF();
			
			closeList.add(current);
			openList.remove(current);
			
			// On recherche les cases adjacentes au noeud courant et on calculs leurs attribut si besoin
			getNeighbours(current);
			
			// Si l'on rajoute le noeud d'arrivé ou si la liste ouverte est vide alors on arrête
			if(isOnCloseList(this.goalNode)) break;
			else if (this.openList.isEmpty()) return null;
		}
		
		//on reconstruit le chemin
		reconstruct();
		
		return path;
		
	}
	
	public void reconstruct() {
		
		this.path = new ArrayList<Node>();
		Node last = closeList.get(closeList.size()-1);
		
		while(last!=null){
			path.add(last);
			last = last.getParent();
		}
		
		Collections.reverse(path);
	}
	
	/**
	 * Recherche les 8 cases adjacentes de la case courante
	 * @param current case courante
	 * @return la liste des noeuds adjacents
	 */
	public void getNeighbours(Node current) {
		
		int xc = (int)current.pos.x;
		int yc = (int)current.pos.y;
		
		if(ALLOW_DIAGONALE_MOVEMENT)
		{
			seekNode(xc-1 , yc-1, true, current); //en haut à gauche
			seekNode(xc-1 , yc-1, true, current); //en haut à droite
			seekNode(xc-1 , yc+1, true, current); //en bas à gauche
			seekNode(xc+1 , yc+1, true, current); //en bas à droite
		}
		
		seekNode(xc , yc-1, false, current); //en haut
		seekNode(xc-1 , yc, false, current); //à gauche
		seekNode(xc+1 , yc, false, current); //à droite
		seekNode(xc , yc+1, false, current); //en bas

	}
	
	
	/**
	 * Vérifie si le noeud étudier est dans les limites de la map
	 * @param node
	 * @return retourne vrai si c'est le cas
	 */
	public boolean outOfBound(Node node){
		
		if (node.pos.x < 0 || node.pos.x >map.getWidth() || node.pos.y<0 || node.pos.y>map.getHeight()) return false;
		
		return true;
	}
	
	/**
	 * Ajoute à liste ouverte s'il possède les bonnes caractéristiques, s'il est : 
	 * -dans la map
	 * -traversable
	 * -non contenu dans la liste dans la liste ouverte
	 * -non contenue dans la liste femée
	 * alors il calcule son H
	 * @param dx coordonnées en x du noeud testé
	 * @param dy coordonnées en y du noeud testé
	 * @param diag indique si c'est un mouvement en diagonale ou non
	 * @param parent noeud courant
	 */
	public void seekNode (int dx, int dy, boolean diag, Node parent)
	{
		Node node = new Node(dx, dy);
		
		//si on est hors des limites ou si le noeud est déjà dans la solution ou s'il nest pas praticable
		if (!(isOnCloseList(node) || map.isTileBlocking(dx, dy))){
			
			// si il n'est pas dans la liste ouverte (pas dans les solutions possible)
			if(!isOnOpenList(node)){
				
				if(diag) node.setG(Pathfinder.DIAGONAL_MOVEMENT_COST + parent.g);
				else node.setG(Pathfinder.STRAIGHT_MOVEMENT_COST + parent.g);
		
				node.setParent(parent);
				node.setH(manhattanDistance(node, this.goalNode));
				
				openList.add(node);
			}
			//S'il est déjà dans la liste ouverte
			else
			{
				//on recalcule son G
				
				int newG;
				node = getList(openList, dx, dy);
				
				if(diag){ 
					newG = Pathfinder.DIAGONAL_MOVEMENT_COST + parent.g;
				}
				else{
					newG = Pathfinder.STRAIGHT_MOVEMENT_COST + parent.g;
				}
				
				if (newG < node.getG())
				{
					node.setParent(parent);
					node.setG(newG);
				}
				
			}
				
		}
	}
	
	/**
	 * Vérifie si le noeud est dans la liste fermée
	 * @param probed noeud considéré
	 * @return
	 */
	public boolean isOnCloseList(Node probed){
		
		Iterator<Node> it =  closeList.iterator();
		
		while(it.hasNext())
		{
			Node node = it.next();
			
			if(node.pos.x == probed.pos.x && node.pos.y == probed.pos.y)
				return true;
		}
		
		return false;
	}
	
	public Node getList(ArrayList<Node> list, int x, int y){
		for(Node n:list){
			if(n.pos.x == x && n.pos.y == y){
				return n;
			}
		}
		return null;
	}

	/**
	 * Vérifie si le noeud est dans la liste ouvert
	 * @param probed noeud considéré
	 */
	public boolean isOnOpenList(Node probed){
		
		Iterator<Node> it =  openList.iterator();
		
		while(it.hasNext())
		{
			Node node = it.next();
			
			if(node.pos.x == probed.pos.x && node.pos.y == probed.pos.y)
				return true;
		}
		
		return false;
	}
	
	public int manhattanDistance(Node probed, Node goal)
	{
		return (int) (Pathfinder.STRAIGHT_MOVEMENT_COST*(Math.abs(probed.pos.x-goal.pos.x) + Math.abs(probed.pos.y-goal.pos.y)));
	}
	
	
	/**
	 * Renvoie le noeud qui à le plus faible F de la liste ouverte
	 * @return
	 */
	public Node findMinF()
	{
		Iterator<Node> it =  openList.iterator();
		
		Node minF = openList.get(0);
		
		while(it.hasNext())
		{
			Node node = it.next();
			
			if(node.getF() < minF.getF()) minF = node;
			
		}
		
		return minF;
	}

	@Override
	public void run() {
		this.aStar();
	}
	
	public ArrayList<Node> getPath(){
		return this.path;
	}


}
