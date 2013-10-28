package enibdevlab.dwarves.models.objects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.items.Item;


/**
 * 
 * Objet qui contient des items
 * 
 * @author Clément Perreau
 *
 */
public abstract class ItemContainer extends GameObject {

	/**
	 * Liste d'items
	 */
	private ArrayList<Item> items;
	
	/**
	 * Capacité max
	 */
	protected int capacity = 8;
	
	/**
	 * Type d'objet contenable
	 */
	protected Class<? extends Item> itemType;
	
	public ItemContainer(Vector2 position, Class<? extends Item> itemType) {
		super(position);
		this.items = new ArrayList<Item>();
		this.itemType = itemType;
	}
	
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public ArrayList<Item> getItems(Class<? extends Item> itemType){
		ArrayList<Item> output = new ArrayList<Item>();
		for(Item item:items){
			if(item.getClass() == itemType) output.add(item);
		}
		return output;
	}
	
	public void addItem(Item toAdd){
		if(itemType.isAssignableFrom(toAdd.getClass()) && !isFull()){
			this.items.add(toAdd);
		}
	}
	
	public boolean isFull(){
		return(this.items.size() >= this.capacity);
	}


	public int getCapacity() {
		return capacity;
	}
	
}
