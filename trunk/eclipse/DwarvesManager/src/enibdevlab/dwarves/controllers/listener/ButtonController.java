package enibdevlab.dwarves.controllers.listener;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import enibdevlab.dwarves.controllers.actions.animations.GUIAnimation;

/**
 * This listener implements animation for buttons
 * @author Clément Perreau
 */
public class ButtonController extends InputListener{
	
	/**
	 * Color when touched
	 */
	protected static final Color pressed = new Color(.8f,.5f,.5f,1f);
	
	/**
	 * Hovered Action
	 */
	protected Action hovered;
	
	/**
	 * To know if the mouse is still on it
	 */
	protected boolean onIt = false;
	
	
	protected float realScaleX = 1f;
	protected float realScaleY = 1f;
	
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		event.getListenerActor().removeAction(hovered);
		event.getListenerActor().setColor(new Color(.5f,1f,1f,1f));
		event.getListenerActor().setScaleX(realScaleX*.95f);
		event.getListenerActor().setScaleY(realScaleY*.95f);
		onIt = true;
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		event.getListenerActor().setScaleX(realScaleX);
		event.getListenerActor().setScaleY(realScaleY);
		for(Action action : event.getListenerActor().getActions()){
			event.getListenerActor().removeAction(action);
		}
		if(onIt) doAction();
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer,
			Actor fromActor) {
		this.hovered = GUIAnimation.hovered();
		event.getListenerActor().addAction(hovered);
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer,
			Actor toActor) {
		event.getListenerActor().setScaleX(realScaleX);
		event.getListenerActor().setScaleY(realScaleY);
		for(Action action : event.getListenerActor().getActions()){
			event.getListenerActor().removeAction(action);
		}
		event.getListenerActor().setColor(Color.WHITE);
		onIt= false;
	}
	
	/**
	 * To override to give the button a behaviour
	 */
	public void doAction(){
		
	}

	public boolean isOnIt() {
		return onIt;
	}

	public void setButtonScale(float scaleX, float scaleY) {
		realScaleX = scaleX;
		realScaleY = scaleY;
	}
	
}
