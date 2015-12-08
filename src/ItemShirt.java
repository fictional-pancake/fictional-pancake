public class ItemShirt extends Item {
    private Item[] items;
    public ItemShirt(String[] names, String description, Item[] items) {
        super(names, description);
        this.items = items;
    }
    @Override
    public void onPickup() {
        if(items != null && items.length > 0) {
            System.out.println("As you pick it up, something falls out.");
            for(int i = 0; i < items.length; i++) {
                Main.getCurrentRoom().addItem(items[i]);
            }
            items = null;
        }
    }
}
