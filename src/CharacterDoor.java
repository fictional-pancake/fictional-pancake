import java.util.ArrayList;

public class CharacterDoor extends Character {
    private boolean open;
    private int side;
    public CharacterDoor(String name, String description, int side, boolean open) {
        super(name, description, new Item[]{});
        this.side = side;
        this.open = open;
    }

    /**
     * Check if the door is blocking the given exit
     * @param checkSide the number of the side to check for being blocked
     * @return true if that side is being blocked, else false
     */
    public boolean isBlockingExit(int checkSide) {
        if (checkSide == this.side) {
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
        if (this.open == false) {
            this.open = true;
        }
        else {
            this.open = false;
        }
    }
}
