package enibdevlab.dwarves.controllers.actions;

import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;

/**
 * Action effectu�e par les nains
 * @author Cl�ment Perreau
 */
public abstract class DwarfAction extends GameAction {

	/**
	 * R�f�rence vers le nain acteur
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
	 * Abandon de l'action pour une raison quelconque qui l'a empech� de se terminer correctement
	 * (Provoque un retour dans un �tat neutre)
	 */
	public void abort(int complaint){
		// Mouvement de t�te comme pour se plaindre
		this.dwarf.getView().addAction(Animation.complaining(this.dwarf.getView(), complaint));
		this.state.clear();
		this.doWaiting(6f);
	}
	

}
