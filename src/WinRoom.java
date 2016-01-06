public class WinRoom extends Room {

    public WinRoom(String name, String description, Item[] items, Character[] chars) {
        super(name, description, items, chars);
    }

    @Override
    public void visit() {
        super.visit();
        System.exit(0);
    }
}
