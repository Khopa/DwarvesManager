package enibdevlab.dwarves.models.misc;

import java.util.Comparator;

import enibdevlab.dwarves.models.rooms.Room;

/**
 * 
 * Comparateur de collections : permet de trier des pièces en fonction de leur distance à un point
 * 
 * @author Clément Perreau
 *
 */
public class RoomDistanceToPointComparator implements Comparator<Room> {
	
	/**
	 * Position X du personnage
	 */
	public int x;
	
	/**
	 * Position Y du personnage
	 */
	public int y;
	
	
	public RoomDistanceToPointComparator(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compare(Room room1, Room room2) {
		
		// Distance objet 1
		int d0 = Math.abs(room1.getX()-x) + Math.abs(room1.getY()-y);
		
		// Distance objet 2
		int d1 = Math.abs(room2.getX()-x) + Math.abs(room2.getY()-y);
		
		if(d0<d1){
			return -1;
		}
		else if(d0==d1){
			return 0;
		}
		else return 1;
		
	}

}
