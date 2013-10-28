package enibdevlab.dwarves.models.items;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.lang.StringManager;

/**
 * Une bière 
 * 
 * @author Clément Perreau
 *
 */
public class Beer extends Item {
	
	/**
	 * Quantité restante dans la bière
	 */
	protected int quantity = 4;

	public Beer(Vector2 pos) {
		super(pos);
	}

	@Override
	public String getName() {
		return StringManager.getString("Beer");
	}

	@Override
	public int getTextureId() {
		return 8;
	}

	public boolean isEmpty() {
		return(quantity<=0);
	}
	
	public void onUse(){
		this.quantity --;
	}



}
