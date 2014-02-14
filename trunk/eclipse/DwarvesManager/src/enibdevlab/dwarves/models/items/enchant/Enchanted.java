package enibdevlab.dwarves.models.items.enchant;

import enibdevlab.dwarves.models.items.Tool;

/**
 * Enchantement générique
 * @author Clément Perreau
 */
public class Enchanted implements IEnchantment {

	/**
	 * Facteur de puissance pour l'outil cible
	 */
	protected float powerFactor;
	
	/**
	 * Facteur de durabilité pour l'outil cible
	 */
	protected float durabilityFactor;
	
	/**
	 * Nombre d'utilisations
	 */
	protected float enchantmentDuration;
	
	
	/**
	 * Cree un enchantement classique 
	 * @param powerFactor Facteur de puissance pour l'outil cible
	 * @param durabilityFactor Facteur de durabilité pour l'outil cible
	 * @param enchantmentDuration Nombre d'utilisation de l'enchantement (-1 = infini)
	 */
	public Enchanted(float powerFactor, float durabilityFactor, float enchantmentDuration){
		this.powerFactor = powerFactor;
		this.durabilityFactor = durabilityFactor;
		this.enchantmentDuration = enchantmentDuration;
	}
	
	@Override
	public float onUse(Tool tool) {
		tool.setDamage(tool.getDamage()+1f/(tool.getDurability()*this.durabilityFactor));
		if(this.enchantmentDuration--==0){
			tool.setEnchantment(new NoEnchantment()); // fin de l'effet
		}
		return tool.getPower()*this.powerFactor;
	}

	@Override
	public float getPower(Tool tool) {
		return tool.getPowerBase()*powerFactor;
	}

	@Override
	public float getDurability(Tool tool) {
		return tool.getDurabilityBase()*durabilityFactor;
	}

}
