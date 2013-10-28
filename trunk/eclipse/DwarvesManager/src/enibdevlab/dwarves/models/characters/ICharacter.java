package enibdevlab.dwarves.models.characters;

import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;

/**
 * 
 * Interface des personnages
 * 
 * 
 * @author Cl�ment Perreau 
 *
 */
public interface ICharacter {

	/**
	 * Type de mod�le � afficher
	 */
	public BodyConfig getBodyConfig();
	
	/**
	 * Prime d'embauche
	 */
	public int getGoldenHello();
	
	/**
	 * Salaire � la journ�e (6 minutes)
	 */
	public int getSalary();
	
	/**
	 * Prime de d�part
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
