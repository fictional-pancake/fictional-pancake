/**
 * An orb for being an orb
 * @author Colin Reeder
 */
public class ItemOrb extends Usable {
    public ItemOrb(String[] names, String description, int weight) {
        super(names, description, weight);
    }

    public void use() {
        System.out.println("Nothing happened.");
    }

    public void use(Character c) {
        if (c instanceof CharacterBuffalo) {
            Main.player.removeItem(this);
            Main.getCurrentRoom().removeCharacter(c);
            Main.getCurrentRoom().addItem(new Item(new String[]{"a green crystalline leaf", "leaf", "green", "crystal", "crystalline"}, "It is a shiny green crystalline leaf.", 5));
            System.out.println("The orb and the buffalo disappear in a cloud of green smoke.  When the smoke clears, a small object is revealed.");
        } else {
            use();
        }
    }
}
