package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.audio.SoundManager;

public class ADiamondEffect extends Actor {
	
	protected static TextureRegion img = new TextureRegion(new Texture("data/sprites/diamond.png"));
	
	public ADiamondEffect(){
		super();
		setWidth(img.getRegionWidth());
		setHeight(img.getRegionHeight());
		setPosition(DwarvesManager.getWidth()/2-getWidth()/2, DwarvesManager.getHeight()/2-getHeight()/2);
		setScale(.1f);
		SoundManager.play("success");
		
		this.addAction(Actions.sequence(
				Actions.parallel(
						Actions.scaleTo(1f,1f,0.5f),
						Actions.rotateBy(1080, 1f)
				),
				Actions.rotateTo(1065, .5f),
				Actions.rotateTo(1095, 1f),
				Actions.parallel(
						Actions.scaleTo(.1f,.1f,0.5f),
						Actions.fadeOut(.5f),
						Actions.rotateTo(1080, 1f)
				),
				Actions.removeActor()
				));

	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//super.draw(batch, parentAlpha);
		batch.setColor(1f,1f,1f, getColor().a);
		batch.draw(img, getX(), getY(), getWidth()/2, getHeight()/2, getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());	
	}
	
	

}
