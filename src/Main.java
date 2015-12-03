import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static final Room nullRoom = new Room("Unimplemented Room", "You have stumbled upon a room that hasn't been coded yet.  There is no escape.");
    private static Room room;
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        goTo(nullRoom);
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
                goTo(room.goNorth());
                break;
            case "south":
            case "s":
                goTo(room.goSouth());
                break;
            case "west":
            case "w":
                goTo(room.goWest());
                break;
            case "east":
            case "e":
                goTo(room.goEast());
                break;
            case "down":
            case "d":
                goTo(room.goDown());
                break;
            case "up":
            case "u":
                goTo(room.goUp());
                break;
            case "quit":
                System.out.println("Goodbye.");
                System.exit(0);
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
