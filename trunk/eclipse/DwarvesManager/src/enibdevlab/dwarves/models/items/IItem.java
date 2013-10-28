package enibdevlab.dwarves.models.items;

public interface IItem {

	/**
	 * Nom de l'objet
	 */
	public String getName();
	
	/**
	 * Index de la texture de l'objet dans l'atlas des textures des objets
	 */
	public int getTextureId();	
	
	/**
	 * Effet à l'utilisation de l'objet
	 */
	public void onUse();
	
}
