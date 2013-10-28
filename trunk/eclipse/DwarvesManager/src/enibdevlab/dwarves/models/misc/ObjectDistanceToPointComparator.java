package enibdevlab.dwarves.models.misc;

import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.objects.GameObject;

/**
 * Compare par rapport � leur distance � un point des positions repr�sent� par des vecteurs
 * 
 * @author Cl�ment Perreau
 *
 */
public class ObjectDistanceToPointComparator implements Comparator<GameObject>{

	/**
	 * Position � comparer
	 */
	public int x;
	
	/**
	 * Position � comparer
	 */
	public int y;
	
	
	public ObjectDistanceToPointComparator(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public int compare(GameObject obj1, GameObject obj2) {
		
		Vector2 vec1 = obj1.getPosition();
		Vector2 vec2 = obj2.getPosition();
		
		// Distance objet 1
		int d0 = (int) (Math.abs(vec1.x-x) + Math.abs(vec1.y-y));
						
		// Distance objet 2
		int d1 = (int) (Math.abs(vec2.x-x) + Math.abs(vec2.y-y));
				
		if(d0<d1){
			return -1;
		}
		else if(d0==d1){
			return 0;
		}
		else return 1;
	}
	
	
}
