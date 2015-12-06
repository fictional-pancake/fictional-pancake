public class Weapon extends Usable {

    /**
     * The damage dealt by this weapon
     */
    private double damage;

    /**
     * Construct a weapon with a name and a description
     * @param name the name of this weapon
     * @param description the description of this weapon
     */
    @Deprecated
    public Weapon(String name, String description) {
        super(name, description);
        damage = 1;
    }

    /**
     * Construct a weapon with names, a description, and an amount of damage
     * @param names the names of this weapon
     * @param description the description of this weapon
     * @param damage the damage dealt by this weapon
     */
    public Weapon(String[] names, String description, double damage) {
        super(names, description);
        this.damage = damage;
    }

    public void use() {
        System.out.println("Use "+names[1]+" on what?");
    }

    public void use(Character c) {
        if(Math.random() < 0.2) {
            if(c instanceof Player) {
                System.out.println("He missed.");
            }
            else {
                System.out.println("You missed.");
            }
            c.damage(0);
        }
        else {
            c.damage(damage);
        }
    }
}
