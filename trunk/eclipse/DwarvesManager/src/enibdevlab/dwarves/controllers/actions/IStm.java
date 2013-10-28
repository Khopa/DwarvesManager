package enibdevlab.dwarves.controllers.actions;

public interface IStm {

	/**
	 * Fonction appelée en entrée
	 */
	public void entry();
	
	/**
	 * Fonction appelée en permanence
	 * @param delta Temps écoulé depuis le dernier appel
	 */
	public void doAction(float delta);
	
	/**
	 * Appelé à la fin
	 */
	public void finish();
	
	
}
