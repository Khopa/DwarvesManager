package enibdevlab.dwarves.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import enibdevlab.dwarves.DwarvesManager;

/**
 * Web port: the original game (libGDX 0.9.8) drew shapes while the stage's
 * SpriteBatch was still active. On modern libGDX the batch only binds its
 * shader in begin(), so sprites flushed after a shape draw end up rendered with
 * the shape shader (untextured white quads). This renderer pauses the active
 * stage batch around each shape pass.
 */
public class BatchAwareShapeRenderer extends ShapeRenderer {

	private Batch paused;

	private Batch activeBatch(){
		DwarvesManager game = DwarvesManager.getInstance();
		if(game == null) return null;
		Stage stage = game.getStage();
		return stage == null ? null : stage.getBatch();
	}

	@Override
	public void begin(ShapeType type){
		Batch b = activeBatch();
		if(b != null && b.isDrawing()){
			b.end();
			paused = b;
		}
		// batch.end() disables blending; shapes in this game are often translucent
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		super.begin(type);
	}

	@Override
	public void end(){
		super.end();
		if(paused != null){
			Batch b = paused;
			paused = null;
			b.begin();
		}
	}
}
