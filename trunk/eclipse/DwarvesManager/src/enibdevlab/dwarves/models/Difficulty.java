package enibdevlab.dwarves.models;


/**
 * 
 * Classe statique chargée de gérer la difficulté du jeu.
 * Le besoin s'est fait sentir en créant le didacticiel du jeu
 * (Certains éléments devant être désactivés pour une prise en main
 * du jeu en douceur)
 * 
 * @author Clément Perreau
 *
 */
public class Difficulty {

	/**
	 * Les pioches peuvent elles se briser
	 */
	private static boolean BREAKABLE_PICKAXE = true;
	
	/**
	 * A on besoin de gérer les besoins des nains
	 */
	private static boolean NEEDS_TO_MANAGE = true;
	
	/**
	 * Multiplicateur de salaire (ajuste la difficulté générale)
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
