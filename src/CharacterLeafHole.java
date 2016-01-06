/**
 * Character that holds leaves
 * @author Colin Reeder and Tony Brar
 */
public class CharacterLeafHole extends Character {
    public CharacterLeafHole() {
        super("hole", "It is a small elliptical hole in the ground, identical to the rest.", new Item[]{});
    }

    @Override
    public boolean give(Item item) {
        if(item.matches("leaf") && item.matches("crystalline")) {
            addToInventory(item);
            System.out.println("You place the leaf in the hole.");
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
