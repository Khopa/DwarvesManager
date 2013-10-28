package enibdevlab.dwarves.controllers;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.models.Game;

/**
 * 
 * Cette classe permet d'ajouter une référence dans le ClickListener
 * de base de libGdx vers le jeu.
 * 
 * @author Clément Perreau
 *
 */
public class GameClickListener extends ClickListener {
	
	protected DwarvesManager dw;
	protected Game game;

	public GameClickListener(Game game){
		super();
		this.game = game;
		this.dw = DwarvesManager.getInstance();
	}
	
}
