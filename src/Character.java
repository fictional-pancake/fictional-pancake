import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Character {
    private String name;
    private String description;
    private List<Item> inventory;
    private double health;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Character(String name, String description, Item[] inventory) {
        this(name, description, inventory, 1);
    }

    public Character(String name, String description, Item[] inventory, double health) {
        this.name = name;
        this.description = description;
        this.inventory = new ArrayList<Item>();
        for(int i = 0; i < inventory.length; i++) {
            this.inventory.add(inventory[i]);
        }
        this.health = health;
    }

    public String getEntry() {
        return "There is a "+getName()+" here.";
    }

    public void damage(double damage) {
        this.health -= damage;
        System.out.println("You hit the "+getName()+".");
        if(health <= 0) {
            System.out.println("The "+getName()+" died.");
            Room r = Main.getCurrentRoom();
            r.removeCharacter(this);
            Iterator<Item> it = inventory.iterator();
            while(it.hasNext()) {
                Item i = it.next();
                r.addItem(i);
            }
        }
    }
    public void addToInventory(Item thing) {
        inventory.add(thing);
    }
}
