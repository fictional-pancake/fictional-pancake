/**
 * Food for being food
 * @author Tony Brar and Colin Reeder
 */
public class Food extends Item implements IEdible {

    public Food(String[] names, String description, int weight) {
        super(names, description, weight);
    }

    @Override
    public void eat(String action) {
        System.out.println("You " + action + " " + names[0] + ".");
        Main.player.removeItem(this);
    }
}
