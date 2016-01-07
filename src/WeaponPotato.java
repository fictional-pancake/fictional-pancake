public class WeaponPotato extends Weapon {
    public WeaponPotato() {
        super(new String[]{"a potato battery", "potato", "battery", "taser"}, "It is a potato with wires coming out of it.  It makes a buzzing noise.", 20, 15);
    }

    @Override
    public void use() {
        System.out.println("And do what?");
    }

    @Override
    public void use(Character c) {
        System.out.println("Now you're thinking with potatoes");
        super.use(c);
    }
}
