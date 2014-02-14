package enibdevlab.dwarves.models.items.enchant;

import enibdevlab.dwarves.models.items.Tool;

/**
 * 
 * Interface qui implémente les effets des outils
 * 
 * @author Clément Perreau
 *
 */
public interface IEnchantment {

	public float onUse(Tool tool);

	public float getPower(Tool tool);

	public float getDurability(Tool tool);

	
}
