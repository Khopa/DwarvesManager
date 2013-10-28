package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.world.MapArea;


/**
 * Vue pour le model MapArea
 * 
 * @author Cl�ment Perreau
 */
public class AMapArea extends Actor{

	/**
	 * R�f�rence vers le mod�le
	 */
	protected MapArea model;
	
	/**
	 * Couleur pr�f�rentielle
	 */
	protected Color color;

	/**
	 * Construit un actor libgdx pour afficher une mapArea
	 * @param model Modele de MapArea � repr�senter
	 */
	public AMapArea(MapArea model){
		this(model, new Color(0f, 0f, 1f, .2f));
	}
	
	/**
	 * Construit un actor libgdx pour afficher une mapArea
	 * @param model Modele de MapArea � repr�senter
	 * @param color Couleur souhait� pour la repr�sentation
	 */
	public AMapArea(MapArea model, Color color){
		this.model = model;
		this.model.setView(this);
		this.color = color;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		
		int w = model.getTilemap().getTileWidth();
		int h = model.getTilemap().getTileHeight();
		
		batch.end();
		Gdx.gl.glEnable(GL10.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		DwarvesManager.renderer.setProjectionMatrix(batch.getProjectionMatrix());
		DwarvesManager.renderer.setTransformMatrix(batch.getTransformMatrix());
		DwarvesManager.renderer.begin(ShapeType.FilledRectangle);
		DwarvesManager.renderer.setColor(color);
		DwarvesManager.renderer.filledRect(model.getI()*w, model.getJ()*h, model.getW()*w, model.getH()*h);
		DwarvesManager.renderer.end();
		Gdx.gl.glDisable(GL10.GL_BLEND);
		batch.begin();

	}

	public MapArea getModel() {
		return model;
	}

	public void setModel(MapArea model) {
		if(this.model != null) this.model.setView(null);
		this.model = model;
		this.model.setView(this);
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
}
