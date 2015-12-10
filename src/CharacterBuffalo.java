public class CharacterBuffalo extends Character {
    public CharacterBuffalo(String name, String description, Item[] inventory) {
        super(name, description, inventory);
    }

    @Override
    public String getDescription() {
        return super.getDescription()+(Main.player.hasItem("orb")?"  Your orb glows a bit brighter when near it.":"");
    }

    @Override
    public void damage(double damage) {
        super.damage(damage);
        if(getHealth()<=0) {
            Item[] inv = Main.player.getInventory();
            for(Item i : inv) {
                if(i instanceof ItemOrb) {
                    Main.player.removeItem(i);
                    System.out.println("The orb explodes violently, killing you.");
                    Main.kill();
                }
            }
        }
    }
}
