public class CharacterBuffalo extends Character {
    public CharacterBuffalo(String name, String description, Item[] inventory) {
        super(name, description, inventory);
    }

    @Override
    public String getDescription() {
        return super.getDescription()+(Main.player.hasItem("orb")?"  Your orb glows a bit brighter when near it.":"");
    }
}
