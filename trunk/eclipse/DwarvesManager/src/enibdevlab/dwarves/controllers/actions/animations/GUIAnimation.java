package enibdevlab.dwarves.controllers.actions.animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 
 * Animation factory for gui elements
 * 
 * @author Clément Perreau
 *
 */
public class GUIAnimation {
	
	public static final Color hoveredA = new Color(1f,1f, 1f,1f);
	public static final Color hoveredB = new Color(.8f,.8f,.8f,1f);
	
	/**
	 * Light Blinking animation
	 */
	public static Action hovered(){
		return Actions.forever(
			   Actions.sequence(
			   Actions.color(hoveredB, .3f),
			   Actions.color(hoveredA, .3f)
			   )
			   );
	}
	
	/**
	 * Light Blinking animation with alpha
	 */
	public static Action alphaHovered(){
		return Actions.forever(
				   Actions.sequence(
				   Actions.alpha(.5f, .2f),
				   Actions.alpha(1f, .2f)
				   )
				   );
	}

	/**
	 * Scaling animation (caption on title screen)
	 */
	public static Action scaling() {
		return Actions.forever(
				   Actions.sequence(
				   Actions.scaleTo(1.1f, 1.1f, .6f),
				   Actions.scaleTo(1f, 1f, .6f)
				   )
				   );
	}
	
	/**
	 * Scaling animation (caption on title screen)
	 */
	public static Action titleAnim() {
		return Actions.forever(
				   Actions.sequence(
						   Actions.color(hoveredB, .3f),
						   Actions.color(hoveredA, .3f)
				   )
				   );
	}
	
	

}
