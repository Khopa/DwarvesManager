package enibdevlab.dwarves.views.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import enibdevlab.dwarves.DwarvesManager;


/**
 * 
 * Image qui occupe tout l'�cran
 * 
 * @author Cl�ment Perreau
 *
 */
public class ABackgroundImage extends AImage {

	public ABackgroundImage(TextureRegion img) {
		super(img, 0, DwarvesManager.getHeight());
		this.prepare();
	}
	
	/**
	 * Redimensionne l'image par rapport � l'�cran
	 */
	private void prepare(){
		
	}
	
	

	
}
