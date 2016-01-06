/**
 * Characters that don't do anything except being there
 * @author Colin Reeder and Tony Brar
 */
public class CharacterScenery extends Character {
    public CharacterScenery(String name, String description) {
        super(name, description, null);
    }

    @Override
    public void damage(double damage) {
        System.out.println("You can't do that.");
    }

    @Override
    public String getEntry() {
        return "";
    }
}
