package enibdevlab.dwarves.models.characters;

import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.controllers.actions.DwarfAction;
import enibdevlab.dwarves.controllers.actions.ManageNeeds;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.views.Loader;

/**
 * 
 * Classe de base des nains
 * 
 * @author Clément Perreau
 *
 */
public abstract class Dwarf extends MCharacter {
	
	
	/**
	 * Besoins du nain
	 */
	protected Needs needs;
	
	/**
	 * Etat
	 */
	protected HashSet<Integer> state = new HashSet<Integer>();
	
	/**
	 * Actions
	 */
	protected DwarfAction dwarfAction; 
	
	public static int RESTING = 0;
	public static int WORKING = 1;
	
	/**
	 * Constructeur
	 * @param position Position du personnage
	 */
	public Dwarf(Vector2 position) {
		super(position);
		this.needs = new Needs(this);
		this.setFirstName("Nain");
		this.setName(Loader.randomName());
	}
	
	public abstract DwarfAction getWorkingAction();

	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		
		if (!this.getNeeds().isAbleToWork() && this.state.contains(WORKING)){
			dwarfAction.finish();
			dwarfAction = new ManageNeeds(game, this);
			state.clear();
			state.add(RESTING);
		}
		
		if(dwarfAction == null || dwarfAction.isFinished()){
			
			// Choix de la prochaine action
			if(!this.getNeeds().isSatisfied()){
				dwarfAction = new ManageNeeds(game, this);
				state.clear();
				state.add(RESTING);
			}
			else{
				dwarfAction = this.getWorkingAction();
				state.clear();
				state.add(WORKING);
			}
		}
		else{
			dwarfAction.act(delta);
		}
		
		needs.update();
	}


	public Needs getNeeds() {
		return this.needs;
	}


	public HashSet<Integer> getState() {
		return this.state;
	}
	
	/**
	 * Dit si le nain porte une pioche ou non
	 */
	public boolean isCarryingPickaxe(){
		// On vérifie qu'on a une pioche
		if(this.getRightHandItem() != null){
			if(this.getRightHandItem() instanceof Pickaxe){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Element saveAsXmlElement() {
		Element output = new Element(this.getClass().getName(), null);
		
		Element position = new Element("position", null);
		position.setAttribute("x", Float.toString(this.position.x));
		position.setAttribute("y", Float.toString(this.position.y));
		
		int i = 0;
		Element items = new Element("items", null);
		if(getRightHandItem()!=null){
			items.addChild(this.getRightHandItem().saveAsXmlElement());
			items.getChild(i).setAttribute("side", "right");
			i++;
		}
		if(getLeftHandItem()!=null){
			items.addChild(this.getLeftHandItem().saveAsXmlElement());
			items.getChild(i).setAttribute("side", "left");
		}
			
		output.addChild(position);
		output.addChild(items);
		output.addChild(needs.saveAsXmlElement());
		
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		
	}
	
	
	
}
