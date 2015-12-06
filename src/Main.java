import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static final Room nullRoom = new Room("Unimplemented Room", "You have stumbled upon a room that hasn't been coded yet.  There is no escape.", new Item[]{}, new Character[]{});
    public static final Player player = new Player(new Item[]{});
    public static final String[] omnipresent = {"narrator", "you", "sky", "air", "game", "room", "code", "intelligence"};
    private static Room room;
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        goTo(setupRooms());
        System.out.println("The guard sets down some food and a spork.");
        room.addItem(new Item(new String[]{"some food", "food", "cabbage"}, "It appears to be cabbage."));
        room.addItem(new Weapon(new String[]{"a spork", "spork", "fork", "spoon"}, "It is approximately 60% spoon and 40% fork.", 5));
        while(true) {
            System.out.print(">");
            String cmd;
            try {
                cmd = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            readCommand(cmd);
        }
    }
    public static Room getCurrentRoom() {
        return room;
    }
    public static Room setupRooms() {
        Room cell = new Room(
                "Cell",
                "You are in an old-looking prison cell.  It is dimly lit by a torch outside.  There is a door to the north.",
                new Item[] {},
                new Character[]{new ExitBlockingCharacter("guard", "He looks tired, and he smells of onions.", new Item[]{})}
        );
        Room corridor = new Room("Corridor",
                "You are in a small corridor.  There is a open door to the south, and a hole to the east.",
                new Item[]{new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.")},
                new Character[]{}
        );
        cell.connectTo(corridor, Side.NORTH);
        Room hoboCave = new Room(
                "The Hobo's Cave",
                "You are in a damp cave. There is a hole in the west wall and a long tunnel to the south with a faint light at the end.",
                new Item[] {},
                new Character[]{new Character("hobo", "He is an old man sitting in the corner, dressed in rags. He looks hungry.",
                        new Item[]{new Weapon(new String[] {"sword", "steel sword", "steel"}, "The Hobo's steel sword.", 100)})}
        );
        corridor.connectTo(hoboCave, Side.EAST);
        return cell;
    }
    public static void addScore(int a) {}
    public static boolean trailing(Object[] o, int n) {
        if(o.length > n) {
            System.out.println("Too much information!");
            return true;
        }
        return false;
    }
    public static String firstCapital(String s) {
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }
    public static void readCommand(String str) {
        String[] t = str.split(" ");
        boolean go = t[0].equals("go");
        if(go) {
            if(!trailing(t,2)) {
                t = new String[] {t[1]};
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
                goTo(room.goDir(Side.fromChar(t[0].charAt(0))));
                break;
            case "quit":
                System.out.println("Goodbye.");
                System.exit(0);
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
                if(t.length==3 && (t[1].equalsIgnoreCase("the") || t[1].equalsIgnoreCase("a"))) {
                    t = new String[] {t[0], t[2]};
                }
                if(t.length == 1) {
                    System.out.println(firstCapital(t[0])+" what?");
                }
                if(!trailing(t,2)) {
                    boolean found = false;
                    Item[] roomItems = room.getItems();
                    for (int i = 0; i < roomItems.length; i++) {
                        if (roomItems[i].matches(t[1])) {
                            Item it = room.takeItem(i);
                            System.out.println("You pick up " + it.getName() + ".");
                            player.addToInventory(it);
                            found = true;
                            break;
                        }
                    }
                    if(found) break;
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
                if(!trailing(t,4)) {
                    if(t.length == 1) {
                        System.out.println(firstCapital(t[0])+" what?");
                    }
                    else if(t.length == 2) {
                        System.out.println("With what do you wish to "+t[0]+"?");
                    }
                    else if(t.length == 3) {
                        System.out.println("I don't know how to do that.");
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
                        Item[] items = player.getInventory();
                        Item item = null;
                        for(int i = 0; i < items.length; i++) {
                            if(items[i].matches(t[1])) {
                                item = items[i];
                                break;
                            }
                        }
                        if(item == null) {
                            System.out.println("I don't see that here.");
                        }
                        else if(item instanceof Usable) {
                            if(t.length == 2) {
                                ((Usable)item).use();
                            }
                            else {
                                Character[] roomChars = room.getCharacters();
                                for(int i = 0; i < roomChars.length; i++) {
                                    if(roomChars[i].getName().equalsIgnoreCase(t[3])) {
                                        ((Usable)item).use(roomChars[i]);
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
                System.out.println("You have the following items:");
                Item[] playerItems = player.getInventory();
                for(int i = 0; i < playerItems.length; i++) {
                    System.out.println(playerItems[i].getInventoryEntry());
                }
                break;
            default:
                System.out.println("What would it mean to "+(go?"go ":"")+t[0]+"?");
        }
    }
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
    public static void kill() {
        System.out.println("You are dead.");
        System.exit(0);
    }
}
