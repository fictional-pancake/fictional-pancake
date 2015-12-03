public class Item {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getInventoryEntry() {
        return " - "+getName();
    }

    public String getDescription() {
        return description;
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
