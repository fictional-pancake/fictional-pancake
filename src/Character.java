import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Character implements IDescribable {
    /**
     * The Character's name
     */
    private String name;

    /**
     * The Character's description
     */
    private String description;

    /**
     * The Character's inventory
     */
    private List<Item> inventory;

    /**
     * The Character's health
     */
    private double health;

    /**
     * A weapon reference to represent bare hands
     */
    public static final Weapon hands = new Weapon(new String[] {"pair of hands", "hands"}, "You can't tell much about it.", 1);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Construct a character with a name, a description, a starting inventory, and a default health of 1
     * @param name The Character's name
     * @param description The Character's description
     * @param inventory The Character's starting inventory
     */
    public Character(String name, String description, Item[] inventory) {
        this(name, description, inventory, 1);
    }

    /**
     * Construct a character with a name, a description, a starting inventory, and a starting health value
     * @param name The Character's name
     * @param description The Character's description
     * @param inventory The Character's starting inventory
     * @param health The Character's starting health value
     */
    public Character(String name, String description, Item[] inventory, double health) {
        this.name = name;
        this.description = description;
        this.inventory = new ArrayList<Item>();
        for(int i = 0; i < inventory.length; i++) {
            this.inventory.add(inventory[i]);
        }
        this.health = health;
    }

    /**
     * Get the room entry for this Character
     * @return the room entry for this Character
     */
    public String getEntry() {
        return "There is a "+getName()+" here.";
    }

    /**
     * Deal damage to this character and cause it to attack the player
     * @param damage the amount of damage to deal, 0 to not deal damage
     */
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

    /**
     * Add an item to the Character's inventory
     * @param thing the item to add
     */
    public void addToInventory(Item thing) {
        inventory.add(thing);
    }

    /**
     * Remove an item from this Character's inventory
     * @param index the index of the item to remove
     * @return the removed item
     */
    public Item takeItem(int index) {
        return inventory.remove(index);
    }

    /**
     * Get a list of items in the Character's inventory
     * @return a list of items in the Character's inventory
     */
    public Item[] getInventory() {
        return inventory.toArray(new Item[0]);
    }
}
