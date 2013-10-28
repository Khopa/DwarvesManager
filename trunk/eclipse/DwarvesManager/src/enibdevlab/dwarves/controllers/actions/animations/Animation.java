package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

public class Animation {

	/**
	 * Animation ou le personnage se plaint
	 * Cette action dure environ 3.7 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction complaining(ACharacter character, int complaint){
		return Actions.sequence(
				new SetDirection(character, Direction.BOTTOM),
				new Look(character, Look.RIGHT, 0.4f),
				Actions.delay(0.2f),
				new Look(character, Look.LEFT, 0.4f),
				Actions.delay(0.2f),
				new Look(character, 0, 0.2f),
				new ComplaintAction(character, complaint),
				Actions.parallel(new HeadUp(character, 10f, 25f,  1.9f),
								 new HandsUp(character, 25f, 25f,  1.9f)),
				new ComplaintAction(character, -1),
				new Eyes(character, true),
				Eyes.blinkAction(character, 0.2f),
				Eyes.blinkAction(character, 0.2f),
				new Eyes(character, false));
	}
	
	/**
	 * Animation ou un personnage attend patiemment
	 * Durée d'environ 1.2 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction waiting(ACharacter character){
		return Actions.sequence(
				new SetDirection(character, Direction.BOTTOM),
				new Look(character, Look.RIGHT, 0.4f),
				Actions.delay(0.2f),
				new Look(character, Look.LEFT, 0.4f),
				Actions.delay(0.2f),
				new Look(character, 0, 0.2f),
				new HeadUp(character, 10f, 5f,1.9f));
	}
	
	/**
	 * Animation d'un barman qui plonge sa chope dans le fut pour remplir une bière
	 * Durée d'environ 1 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction replenishing(ACharacter character){
		return Actions.sequence(
				new SetDirection(character, Direction.BOTTOM),
				Actions.rotateTo(-2,.1f),
				Actions.rotateTo(0, .1f),
				Actions.rotateTo(2, .1f),
				Actions.rotateTo(0, .1f),
				new HandsUp(character, 4f, 5f,  .5f),
				new HandsShake(character, 25f, 7f, .5f, true));
	}
	
	/**
	 * Nain qui boit
	 * Durée d'environ 1 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction drinking(ACharacter character){
		return Actions.sequence(
				new SetDirection(character, Direction.BOTTOM),
				new HandsUp(character, 4f, 30f, .7f),
				new HeadUp(character, 4f, -2f, .3f));
	}
	
	/**
	 * Nain qui réclame sa bière au comptoir
	 * Durée d'environ 1 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction requestBeer(ACharacter character){
		return Actions.sequence(
				new ComplaintAction(character, Complaint.NO_BEER),
				new HandsUp(character, 27f, 40f, 1f),
				new ComplaintAction(character, Complaint.NONE));
	}

	
	/**
	 * Action de crafter ou réparer qqch à l'enclume
	 * Environ 3.5 secondes
	 * @param character Vue à animer
	 */
	public static SequenceAction crafting(ACharacter character){
		return Actions.sequence(
				new HandsUp(character, 4f, 30f, .7f),
				new HeadUp(character, 4f, -2f, .3f),
				new HandsShake(character, 25f, 15f, 1f, false),
				Actions.rotateTo(-2,.1f),
				Actions.rotateTo(0, .1f),
				Actions.rotateTo(2, .1f),
				Actions.rotateTo(0, .1f),
				new HandsShake(character, 20f, 11f, 1f, true)
				);
	}
	
	
	/**
	 * Action Disparition
	 */
	public static SequenceAction dissapear(){
		return Actions.sequence(
				Actions.moveBy(50, 50, .5f),
				Actions.parallel(
						Actions.moveBy(50, 50, .5f),
						Actions.fadeOut(.5f)),
				Actions.removeActor());	
	}
	
	
	
	
	
	
	
}
