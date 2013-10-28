package enibdevlab.dwarves.models.objects;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.items.Item;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.models.items.Tool;
import enibdevlab.dwarves.views.actors.AGameObject;
import enibdevlab.dwarves.views.actors.gameObjects.ARack;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Ratelier ou l'on stocke les pioches
 * 
 * @author Clément Perreau
 *
 */
public class Rack extends ItemContainer {

	public Rack(Vector2 position) {
		super(position, Tool.class);
		this.capacity = 5;
	}

	@Override
	public int getTextureId() {
		return 9;
	}

	@Override
	public int getNominalXSize() {
		return 1;
	}

	@Override
	public int getNominalYSize() {
		return 1;
	}

	@Override
	public int getIconId() {
		return 1;
	}
	
	@Override
	public int getPrice() {
		return 200;
	}
	
	@Override
	public String getName() {
		return StringManager.getString("Rack");
	}
	
	
	public boolean hasItemToRepair(){
		for(Item item:this.getItems(Pickaxe.class)){
			Pickaxe pkx = (Pickaxe)item;
			if(pkx.getDamage()>0){
				return true;
			}
		}
		return false;
	}
	
	public Pickaxe getItemToRepair(){
		for(Item item:this.getItems(Pickaxe.class)){
			Pickaxe pkx = (Pickaxe)item;
			if(pkx.getDamage()>0){
				return pkx;
			}
		}
		return null;
	}
	
	/**
	 * Retourne une pioche neuve
	 */
	public Pickaxe getNewPickaxe(){
		Pickaxe output = null;
		for(Item item:this.getItems(Pickaxe.class)){
			output = (Pickaxe)item;
			if(output.getDamage()<=0){
				return output;
			}
		}
		return output;
	}
	
	@Override
	public Class<? extends AGameObject> getViewType() {
		return ARack.class; // On a besoin d'une vue spéciale car on doit afficher les pioches contenues
	}


	
	
	
	
	
	
	

}
