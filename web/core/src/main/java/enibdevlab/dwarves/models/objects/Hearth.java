package enibdevlab.dwarves.models.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.lang.StringManager;

public class Hearth extends GameObject {

	public Hearth(Vector2 position) {
		super(position);
	}

	@Override
	public int getTextureId() {
		return 27;
	}

	@Override
	public int getNominalXSize() {
		return 1;
	}

	@Override
	public int getNominalYSize() {
		return 1;
	}

	@Override
	public int getIconId() {
		return 3;
	}

	@Override
	public int getPrice() {
		return 550;
	}

	@Override
	public String getName() {
		return StringManager.getString("Hearth");
	}
	
	
	@Override
	public void update() {
		super.update();
		
		for(MCharacter character:Game.getInstance().getCharacters().getCharacters()){
			
			if(character.getSpeed() <= 1f){
				float euclidian = (float) Math.sqrt(Math.pow(Math.abs(character.getX()-this.getX()), 2) + Math.pow(Math.abs(character.getY()-this.getY()), 2));
				if(euclidian < 4){
					character.setSpeed(1.75f);
					character.setSpeedTimer(5.5f);
					character.getView().setColor(Color.YELLOW);
				}
			}
		}
		
	}

}
