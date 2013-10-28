package enibdevlab.dwarves.models;


/**
 * 
 * Classe statique charg�e de g�rer la difficult� du jeu.
 * Le besoin s'est fait sentir en cr�ant le didacticiel du jeu
 * (Certains �l�ments devant �tre d�sactiv�s pour une prise en main
 * du jeu en douceur)
 * 
 * @author Cl�ment Perreau
 *
 */
public class Difficulty {

	/**
	 * Les pioches peuvent elles se briser
	 */
	private static boolean BREAKABLE_PICKAXE = true;
	
	/**
	 * A on besoin de g�rer les besoins des nains
	 */
	private static boolean NEEDS_TO_MANAGE = true;
	
	/**
	 * Multiplicateur de salaire (ajuste la difficult� g�n�rale)
	 */
	private static float SALARY_FACTOR = 1.0f;
	
	
	public static void setNormalDifficulty(){
		BREAKABLE_PICKAXE = true;
		NEEDS_TO_MANAGE = true;
		SALARY_FACTOR = 1.0f;
	}
	
	public static void setTutorialSettings(){
		BREAKABLE_PICKAXE = false;
		NEEDS_TO_MANAGE = false;
		SALARY_FACTOR = 1.0f;
	}
	
	public static void setTutorial2Settings(){
		BREAKABLE_PICKAXE = true;
		NEEDS_TO_MANAGE = false;
		SALARY_FACTOR = 1.0f;
	}

	public static boolean isPickaxeBreakable() {
		return BREAKABLE_PICKAXE;
	}

	public static boolean areNeedsToManage() {
		return NEEDS_TO_MANAGE;
	}

	public static float getSalaryFactor() {
		return SALARY_FACTOR;
	}
	
}
