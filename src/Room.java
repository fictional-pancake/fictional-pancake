import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A room for being a room
 * @author Tony Brar and Colin Reeder
 */
public class Room implements IDescribable {
    /**
     * The rooms in each direction (see {@link Side})
     */
    private Room[] dirs = new Room[6];

    /**
     * The description of the Room
     */
    private String description;

    /**
     * The name of the Room
     */
    private String name;

    /**
     * The items in the room
     */
    private List<Item> items;

    /**
     * The buried items in the room
     */
    private List<Item> buriedItems;

    /**
     * The characters in the room
     */
    private List<Character> chars;

    /**
     * Whether this room has already been visited
     */
    private boolean visited = false;

    /**
     * Whether this room is always lit
     */
    private boolean alwaysLit = false;

    /**
     * Return the room to the north
     *
     * @return the room to the north
     */
    public Room goNorth() {
        return goDir(Side.NORTH);
    }

    /**
     * Return the room below
     *
     * @return the room below
     */
    public Room goDown() {
        return goDir(Side.DOWN);
    }

    /**
     * Return the room to the south
     *
     * @return the room to the south
     */
    public Room goSouth() {
        return goDir(Side.SOUTH);
    }

    /**
     * Return the room above
     *
     * @return the room above
     */
    public Room goUp() {
        return goDir(Side.UP);
    }

    /**
     * Return the room to the west
     *
     * @return the room to the west
     */
    public Room goWest() {
        return goDir(Side.WEST);
    }

    /**
     * Return the room to the east
     *
     * @return the room to the east
     */
    public Room goEast() {
        return goDir(Side.EAST);
    }

    /**
     * Return the room in the specified direction
     *
     * @param s the direction to go (see {@link Side})
     * @return the room in the specified direction
     */
    public Room goDir(int s) {
        Iterator<Character> it = chars.iterator();
        while (it.hasNext()) {
            Character c = it.next();
            if (c.isBlockingExit(s)) {
                // character is blocking this exit
                if(!Main.cheatMode) {
                    System.out.println("The " + c.getName() + " is blocking the way.");
                    return null;
                }
                else {
                    System.out.println("You cheat past the "+c.getName()+".");
                }
            }
        }
        if (!Main.isLight() && Math.random() < 0.9) {
            if(!Main.cheatMode) {
                System.out.println("You were eaten by a grue.");
                Main.kill();
            }
            else {
                System.out.println("Your cheating vibes repel the grue.  It cannot eat you.");
            }
        }
        if (dirs[s] == null) {
            System.out.println("You can't go that way.");
        }
        return dirs[s];
    }

    /**
     * Set the room in the specified direction
     *
     * @param s the direction to set (see {@link Side})
     * @param r the room to set
     */
    public void setDir(int s, Room r) {
        dirs[s] = r;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the full description of the room
     *
     * @param v whether to hide the description
     * @return the full description of the room
     */
    public String getFullDescription(boolean v) {
        if (Main.isLight()) {
            // show name
            String tr = getName();
            if (!v) {
                // first visit, or manual look; show description
                tr += "\n" + getDescription();
            }
            // list items
            Iterator<Item> it = items.iterator();
            while (it.hasNext()) {
                Item i = it.next();
                tr += "\nThere is " + i.getName() + " here.";
            }
            // list characters
            Iterator<Character> ci = chars.iterator();
            while (ci.hasNext()) {
                Character c = ci.next();
                tr = (tr + "\n" + c.getEntry()).trim();
            }
            return tr;
        } else {
            // darkness
            if(!Main.cheatMode) {
                return "It is pitch dark.  You are likely to be eaten by a grue.";
            }
            else {
                return "It is pitch dark, but with cheatiness, the grues can't harm you!";
            }
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Return whether the room has been visited before
     *
     * @return whether the room has been visited before
     */
    public boolean getVisited() {
        return visited;
    }

    /**
     * Trigger events that happen on visit
     */
    public void visit() {
        visited = true;
    }

    /**
     * Add an item to the room
     *
     * @param i the item to add
     */
    public void addItem(Item i) {
        this.items.add(i);
    }

    /**
     * Construct a room with a name, description, items, characters, and light status
     *
     * @param name        the room's name
     * @param description the room's description
     * @param items       the room's items
     * @param chars       the room's characters
     * @param buriedItems the items buried in this room (null for hard ground)
     * @param light       whether the room is always lit
     */
    public Room(String name, String description, Item[] items, Character[] chars, Item[] buriedItems, boolean light) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<Item>();
        for (int i = 0; i < items.length; i++) {
            this.items.add(items[i]);
        }
        this.chars = new ArrayList<Character>();
        for (int i = 0; i < chars.length; i++) {
            this.chars.add(chars[i]);
        }
        if(buriedItems != null) {
            this.buriedItems = new ArrayList<Item>();
            for(int i = 0; i < buriedItems.length; i++) {
                this.buriedItems.add(buriedItems[i]);
            }
        }
        this.alwaysLit = light;
    }

    /**
     * Construct a room with a name, description, items, and characters
     *
     * @param name        the room's name
     * @param description the room's description
     * @param items       the room's items
     * @param chars       the room's characters
     * @param buriedItems the items buried in this room (null for hard ground)
     */
    public Room(String name, String description, Item[] items, Character[] chars, Item[] buriedItems) {
        this(name, description, items, chars, buriedItems, false);
    }

    /**
     * Get an array of items in this room
     *
     * @return an array of items in this room
     */
    public Item[] getItems() {
        return items.toArray(new Item[0]);
    }

    /**
     * Get whether you can dig in this room
     * @return whether you can dig in this room
     */
    public boolean isDiggable() {
        return buriedItems != null;
    }

    /**
     * Dig up the items in this room
     * @return items dug up or null if there aren't any
     */
    public Item[] dig() {
        if(isDiggable()) {
            Item[] tr = buriedItems.toArray(new Item[0]);
            buriedItems.clear();
            if(tr.length != 0) {
                return tr;
            }
        }
        return null;
    }

    /**
     * Get an array of characters in the room
     *
     * @return an array of characters in the room
     */
    public Character[] getCharacters() {
        return chars.toArray(new Character[0]);
    }

    /**
     * Remove an item from the room
     *
     * @param index the index of the item to remove
     * @return the removed item
     */
    public Item takeItem(int index) {
        return items.remove(index);
    }

    /**
     * Remove a character from the room
     *
     * @param c the character to remove
     */
    public void removeCharacter(Character c) {
        chars.remove(c);
    }

    /**
     * Connect a room to another room
     *
     * @param r the room to connect to
     * @param s the side to connect on (see {@link Side})
     */
    public void connectTo(Room r, int s) {
        setDir(s, r);
        r.setDir(Side.opposite[s], this);
    }

    /**
     * Return whether this room is always lit
     *
     * @return whether this room is always lit
     */
    public boolean isAlwaysLit() {
        return alwaysLit;
    }

    /**
     * Set the description of the room
     *
     * @param description the description for the room
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Add a character to the room
     * @param chr the character to add
     */
    public void addCharacter(Character chr) {
        chars.add(chr);
    }

    /**
     * Trigger after entry
     */
    public void afterVisit() {}
}
