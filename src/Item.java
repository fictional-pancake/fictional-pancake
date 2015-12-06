public class Item {
    protected String[] names;
    private String description;

    public String getName() {
        return names[0];
    }

    public String getInventoryEntry() {
        return " - "+getName();
    }

    public String getDescription() {
        return description;
    }

    public Item(String name, String description) {
        this(new String[] {name}, description);
    }

    public Item(String[] names, String description) {
        this.names = names;
        this.description = description;
    }

    public boolean matches(String inp) {
        for(int i = 0; i < names.length; i++) {
            if(inp.equalsIgnoreCase(names[i])) {
                return true;
            }
        }
        return false;
    }
}
