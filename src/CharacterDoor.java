import java.util.ArrayList;

/**
 * A door for being a door
 * @author Tony Brar and Colin Reeder
 */
public class CharacterDoor extends Character implements IUsable {
    /**
     * Whether the door is open
     */
    LinkableBoolean open = new LinkableBoolean();

    /**
     * Whether the door is locked
     */
    LinkableBoolean locked = new LinkableBoolean(false);

    /**
     * The side the door is on
     */
    private int side;

    /**
     * Construct a Door
     *
     * @param name        the name of the door
     * @param description the description of the door
     * @param side        the side the door is on
     * @param open        whether the door is open
     * @param locked      whether the door is locked
     */
    public CharacterDoor(String name, String description, int side, boolean open, boolean locked) {
        super(name, description, null);
        this.side = side;
        this.open.value = open;
        this.locked.value = locked;
    }

    /**
     * Construct a Door
     *
     * @param name        the name of the door
     * @param description the description of the door
     * @param side        the side the door is on
     * @param open        whether the door is open
     */
    public CharacterDoor(String name, String description, int side, boolean open) {
        this(name, description, side, open, false);
    }

    /**
     * Construct a Door
     *
     * @param name        the name of the door
     * @param description the description of the door
     * @param side        the side the door is on
     * @param open        whether the door is open
     * @param locked      whether the door is locked
     */
    public CharacterDoor(String name, String description, int side, LinkableBoolean open, LinkableBoolean locked) {
        super(name, description, null);
        this.side = side;
        this.open = open;
        this.locked = locked;
    }

    /**
     * Construct a Door
     *
     * @param name        the name of the door
     * @param description the description of the door
     * @param side        the side the door is on
     * @param open        whether the door is open
     */
    public CharacterDoor(String name, String description, int side, LinkableBoolean open) {
        super(name, description, null);
        this.side = side;
        this.open = open;
    }

    @Override
    public boolean isBlockingExit(int checkSide) {
        if (checkSide == this.side && !isOpen()) {
            return true;
        }
        return false;
    }

    /**
     * If the player attacks the door (doesn't actually do anything)
     *
     * @param damage the amount of damage to deal, 0 to not deal damage
     */
    @Override
    public void damage(double damage) {
        System.out.println("You can't attack a door. That would be silly!");
    }

    /**
     * Lock/Unlock the door
     */
    public void toggleLocked() {
        if (this.locked.value == true) {
            locked.value = false;
            System.out.println("You unlock the door.");
        } else {
            locked.value = true;
            System.out.println("You lock the door.");
        }
    }

    /**
     * Open/Close the door
     */
    public void toggleOpen() {
        if (this.locked.value == true) {
          System.out.println("The door is locked!");
          return;
        }
        if (!isOpen()) {
            open.value = true;
            System.out.println("You open the door.");
        } else {
            open.value = false;
            System.out.println("You close the door.");
        }
    }

    /**
     * Check whether the door is open
     *
     * @return whether the door is open
     */
    public boolean isOpen() {
        return open.value;
    }

    @Override
    public void use() {
        toggleOpen();
    }

    @Override
    public void use(Character c) {
        System.out.println("You can't do that.");
    }

    @Override
    public String getEntry() {
        return "";
    }
}
