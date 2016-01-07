/**
 * A key for being a key
 * @author Tony Brar and Colin Reeder
 */
public class ItemKey extends Usable {

    /**
     * The name of the lock the Key works on
     */
    private String lock;

    /**
     * Constructor for an ItemKey
     * @param names names the Key can be referred to as
     * @param description description of the Key
     * @param weight weight of the Key
     * @param lock name of the lock the Key works on
     */
    public ItemKey(String[] names, String description, int weight, String lock) {
        super(names, description, weight);
        this.lock = lock;
    }

    @Override
    public void use() {
        Character[] roomChars = Main.getCurrentRoom().getCharacters();
        for (int i = 0; i < roomChars.length; i++) {
            if (roomChars[i] instanceof CharacterLock) {
                this.use(roomChars[i]);
                return;
            }
        }
        System.out.println("Use on what?");
    }

    @Override
    public void use(Character c) {
        if (!(c.matches(lock))) {
            System.out.println("This item must be used on the correct lock!");
        }
        else {
            ((CharacterLock) c).getDoor().toggleLocked();
        }
    }
}
