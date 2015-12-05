import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static final Room nullRoom = new Room("Unimplemented Room", "You have stumbled upon a room that hasn't been coded yet.  There is no escape.", new Item[]{}, new Character[]{});
    public static final String[] omnipresent = {"narrator", "you", "sky", "air", "game", "room", "code", "intelligence"};
    private static Room room;
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        goTo(setupRooms());
        System.out.println("The guard sets down some food and a spork.");
        room.addItem(new Item(new String[]{"some food", "food", "cabbage"}, "It appears to be cabbage."));
        room.addItem(new Item(new String[]{"a spork", "spork", "fork", "spoon"}, "It is approximately 60% spoon and 40% fork."));
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
    public static Room setupRooms() {
        Room cell = new Room(
                "Cell",
                "You are in an old-looking prison cell.  It is dimly lit by a torch outside.  There is a door to the north.",
                new Item[] {},
                new Character[]{new ExitBlockingCharacter("a guard", "He looks tired, and he smells of onions.", new Item[]{})});
        Room corridor = new Room("Corridor", "You are in a small corridor.  There is a open door to the south, and a hole to the east.", new Item[]{new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.")}, new Character[]{});
        cell.connectTo(corridor, Side.NORTH);
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
                if(t.length==3 && (t[1].equalsIgnoreCase("the") || t[1].equalsIgnoreCase("a"))) {
                    t = new String[] {t[0], t[2]};
                }
                if(t.length == 1) {
                    System.out.println(t[0].substring(0,1).toUpperCase()+t[0].substring(1)+" what?");
                }
                if(!trailing(t,2)) {
                    boolean found = false;
                    Item[] roomItems = room.getItems();
                    for (int i = 0; i < roomItems.length; i++) {
                        if (roomItems[i].matches(t[1])) {
                            Item it = room.takeItem(i);
                            System.out.println("You pick up " + it.getName() + ".");
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
}
