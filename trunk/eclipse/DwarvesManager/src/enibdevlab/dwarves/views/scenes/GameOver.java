package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.actors.AImage;
import enibdevlab.dwarves.views.audio.MusicManager;
import enibdevlab.dwarves.views.audio.SoundManager;



public class GameOver extends Stage {

	protected Game game;
	protected static TextureRegion img = new TextureRegion(new Texture("data/sprites/gameover.png"));
	protected float elapsed;
	protected float start;
	
	public GameOver(Game game){
		this.game = game;
		AImage aimg = new AImage(img, DwarvesManager.getWidth()/2,
									  DwarvesManager.getHeight()/2);
		
		
		MusicManager.stop();
		SoundManager.play("gameover");
		
		start = DwarvesManager.elapsedTime;
		elapsed = DwarvesManager.elapsedTime;
		
		aimg.setScale(0.1f);
		aimg.addAction(Actions.sequence(
				Actions.parallel(
						Actions.scaleTo(2f,2f,1f),
						Actions.rotateTo(360, 1f)
						),
				Actions.parallel(
						Actions.scaleTo(1f, 1f, .5f),
						Actions.rotateTo(375, .5f)
						),
				Actions.rotateTo(345, 1f),
				Actions.color(Color.RED, 2f)
				));
		this.addActor(aimg);
		
		this.addAction(Actions.color(new Color(0,0,0,0)));
		this.addAction(Actions.color(new Color(1,1,1,1), 2f));
		
	}
	
	@Override
	public void act() {
		super.act();
		elapsed = DwarvesManager.elapsedTime;
		if((elapsed-start)>=6){
			DwarvesManager.getInstance().setStage(new MainMenu(DwarvesManager.getInstance()));
		}
	}
	
	
	
}
