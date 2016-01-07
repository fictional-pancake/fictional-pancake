/**
 * Character that holds leaves
 * @author Colin Reeder and Tony Brar
 */
public class CharacterLeafHole extends Character {
    /**
     * The number of holes
     */
    private int numHoles;

    /**
     * The room to connect
     */
    private Room room1;

    /**
     * The room to connect to
     */
    private Room room2;

    /**
     * Construct a set of leaf holes
     * @param numHoles the number of holes
     * @param room1 the room to connect
     * @param room2 the room to connect to
     */
    public CharacterLeafHole(int numHoles, Room room1, Room room2) {
        super("hole", "It is a small elliptical hole in the ground, identical to the rest.", new Item[]{});
        this.numHoles = numHoles;
        this.room1 = room1;
        this.room2 = room2;
    }

    @Override
    public boolean give(Item item) {
        if(item instanceof ItemLeaf) {
            addToInventory(item);
            System.out.println("You place the leaf in the hole.");
            if(getInventory().length >= numHoles) {
                room1.setDir(Side.SOUTH, room2);
                room1.setDescription(room1.getDescription()+"  There is an opening to the south.");
                System.out.println("You hear a crumbling noise to the south.");
            }
            return true;
        }
        else {
            System.out.println("It doesn't seem to fit.");
            return false;
        }
    }

    @Override
    public String getEntry() {
        Item[] inv = getInventory();
        if(inv.length == 0) {
            return "";
        }
        else if(inv.length == 1) {
            return "One hole contains "+inv[0].getName()+".";
        }
        else {
            String tr = "";
            for(int i = 0; i < inv.length; i++) {
                tr += inv[i].names[2];
                if(i < inv.length-1) {
                    if(inv.length != 2) {
                        tr += ",";
                    }
                    tr += " ";
                }
                if(i == inv.length-2) {
                    tr += "and ";
                }
            }
            tr += " crystalline leaves are in the holes.";
            return Main.firstCapital(tr);
        }
    }
}
