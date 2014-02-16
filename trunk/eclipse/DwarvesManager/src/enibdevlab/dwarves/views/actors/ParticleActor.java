package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.khopa.skhopa.controllers.ConfigurationManager;

public class ParticleActor extends Actor {
	
	/**
	 * Effect
	 */
	private ParticleEffect effect;
	
	public ParticleActor(float x, float y, String particle){
		setPosition(x, y);
		if(ConfigurationManager.getInstance().getBooleanValue("particle")){
			effect = new ParticleEffect();
			effect.load(Gdx.files.internal("data/particles/"+particle+".p"),Gdx.files.internal("data/particles"));
			effect.start();
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(effect == null){
			this.remove();
			return;
		}
		effect.draw(batch);
		if (effect.isComplete()) {
            this.remove();
		}
	}
	
	public void act(float delta) {
	      super.act(delta);
	      if(effect == null){
			this.remove();
			return;
	      }
	      effect.setPosition(this.getX(), this.getY()); //set to whatever x/y you prefer
	      effect.update(delta); //update it
	  }

}
