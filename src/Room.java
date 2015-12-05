import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Room implements IDescribable {
    private Room[] dirs = new Room[6];
    private String description;
    private String name;
    private List<Item> items;
    private List<Character> chars;
    private boolean visited = false;
    public Room goNorth() {
        return goDir(Side.NORTH);
    }
    public Room goDown() {
        return goDir(Side.DOWN);
    }
    public Room goSouth() {
        return goDir(Side.SOUTH);
    }
    public Room goUp() {
        return goDir(Side.UP);
    }
    public Room goWest() {
        return goDir(Side.WEST);
    }
    public Room goEast() {
        return goDir(Side.EAST);
    }
    public Room goDir(int s) {
        Iterator<Character> it = chars.iterator();
        while(it.hasNext()) {
            Character c = it.next();
            if(c instanceof ExitBlockingCharacter) {
                // character is blocking the exit
                return null;
            }
        }
        return dirs[s];
    }
    public void setDir(int s, Room r) {
        dirs[s] = r;
    }
    public String getDescription() {
        return description;
    }
    public String getFullDescription(boolean v) {
        // show name
        String tr = getName();
        if(!v) {
            // first visit, or manual look; show description
            tr += "\n"+getDescription();
        }
        // list items
        Iterator<Item> it = items.iterator();
        while(it.hasNext()) {
            Item i = it.next();
            tr += "\nThere is "+i.getName()+" here.";
        }
        // list characters
        Iterator<Character> ci = chars.iterator();
        while(ci.hasNext()) {
            Character c = ci.next();
            tr += "\n"+c.getEntry();
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

    public void addItem(Item i) {
        this.items.add(i);
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
    public Character[] getCharacters() {
        return chars.toArray(new Character[0]);
    }

    public Item takeItem(int index) {
        return items.remove(index);
    }

    public void removeCharacter(Character c) {
        chars.remove(c);
    }

    public void connectTo(Room r, int s) {
        setDir(s, r);
        r.setDir(Side.opposite[s], this);
    }
}
