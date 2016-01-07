public class CharacterTeleportButton extends Character implements IUsable {
    private Room dest;
    public CharacterTeleportButton(String name, String description, Room dest) {
        super(name, description, null);
        this.dest = dest;
    }

    @Override
    public void use() {
        System.out.println("Whoosh!");
        Main.goTo(dest);
    }

    @Override
    public void use(Character c) {
        System.out.println("You can't do that.");
    }
}
