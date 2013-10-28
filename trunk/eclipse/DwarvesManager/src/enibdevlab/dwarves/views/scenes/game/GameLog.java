package enibdevlab.dwarves.views.scenes.game;

import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import enibdevlab.dwarves.DwarvesManager;


public class GameLog extends Table {

	/**
	 * Liste des infos à afficher
	 */
	protected ArrayList<String> logs;
	
	/**
	 * Taille historique
	 */
	private static int HISTORY_SIZE = 8;
	
	
	/**
	 * Taille du log
	 */
	
	public GameLog(){
		super(DwarvesManager.getSkin());
		logs = new ArrayList<String>();
		for(int i = 0; i < 5; i++){
			logs.add(" ");
		}
		
		this.update();
		
	}
	
	public void update(){
		this.clear();
		for(String log:logs.subList(logs.size()-5, logs.size())){
			this.add(log).left();
			this.row();
		}
		this.setPosition(5, 0);
		this.pack();
	}
	
	public void log(String str){
		System.out.println("LOG : " + str);
		this.logs.add(str);
		if(this.logs.size() > HISTORY_SIZE){
			this.logs.remove(0);
		}
		this.update();
	}
	
	public void clearLog(){
		for(int i = 0; i < 5; i++){
			logs.add(" ");
		}
	}

}
