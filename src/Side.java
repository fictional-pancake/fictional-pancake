/**
 * A class for working with directions
 * @author Colin Reeder and Tony Brar
 */
public class Side {
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    public static final int UP = 4;
    public static final int DOWN = 5;

    /**
     * Map directions to their opposites
     */
    public static int[] opposite = {SOUTH, NORTH, WEST, EAST, DOWN, UP};

    /**
     * Map directions to their names
     */
    public static String[] names = {"north", "south", "east", "west", "up", "down"};

    /**
     * Return a direction, given the first character
     *
     * @param c the character to use
     * @return the direction associated with that character
     */
    public static int fromChar(char c) {
        switch (c) {
            case 'n':
                return NORTH;
            case 's':
                return SOUTH;
            case 'e':
                return EAST;
            case 'w':
                return WEST;
            case 'u':
                return UP;
            case 'd':
                return DOWN;
            default:
                throw new IllegalArgumentException("No direction for char " + c);
        }
    }

    /**
     * Get the name for a side
     * @param side the side to check
     * @return the name for the side
     */
    public static String getName(int side) {
        return names[side];
    }
}
