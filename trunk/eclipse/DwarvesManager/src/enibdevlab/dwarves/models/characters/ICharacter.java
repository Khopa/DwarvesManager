package enibdevlab.dwarves.models.characters;

import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;

/**
 * 
 * Interface des personnages
 * 
 * 
 * @author Clément Perreau 
 *
 */
public interface ICharacter {

	/**
	 * Type de modèle à afficher
	 */
	public BodyConfig getBodyConfig();
	
	/**
	 * Prime d'embauche
	 */
	public int getGoldenHello();
	
	/**
	 * Salaire à la journée (6 minutes)
	 */
	public int getSalary();
	
	/**
	 * Prime de départ
	 */
	public int getGoldenParachute();
	
	/*
	 * Icone du Type de personnage
	 */
	public int getIconId();
	
	/*
	 * Nom du type de personnage
	 */
	public String getJobName();
	
}
