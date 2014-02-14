package enibdevlab.dwarves.controllers.listener;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.DwarvesManager;
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
		
		GameplayLayer gameplay = this.scene.getGameplayLayer();
		float amount = (initialDistance-distance)/500f;
		
		float r_amount = (float) (this.scene.getGameplayLayer().getScaleX() - amount/25); 
		if (r_amount > 3f){
			r_amount = 3f;
		}
		else if (r_amount < 0.3f){
			r_amount = 0.3f;
		}
		
		float o_amount = gameplay.getScaleX();
		double d_amount = (double)(o_amount-r_amount);
		
		// On recupère la case centrale de l'écran
		// Vector2 center  = gameplay.getTilemap().getCenterCoordinate();
		// On effectue le scaling
		gameplay.setScale(r_amount);
		// gameplay.clamp();
		// On recupère la nouvelle case centrale de l'écran
		// Vector2 center2  = gameplay.getTilemap().getCenterCoordinate();
		// On calcule la différence
		// Vector2 diff    = new Vector2(center.x-center2.x, center.y-center2.y);
		Vector2 realPos = new Vector2(gameplay.getX()/o_amount, gameplay.getY()/o_amount);
		Vector2 newPos = new Vector2((float)(realPos.x*r_amount), (float)(realPos.y*r_amount));
		Vector2 diff   = new Vector2((float)(d_amount*(DwarvesManager.getWidth())), (float)(d_amount*DwarvesManager.getHeight()));
		System.out.println(newPos);
		// TODO : Il manque sans doute une valeur de scaling sur le décalage, je verrai ça plus tard si j'ai le temps
		// décalage pour centrer le zoom
		gameplay.setPosition(newPos.x+diff.x, newPos.y+diff.y);
		//gameplay.setPosition(gameplay.getX()-diff.x, gameplay.getY()-diff.y);
		gameplay.clamp();
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
