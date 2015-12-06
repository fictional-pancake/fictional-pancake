public class ExitBlockingCharacter extends Character {

    public ExitBlockingCharacter(String name, String description, Item[] inventory) {
        super(name, description, inventory);
    }

    @Override
    public String getEntry() {
        return "There is a "+getName()+" here, blocking all exits.";
    }
}
