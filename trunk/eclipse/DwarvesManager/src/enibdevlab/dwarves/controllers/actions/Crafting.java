package enibdevlab.dwarves.controllers.actions;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.controllers.pathfinder.Node;
import enibdevlab.dwarves.controllers.pathfinder.PathUtils;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Craftman;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.models.objects.Anvil;
import enibdevlab.dwarves.models.objects.GameObject;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.models.rooms.Workshop;
import enibdevlab.dwarves.views.audio.SoundManager;

/**
 * 
 * Action de reparer ou creer des objets
 * 
 * @author Clément Perreau
 *
 */
public class Crafting extends DwarfAction {
	
	
	/**
	 * Rapidité
	 */
	private static int SPEED = 20;
	
	/**
	 * Atelier ou on travaille
	 */
	protected Workshop workshop;
	
	/**
	 * Enclume sur laquelle on travaille
	 */
	protected Anvil anvil;
	
	/**
	 * Sous action de mouvement
	 */
	protected Movement movement;
	
	/**
	 * Référence vers un rack
	 */
	protected Rack rack;
	
	/**
	 * Position gardée en mcémoire
	 */
	protected Vector2 tile;
	
	/**
	 * Pourcentage de création de l'item en cours de création
	 */
	protected int craftPercent = 0;
	
	/**
	 * Chemin gardé en mémoire vers le rack vu qu'on fait plein d'aller retour
	 */
	protected ArrayList<Node> path;
	
	private int REPAIRING = 0;
	private int CRAFTING = 1;
	
	private int GOINGTORACK = 2;
	private int GOINGBACKTOANVIL = 3;
	private int WORKING = 4;
	private int TAKINGSTUFFTORACK = 5;	
	private int GOINGBACKTOANVIL2 = 6;
	
	public Crafting(Game game, Dwarf dwarf, Workshop workshop, Anvil anvil) {
		super(game, dwarf);
		this.workshop = workshop;
		this.anvil = anvil;
	}

	@Override
	public void entry() {
		
		state.clear();
		
		/*
		 * Trouver un rack qui a des pioches à réparer
		 */
		ArrayList<GameObject> racks = PathUtils.getSortedUsableObjectInRoom(Rack.class, Craftman.class, workshop, dwarf.getPosition(), game);
		
		Rack rck; 
		
		if(racks == null){
			abort(Complaint.TROLL);
			return;
		}
		
		for(GameObject obj: racks){
			rck = (Rack)obj;
			if(rck.hasItemToRepair()){
				rack = rck;
				path = PathUtils.pathToAdjacentTile(game, dwarf, rck.getPosition());
				if(path != null){
					this.state.add(REPAIRING);
					this.state.add(GOINGTORACK);
					this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
					this.movement = new Movement(game, dwarf, path);
					return;
				}
			}
		}
		
		/*
		 * Si on ne trouve pas de rack avec des pioches à réparer, alors on fabrique des pioches (à condition qu'il reste de la place pour les stocker)
		 */
		for(GameObject obj: racks){
			rck = (Rack)obj;
			if(!rck.isFull()){
				rack = rck;
				path = PathUtils.pathToAdjacentTile(game, dwarf, rck.getPosition());
				if(path != null){
					this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
					this.state.add(CRAFTING);
					this.state.add(WORKING);
					craftPercent = 0;
					this.dwarf.setRightHandItem(new Pickaxe(new Vector2()));
					return;
				}
			}
		}
		
		abort(Complaint.NO_PICKAXE);
		return;
		
	}

	@Override
	public void doAction(float delta) {
		
		if(this.movement != null){
			movement.act(delta);
			if(movement.isFinished()) movement = null;
		}
		
		if(state.contains(REPAIRING)){
			if(state.contains(GOINGTORACK)){
				if(dwarf.getPosition().equals(tile)){
					takeItemToRepair();
				}
			}
			else if(state.contains(WORKING)){
				repair();
			}
			else if(state.contains(GOINGBACKTOANVIL)){
				if(dwarf.getPosition().equals(anvil.getSlots().get(0).getRealPosition())){
					this.state.remove(GOINGBACKTOANVIL);
					this.state.add(WORKING);
				}
			}
			else if(state.contains(TAKINGSTUFFTORACK)){
				if(dwarf.getPosition().equals(tile)){
					takeBackRepairedPickaxe();
				}
			}
			else if(state.contains(GOINGBACKTOANVIL2)){
				if(dwarf.getPosition().equals(anvil.getSlots().get(0).getRealPosition())){
					finished = true;
				}
			}
		}
		else if(state.contains(CRAFTING)){
			
			if(state.contains(WORKING)){
				if(!dwarf.getPosition().equals(anvil.getSlots().get(0).getRealPosition()) && movement == null){
					movement = new Movement(game, dwarf, PathUtils.pathToSlot(dwarf, anvil.getSlots().get(0), game));
				}
				else if(dwarf.getPosition().equals(anvil.getSlots().get(0).getRealPosition())){
					craft();
				}
			}
			if(state.contains(TAKINGSTUFFTORACK)){
				if(dwarf.getPosition().equals(tile)){
					takeBackRepairedPickaxe();
				}
			}
			else if(state.contains(GOINGBACKTOANVIL2)){
				if(dwarf.getPosition().equals(anvil.getSlots().get(0).getRealPosition())){
					finished = true;
				}
			}
		}
		else{
			entry();
		}
		
		
	}

