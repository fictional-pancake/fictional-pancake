import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Character implements IDescribable {
    private String name;
    private String description;
    private List<Item> inventory;
    private double health;

    public static final Weapon hands = new Weapon(new String[] {"pair of hands", "hands"}, "You can't tell much about it.", 1);

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
        // reduce health
        this.health -= damage;
        if(damage != 0) {
            // damage was actually done
            if (this instanceof Player) {
                // player being attacked
                System.out.println("He strikes back!");
            } else {
                // player attacking
                System.out.println("You hit the " + getName() + ".");
            }
        }
        if(health <= 0) {
            // character is dead
            if(this instanceof Player) {
                // player is dead
                Main.kill();
            }
            else {
                System.out.println("The " + getName() + " died.");
            }
            // drop inventory
            Room r = Main.getCurrentRoom();
            r.removeCharacter(this);
            Iterator<Item> it = inventory.iterator();
            while(it.hasNext()) {
                Item i = it.next();
                r.addItem(i);
            }
        }
        else if(!(this instanceof Player)) {
            // NPC, fight back
            Iterator<Item> it = inventory.iterator();
            while(it.hasNext()) {
                Item i = it.next();
                if(i instanceof Weapon) {
                    ((Usable)i).use(Main.player);
                    return;
                }
            }
            // if all else fails, use hands
            hands.use(Main.player);
        }
    }
    public void addToInventory(Item thing) {
        inventory.add(thing);
    }

    public Item takeItem(int index) {
        return inventory.remove(index);
    }

    public Item[] getInventory() {
        return inventory.toArray(new Item[0]);
    }
}
