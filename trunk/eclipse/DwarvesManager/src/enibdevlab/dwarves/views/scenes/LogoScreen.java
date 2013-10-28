package enibdevlab.dwarves.views.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.actors.AImage;
import enibdevlab.dwarves.views.audio.SoundManager;


/**
 * 
 * Scène qui affiche un écran de présentation avec le logo
 * 
 * @author Clément
 *
 */
public class LogoScreen extends Stage {

	protected DwarvesManager game;
	protected boolean soundPlayed = false;
	protected float elapsedTime = 0f;
	protected float refTime = -1f;
	protected AImage logo;
	
	public LogoScreen(DwarvesManager game){
		super(DwarvesManager.getWidth(),DwarvesManager.getHeight(), false);
		this.game = game;
		init();
	}
	
	private void init(){
		
		// Titre
		TextureRegion title = new TextureRegion(new Texture("data/sprites/logo.png"));
		this.logo = new AImage(title, Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		this.addActor(logo);
		
		/* Ajout d'un mouvement pour le logo */
		
		float sizeLogo = (0.75f*Gdx.graphics.getWidth())/this.logo.getWidth();
		
		logo.addAction(Actions.scaleBy(.1f,.1f));
		logo.addAction(Actions.color(new Color(1f,1f,1f,0f)));
		logo.addAction(Actions.parallel(
								Actions.rotateBy(360f,4f),
					            Actions.scaleTo(sizeLogo,sizeLogo,4f),
					            Actions.color(new Color(1f,1f,1f,1f), 4f)
					            ));
		
		SoundManager.load("KhopaGamesTheme");
				            
	}
	
	@Override
	public void act() {
		super.act();
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		if(elapsedTime > .5f && !soundPlayed){
			SoundManager.play("KhopaGamesTheme");
			soundPlayed = true;
		}
		
		if (elapsedTime > 6f){
			if(refTime < 0 && this.logo.getActions().size == 0){
				refTime = elapsedTime;
			}
		}
		
		if(refTime>0 && elapsedTime > refTime+1f){
			game.setStage(new MainMenu(game));
		}
		
	}
	
}
