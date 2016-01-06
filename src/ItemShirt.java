/**
 * An item that drops items when picked up
 * @author Colin Reeder and Tony Brar
 */
public class ItemShirt extends Item {
    /**
     * The items contained in this shirt
     */
    private Item[] items;

    /**
     * Construct a shirt
     *
     * @param names       the names for the shirt
     * @param description the description of the shirt
     * @param items       the items in the shirt
     * @param weight      the weight of the shirt
     */
    public ItemShirt(String[] names, String description, Item[] items, int weight) {
        super(names, description, weight);
        this.items = items;
    }

    @Override
    public void onPickup() {
        if (items != null && items.length > 0) {
            System.out.println("As you pick it up, something falls out.");
            for (int i = 0; i < items.length; i++) {
                Main.getCurrentRoom().addItem(items[i]);
            }
            items = null;
        }
    }
}
