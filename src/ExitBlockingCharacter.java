public class ExitBlockingCharacter extends Character {

    public ExitBlockingCharacter(String name, String description, Item[] inventory, double health) {
        super(name, description, inventory, health);
    }

    @Override
    public String getEntry() {
        return "There is a "+getName()+" here, blocking all exits.";
    }
}
