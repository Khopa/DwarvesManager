package enibdevlab.dwarves.models;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.characters.Dwarf;
import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.misc.IPersistent;
import enibdevlab.dwarves.views.Loader;
import enibdevlab.dwarves.views.actors.characters.ACharacter;

/**
 * 
 * Liste intelligente des personnages en jeu
 * 
 * @author Clément Perreau
 *
 */
public class Characters implements IPersistent{

	/**
	 * Liste des personnages
	 */
	protected ArrayList<MCharacter> characters;
	
	/**
	 * Lien vers le jeu
	 */
	protected Game game;
	
	public Characters(Game game){
		this.game = game;
		this.characters = new ArrayList<MCharacter>();	
	}

	/**
	 * Ajoute un personnage dans la partie
	 * @param character Personnage à ajouter
	 */
	public void addCharacter(MCharacter character){
		this.characters.add(character);
		character.setGame(this.game);
		ACharacter view = new ACharacter(character);
		character.setView(view);
		this.game.getView().getGameplayLayer().getCharacterLayer().addActor(view); // Ajout de la vue
	}
	
	/**
	 * Supprime un personnage dans la partie 
	 * @param character Personnage à enlever
	 */
	public void removeCharacter(MCharacter character){
		this.characters.remove(character);
		character.getView().remove();
		
		// Il faut libérer le personnage de son slot
		character.leaveSlot();
		
		// Si c'est un mineur, il faut faire part au task manager de l'arrêt de la tâche
		if(character instanceof Miner) ((Miner) character).stopWorking();
		
	}

	public ArrayList<MCharacter> getCharacters() {
		return this.characters;
	}
	
	public ArrayList<Miner> getMiner(){
		ArrayList<Miner> output = new ArrayList<Miner>();
		for(MCharacter character : this.characters){
			if(character instanceof Miner){
				output.add((Miner)(character));
			}
		}
		return output;
	}

	public int getPayroll() {
		int output = 0;
		for(MCharacter chara:this.getCharacters()){
			output += chara.getSalary();
		}
		return (int) (output*Difficulty.getSalaryFactor());
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("Characters", null);
		for(MCharacter character:this.characters){
			output.addChild(character.saveAsXmlElement());
		}
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		
		// Instanciation de tous les personnages
		Element character;
		for(int i = 0; i < xmlElement.getChildCount(); i++){
			character = xmlElement.getChild(i);
			
			Element position = character.getChildByName("position");
			MCharacter mcharacter = Loader.instantiateCharacterToPlace(character.getName(),
				new Vector2(Float.parseFloat(position.getAttribute("x")), Float.parseFloat(position.getAttribute("y"))),
				false);
			if(mcharacter!=null){
				
				if(mcharacter instanceof Dwarf){
					Element needs = character.getChildByName("needs");
					((Dwarf)mcharacter).getNeeds().setSleep(Integer.parseInt(needs.getAttribute("sleep")));
					((Dwarf)mcharacter).getNeeds().setThirst(Integer.parseInt(needs.getAttribute("beer")));
					((Dwarf)mcharacter).getNeeds().setSleepNeed(Double.parseDouble(needs.getAttribute("sleepNeed")));
					((Dwarf)mcharacter).getNeeds().setBeerNeed(Double.parseDouble(needs.getAttribute("beerNeed")));
				}
				mcharacter.setGame(this.game);
				this.addCharacter(mcharacter);
			}
		}
		
	}


	
	
	
	
	
}
