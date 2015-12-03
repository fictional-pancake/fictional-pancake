import java.util.Iterator;
import java.util.List;

public class Room {
    Room north;
    Room south;
    Room west;
    Room east;
    Room up;
    Room down;
    private String description;
    private String name;
    private List<Item> items;
    private boolean visited = false;
    public Room goNorth() {
        return north;
    }
    public Room goSouth() {
        return south;
    }
    public Room goWest() {
        return west;
    }
    public Room goEast() {
        return east;
    }
    public Room goUp() {
        return up;
    }
    public Room goDown() {
        return down;
    }
    public String getDescription() {
        return description;
    }
    public String getFullDescription(boolean v) {
        String tr = getName();
        if(!v) {
            tr += "\n"+getDescription();
        }
        Iterator<Item> it = items.iterator();
        while(it.hasNext()) {
            Item i = it.next();
            tr += "\nThere is "+i.getName()+" here.";
        }
        return tr;
    }
    public String getName() {
        return name;
    }
    public boolean getVisited() {
        return visited;
    }
    public void visit() {
        visited = true;
    }

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
