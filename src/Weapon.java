public class Weapon extends Usable {

    private double damage;
    public Weapon(String name, String description) {
        super(name, description);
        damage = 1;
    }

    public Weapon(String[] names, String description, double damage) {
        super(names, description);
        this.damage = damage;
    }

    public void use() {
        System.out.println("Use "+names[1]+" on what?");
    }

    public void use(Character c) {
        if(Math.random() < 0.2) {
            System.out.println("You missed.");
        }
        else {
            c.damage(damage);
        }
    }
}
