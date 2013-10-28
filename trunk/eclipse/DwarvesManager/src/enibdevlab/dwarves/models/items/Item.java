package enibdevlab.dwarves.models.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.Entity;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.misc.IPersistent;

public abstract class Item extends Entity implements IItem, IPersistent{

	/**
	 * Personnage possédant l'objet
	 */
	protected MCharacter owner;
	
	/**
	 * Création d'un objet
	 * @param pos Position de l'objet dans le monde
	 */
	public Item(Vector2 pos){
		super(pos);
	}

	public MCharacter getOwner() {
		return owner;
	}

	public void setOwner(MCharacter owner) {
		this.owner = owner;
	}
	
	@Override
	public Element saveAsXmlElement() {
		Element output = new Element(this.getClass().getName(), null);
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
	}
	
}
