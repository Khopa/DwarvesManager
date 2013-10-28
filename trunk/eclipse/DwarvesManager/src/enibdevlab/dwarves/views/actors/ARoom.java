package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.rooms.Room;
import enibdevlab.dwarves.models.world.MapArea;


/**
 * 
 * Affichage d'une pi�ce
 * 
 * @author Cl�ment Perreau
 *
 */
public class ARoom extends Actor{

	/**
	 * Couleur � afficher quand une pi�ce n'est pas op�rationnelle
	 */
	private static Color roomNotOperationnalColor = new Color(1f,0,0,.2f);
	
	/**
	 * Mod�le de pi�ce
	 */
	protected Room model;
	
	/**
	 * Couleur de repr�sentation
	 */
	protected Color color;
	
	/**
	 * Constructeur de pi�ce 
	 * @param model Mod�le associ�
	 * @param type Type de pi�ce
	 */
	public ARoom(Room model){
		this.model = model;
		this.model.setView(this);
		this.setColor(new Color(1f,1f,1f,.3f));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		
		super.draw(batch, parentAlpha);
		
		int w = model.getTilemap().getTileWidth();
		int h = model.getTilemap().getTileHeight();
		
		// Couleur
		Color color;
		if(model.isOperationnal()) color = this.model.getRoomColor();
		else color = ARoom.roomNotOperationnalColor;
		
		// Dessin des rectangles
		batch.end();
		Gdx.gl.glEnable(GL10.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		for(MapArea area:this.model.getAreas()){
			DwarvesManager.renderer.setProjectionMatrix(batch.getProjectionMatrix());
			DwarvesManager.renderer.setTransformMatrix(batch.getTransformMatrix());
			DwarvesManager.renderer.begin(ShapeType.FilledRectangle);
			DwarvesManager.renderer.setColor(color);
			DwarvesManager.renderer.filledRect(area.getI()*w, area.getJ()*h, area.getW()*w, area.getH()*h);
			DwarvesManager.renderer.end();
		}
		Gdx.gl.glDisable(GL10.GL_BLEND);
		batch.begin();
		
		// Dessin du nom de la pi�ce 
		Vector2 barycenter = model.barycenter();
		DwarvesManager.font.setColor(Color.WHITE);
		DwarvesManager.font.draw(batch, model.getRoomName(), barycenter.x*w,barycenter.y*h);
		
	}
	
}
