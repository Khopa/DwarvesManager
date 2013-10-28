package enibdevlab.dwarves.controllers.actions;

import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;

/**
 * Action effectuée par les nains
 * @author Clément Perreau
 */
public abstract class DwarfAction extends GameAction {

	/**
	 * Référence vers le nain acteur
	 */
	protected Dwarf dwarf;
	
	public DwarfAction(Game game, Dwarf dwarf){
		super(game);
		this.setActor(dwarf.getView());
		this.dwarf = dwarf;
	}

	public Dwarf getDwarf() {
		return dwarf;
	}
	
	/**
	 * Abandon de l'action pour une raison quelconque qui l'a empeché de se terminer correctement
	 * (Provoque un retour dans un état neutre)
	 */
	public void abort(int complaint){
		// Mouvement de tête comme pour se plaindre
		this.dwarf.getView().addAction(Animation.complaining(this.dwarf.getView(), complaint));
		this.state.clear();
		this.doWaiting(6f);
	}
	

}
