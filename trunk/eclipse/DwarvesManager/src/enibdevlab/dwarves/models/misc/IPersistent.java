package enibdevlab.dwarves.models.misc;

import com.badlogic.gdx.utils.XmlReader.Element;


/**
 * 
 * Interface implémentée par les objets que l'on peut sauvegarder
 * 
 * @author Clément Perreau
 *
 */
public interface IPersistent {
	
	/**
	 * Sauvegarde au format XML
	 */
	public Element saveAsXmlElement();
	
	/**
	 * Chargement de données XML
	 */
	public void loadFromXmlElement(Element xmlElement);
	
}
