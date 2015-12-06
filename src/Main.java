import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class Main {
    /**
     * A room for temporary usage
     */
    public static final Room nullRoom = new Room("Unimplemented Room", "You have stumbled upon a room that hasn't been coded yet.  There is no escape.", new Item[]{}, new Character[]{});

    /**
     * The player object
     */
    public static final Player player = new Player(new Item[]{});

    /**
     * List of objects which always exist, but cannot be interacted with
     */
    public static final String[] omnipresent = {"narrator", "you", "sky", "air", "game", "room", "code", "intelligence"};

    /**
     * The current room
     */
    private static Room room;

    /**
     * Run game
     * @param args the argument array.  Ignored currently
     */
    public static void main(String[] args) {
        //setup
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        goTo(setupRooms());
        System.out.println("The guard sets down some food and a spork.");
        room.addItem(new Item(new String[]{"some food", "food", "cabbage"}, "It appears to be cabbage."));
        room.addItem(new Weapon(new String[]{"a spork", "spork", "fork", "spoon"}, "It is approximately 60% spoon and 40% fork.", 10));
        while(true) {
            System.out.print(">");
            String cmd;
            // read command
            try {
                cmd = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // parse and execute command
            readCommand(cmd);
        }
    }

    /**
     * Get the current room
     * @return the current room
     */
    public static Room getCurrentRoom() {
        return room;
    }

    /**
     * Set up the rooms
     * @return the starting room
     */
    public static Room setupRooms() {
        Room cell = new Room(
                "Cell",
                "You are in an old-looking prison cell.  It is dimly lit by a torch outside.  There is a door to the north.",
                new Item[] {},
                new Character[]{new ExitBlockingCharacter("guard", "He looks tired, and he smells of onions.", new Item[]{}, 10)},
                true
        );
        Room corridor = new Room("East end of corridor",
                "You are in a small corridor going west.  There is a open door to the south, and a hole to the east.",
                new Item[]{new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.")},
                new Character[]{}
        );
        cell.connectTo(corridor, Side.NORTH);
        Room hoboCave = new Room(
                "The Hobo's Cave",
                "You are in a damp cave. There is a hole in the west wall and a long tunnel to the south with a faint light at the end.",
                new Item[] {},
                new Character[]{new Character("hobo", "He is an old man sitting in the corner, dressed in rags, holding a torch. He looks hungry.",
                        new Item[]{
                                new Weapon(new String[] {"steel sword", "sword", "steel"}, "The Hobo's steel sword.", 100),
                                new Item(new String[]{"the Hobo's torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.")
                        }, 100)}
        );
        corridor.connectTo(hoboCave, Side.EAST);
        Room ladderRoom = new Room(
                "West end of corridor",
                "You are in a small corridor going east.  There is a ladder in the wall here, leading through a hole in the ceiling.",
                new Item[]{},
                new Character[]{}
        );
        corridor.connectTo(ladderRoom, Side.WEST);
        return cell;
    }

    /**
     * Does nothing
     * @param a a useless parameter
     */
    public static void addScore(int a) {}

    /**
     * Check an array for trailing elements, and print an error message if present
     * @param o the array to check
     * @param n the maximum length it should have
     * @return whether the array passes
     */
    public static boolean trailing(Object[] o, int n) {
        if(o.length > n) {
            System.out.println("Too much information!");
            return true;
        }
        return false;
    }

    /**
     * Make the first character of a string capital
     * @param s the string to change
     * @return the string after changing the capitalization
     */
    public static String firstCapital(String s) {
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    /**
     * Parse a command
     * @param str the command to parse
     */
    public static void readCommand(String str) {
        String[] t = str.toLowerCase().split(" ");
        // check if they even typed anything
        if(t.length == 0 || t[0].length() == 0) {
            System.out.println("What do you want to do?");
            return;
        }
        // workaround for go
        boolean go = t[0].equals("go");
        if(go) {
            if(!trailing(t,2)) {
                if(t.length == 1) {
                    System.out.println("Go where?");
                    return;
                }
                else {
                    t = new String[]{t[1]};
                }
            }
            else {
                return;
            }
        }
        switch(t[0]) {
            case "north":
            case "n":
            case "south":
            case "s":
            case "west":
            case "w":
            case "east":
            case "e":
            case "down":
            case "d":
            case "up":
            case "u":
                if(!trailing(t,1)) {
                    // go that direction
                    goTo(room.goDir(Side.fromChar(t[0].charAt(0))));
                }
                break;
            case "quit":
                if(!trailing(t,1)) {
                    System.out.println("Goodbye.");
                    System.exit(0);
                }
                break;
            case "take":
            case "get":
            case "pickup":
            case "obtain":
                // take all
                if (t.length==2 && t[1].equals("all")) {
                    int numItems = room.getItems().length;
                    for (int i = 0; i < numItems; i++) {
                        Item it = room.takeItem(0);
                        System.out.println("You pick up " + it.getName() + ".");
                        player.addToInventory(it);
                    }
                    break;
                }
                // strip out "the" and "a"
                if(t.length==3 && (t[1].equalsIgnoreCase("the") || t[1].equalsIgnoreCase("a"))) {
                    t = new String[] {t[0], t[2]};
                }
                if(t.length == 1) {
                    System.out.println(firstCapital(t[0])+" what?");
                    break;
                }
                if(!trailing(t,2)) {
                    boolean found = false;
                    // loop through items in room
                    Item[] roomItems = room.getItems();
                    for (int i = 0; i < roomItems.length; i++) {
                        if (roomItems[i].matches(t[1])) {
                            // found a match!
                            Item it = room.takeItem(i);
                            System.out.println("You pick up " + it.getName() + ".");
                            player.addToInventory(it);
                            found = true;
                            break;
                        }
                    }
                    if(found) break;
                    // check omnipresents
                    for (int i = 0; i < omnipresent.length; i++) {
                        if (omnipresent[i].equalsIgnoreCase(t[1])) {
                            System.out.println("You can't " + t[0] + " that.");
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        System.out.println("I don't see that here.");
                    }
                }
                break;
            case "attack":
            case "kill":
                // "attack" and "kill" actually just rearrange t and leave it to "use" to handle it
                if(!trailing(t,4)) {
                    if(t.length == 1) {
                        System.out.println(firstCapital(t[0])+" what?");
                        break;
                    }
                    else if(t.length == 2) {
                        System.out.println("With what do you wish to "+t[0]+"?");
                        break;
                    }
                    else if(t.length == 3) {
                        System.out.println("I don't know how to do that.");
                        break;
                    }
                    else {
                        t = new String[] {"use", t[3], "on", t[1]};
                    }
                }
            case "use":
                if(!trailing(t,4)) {
                    if(t.length == 1) {
                        System.out.println("Use what?");
                    }
                    else if(t.length == 3) {
                        System.out.println("I don't know how to do that.");
                    }
                    else {
                        // look for a match in inventory
                        Item[] items = player.getInventory();
                        Item item = null;
                        for(int i = 0; i < items.length; i++) {
                            if(items[i].matches(t[1])) {
                                item = items[i];
                                break;
                            }
                        }
                        // check if it's hands
                        if(Character.hands.matches(t[1])) {
                            item = Character.hands;
                        }
                        if(item == null) {
                            System.out.println("You don't have that.");
                        }
                        else if(item instanceof Usable) {
                            // It's usable!
                            if(t.length == 2) {
                                // simple use
                                ((Usable)item).use();
                            }
                            else {
                                // search for character to use on
                                Character[] roomChars = room.getCharacters();
                                for(int i = 0; i < roomChars.length; i++) {
                                    if(roomChars[i].getName().equalsIgnoreCase(t[3])) {
                                        ((Usable)item).use(roomChars[i]);
                                        break;
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println("You can't use that.");
                        }
                    }
                }
                break;
            case "inventory":
            case "i":
                if(!trailing(t,1)) {
                    System.out.println("You have the following items:");
                    Item[] playerItems = player.getInventory();
                    for (int i = 0; i < playerItems.length; i++) {
                        System.out.println(playerItems[i].getInventoryEntry());
                    }
                }
                break;
            case "look":
            case "l":
            case "ls":
            case "examine":
            case "what":
                if(t.length == 3 && t[1].equals("at")) {
                    t = new String[] {t[0], t[2]};
                }
                if(trailing(t,2)) {}
                else if(t.length == 1) {
                    // look at room
                    System.out.println(room.getFullDescription(false));
                }
                else {
                    // look at item or character
                    IDescribable item = null;
                    // check player inventory first
                    Item[] inv = player.getInventory();
                    for(Item i : inv) {
                        if(i.matches(t[1])) {
                            item = i;
                            break;
                        }
                    }
                    if(item == null) {
                        // no match yet, check items in room
                        Item[] roomItems = room.getItems();
                        for (Item i : roomItems) {
                            if (i.matches(t[1])) {
                                item = i;
                                break;
                            }
                        }
                        if(item == null) {
                            // still no match, check characters in room
                            Character[] roomChars = room.getCharacters();
                            for(Character c : roomChars) {
                                if(c.getName().equals(t[1])) {
                                    item = c;
                                    break;
                                }
                            }
                        }
                    }
                    if(item != null) {
                        // Yay, got a match!
                        System.out.println(item.getDescription());
                    }
                    else {
                        // Aww, no match.
                        System.out.println("I don't see that here.");
                    }
                }
                break;
            case "drop":
                if(!trailing(t,2)) {
                    // look through player inventory
                    Item[] playerItems = player.getInventory();
                    for(int i = 0; i < playerItems.length; i++) {
                        if(playerItems[i].matches(t[1])) {
                            // found it!
                            Item it = player.takeItem(i);
                            room.addItem(it);
                            System.out.println("Dropped "+it.getName()+".");
                        }
                    }
                }
                break;
            default:
                System.out.println("What would it mean to "+(go?"go ":"")+t[0]+"?");
        }
    }

    /**
     * Go into a room and print info
     * @param r the room to enter
     */
    public static void goTo(Room r) {
        if(r==null) {
            System.out.println("You can't go that way.");
            return;
        }
        room = r;
        boolean v = r.getVisited();
        r.visit();
        System.out.println(r.getFullDescription(v));
    }

    /**
     * Show death message
     */
    public static void kill() {
        System.out.println("You are dead.");
        System.exit(0);
    }

    /**
     * Check if the current room is lit
     * @return whether the current room is lit
     */
    public static boolean isLight() {
        // check if room is always lit (outside, starting cell, etc.)
        if(room.isAlwaysLit()) {
            return true;
        }
        // check for light source in player's inventory
        Item[] playerItems = player.getInventory();
        for(Item i : playerItems) {
            if(i.matches("torch")) {
                // found the torch!
                return true;
            }
        }
        // check for light source on room floor
        Item[] roomItems = room.getItems();
        for(Item i : roomItems) {
            if(i.matches("torch")) {
                // found the torch
                return true;
            }
        }
        // check for light source in NPC's inventories
        Character[] roomChars = room.getCharacters();
        for(Character c : roomChars) {
            Item[] charItems = c.getInventory();
            for(Item i : charItems) {
                if(i.matches("torch")) {
                    // found the torch
                    return true;
                }
            }
        }
        // if not lit
        return false;
    }
}
