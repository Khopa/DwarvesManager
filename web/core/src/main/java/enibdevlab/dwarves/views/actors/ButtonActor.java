package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import enibdevlab.dwarves.controllers.listener.ButtonController;

public class ButtonActor extends SpriteActor {

	private ButtonController controller;
	
	public ButtonActor(TextureRegion region) {
		super(region);
		controller = new ButtonController();
		addListener(controller);
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		super.setScale(scaleX, scaleY);
		controller.setButtonScale(scaleX, scaleY);
	}
	
	@Override
	public void setScale(float scaleX) {
		setScale(scaleX, scaleX);
	}

}
