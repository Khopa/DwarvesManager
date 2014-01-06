package enibdevlab.dwarves.views.scenes.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.GameClickListener;
import enibdevlab.dwarves.models.Game;

public class TextMessageWindow extends Window {

	protected String text;
	
	public TextMessageWindow(String title, String text) {
		super(title, DwarvesManager.getSkin());
		this.text = text;
		createView();
	}

	private void createView() {
		
		String lines[] = text.split("\n");
		
		Table container = new Table(DwarvesManager.getSkin());
		ScrollPane scroll = new ScrollPane(container, DwarvesManager.getSkin());
		//scroll.setScrollingDisabled(true, false);
		scroll.setHeight(DwarvesManager.getWidth()/3);
		scroll.setWidth(DwarvesManager.getHeight()/3);
		
		for(String line:lines){
			container.add(line).left();
			container.row();
		}
		
		TextButton ok = new TextButton("Ok", DwarvesManager.getSkin());
		ok.pad(10,40,10,40);
		ok.addListener(new GameClickListener(null){
			public void clicked (InputEvent event, float x, float y) {
				event.getListenerActor().getParent().remove();
				Game.getInstance().fireDwarfEvent("popupClosed");
			}
		});
		
		this.defaults().space(10);
		this.defaults().pad(15);
		this.add(container);
		this.row();
		this.add().height(50);
		this.row();
		this.add(ok).center();
		this.pack();
		
		this.setPosition(DwarvesManager.getWidth()/2-this.getWidth()/2, DwarvesManager.getHeight()/2-this.getHeight()/2);
		this.setMovable(true);
	}
	
	
	
	
	

}
