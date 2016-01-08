public class CharacterLeafLever extends Character implements IUsable {

    /**
     * Room to connect to the current room (should be win room)
     */
    private Room roomToConnect;

    /**
     * CharacterLeafHole
     */
    private CharacterLeafHole leafHoles;

    /**
     * Number of leaves needed to open win room
     */
    private int numLeaves;

    public CharacterLeafLever(String name, String description, Room roomToConnect, CharacterLeafHole leafHoles, int numLeaves) {
        super(name, description, new Item[]{});
        this.roomToConnect = roomToConnect;
        this.leafHoles = leafHoles;
        this.numLeaves = numLeaves;
    }

    @Override
    public void use() {
        if (leafHoles.getInventory().length == numLeaves) {
            // open the win room
            Main.getCurrentRoom().connectTo(roomToConnect, Side.SOUTH);
            Main.getCurrentRoom().setDescription(Main.getCurrentRoom().getDescription()+" There is an opening to the south.");
            System.out.println("You hear a crumbling noise to the south.");
        } else {
            System.out.println("It seems to have no effect.");
        }
    }

    @Override
    public void use(Character c) {
        System.out.println("You can't use that on a character!");
    }

    @Override
    public void damage(double damage) {
        System.out.println("Trying to attack a lever? Do you need me to refer you to a psychologist?");
    }
}
