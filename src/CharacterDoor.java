import java.util.ArrayList;

public class CharacterDoor extends Character implements IUsable {
    LinkableBoolean open = new LinkableBoolean();
    private int side;
    public CharacterDoor(String name, String description, int side, boolean open) {
        super(name, description, new Item[]{});
        this.side = side;
        this.open.value = open;
    }
    public CharacterDoor(String name, String description, int side, LinkableBoolean open) {
        super(name, description, new Item[]{});
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
     * @param damage the amount of damage to deal, 0 to not deal damage
     */
    @Override
    public void damage(double damage) {
        System.out.println("You can't attack a door. That would be silly!");
    }

    public void toggleOpen() {
        if (!isOpen()) {
            open.value = true;
            System.out.println("You open the door.");
        }
        else {
            open.value = false;
            System.out.println("You close the door.");
        }
    }

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
