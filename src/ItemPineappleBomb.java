/**
 * A class representing a Usable pineapple bomb
 *
 * @author Tony Brar and Colin Reeder
 */
public class ItemPineappleBomb extends Usable {
    /**
     * Constructor for pineapple bombs that uses the Usable constructor
     *
     * @param names       the names for the ItemPineappleBomb
     * @param description the description of the ItemPineappleBomb
     * @param weight      the weight of the ItemPineappleBomb
     */
    public ItemPineappleBomb(String[] names, String description, int weight) {
        super(names, description, weight);
    }

    @Override
    public void use() {
        System.out.println("Hiss, hiss, hiss, BOOM! Suddenly, pineapples.");
        for (int i = 0; i < 10; i++) {
            Main.getCurrentRoom().addItem(new Food(new String[]{"a pineapple", "pineapple", "fruit"}, "It is a normal pineapple, indistinguishable from all the others. Dig in!", 30));
        }
        Main.getCurrentRoom().addItem(new ItemLeaf("red"));
        boolean painting = false;
        Item[] roomItems = Main.getCurrentRoom().getItems();
        for(int i = 0; i < roomItems.length; i++) {
            if(roomItems[i].matches("painting")) {
                painting = true;
            }
        }
        painting = painting || Main.player.hasItem("painting");
        if(painting) {
            System.out.println("The painting says:\nYOU HAVE REACHED LEVEL DANIELS");
        }
        Main.player.removeItem(this);
    }

    @Override
    public void use(Character c) {
        this.use();
    }
}
