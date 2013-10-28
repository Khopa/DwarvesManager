package enibdevlab.dwarves.models;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.models.misc.VectorDistanceToPointComparator;
import enibdevlab.dwarves.models.world.MapArea;
import enibdevlab.dwarves.views.world.Tile;

public class TaskManager implements IPersistent{

	/**
	 * Liste des tiles � miner
	 */
	protected ArrayList<Vector2> toMine;
	
	/**
	 * Liste des tiles assign�s � des nains mineurs
	 */
	protected ArrayList<Vector2> assignedTile;
	
	/**
	 * Liste des tiles � miner qui sont accesibles th�oriquement (tile sans collision � cot�)
	 */
	protected ArrayList<Vector2> accesibleTile;
	
	/**
	 * R�f�rence vers le jeu
	 */
	protected Game game;
	
	public TaskManager(Game game){
		this.game = game;
		this.toMine = new ArrayList<Vector2>();
		this.assignedTile = new ArrayList<Vector2>();
		this.accesibleTile = new ArrayList<Vector2>();
	}
	
	public void addArea(MapArea mapArea){
		
		ArrayList<Vector2> tiles = mapArea.getTiles();
		boolean ok = false;
		
		for(Vector2 tile:tiles){
			ok = true;
		
			for(Vector2 toMine:this.toMine){
				if(tile.equals(toMine)){
					ok = false;
					break; // On ajoute pas les tiles 
				}
			}
			if(!ok) continue; // Tile d�j� pr�sent
			
			// On regarde si le tile existe
			Tile tmp = game.getLevel().getTilemap().getTile(0, (int)(tile.x), (int)(tile.y));
			if(tmp == null) continue;
			
			// On ajoute pas les tiles qui sont pas bloquants ou qui ne peuvent �tre min�
			if(!tmp.isBlocking() || !tmp.isMinable()) continue;
			
			toMine.add(tile);
			addInAccesibleTiles(tile);
		}
		
	}
	
	public void tileMined(Vector2 tile){
		this.assignedTile.remove(tile);
		this.accesibleTile.remove(tile);
		this.toMine.remove(tile);
		this.update();
	}
	
	/*
	 * Tente d'ajouter un tile dans la liste des tiles accesibles
	 */
	private void addInAccesibleTiles(Vector2 tile){
		
		Tile tmp = game.getLevel().getTilemap().getTile(0, (int)(tile.x), (int)(tile.y));
		
		Tile bottom = tmp.bottom();
		Tile top    = tmp.top();
		Tile left   = tmp.right();
		Tile right  = tmp.left();
		
		if(bottom == null || top == null || left == null || right == null){
			// Alors c'est pas accesible
		}
		else if(!bottom.isBlocking() || !top.isBlocking() || !left.isBlocking() || !right.isBlocking()){
			if(!accesibleTile.contains(tile)) accesibleTile.add(tile);
		}
	}
	
	/**
	 * Assignation d'un tile � un mineur
	 */
	public void assignTask(Miner miner){
		
		Collections.sort(accesibleTile, new VectorDistanceToPointComparator((int)miner.getX(), (int)miner.getY()));
		
		for(Vector2 tile:this.accesibleTile){
			if(!this.assignedTile.contains(tile)){
				miner.setToMine(tile);
				this.assignedTile.add(tile);
				return;
			}
		}
		miner.setToMine(null);
		return;
	}
	
	/**
	 * Assignation d'un tile diff�rent de celui donn� en param�tre au mineur
	 */
	public void assignTask(Miner miner, Vector2 notThisOne){
		
		Collections.sort(accesibleTile, new VectorDistanceToPointComparator((int)miner.getX(), (int)miner.getY()));
		
		for(Vector2 tile:this.accesibleTile){
			if(!this.assignedTile.contains(tile)){
				if(tile.equals(notThisOne)) continue;
				miner.setToMine(tile);
				this.assignedTile.add(tile);
				return;
			}
		}
		miner.setToMine(null);
		return;
	}
	
	

	public void removeArea(MapArea mapArea) {
		
		ArrayList<Vector2> tiles = mapArea.getTiles();
		
		for(Vector2 tile:tiles){
			toMine.remove(tile);
			accesibleTile.remove(tile);
			/**for(Miner miner:this.game.getCharacters().getMiner()){
				if(miner.getToMine().equals(tile)){
					miner.stopWorking();
					break;
				}
			}**/
			assignedTile.remove(tile);
		}
		
	}
	
	/**
	 * Les listes de tiles sont mises � jour
	 * On essaye de voir si des tiles sont devenus accesibles
	 */
	private void update(){
		for(Vector2 tile:this.toMine){
			if(!accesibleTile.contains(tile)){
				addInAccesibleTiles(tile);
			}
		}
	}
	
	
	public ArrayList<Vector2> getToMine() {
		return toMine;
	}

	public ArrayList<Vector2> getAssignedTile() {
		return assignedTile;
	}

	public ArrayList<Vector2> getAccesibleTile() {
		return accesibleTile;
	}
	
	public ArrayList<Vector2> getInaccesibleTile() {
		ArrayList<Vector2> output = new ArrayList<Vector2>();
		for(Vector2 tile: this.toMine){
			if(!accesibleTile.contains(tile)){
				output.add(tile);
			}
		}
		return output;
	}

	/**
	 * Un tile ne peut �tre min� par un mineur
	 */
	public void tileRefused(Vector2 toMine) {
		
		// On enl�ve le tile des tiles assign�s
		this.assignedTile.remove(toMine);
		
		// On le met � la fin de la liste des toMine et de la liste des accesibles pour r�duire la priorit�
		this.toMine.remove(toMine);
		this.toMine.add(toMine);
		
		if(this.accesibleTile.contains(toMine)){
			accesibleTile.remove(toMine);
			addInAccesibleTiles(toMine); // Permet de refaire un test d'accesibilit� au cas ou
		}
		
	}

	/**
	 * Un tile assign� est abandonn� car le nain qui devait le faire va en pause
	 */
	public void tileLeft(Vector2 toMine) {
		this.assignedTile.remove(toMine);
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("TaskManager", null);
		for(Vector2 tile:toMine){
			Element tileToMine = new Element("Tile", null);
			tileToMine.setAttribute("x", Float.toString(tile.x));
			tileToMine.setAttribute("y", Float.toString(tile.y));
			output.addChild(tileToMine);
		}
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		
		if(xmlElement == null) return;
		
		Array<Element> tiles = xmlElement.getChildrenByName("Tile");
		
		if(tiles == null) return;
		
		for(Element tileData:tiles){
			Vector2 tile = new Vector2(Float.parseFloat(tileData.getAttribute("x")),
									   Float.parseFloat(tileData.getAttribute("y")));
			this.toMine.add(tile);
			this.addInAccesibleTiles(tile);
		}
		
	}


}
