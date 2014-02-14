package enibdevlab.dwarves.controllers.actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.items.Item;
import enibdevlab.dwarves.models.items.Tool;
import enibdevlab.dwarves.models.items.enchant.Enchanted;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.views.actors.ParticleActor;

public class Enchanting extends DwarfAction {

	protected static float DURATION = 7f;
	
	/**
	 * Ratelier à enchanter
	 */
	protected Rack rack;
	
	/**
	 * Progression en temps
	 */
	protected float progression = 0;
	
	
	/**
	 * 
	 * @param game
	 * @param dwarf
	 * @param target
	 */
	
	public Enchanting(Game game, Dwarf dwarf, Rack target) {
		super(game, dwarf);
		this.rack = target;
	}

	@Override
	public void entry() {
		// Planifie l'animation
		dwarf.getView().addAction(Animation.enchant(dwarf.getView(), DURATION));
	}

	@Override
	public void doAction(float delta) {
		progression+= delta;
		if(progression > DURATION){
			finished = true;
		}
	}

	@Override
	public void finish() {
		for(Item item:rack.getItems()){
			Tool tool = (Tool) item;
			if(!tool.isEnchanted()){
				tool.setEnchantment(new Enchanted(8.0f, 2.0f, -1));
				dwarf.getNeeds().addSleep(DwarvesManager.random.nextInt(5000)+5000);
				dwarf.getNeeds().addThirst(2000);
				break; // on enchante qu'une pioche par animation
			}
		}
		// Effets de particules pour la hype
		int tw = Game.getInstance().getView().getGameplayLayer().getTilemap().getTileWidth();
		int th = Game.getInstance().getView().getGameplayLayer().getTilemap().getTileHeight();
		Game.getInstance().getView().getGameplayLayer().addActor(new ParticleActor(rack.getX()*tw+tw/2f, rack.getY()*th+th/2f, "enchant"));
	}

}
