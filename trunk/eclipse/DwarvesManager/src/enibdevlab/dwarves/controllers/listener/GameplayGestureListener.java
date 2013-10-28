package enibdevlab.dwarves.controllers.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.scenes.game.GameScene;
import enibdevlab.dwarves.views.scenes.game.GameState;
import enibdevlab.dwarves.views.scenes.game.GameplayLayer;

/**
 * 
 * Listener spécifiques pour gérer certains évenements spécifiques android
 * 
 * @author Clément Perreau
 *
 */
public class GameplayGestureListener implements GestureListener{

	private GameScene scene;
	protected float pinchValue = 0f;

	public GameplayGestureListener(GameScene scene){
		this.scene = scene;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		
		if(!(scene.getGameState() == GameState.NORMAL)) return false;
		
		float amount = (initialDistance-distance)/500f;
		
		float r_amount = (float) (this.scene.getGameplayLayer().getScaleX() - amount/25); 
		if (r_amount > 3f){
			r_amount = 3f;
		}
		else if (r_amount < 0.3f){
			r_amount = 0.3f;
		}
		
		GameplayLayer gameplay = this.scene.getGameplayLayer();
		float scale1 = gameplay.getScaleX();
		Vector2 center  = gameplay.getTilemap().getPixelCoordinate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		gameplay.setScale(r_amount);
		gameplay.clamp();
		Vector2 center2  = this.scene.getGameplayLayer().getTilemap().getPixelCoordinate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Vector2 diff    = new Vector2(center.x*scale1-center2.x*r_amount, center.y*scale1-center2.y*r_amount); 
		gameplay.setPosition(gameplay.getX()-diff.x, gameplay.getY()-diff.y);
		
		

		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		
		if(scene.getGameState() == GameState.PLACING_OBJECT){
			pinchValue += .05f;
			if(pinchValue >= 5){
				scene.getListener().rotateObject();
				pinchValue = 0f;
			}
			
		}
		
		return true;
	}

}
