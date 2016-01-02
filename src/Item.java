public class Item implements IDescribable {
    /**
     * The valid names of the item
     */
    protected String[] names;

    /**
     * The description of the item
     */
    private String description;

    /**
     * The weight of the item
     */
    private int weight;

    /**
     * Get the primary name for this item
     *
     * @return the primary name of the item
     */
    public String getName() {
        return names[0];
    }

    /**
     * Get the inventory entry for this item
     *
     * @return the inventory entry for this item
     */
    public String getInventoryEntry() {
        return " - " + getName();
    }

    public String getDescription() {
        return description;
    }

    /**
     * Construct an item with a name and a description
     *
     * @param name        the name of the item
     * @param description the description of the item
     */
    @Deprecated
    public Item(String name, String description) {
        this(new String[]{name}, description);
    }

    /**
     * Construct an item with names and a description, weight will default to 100
     *
     * @param names       the names of the item
     * @param description the description of the item
     */
    public Item(String[] names, String description) {
        this(names, description, 100);
    }

    /**
     * Construct an item with names, a description, and weight
     *
     * @param names       the names of the item
     * @param description the description of the item
     * @param weight      the weight value of the item
     */
    public Item(String[] names, String description, int weight) {
        this.names = names;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Check whether this item matches a string
     *
     * @param inp the string to check against
     * @return whether this item matches the string
     */
    public boolean matches(String inp) {
        for (int i = 0; i < names.length; i++) {
            if (inp.equalsIgnoreCase(names[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Perform an action when the item is picked up
     */
    public void onPickup() {

    }

    /**
     * Get the weight of the object
     *
     * @return the weight of the object
     */
    public int getWeight() {
        return this.weight;
    }
}
