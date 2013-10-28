package enibdevlab.dwarves.views.scenes.editor;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import enibdevlab.dwarves.views.Primitives;
import enibdevlab.dwarves.views.Tileset;


/**
 * 
 * Un tileset pour afficher et selectionner des tiles d'un tileset à placer dans l'éditeur
 * 
 * @author Clément Perreau
 *
 */
public class TilesetWidget extends Widget {

	/**
	 * Couleur par defaut de la selection
	 */
	protected static Color SELECTION_COLOR = new Color(0.1f,0.1f,0.7f,0.7f);
	
	/**
	 * Tileset à afficher
	 */
	protected Tileset tileset;
	
	/**
	 * Pinceau de tile (selection de tiles)
	 */
	protected Rectangle brush;
	
	public TilesetWidget(Tileset tileset){
		this.tileset = tileset;
		this.brush = new Rectangle(0, 0, 1, 1); // Par défaut le tile haut gauche est selectionné
	}
	
	@Override
	public float getPrefHeight() {
		return this.tileset.getImgdata().getHeight();
	}
	
	@Override
	public float getPrefWidth() {
		return this.tileset.getImgdata().getWidth();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(tileset.getImgdata(), this.getX(), this.getY());
		batch.end();
		Primitives.prepareRenderer(batch);
		Primitives.enableAlphaBlending();
		Primitives.lineWidth(3);
		Primitives.rect((int)(brush.x*tileset.getTileWidth()),
						(int)(brush.y*tileset.getTileHeight()),
						(int)(brush.width*tileset.getTileWidth()),
						(int)(brush.height*tileset.getTileHeight()), SELECTION_COLOR);
		Primitives.disableAlphaBlending();
		batch.begin();
	}
	
	
}