	private void takeBackRepairedPickaxe() {
		
		if(dwarf.getRightHandItem() == null){
			abort(Complaint.NO_PICKAXE);
			return;
		}
		else if(!(dwarf.getRightHandItem() instanceof Pickaxe)){
			abort(Complaint.NO_PICKAXE);
			return;
		}
		
		if(!rack.isFull()){
			rack.addItem(dwarf.getRightHandItem());
			dwarf.setRightHandItem(null);
			state.remove(TAKINGSTUFFTORACK);
			state.add(GOINGBACKTOANVIL2);
			path = PathUtils.pathToSlot(dwarf, dwarf.getSlot(), game);
			movement = new Movement(game, dwarf, path);
		}
		else{
			if(!goToNotFullRack()){
				abort(Complaint.NO_PICKAXE);
				return;
			}
		}
	}

	/**
	 * Action de réparer un objet
	 */
	private void repair() {
		
		if(dwarf.getRightHandItem() == null){
			abort(Complaint.NO_PICKAXE);
			return;
		}
		else if(!(dwarf.getRightHandItem() instanceof Pickaxe)){
			abort(Complaint.NO_PICKAXE);
			return;
		}
		
		Pickaxe toRepair = (Pickaxe)dwarf.getRightHandItem();
		
		if(toRepair.getDamage() > 0){
			toRepair.repair(SPEED);
			this.dwarf.getView().addAction(Animation.crafting(this.dwarf.getView()));
			this.doWaiting(3.7f);
			SoundManager.play("anvil"+Integer.toString(DwarvesManager.random.nextInt(2)));
		}
		else{
			this.state.remove(WORKING);
			state.add(TAKINGSTUFFTORACK);
			
			if(!rack.isFull()){
				// Retour vers le rack
				Collections.reverse(path);
				movement = new Movement(game, dwarf, path);
				return;
			}
			else{
				// alors il faut trouver un nouveau rack pour stocker la pioche
				if(goToNotFullRack()) return;
			}
			
			abort(Complaint.NO_PICKAXE);
			return;
			
		}
	}

	/**
	 * Prendre une pioche à réparer
	 */
	private void takeItemToRepair() {
		
		if(rack == null || !game.getObjects().getObjects().contains(rack)){
			abort(Complaint.TROLL);
			return;
		}
		
		Pickaxe toRepair = rack.getItemToRepair();
		
		if(toRepair != null){
			dwarf.setRightHandItem(toRepair);
			state.remove(GOINGTORACK);
			state.add(GOINGBACKTOANVIL);
			Collections.reverse(path);
			movement = new Movement(game, dwarf, path);
		}
		else{
			abort(Complaint.NO_PICKAXE);
			return;
		}
		
	}
	
	/**
	 * Crée un mouvement vers un rack vide
	 */
	private boolean goToNotFullRack(){
		ArrayList<GameObject> racks = PathUtils.getSortedUsableObjectInRoom(Rack.class, Craftman.class, workshop, dwarf.getPosition(), game);
		
		Rack rck;
		
		if(racks == null) return false;
		
		for(GameObject obj: racks){
			rck = (Rack)obj;
			if(!rck.isFull()){
				rack = rck;
				path = PathUtils.pathToAdjacentTile(game, dwarf, rck.getPosition());
				if(path != null){
					this.tile = new Vector2(path.get(path.size()-1).getPos().x, path.get(path.size()-1).getPos().y);
					this.movement = new Movement(game, dwarf, path);
					return true;
				}
			}
		}
		return false;
	}
	
	
	/*
	 * Craft d'une nouvelle pioche
	 */
	private void craft() {
		
		if(craftPercent < 100){
			craftPercent += SPEED;
			this.dwarf.getView().addAction(Animation.crafting(this.dwarf.getView()));
			this.doWaiting(3.7f);
			SoundManager.play("anvil"+Integer.toString(DwarvesManager.random.nextInt(2)));
		}
		else{
			if(goToNotFullRack()){
				this.state.remove(WORKING);
				this.state.add(TAKINGSTUFFTORACK);
			}
			else{
				abort(Complaint.NO_PICKAXE);
			}
		}
			
	}

	@Override
	public void finish() {
		this.dwarf.setRightHandItem(null);
	}

}
