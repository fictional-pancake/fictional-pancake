public class DisgustingFood extends Item implements IEdible {

    public DisgustingFood(String[] names, String description, int weight) {
        super(names, description, weight);
    }

    @Override
    public void eat(String action) {
        System.out.println("It doesn't look very good.  You can't bring yourself to " + action + " it.");
    }
}
