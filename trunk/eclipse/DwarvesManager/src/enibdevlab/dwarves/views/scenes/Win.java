package enibdevlab.dwarves.views.scenes;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.khopa.skhopa.controllers.ScoreManager;
import com.khopa.skhopa.models.Score;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.controllers.script.LevelFile;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.views.actors.AImage;
import enibdevlab.dwarves.views.audio.MusicManager;
import enibdevlab.dwarves.views.lang.StringManager;

public class Win extends Stage{

	protected Game game;
	protected static TextureRegion img = new TextureRegion(new Texture("data/sprites/victory.png"));

	public Win(Game game){
		
		this.game = game;

		/**
		 * Soumission du score
		 */
		Score score = new Score("local", game.getLevel().getLevelName(), (int)game.getLevel().getElapsedTime());		
		ScoreManager.getInstance().submit(score);
		
		/**
		 * Creation de l'interface
		 */
		MusicManager.stop();
		
		AImage win = new AImage(img, DwarvesManager.getWidth()/2,
				  					 DwarvesManager.getHeight()/2);
		
		win.setY(4f/5f*DwarvesManager.getHeight());
		win.setScale(0.1f);
		win.addAction(Actions.sequence(
				Actions.parallel(
						Actions.scaleTo(2f,2f,1f),
						Actions.rotateTo(360, 1f)
						),
				Actions.parallel(
						Actions.scaleTo(1f, 1f, .5f),
						Actions.rotateTo(375, .5f)
						),
				Actions.rotateTo(345, 1f),
				Actions.rotateTo(360, .5f)
				));
		this.addActor(win);
		
		Table table = new Table(DwarvesManager.getSkin());
		table.defaults().space(30);
		
		Table scores = new Table(DwarvesManager.getSkin());
		table.defaults().space(30);
		
		// Get the 3 best scores :
		List<Score> bestScores = ScoreManager.getInstance().getLevelScore(game.getLevel().getLevelName(), 3);
		System.out.println("Len : " + String.valueOf(bestScores.size()));
		for(Score sco:bestScores){
			scores.add(formatScore(sco.getScore()));
			scores.row();
			System.out.println(formatScore(sco.getScore()));
		}
	
		
		Table buttons = new Table(DwarvesManager.getSkin());
		buttons.defaults().space(30);
		
		ImageButton menu =      new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(13)),
                				new TextureRegionDrawable(MainMenu.buttonsImg.getTile(15)));
		ImageButton next = 		new ImageButton(new TextureRegionDrawable(MainMenu.buttonsImg.getTile(9)),
                				new TextureRegionDrawable(MainMenu.buttonsImg.getTile(11)));
		
		buttons.add(menu);
		
		LevelFile nextLevel = game.getLevel().getNextLevel();
		if(nextLevel!=null){
			buttons.add(next);
		}
		
		
		Table scoresH = new Table(DwarvesManager.getSkin());
		scoresH.defaults().space(30);
		
		scoresH.add(StringManager.getString("BestScores"));
		scoresH.add(scores);
		
		table.add(scoresH).center();
		table.row();
		table.add(StringManager.getString("YourTime") + game.getLevel().getFormattedElapsedTime());
		table.row();
		table.add(buttons).center();
		table.pack();
		table.setPosition(DwarvesManager.getWidth()/2-table.getWidth()/2, DwarvesManager.getHeight()/2-table.getHeight()/2);
		this.addActor(table);
		
		
		
		/**
		 * Listeners
		 */
		
		menu.addListener(new GameClickListener(game) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dw.setStage(new MainMenu(DwarvesManager.getInstance()));
				super.clicked(event, x, y);
			}
		});
		
		next.addListener(new GameClickListener(game) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dw.newGame(game.getLevel().getNextLevel());
				super.clicked(event, x, y);
			}
		});
		
		this.addAction(Actions.color(new Color(0,0,0,0)));
		this.addAction(Actions.color(new Color(1,1,1,1), 2f));
		
	}
	
	private static String formatScore(float time){
		float elapsedTime = time;
		int seconds = (int) elapsedTime;
		int minutes = (int) Math.floor(elapsedTime/60);
		
		seconds-= minutes*60;
		int hours = minutes/60;
		minutes -= hours*60;
		if(hours > 0){
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
		else return String.format("%02d:%02d", minutes, seconds);
	}
	
	
	
}
