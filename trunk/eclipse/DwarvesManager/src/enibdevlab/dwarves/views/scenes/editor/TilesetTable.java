package enibdevlab.dwarves.views.scenes.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.Primitives;
import enibdevlab.dwarves.views.Tileset;
import enibdevlab.dwarves.views.widgets.ObjectButton;

/**
 * 
 * Table de la Gui dans laquelle est positionné le Widget de selection de tile
 * de l'editeur
 * 
 * @author Clément Perreau
 *
 */
public class TilesetTable extends Table {
	
	private static final int MAINMENU = 0;
	private static final int TILEEDIT  = 0;
	@SuppressWarnings("unused")
	private static final int SCRIPTEDIT = 0;
	
	@SuppressWarnings("unused")
	private int state;
	
	
	private ScrollPane scrollPane;
	private Table container;
	private ScrollPane tilesetScrollPane;
	private Tileset tileset;
	private TilesetWidget tilesetWidget;
	private ImageButton backButton;
	@SuppressWarnings("unused")
	private ImageButton menuButton;
	private ImageButton tileEdit;
	private ImageButton script;

	
	public TilesetTable(Tileset tileset){
		super(DwarvesManager.getSkin());
		this.tileset = tileset;
		init();
		setTileEditMode();
	}

	private void init() {
		
		backButton   = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										Loader.iconAtlas, 34);
		
		menuButton   = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
				 	   					new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
				 	   					Loader.iconAtlas, 39);
		
		tileEdit     = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										Loader.iconAtlas, 40);
		
		script   	 = new ObjectButton(new TextureRegionDrawable(Loader.guiAtlas.getTile(0)),
										new TextureRegionDrawable(Loader.guiAtlas.getTile(1)),
										Loader.iconAtlas, 41);
		
		this.tilesetWidget = new TilesetWidget(tileset);
		this.tilesetScrollPane = new ScrollPane(tilesetWidget, DwarvesManager.getSkin());
		tilesetScrollPane.setScrollingDisabled(false, false);
		tilesetScrollPane.setFadeScrollBars(false);
		
	}

	public void setMainMode(){
		state = MAINMENU;
		this.clear();
		
		this.container = new Table(DwarvesManager.getSkin());
		this.scrollPane = new ScrollPane(container, DwarvesManager.getSkin());
		this.scrollPane.setScrollingDisabled(true, false);
		
		container.add(this.tileEdit);
		container.row();
		container.add(this.script);
		container.row();
		
		this.add(scrollPane).height(DwarvesManager.getHeight()).width(128+25);
		
		this.repack();
	}
	
	public void setTileEditMode(){
		state = TILEEDIT;
		this.clear();
		
		this.container = new Table(DwarvesManager.getSkin());
		this.scrollPane = new ScrollPane(container, DwarvesManager.getSkin());
		this.scrollPane.setScrollingDisabled(true, false);
		
		container.add(this.backButton);
		container.row();
		container.add(this.tilesetScrollPane).width(128).height(512);
		container.row();
		
		this.add(scrollPane).height(DwarvesManager.getHeight()).width(128+25);
		this.repack();
	}
	
	public void repack(){
		this.pack();
		this.setPosition(DwarvesManager.getWidth()-this.getWidth()-25, 0);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();
		Primitives.prepareRenderer(batch);
		Primitives.filledRect((int)(DwarvesManager.getWidth()-this.getWidth()-25), 0, (int) (this.getWidth()+25), (int)(this.getHeight()), Color.DARK_GRAY);
		batch.begin();
		super.draw(batch, parentAlpha);
	}
	
}
