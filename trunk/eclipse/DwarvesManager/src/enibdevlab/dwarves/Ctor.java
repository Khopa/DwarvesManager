package enibdevlab.dwarves;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.models.Rooms;
import enibdevlab.dwarves.models.characters.Barman;
import enibdevlab.dwarves.models.characters.Craftman;
import enibdevlab.dwarves.models.characters.Enchanter;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.objects.Anvil;
import enibdevlab.dwarves.models.objects.Barrel;
import enibdevlab.dwarves.models.objects.Bed;
import enibdevlab.dwarves.models.objects.Counter;
import enibdevlab.dwarves.models.objects.Hearth;
import enibdevlab.dwarves.models.objects.Rack;
import enibdevlab.dwarves.models.objects.TableObject;
import enibdevlab.dwarves.models.rooms.Dorm;
import enibdevlab.dwarves.models.rooms.Tavern;
import enibdevlab.dwarves.models.rooms.Workshop;
import enibdevlab.dwarves.models.world.MapArea;

/**
 * Web port: reflection-free stand-in for java.lang.reflect.Constructor.
 *
 * The original game discovered its object/room/character classes with
 * Class.forName + getConstructor, which TeaVM cannot compile. This class keeps
 * the same shape (getName / newInstance) but dispatches to explicit `new` calls.
 */
public abstract class Ctor {

	private final String name;

	protected Ctor(String name){
		this.name = name;
	}

	/** Simple class name (Constructor.getDeclaringClass().getSimpleName()). */
	public String getSimpleName(){
		return name.substring(name.lastIndexOf('.') + 1);
	}

	/** Fully qualified class name, as Constructor.getName() returned. */
	public String getName(){
		return name;
	}

	protected abstract Object create(Object[] args);

	public Object newInstance(Object... args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object o = create(args);
		if(o == null) throw new InstantiationException("Unknown class " + name);
		return o;
	}

	@Override
	public String toString(){
		return name;
	}

	/**
	 * Returns the factory for the given fully-qualified class name, or null.
	 */
	public static Ctor forName(String className){
		final String n = className;
		final String simple = n.substring(n.lastIndexOf('.') + 1);
		switch(simple){
			// Objects: (Vector2)
			case "Bed": case "TableObject": case "Barrel": case "Anvil": case "Rack": case "Counter": case "Hearth":
				return new Ctor(n){
					protected Object create(Object[] a){
						Vector2 p = (Vector2) a[0];
						switch(simple){
							case "Bed": return new Bed(p);
							case "TableObject": return new TableObject(p);
							case "Barrel": return new Barrel(p);
							case "Anvil": return new Anvil(p);
							case "Rack": return new Rack(p);
							case "Counter": return new Counter(p);
							case "Hearth": return new Hearth(p);
						}
						return null;
					}
				};
			// Rooms: (MapArea, Rooms)
			case "Dorm": case "Tavern": case "Workshop":
				return new Ctor(n){
					protected Object create(Object[] a){
						MapArea area = (MapArea) a[0];
						Rooms rooms = (Rooms) a[1];
						switch(simple){
							case "Dorm": return new Dorm(area, rooms);
							case "Tavern": return new Tavern(area, rooms);
							case "Workshop": return new Workshop(area, rooms);
						}
						return null;
					}
				};
			// Characters: (Vector2)
			case "Miner": case "Barman": case "Craftman": case "Enchanter":
				return new Ctor(n){
					protected Object create(Object[] a){
						Vector2 p = (Vector2) a[0];
						switch(simple){
							case "Miner": return new Miner(p);
							case "Barman": return new Barman(p);
							case "Craftman": return new Craftman(p);
							case "Enchanter": return new Enchanter(p);
						}
						return null;
					}
				};
		}
		return null;
	}
}
