public class CharacterLamb extends Character {
    private int side;
    public CharacterLamb(String name, String description, Item[] inventory, int health, int side) {
        super(name, description, inventory, health);
        this.side = side;
    }

    @Override
    public String getEntry() {
        return "There is a "+getName()+" here, blocking the "+Side.getName(side)+" exit.";
    }

    @Override
    public boolean isBlockingExit(int side) {
        return side == this.side;
    }
}
