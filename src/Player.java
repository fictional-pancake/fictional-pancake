import java.util.List;

public class Player extends Character {
    /**
     * Construct a player with a starting inventory
     * @param i the starting inventory
     */
    public Player(Item[] i) {
        super("player", "It's you!", i, 20);
    }
}
