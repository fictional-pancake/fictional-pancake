public class ExitBlockingCharacter extends Character {

    /**
     * Construct a character with a name, a description, a starting inventory, and a starting health value
     * @param name The Character's name
     * @param description The Character's description
     * @param inventory The Character's starting inventory
     * @param health The Character's starting health value
     */
    public ExitBlockingCharacter(String name, String description, Item[] inventory, double health) {
        super(name, description, inventory, health);
    }

    @Override
    public String getEntry() {
        return "There is a "+getName()+" here, blocking all exits.";
    }
}
