/**
 * A keycard for being a keycard
 * @author Tony Brar and Colin Reeder
 */
public class ItemKeycard extends Usable {

    public ItemKeycard(String[] names, String description, int weight) {
        super(names, description, weight);
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
        if (!(c instanceof CharacterLock)) {
            System.out.println("This item must be used on a lock, not a "+c.getName());
        }
        else {
            ((CharacterLock) c).getDoor().toggleLocked();
        }
    }
}
