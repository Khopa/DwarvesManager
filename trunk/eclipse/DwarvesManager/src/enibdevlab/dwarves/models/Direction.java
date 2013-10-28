package enibdevlab.dwarves.models;


/**
 * 
 * Enum pour représenter les directions
 * 
 * @author Clément Perreau
 *
 */
public enum Direction{
	BOTTOM,
	RIGHT,
	LEFT,
	TOP;
	
	public int toInt(){
		switch(this){
			case BOTTOM:
				return 0;
			case RIGHT:
				return 1;
			case LEFT:
				return 2;
			case TOP:
				return 3;
			default:
				return 0;
		}
	}
}