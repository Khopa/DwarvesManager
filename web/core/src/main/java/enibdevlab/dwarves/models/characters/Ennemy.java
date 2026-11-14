package enibdevlab.dwarves.models.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;

public class Ennemy extends MCharacter {

	public Ennemy(Vector2 position) {
		super(position);
	}

	@Override
	public BodyConfig getBodyConfig() {
		return Loader.elf;
	}

	@Override
	public int getGoldenHello() {
		return 400;
	}

	@Override
	public int getSalary() {
		return 50;
	}

	@Override
	public int getGoldenParachute() {
		return 100;
	}

	@Override
	public int getIconId() {
		return 0;
	}

	@Override
	public String getJobName() {
		return "Ennemy";
	}

	@Override
	public Element saveAsXmlElement() {
		return null;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
	}

}
