import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private String description;
    private List<Item> inventory;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Character(String name, String description, Item[] inventory) {
        this.name = name;
        this.description = description;
        this.inventory = new ArrayList<Item>();
        for(int i = 0; i < inventory.length; i++) {
            this.inventory.add(inventory[i]);
        }
    }
}
