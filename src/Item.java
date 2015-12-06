public class Item implements IDescribable {
    /**
     * The valid names of the item
     */
    protected String[] names;

    /**
     * The description of the item
     */
    private String description;

    public String getName() {
        return names[0];
    }

    /**
     * Get the inventory entry for this item
     * @return the inventory entry for this item
     */
    public String getInventoryEntry() {
        return " - "+getName();
    }

    public String getDescription() {
        return description;
    }

    /**
     * Construct an item with a name and a description
     * @param name the name of the item
     * @param description the description of the item
     */
    @Deprecated
    public Item(String name, String description) {
        this(new String[] {name}, description);
    }

    /**
     * Construct an item with names and a description
     * @param names the names of the item
     * @param description the description of the item
     */
    public Item(String[] names, String description) {
        this.names = names;
        this.description = description;
    }

    /**
     * Check whether this item matches a string
     * @param inp the string to check against
     * @return whether this item matches the string
     */
    public boolean matches(String inp) {
        for(int i = 0; i < names.length; i++) {
            if(inp.equalsIgnoreCase(names[i])) {
                return true;
            }
        }
        return false;
    }
}
