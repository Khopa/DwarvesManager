package enibdevlab.dwarves.models.objects;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.characters.MCharacter;
import enibdevlab.dwarves.views.actors.AGameObject;

/**
 * 
 * Interface � impl�menter par les objets
 * 
 * @author Cl�ment Perreau
 *
 */
public interface IGameObject {

	/**
	 * Info pour g�n�rer la vue (index de la position bas gauche de l'objet dans l'atlas des textures d'objet)
	 */
	public int getTextureId();	
	
	/**
	 * Retourne la taille X de l'objet dans le cas o� il n'a pas �t� tourn�
	 */
	public int getNominalXSize();
	
	/**
	 * Retourne la taille Y de l'objet dans le cas o� il n'a pas �t� tourn�
	 */
	public int getNominalYSize();
	
	/*
	 * Icone du Type d'objet pour la gui
	 */
	public int getIconId();
	
	/*
	 * Prix
	 */
	public int getPrice();
	
	/**
	 * Nom de l'objet
	 */
	public String getName();
	
	/**
	 * Retourne la liste des positions de slots pour cet objet en particulier
	 */
	public HashMap<Vector2, Class<? extends MCharacter>> getSlotsPosition();
	
	/**
	 * Type de vue � cr�er
	 */
	public Class<? extends AGameObject> getViewType();
	
	public void update();
	
}
