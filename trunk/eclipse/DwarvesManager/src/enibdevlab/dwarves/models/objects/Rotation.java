package enibdevlab.dwarves.models.objects;

import enibdevlab.dwarves.models.Direction;

/**
 * 
 * Modèle pour la rotation d'un objet
 * 
 * @author Clément Perreau
 *
 */
public enum Rotation {
	ZERO,
	ONE_QUARTER,
	TWO_QUARTER,
	THREE_QUARTER;
	
	public static Rotation rotateClockwise(Rotation current){
		if(current==ZERO){
			return ONE_QUARTER;
		}
		else if(current==ONE_QUARTER){
			return TWO_QUARTER;
		}
		else if(current==TWO_QUARTER){
			return THREE_QUARTER;
		}
		else if(current==THREE_QUARTER){
			return ZERO;
		}
		else return ZERO;
	}
	
	public static Rotation rotateAntiClockwise(Rotation current){
		if(current==ZERO){
			return THREE_QUARTER;
		}
		else if(current==ONE_QUARTER){
			return ZERO;
		}
		else if(current==TWO_QUARTER){
			return ONE_QUARTER;
		}
		else if(current==THREE_QUARTER){
			return TWO_QUARTER;
		}
		else return ZERO;
	}
	
	public static int toInt(Rotation current){
		if(current==ZERO){
			return 0;
		}
		else if(current==ONE_QUARTER){
			return 90;
		}
		else if(current==TWO_QUARTER){
			return 180;
		}
		else if(current==THREE_QUARTER){
			return 270;
		}
		else return 0;
	}

	public static Direction toDirection(Rotation current) {
		if(current==ZERO){
			return Direction.BOTTOM;
		}
		else if(current==ONE_QUARTER){
			return Direction.BOTTOM;
		}
		else if(current==TWO_QUARTER){
			return Direction.BOTTOM;
		}
		else if(current==THREE_QUARTER){
			return Direction.BOTTOM;
		}
		else return Direction.BOTTOM;
	}

	public static Rotation fromInt(int current) {
		if(current==0){
			return ZERO;
		}
		else if(current==90){
			return ONE_QUARTER;
		}
		else if(current==180){
			return TWO_QUARTER;
		}
		else if(current==270){
			return THREE_QUARTER;
		}
		else return ZERO;
	}
	
	
}
