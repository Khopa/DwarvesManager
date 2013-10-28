package enibdevlab.dwarves.views.scenes.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Bank;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.lang.StringManager;

/**
 * 
 * Barre d'info située en haut de l'écran
 * 
 * @author Clément Perreau
 *
 */
public class InfoBar extends Table{

	/**
	 * Compte à afficher
	 */
	protected Bank model;
	
	/**
	 * Skin
	 */
	protected Skin skin;
	
	/**
	 * Scene
	 */
	protected GameScene scene;
	
	protected Image money;
	protected Label moneyAmount;
	protected Image diamond;
	protected Label diamondAmount;
	protected Label time;
	protected Label nextSalaries;
	
	/**
	 */
	public InfoBar(GameScene scene) {
		super(DwarvesManager.getSkin());
		skin = DwarvesManager.getSkin();
		this.model = scene.getGame().getBank();
		this.scene = scene;
		this.initWidget();
		this.createView();
		
		this.setPosition(10, DwarvesManager.getHeight()-this.getHeight());
		
	}


	private void initWidget() {

		money = new Image(Loader.guiAtlasSmall.getTile(0));
		moneyAmount = new Label("0", skin);
		diamond = new Image(Loader.guiAtlasSmall.getTile(8));
		diamondAmount = new Label("0", skin);
		time = new Label("0:0", skin);
		nextSalaries = new Label("Versement des salaires () dans :", skin);
		
	}
	
	private void createView() {
		
		this.clear();
		this.add(money);
		this.add(moneyAmount);
		this.add().width(50);
		this.add(diamond);
		this.add(diamondAmount);
		this.add().width(50);
		this.add(time);
		this.add().width(50);
		this.add(nextSalaries);
		this.pack();

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		moneyAmount.setText(Integer.toString(model.getMoney()));
		diamondAmount.setText(Integer.toString(model.getDiamonds()));
		time.setText(StringManager.getString("Time") + scene.getGame().getLevel().getFormattedElapsedTime());
		nextSalaries.setText(StringManager.getString("WagesIn1") + "(" +
							Integer.toString(scene.getGame().getCharacters().getPayroll()) + ")" + StringManager.getString("WagesIn2")  +
							scene.getGame().getLevel().getFormattedWageTimer());
		this.pack();
	}
	
	
	
}
