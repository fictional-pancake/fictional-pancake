public class CharacterLock extends Character {
    /**
     * The door this lock controls
     */
    private CharacterDoor door;

    /**
     * Construct a Lock
     *
     * @param name        the name of the lock
     * @param description the description of the lock
     * @param door        the door opened by the lock
     */
    public CharacterLock(String name, String description, CharacterDoor door) {
        super(name, description, new Item[]{});
        this.door = door;
    }

    /**
     * Get the door that this lock controls
     *
     * @return the door controlled by this lock
     */
    public CharacterDoor getDoor() {
        return this.door;
    }

    @Override
    public void damage(double damage) {
        System.out.println("You really expected that to work?");
    }
}