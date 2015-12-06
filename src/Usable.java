public class Usable extends Item {

    /**
     * Construct a Usable with a name and a description
     * @param name the name of the Usable
     * @param description the description of the Usable
     */
    @Deprecated
    public Usable(String name, String description) {
        super(name, description);
    }

    /**
     * Construct a Usable with names and a description
     * @param names the names of the Usable
     * @param description the description of the Usable
     */
    public Usable(String[] names, String description) {
        super(names, description);
    }

    /**
     * Use the Usable
     */
    public void use() {}

    /**
     * Use the Usable on a Character
     * @param c the Character to use the Usable on
     */
    public void use(Character c) {}
}
