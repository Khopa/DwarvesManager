package enibdevlab.dwarves.controllers.actions;

public interface IStm {

	/**
	 * Fonction appel�e en entr�e
	 */
	public void entry();
	
	/**
	 * Fonction appel�e en permanence
	 * @param delta Temps �coul� depuis le dernier appel
	 */
	public void doAction(float delta);
	
	/**
	 * Appel� � la fin
	 */
	public void finish();
	
	
}
