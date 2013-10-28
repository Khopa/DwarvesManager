package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.Primitives;

/**
 * 
 * Provoque attente de synchronisation sur un thread de connection
 * 
 * @author Clément Perreau
 *
 */
public class WaitingEffect extends Group{

	protected String text;
	
	private static TextureRegion loading = new TextureRegion(new Texture(Gdx.files.internal("data/sprites/loading.png")));
	private static TextureRegion loading2 = new TextureRegion(new Texture(Gdx.files.internal("data/sprites/loading2.png")));
	private static Color bgColor= new Color(.1f,.1f,.1f,.8f);
	
	public WaitingEffect(String text){
		super();
		this.text = text;
		
		AImage loadingImage = new AImage(loading, DwarvesManager.getWidth()/2, DwarvesManager.getHeight()/2);
		loadingImage.addAction(Actions.forever(Actions.sequence(
								Actions.rotateBy(360, 1.5f),
								Actions.rotateBy(-360, 1.2f))));
		loadingImage.addAction(Actions.forever(Actions.sequence(
				Actions.color(new Color(.4f,.4f,1f,1f), 5f), Actions.color(new Color(1f,1f,1f,1f), 5f))));
				
		
		this.addActor(loadingImage);
		this.addActor(new AImage(loading2, DwarvesManager.getWidth()/2, DwarvesManager.getHeight()/2));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		
		batch.end();
		Primitives.prepareRenderer(batch);
		
		// Fond
		Primitives.enableAlphaBlending();
		Primitives.filledRect(0, 0, DwarvesManager.getWidth(), DwarvesManager.getHeight(), bgColor);
		
		TextBounds textbnd = DwarvesManager.font.getBounds(text);
		int w = (int) (textbnd.width + 10);
		int h = (int) (textbnd.height + 10);
		
		// Zone de loading
		Primitives.filledRect(DwarvesManager.getWidth()/2-w/2, DwarvesManager.getHeight()/2-h/2,
						w,h,bgColor);
		Primitives.disableAlphaBlending();
		batch.begin();
		
		super.draw(batch, parentAlpha);
	}
	
}
