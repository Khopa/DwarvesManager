package enibdevlab.dwarves.controllers.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.controllers.actions.animations.Eyes;
import enibdevlab.dwarves.controllers.actions.animations.HandsUp;
import enibdevlab.dwarves.controllers.actions.animations.HeadUp;
import enibdevlab.dwarves.controllers.actions.animations.SetDirection;
import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.objects.Bed;
import enibdevlab.dwarves.models.objects.Slot;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

public class Sleeping extends DwarfAction {

	/**
	 * Lit dans lequel le nain dort
	 */
	protected Bed bed;
	
	/**
	 * Slot du lit
	 */
	protected Slot slot;
	
	/**
	 * Type de réveil
	 */
	protected boolean easyWakeUp = false;
	
	public Sleeping(Game game, Dwarf dwarf, Bed bed) {
		super(game, dwarf);
		this.bed = bed;
	}

	@Override
	public void entry() {
		
		if(bed.slotAvailable()){ // On recupère et on occupe l'unique slot du lit, échec sinon
			this.slot = bed.getAvailableSlot();
			this.slot.setOccupant(dwarf);
		}
		else{
			finished = true;
			return;
		}
		
		// Positionner le nain dans le sens du lit
		
		// Lancer l'animation de sommeil
		ACharacter view = dwarf.getView();
		view.setDirection(Direction.BOTTOM);
		
		view.addAction(Actions.sequence(
				Eyes.blinkAction(view, 0.2f),
				Eyes.blinkAction(view, 0.2f), // Cligne des yeux
				new Eyes(view, true) // ferme les yeux
				));
		
	}	

	@Override
	public void doAction(float delta) {
		
		// On vérifie si le joueur n'a pas supprimé le lit sur lequel le nain dort
		// Si c'est la cas, il se réveillera mécontent		
		if(!game.getObjects().getObjects().contains(bed) || bed == null){ 
			finished = true;
			easyWakeUp = false;
			return;
		}
		
		// Ajout d'energie au nain
		dwarf.getNeeds().setSleep((int) (dwarf.getNeeds().getSleep()-delta*1600)); 
		
		// Si le nain a fini de dormir, il se réveille tranquillement
		if(dwarf.getNeeds().hasMaxEnergy()){
			finished = true; // Le nain se lève
			easyWakeUp = true;
			return;
		}
		
	}

	@Override
	public void finish() {
		
		ACharacter view = dwarf.getView();
		
		// On retire le nain du slot
		if(slot!=null){
			slot.occupantLeave();
		}
		
		if(easyWakeUp){
			// Le nain se réveille tranquillement
			view.addAction(Actions.sequence(
					new SetDirection(view, Direction.BOTTOM), // Se retourne
					Actions.rotateTo(0, 1f),  // Se lève
					Eyes.blinkAction(view, 0.2f),
					Eyes.blinkAction(view, 0.2f), // Cligne des yeux
					new Eyes(view, false)
					));
		}
		else{
			// Le nain se réveille en colère
			view.addAction(Actions.sequence(
					new SetDirection(view, Direction.BOTTOM), // Se retourne
					Actions.rotateTo(0, 1f),  // Se lève
					Eyes.blinkAction(view, 0.2f),
					Eyes.blinkAction(view, 0.2f), // Cligne des yeux
					Actions.parallel(new HeadUp(this.dwarf.getView(), 10f, 25f,  1.9f),
					new HandsUp(this.dwarf.getView(), 25f, 25f,  1.9f)),
					new Eyes(view, false)));
		}
		
	}

}
