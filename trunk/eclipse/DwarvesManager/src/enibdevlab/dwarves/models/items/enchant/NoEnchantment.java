package enibdevlab.dwarves.models.items.enchant;

import enibdevlab.dwarves.models.items.Tool;

public class NoEnchantment implements IEnchantment {

	@Override
	public float onUse(Tool tool) {
		tool.setDamage(tool.getDamage()+1f/tool.getDurability());
		return 0;
	}

	@Override
	public float getPower(Tool tool) {
		return tool.getPowerBase();
	}

	@Override
	public float getDurability(Tool tool) {
		// TODO Auto-generated method stub
		return tool.getDurabilityBase();
	}

}
