public class Side {
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    public static final int UP = 4;
    public static final int DOWN = 5;
    public static int[] opposite = {SOUTH, NORTH, WEST, EAST, DOWN, UP};
    public static int fromChar(char c) {
        switch(c) {
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
                throw new IllegalArgumentException("No direction for char "+c);
        }
    }
}
