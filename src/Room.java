import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Room {
    private Room[] dirs = new Room[6];
    private String description;
    private String name;
    private List<Item> items;
    private List<Character> chars;
    private boolean visited = false;
    public Room goNorth() {
        return dirs[Side.NORTH];
    }
    public Room goSouth() {
        return dirs[Side.SOUTH];
    }
    public Room goWest() {
        return dirs[Side.WEST];
    }
    public Room goEast() {
        return dirs[Side.EAST];
    }
    public Room goUp() {
        return dirs[Side.UP];
    }
    public Room goDown() {
        return dirs[Side.DOWN];
    }
    public Room goDir(int s) {
        return dirs[s];
    }
    public void setDir(int s, Room r) {
        dirs[s] = r;
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
        Iterator<Character> ci = chars.iterator();
        while(ci.hasNext()) {
            Character c = ci.next();
            tr += "\nThere is "+c.getName()+" here.";
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

    public Room(String name, String description, Item[] items, Character[] chars) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<Item>();
        for(int i = 0; i < items.length; i++) {
            this.items.add(items[i]);
        }
        this.chars = new ArrayList<Character>();
        for(int i = 0; i < chars.length; i++) {
            this.chars.add(chars[i]);
        }
    }

    public Item[] getItems() {
        return items.toArray(new Item[0]);
    }

    public Item takeItem(int index) {
        return items.remove(index);
    }
}
