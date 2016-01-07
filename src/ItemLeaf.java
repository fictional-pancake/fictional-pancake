/**
 * A leaf for being a leaf
 * @author Tony Brar and Colin Reeder
 */
public class ItemLeaf extends Item {
    public ItemLeaf(String color) {
        super(new String[]{"a " + color + " crystalline leaf", "leaf", color, "crystal", "crystalline"}, "A shiny " + color + " crystalline leaf.", 5);
    }
}
