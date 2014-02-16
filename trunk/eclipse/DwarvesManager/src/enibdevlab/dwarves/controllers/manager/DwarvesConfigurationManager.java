package enibdevlab.dwarves.controllers.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.khopa.skhopa.controllers.ConfigurationManager;

import enibdevlab.dwarves.DwarvesManager;

/**
 * 
 * Gestionnaire de configuration pour Dwarves Manager 
 * (ou pour tout jeu libgdx en fait)
 * 
 * @author Clément Perreau
 *
 */
public class DwarvesConfigurationManager extends ConfigurationManager {

	/**
	 * Chemin du fichier de configuration
	 */
	private final static String filePath = "DwarvesManager/Config/config.xml";
	
	/**
	 * Racine du fichier de conf xml
	 */
	private Element root = null;
	
	@Override
	public void save() {
		FileHandle file = Gdx.files.external(filePath);
		
		this.root = new Element("config", null);
		Element floats   = new Element("floats",null);
		Element integers = new Element("integers",null);
		Element strings  = new Element("strings",null);
		Element booleans = new Element("booleans",null);
		
		for(String key:floatValues.keySet()){
			Element newElement = new Element("float", null);
			newElement.setAttribute("key", key);
			newElement.setText(Float.toString(floatValues.get(key)));
			floats.addChild(newElement);
		}
		
		for(String key:integerValues.keySet()){
			Element newElement = new Element("integer", null);
			newElement.setAttribute("key", key);
			newElement.setText(Integer.toString(integerValues.get(key)));
			integers.addChild(newElement);
		}
		
		for(String key:stringValues.keySet()){
			Element newElement = new Element("string", null);
			newElement.setAttribute("key", key);
			newElement.setText(stringValues.get(key));
			strings.addChild(newElement);
		}
		
		for(String key:booleanValues.keySet()){
			Element newElement = new Element("boolean", null);
			newElement.setAttribute("key", key);
			newElement.setText(Boolean.toString(booleanValues.get(key)));
			booleans.addChild(newElement);
			System.out.println(key);
		}
		
		this.root.addChild(floats);
		this.root.addChild(integers);
		this.root.addChild(strings);
		this.root.addChild(booleans);
		
		file.writeString(root.toString(), false);
	
}

	@Override
	public void load() {
		XmlReader reader = new XmlReader();
		FileHandle file = Gdx.files.external(filePath);
		if(!file.exists()){ // Cree le fichier s'il n'existe pas
			createNewConfigurationFile(file);
			firstConfig();
		}
		else{
			try {
				this.root = reader.parse(file);
				Element floats   = this.root.getChildByName("floats");
				Element integers = this.root.getChildByName("integers");
				Element strings  = this.root.getChildByName("strings");
				Element booleans = this.root.getChildByName("booleans");
				
				for(Element floatElement:floats.getChildrenByName("float")){
					this.floatValues.put(floatElement.getAttribute("key"), Float.parseFloat(floatElement.getText()));
				}
				
				for(Element intElement:integers.getChildrenByName("integer")){
					this.integerValues.put(intElement.getAttribute("key"), Integer.parseInt(intElement.getText()));
				}

				for(Element strElement:strings.getChildrenByName("string")){
					this.stringValues.put(strElement.getAttribute("key"), strElement.getText());
				}
				
				for(Element boolElement:booleans.getChildrenByName("boolean")){
					this.booleanValues.put(boolElement.getAttribute("key"), Boolean.parseBoolean(boolElement.getText()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				createNewConfigurationFile(file);
				firstConfig();
			}
		}
		
	}
	
	/**
	 * Cree un nouveau fichier de configuration
	 * @param file
	 */
	private void createNewConfigurationFile(FileHandle file){
		this.root = new Element("config", null);
		Element floats = new Element("floats", null);
		Element integers = new Element("integers", null);
		Element strings = new Element("strings", null);
		Element booleans = new Element("booleans", null);
		this.root.addChild(floats);
		this.root.addChild(integers);
		this.root.addChild(strings);
		this.root.addChild(booleans);
		file.writeString(root.toString(), false);
	}
	
	/**
	 * Configure pour la première fois le jeu (valeurs par defaut)
	 */
	private void firstConfig(){
		
		this.setValue("particle", true);
		
		if(DwarvesManager.getWidth() > 768){
			this.setValue("bigFont", true);
		}
		else{
			this.setValue("bigFont", false);
		}
		
		this.setValue("sfxVolume", .85f);
		this.setValue("musicVolume", .75f);
		
		this.save();
		
	}
	
}

