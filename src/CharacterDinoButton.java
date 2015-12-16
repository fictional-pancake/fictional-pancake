public class CharacterDinoButton extends Character implements IUsable {

    private boolean used = false;
    private Room room1;
    private Room room2;
    public CharacterDinoButton(String name, String description, Room r1, Room r2) {
        super(name, description, new Item[]{});
        room1 = r1;
        room2 = r2;
    }

    @Override
    public void use() {
        System.out.println("You don't notice anything happen.");
        if(!used) {
            used = true;
            room1.setDescription(room1.getDescription()+"  There is a hole in the floor here.");
            room1.setDir(Side.DOWN, room2);
        }
    }

    @Override
    public void use(Character c) {
        System.out.println("You can't do that.");
    }
}
