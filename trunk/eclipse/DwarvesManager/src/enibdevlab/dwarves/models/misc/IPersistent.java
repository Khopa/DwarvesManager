package enibdevlab.dwarves.models.misc;

import com.badlogic.gdx.utils.XmlReader.Element;


/**
 * 
 * Interface impl�ment�e par les objets que l'on peut sauvegarder
 * 
 * @author Cl�ment Perreau
 *
 */
public interface IPersistent {
	
	/**
	 * Sauvegarde au format XML
	 */
	public Element saveAsXmlElement();
	
	/**
	 * Chargement de donn�es XML
	 */
	public void loadFromXmlElement(Element xmlElement);
	
}
