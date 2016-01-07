import java.util.List;

public class CharacterVolcano extends Character {

    public CharacterVolcano(String name, String description, Item[] inventory) {
        super(name, description, inventory);
    }

    @Override
    public boolean give(Item item) {
        if (item.matches("powder") || item.matches("vinegar")) {
            inventory.add(item);
            System.out.println("You add " + item.getName() + " to the volcano.");
            // check if the volcano has both ingredients
            if (this.hasItem("powder") && this.hasItem("vinegar")) {
                // erupt
                System.out.println("The volcano violently erupts in an awesome display of vinegar and baking soda fueled power! You notice a small brown item in the wave of \"lava\".");
                Main.getCurrentRoom().addItem(new ItemLeaf("yellow"));
            }
            return true;
        } else {
            System.out.println("Why would you put such a thing in a volcano?");
            return false;
        }
    }

    @Override
    public void damage(double damage) {
        System.out.println("You really expected that to work?");
    }
}