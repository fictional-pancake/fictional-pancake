public class Usable extends Item {

    public Usable(String name, String description) {
        super(name, description);
    }
    public Usable(String[] names, String description) {
        super(names, description);
    }

    public void use() {}
    public void use(Character c) {}
}
