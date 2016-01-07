public class CharacterCollector extends Character {
    public CharacterCollector(String name, String description, Item[] inventory) {
        super(name, description, inventory);
    }

    @Override
    public boolean give(Item item) {
        if(item.matches("arrowhead")) {
            addToInventory(item);
            Item[] inv = getInventory();
            for(int i = 0; i < inv.length; i++) {
                if(inv[i] instanceof ItemLeaf) {
                    System.out.println("\"Now I can finally complete my collection! Here, have some trinket I found.\"");
                    Item it = takeItem(i);
                    System.out.println("He hands you "+it.getName()+".");
                    Main.player.addToInventory(it);
                    return true;
                }
            }
            System.out.println("\"Thanks I guess. You can't have anything else of mine, get out of here.\"");
            return true;
        }
        else {
            System.out.println("\"What is that supposed to be?\"");
            return false;
        }
    }

    @Override
    public void damage(double damage) {
        System.out.println("\"Hahaha, you can't kill me. Give me what I need or get out of here.\"");
    }
}
