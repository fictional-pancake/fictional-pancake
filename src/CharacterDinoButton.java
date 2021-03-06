/**
 * A button to open holes in the floor
 * @author Colin Reeder and Tony Brar
 */
public class CharacterDinoButton extends Character implements IUsable {

    /**
     * Whether the button has already been used
     */
    private boolean used = false;

    /**
     * The room to connect
     */
    private Room room1;

    /**
     * The room to connect it to
     */
    private Room room2;

    /**
     * Construct a DinoButton
     *
     * @param name        the name of the button
     * @param description the description of the button
     * @param r1          the room to connect
     * @param r2          the room to connect it to
     */
    public CharacterDinoButton(String name, String description, Room r1, Room r2) {
        super(name, description, null);
        room1 = r1;
        room2 = r2;
    }

    @Override
    public void use() {
        if (!used) {
            System.out.println("You hear a faint rumbling below you. It's probably nothing.");
            used = true;
            room1.setDescription(room1.getDescription() + "  There is a hole in the floor here.");
            room1.setDir(Side.DOWN, room2);
        } else {
            System.out.println("You don't notice anything happen.");
        }
    }

    @Override
    public void use(Character c) {
        System.out.println("You can't do that.");
    }

    @Override
    public void damage(double damage) {
        use();
    }
}
