/**
 * A hobo for being a hobo, rewards you for giving food
 * @author Tony Brar and Colin Reeder
 */
public class CharacterHobo extends Character {
    public CharacterHobo(String name, String description, Item[] inventory, double health) {
        super(name, description, inventory, health);
    }

    @Override
    public boolean give(Item item) {
        if(item instanceof IEdible) {
            addToInventory(item);
            Item[] inv = getInventory();
            for(int i = 0; i < inv.length; i++) {
                if(inv[i] instanceof ItemLeaf) {
                    System.out.println("\"Thanks, I was really hungry.  Take this.\"");
                    Item it = takeItem(i);
                    System.out.println("He hands you "+it.getName()+".");
                    Main.player.addToInventory(it);
                    return true;
                }
            }
            System.out.println("\"Thanks, but I don't have anything left to give you.\"");
            return true;
        }
        else {
            System.out.println("\"I don't need that, keep it.\"");
            return false;
        }
    }
}
