package enibdevlab.dwarves.controllers.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Dwarf;

public class ManageNeeds extends DwarfAction {

	// Etats
	private static int MANAGING_SLEEP = 0;
	private static int MANAGING_THIRST = 1;
	
	/**
	 * Action MANAGING_THIRTSen cours
	 */
	protected DwarfAction currentAction;
		
	public ManageNeeds(Game game, Dwarf dwarf) {
		super(game, dwarf);
	}

	@Override
	public void entry() {
		state.clear();
		
		if(this.dwarf.getNeeds().getThirst() >= this.dwarf.getNeeds().getSleep()){
			currentAction = new GoDrinking(game, dwarf);
			currentAction.setActor(dwarf.getView());
			state.add(MANAGING_THIRST);
		}
		else{
			currentAction = new GoSleeping(game, dwarf);
			currentAction.setActor(dwarf.getView());
			state.add(MANAGING_SLEEP);
		}
	}

	@Override
	public void doAction(float delta) {
	
		if(currentAction != null){
			currentAction.act(delta);
			if(currentAction.finished){
				currentAction = null;
				state.clear();
				// Si les besoins sont satisfaits, on arrête tout
				if(dwarf.getNeeds().isSatisfied()){
					finished = true;
					return;
				}
			}
		}
		
		dwarf.getNeeds().update();
		
		if(state.isEmpty()){
			entry();
		}
		
	}

	@Override
	public void finish() {
		// Le nain va retourner au travail
		// Animation : saut de mise en forme
		dwarf.getView().addAction(Actions.sequence(
				Actions.moveBy(0, 15, .1f),
				Actions.moveBy(0, -15, .1f)));
	}

}
