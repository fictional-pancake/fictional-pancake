import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main class for the game
 * You just lost the game
 * @author Colin Reeder and Tony Brar
 */
public class Main {
    /**
     * A room for temporary usage
     */
    public static final Room nullRoom = new Room("Unimplemented Room", "You have stumbled upon a room that hasn't been coded yet.  There is no escape.", new Item[]{}, new Character[]{});

    /**
     * The player object
     */
    public static final Player player = new Player(new Item[]{});

    /**
     * List of objects which always exist, but cannot be interacted with
     */
    public static final String[] omnipresent = {"narrator", "you", "sky", "air", "game", "room", "code", "intelligence", "cell", "floor", "ceiling"};

    /**
     * The current room
     */
    private static Room room;

    /**
     * The player's current score
     */
    private static double score;

    /**
     * Number of moves made
     */
    private static int moves;

    /**
     * Whether cheat mode is enabled
     */
    public static boolean cheatMode = false;

    /**
     * Run game
     *
     * @param args the argument array.
     */
    public static void main(String[] args) {
        HashMap<String, Boolean> optMap = new HashMap<String, Boolean>();
        optMap.put("tts", true);
        optMap.put("cheat", false);
        optMap.put("help", false);
        HashMap<String, Object> opts = parseOptions(args, new char[]{}, optMap);
        if(opts.containsKey("tts")) {
            System.setOut(new SpeakerStream((String)opts.get("tts")));
        }
        if(opts.containsKey("help")) {
            System.out.println("Possible options are:");
            System.out.println("\t--tts [program] (set program for TTS)");
            System.out.println("\t--cheat (enable cheat mode)");
            System.out.println("\t--help (print this message)");
            System.exit(0);
        }
        if(opts.containsKey("cheat")) {
            cheatMode = true;
        }
        //setup
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        goTo(setupRooms());
        System.out.println("The guard sets down some food and a spork.");
        room.addItem(new DisgustingFood(new String[]{"some food", "food", "cabbage"}, "It appears to be cabbage.", 40));
        room.addItem(new Weapon(new String[]{"a spork", "spork", "fork", "spoon"}, "It is approximately 60% spoon and 40% fork.  It looks quite sharp.", 10, 5));
        while (true) {
            System.out.print(">");
            String cmd;
            // read command
            try {
                cmd = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // parse and execute command
            readCommand(cmd);
            moves++;
        }
    }

    /**
     * Add to the player's running score
     *
     * @param add amount of score to add
     */
    public static void addScore(int add) {
        score += add;
    }

    /**
     * Get the current room
     *
     * @return the current room
     */
    public static Room getCurrentRoom() {
        return room;
    }

    /**
     * Set up the rooms
     *
     * @return the starting room
     */
    public static Room setupRooms() {
        LinkableBoolean cellDoor = new LinkableBoolean();
        cellDoor.value = true;
        Room cell = new Room(
                "Cell",
                "You are in an old-looking prison cell.  It is dimly lit by a torch outside.  There is a door to the north.",
                new Item[]{},
                new Character[]{
                        new Character("guard", "He looks tired, and he smells of onions.", new Item[]{
                                new ItemKeycard(new String[]{"a keycard", "keycard", "key", "card"}, "It's a white keycard, labeled in red marker with \"SCGLGD\"", 5)
                        }, 10).setBlockingExits(),
                        new CharacterDoor("door", "It is a metal door.", Side.NORTH, cellDoor)
                },
                true
        );

        Character hoboHole = new CharacterScenery("hole", "It is quite narrow, but it looks like you could fit through.");
        Room corridor = new Room("East end of corridor",
                "You are in a small corridor going west.  There is a open door to the south, and a hole to the east.",
                new Item[]{new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.", 15)},
                new Character[]{
                        new CharacterDoor("door", "It is a metal door.", Side.SOUTH, cellDoor),
                        hoboHole
                }
        );
        corridor.connectTo(cell, Side.SOUTH);

        Room hoboCave = new Room(
                "The Hobo's Cave",
                "You are in a damp cave. There is a hole in the west wall and a long tunnel to the south with a faint light at the end.",
                new Item[]{},
                new Character[]{new Character("hobo", "He is an old man sitting in the corner, dressed in rags, holding a torch. He looks hungry.",
                        new Item[]{
                                new Weapon(new String[]{"steel sword", "sword", "steel"}, "The Hobo's steel sword.", 100, 40),
                                new Item(new String[]{"the Hobo's torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.", 10)
                        }, 100),
                        hoboHole
                }
        );
        hoboCave.connectTo(corridor, Side.WEST);

        Room hallway = new Room(
                "Temple Hallway",
                "You are in a long hallway with a bright light at the south end. To the north is an archway leading to a darker room.",
                new Item[]{},
                new Character[]{
                        new CharacterScenery("archway", "There isn't much to say about it.")
                },
                true
        );
        hallway.connectTo(hoboCave, Side.NORTH);

        Room temple = new Room(
                "Holey Temple",
                "You are in a huge room with a very high ceiling. Although there is no discernable light source you can't find a shadow anywhere. In the center is a bronze tree. Around the base of the tree are seven elliptical holes.",
                new Item[]{},
                new Character[]{
                        new CharacterLeafHole(),
                        new CharacterScenery("tree", "It is a large bronze tree.  The leaves are quite colorful.")
                },
                true
        );
        temple.connectTo(hallway, Side.NORTH);

        Room winRoom = new WinRoom(
                "You Win!",
                "For returning the missing leaves to the tree, you have earned this great ASCII ribbon!" +
                        "\n  ****  " +
                        "\n ******" +
                        "\n********" +
                        "\n********" +
                        "\n ******" +
                        "\n  ****" +
                        "\n  ****" +
                        "\n **  **" +
                        "\n **  **" +
                        "\n**    **" +
                        "\n**    **",
                new Item[] {},
                new Character[]{}
        );

        // TODO allow feeding the hobo
        Room ladderRoom = new Room(
                "West end of corridor",
                "You are in a small corridor going east.  There is a ladder on the wall here, leading through a hole in the ceiling.",
                new Item[]{},
                new Character[]{
                        new CharacterScenery("ladder", "It is an old-looking wooden ladder, firmly attached to the wall.")
                }
        );
        ladderRoom.connectTo(corridor, Side.EAST);

        Character labStairs = new CharacterScenery("staircase", "It is a long concrete staircase.  You can't tell where it leads.");
        Room dangerousItemStorage = new Room(
                "Dangerous item storage room",
                "You are in a small storage room with a damp concrete floor. There are a variety of dangerous looking implements attached to the walls. The ceiling is so low that you can climb into the room above. A staircase leads downwards into the darkness.",
                new Item[]{
                        new ItemPineappleBomb(new String[]{"pineapple bomb", "pineapple", "bomb", "fruit"}, "It is a delicious looking pineapple. You would assume it was harmless if it weren't for the fuse coming out of the top.", 100)
                },
                new Character[]{
                        labStairs
                }
        );
        dangerousItemStorage.setDir(Side.UP, ladderRoom);

        //TODO make an instance of keycard scanner to open door
        LinkableBoolean labDoorOpen = new LinkableBoolean(false);
        LinkableBoolean labDoorLocked = new LinkableBoolean(true);
        CharacterDoor labEntranceDoor = new CharacterDoor("door", "It is a fancy glass door that you can't quite see through.", Side.EAST, labDoorOpen, labDoorLocked);
        Room labEntrance = new Room(
                "Lab Entrance",
                "You are in front of a fancy translucent glass door on the east wall with a keycard scanner next to it. The staircase leads back upward.",
                new Item[]{},
                new Character[]{
                        labEntranceDoor,
                        new CharacterLock("scanner", "It is a high-tech keycard scanner that looks as though it can never be fooled.", labEntranceDoor),
                        labStairs
                }
        );
        labEntrance.connectTo(dangerousItemStorage, Side.UP);

        CharacterDoor labExitDoor = new CharacterDoor("door", "It is a fancy glass door that you can't quite see through.", Side.WEST, labDoorOpen, labDoorLocked);
        Room labRoomOne = new Room(
                "Lab Room One",
                "You are in a large room in what appears to be a laboratory. There is a glass door on the west wall controlled by a keycard scanner.",
                new Item[]{
                        new Usable(new String[] {"a beaker", "beaker", "vinegar", "acid", "liquid", "smelly"}, "It is a 1 Liter beaker filled with an unknown liquid. Smells terrible.", 30)
                },
                new Character[]{
                        labExitDoor,
                        new CharacterLock("scanner", "It is a high-tech keycard scanner that looks as though it can never be fooled.", labExitDoor)
                }
        );
        labRoomOne.connectTo(labEntrance, Side.WEST);

        Room museumBasement = new Room(
                "Museum Basement",
                "You are in what appears to be the basement of a museum. There are many exhibits here which are not currently on display. There is a hole in the floor with a ladder leading down into the darkness. Stairs in the corner lead upward.",
                new Item[]{
                        new ItemOrb(new String[]{"an orb", "orb", "shiny"}, "A small orb. It is glowing softly but doesn't seem to light up anything around it.", 100)
                },
                new Character[]{}
        );
        museumBasement.connectTo(ladderRoom, Side.DOWN);

        Room closet = new Room(
                "Museum Closet",
                "You are in a small side closet. Stairs lead down out of sight. There is an entryway on the east wall that looks like it used to contain a door.",
                new Item[]{
                        new ItemShirt(new String[]{"a shirt", "shirt", "Hawaiian", "fancy"}, "A fancy Hawaiian shirt. It's exactly your size!", new Item[]{new Item(new String[]{"a blue crystalline leaf", "leaf", "blue", "crystal", "crystalline"}, "A shiny blue crystalline leaf.", 5)}, 30)
                },
                new Character[]{}
        );
        closet.connectTo(museumBasement, Side.DOWN);

        Room lobby = new Room(
                "Museum Lobby",
                "You are in the lobby of a museum. It appears to be abandoned. Hallways go off to the south and north, likely leading to various exhibits, and there is a small room to the west.",
                new Item[]{},
                new Character[]{}
        );
        lobby.connectTo(closet, Side.WEST);

        Room nativeAmericanExhibit = new Room(
                "Native American Exhibit",
                "You are in a museum exhibit room highlighting history and culture of Native American people.  The exit is to the south.",
                new Item[]{
                        new Weapon(new String[]{"a spear", "spear"}, "It is a spear with a wooden handle.  You aren't sure what the tip is made of.", 15, 75),
                        new Item(new String[]{"an arrowhead", "arrowhead"}, "It is an arrowhead, similar to the tip of the spear.", 25)
                },
                new Character[]{
                        new CharacterBuffalo("buffalo", "It is a brownish-green buffalo.", null)
                }
        );
        nativeAmericanExhibit.connectTo(lobby, Side.SOUTH);

        Room dinosaurExhibit = new Room(
                "Dinosaur Exhibit",
                "You are in a museum exhibit room celebrating the discovery of dinosaur bones.  There are several reconstructions of dinosaur skeletons around the room.  There are exits to the north and east.",
                new Item[]{

                },
                new Character[]{
                        new CharacterDinoButton("button", "It is a circular red button, about half an inch in diameter.", ladderRoom, dangerousItemStorage)
                }
        );
        dinosaurExhibit.connectTo(lobby, Side.NORTH);

        Room scrollRoom = new Room(
                "Scroll Room",
                "You are in what appears to be an unfinished room of the museum.  Large colorful leaves are painted on the walls.  The exit is to the west.",
                new Item[]{
                        new Item(new String[]{"a scroll", "scroll", "proclamation", "letter", "legend", "prophecy"}, "The scroll says:\n\"A very long time ago,\nwhen this town was new,\nsomeone came and hid some leaves,\nwhose name nobody knew.\nThe legends say that someone\nwho finds these leaves of old\nmay find the greatest treasure\nthat man could ever hold.\"", 10)
                },
                new Character[]{}
        );
        scrollRoom.connectTo(dinosaurExhibit, Side.WEST);

        return cell;
    }

    /**
     * Check an array for trailing elements, and print an error message if present
     *
     * @param o the array to check
     * @param n the maximum length it should have
     * @return whether the array passes
     */
    public static boolean trailing(Object[] o, int n) {
        if (o.length > n) {
            System.out.println("Too much information!");
            return true;
        }
        return false;
    }

    /**
     * Make the first character of a string capital
     *
     * @param s the string to change
     * @return the string after changing the capitalization
     */
    public static String firstCapital(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Parse a command
     *
     * @param str the command to parse
     */
    public static void readCommand(String str) {
        String[] t = str.toLowerCase().split(" ");
        // check if they even typed anything
        if (t.length == 0 || t[0].length() == 0) {
            System.out.println("What do you want to do?");
            return;
        }
        // workaround for go
        boolean go = t[0].equals("go") || t[0].equals("cd") || t[0].equals("walk") || t[0].equals("move") || t[0].equals("relocate");
        if (go) {
            if (!trailing(t, 2)) {
                if (t.length == 1) {
                    System.out.println("Go where?");
                    return;
                } else {
                    t = new String[]{t[1]};
                }
            } else {
                return;
            }
        }
        switch (t[0]) {
            case "north":
            case "n":
            case "south":
            case "s":
            case "west":
            case "w":
            case "east":
            case "e":
            case "down":
            case "d":
            case "up":
            case "u":
                if (!trailing(t, 1)) {
                    // go that direction
                    goTo(room.goDir(Side.fromChar(t[0].charAt(0))));
                }
                break;
            case "exit":
            case "quit":
            case "surrender":
            case "retire":
                if (!trailing(t, 1)) {
                    System.out.println("Goodbye.");
                    System.exit(0);
                }
                break;
            case "take":
            case "get":
            case "pickup":
            case "obtain":
            case "loot":
            case "cut":
            case "haul":
            case "catch":
                // take all
                if (t.length == 2 && t[1].equals("all")) {
                    int numItems = room.getItems().length;
                    for (int i = 0; i < numItems; i++) {
                        Item it = room.takeItem(0);
                        System.out.println("You pick up " + it.getName() + ".");
                        player.addToInventory(it);
                        it.onPickup();
                    }
                    break;
                }
                // strip out "the" and "a"
                if (t.length == 3 && (t[1].equalsIgnoreCase("the") || t[1].equalsIgnoreCase("a"))) {
                    t = new String[]{t[0], t[2]};
                }
                if (t.length == 1) {
                    System.out.println(firstCapital(t[0]) + " what?");
                    break;
                }
                if (!trailing(t, 2)) {
                    boolean found = false;
                    // loop through items in room
                    Item[] roomItems = room.getItems();
                    for (int i = 0; i < roomItems.length; i++) {
                        if (roomItems[i].matches(t[1])) {
                            // found a match!
                            // check if item is too heavy to add to inventory
                            if (player.getInventoryWeight() + roomItems[i].getWeight() > 500) {
                                System.out.println("Your load is too heavy to pick that up!");
                                break;
                            }
                            Item it = room.takeItem(i);
                            System.out.println("You pick up " + it.getName() + ".");
                            player.addToInventory(it);
                            it.onPickup();
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                    // check omnipresents
                    for (int i = 0; i < omnipresent.length; i++) {
                        if (omnipresent[i].equalsIgnoreCase(t[1])) {
                            System.out.println("You can't " + t[0] + " that.");
                            found = true;
                            break;
                        }
                    }
                    // you can't take characters
                    Character[] roomChars = room.getCharacters();
                    for (Character c : roomChars) {
                        if (c.getName().equalsIgnoreCase(t[1])) {
                            System.out.println("You can't " + t[0] + " that.");
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("I don't see that here.");
                    }
                }
                break;
            case "punch":
                if(!trailing(t, 3)) {
                    if(t.length == 1) {
                        System.out.println(firstCapital(t[0])+" what?");
                        break;
                    }
                    else if(t.length == 2) {
                        t = new String[]{"attack", t[1], "with", "hands"};
                    }
                    else if(t.length == 3) {
                        if(t[1].equals("the")) {
                            t = new String[]{"attack", t[2], "with", "hands"};
                        }
                        else {
                            System.out.println("Too much information!");
                            break;
                        }
                    }
                }
                else {
                    break;
                }
            case "attack":
            case "kill":
                // "attack" and "kill" actually just rearrange t and leave it to "use" to handle it
                if (!trailing(t, 4)) {
                    if (t.length == 1) {
                        System.out.println(firstCapital(t[0]) + " what?");
                        break;
                    } else if (t.length == 2) {
                        System.out.println("With what do you wish to " + t[0] + "?");
                        break;
                    } else if (t.length == 3) {
                        System.out.println("I don't know how to do that.");
                        break;
                    } else {
                        t = new String[]{"use", t[3], "on", t[1]};
                    }
                }
                else {
                    break;
                }
            case "press":
            case "push":
            case "open":
            case "close":
            case "use":
                if (!trailing(t, 4)) {
                    if (t.length == 1) {
                        System.out.println("Use what?");
                    } else if (t.length == 3) {
                        System.out.println("I don't know how to do that.");
                    } else {
                        Object item = null;
                        // look for a match in inventory
                        Item[] items = player.getInventory();
                        for (int i = 0; i < items.length; i++) {
                            if (items[i].matches(t[1])) {
                                item = items[i];
                                break;
                            }
                        }
                        if (item == null) {
                            // look for a character
                            Character[] chars = room.getCharacters();
                            for (Character c : chars) {
                                if (c.getName().equalsIgnoreCase(t[1])) {
                                    item = c;
                                    break;
                                }
                            }
                        }
                        // check if it's hands
                        if (Character.hands.matches(t[1])) {
                            item = Character.hands;
                        }
                        if (item == null) {
                            System.out.println("You don't have that.");
                        } else if (item instanceof IUsable) {
                            // It's usable!
                            if (t.length == 2) {
                                // simple use
                                ((IUsable) item).use();
                            } else {
                                // search for character to use on
                                Character[] roomChars = room.getCharacters();
                                for (int i = 0; i < roomChars.length; i++) {
                                    if (roomChars[i].getName().equalsIgnoreCase(t[3])) {
                                        ((IUsable) item).use(roomChars[i]);
                                        break;
                                    }
                                }
                            }
                        } else {
                            System.out.println("You can't use that.");
                        }
                    }
                }
                break;
            case "score":
                System.out.println("Your current score is " + score + ".");
                break;
            case "turns":
            case "moves":
                if (moves == 1) {
                    System.out.println("You have made 1 move.");
                } else {
                    System.out.println("You have made " + moves + " moves.");
                }
                break;
            case "inventory":
            case "i":
            case "supply":
            case "index":
            case "items":
                if (!trailing(t, 1)) {
                    System.out.println("You have the following items:");
                    Item[] playerItems = player.getInventory();
                    for (int i = 0; i < playerItems.length; i++) {
                        System.out.println(playerItems[i].getInventoryEntry());
                    }
                    // print a message about how much weight the player is carrying
                    String loadMessage;
                    int totalWeight = player.getInventoryWeight();
                    if (totalWeight < 100) {
                        loadMessage = "light";
                    } else if (totalWeight > 100 && totalWeight < 250) {
                        loadMessage = "medium weight";
                    } else if (totalWeight > 250 && totalWeight < 400) {
                        loadMessage = "heavy";
                    } else {
                        loadMessage = "very heavy";
                    }
                    System.out.println("Your load is " + loadMessage + ".");
                }
                break;
            case "look":
            case "l":
            case "ls":
            case "examine":
            case "what":
            case "read":
            case "stare":
            case "peek":
            case "leer":
            case "eye":
            case "glimpse":
            case "glance":
            case "view":
            case "gaze":
            case "gander":
            case "survey":
            case "behold":
            case "regard":
            case "notice":
            case "scrutinize":
                if (t.length == 3 && t[1].equals("at")) {
                    t = new String[]{t[0], t[2]};
                }
                if (trailing(t, 2)) {
                } else if (t.length == 1) {
                    // look at room
                    System.out.println(room.getFullDescription(false));
                } else {
                    // look at item or character
                    IDescribable item = null;
                    // check player inventory first
                    Item[] inv = player.getInventory();
                    for (Item i : inv) {
                        if (i.matches(t[1])) {
                            item = i;
                            break;
                        }
                    }
                    if (item == null) {
                        // no match yet, check items in room
                        Item[] roomItems = room.getItems();
                        for (Item i : roomItems) {
                            if (i.matches(t[1])) {
                                item = i;
                                break;
                            }
                        }
                        if (item == null) {
                            // still no match, check characters in room
                            Character[] roomChars = room.getCharacters();
                            for (Character c : roomChars) {
                                if (c.getName().equals(t[1])) {
                                    item = c;
                                    break;
                                }
                            }
                        }
                    }
                    if (item != null) {
                        // Yay, got a match!
                        System.out.println(item.getDescription());
                    } else {
                        // Aww, no match.
                        System.out.println("I don't see that here.");
                    }
                }
                break;
            case "drop":
                if (!trailing(t, 2)) {
                    // look through player inventory
                    Item[] playerItems = player.getInventory();
                    for (int i = 0; i < playerItems.length; i++) {
                        if (playerItems[i].matches(t[1])) {
                            // found it!
                            Item it = player.takeItem(i);
                            room.addItem(it);
                            System.out.println("Dropped " + it.getName() + ".");
                        }
                    }
                }
                break;
            case "eat":
            case "devour":
            case "nom":
            case "consume":
            case "rm":
            case "bite":
            case "munch":
            case "absorb":
            case "digest":
            case "wolf":
            case "ingest":
            case "chew":
            case "inhale":
            case "nibble":
                if (!trailing(t, 2)) {
                    Object object = null;
                    // look through player inventory
                    Item[] playerItems = player.getInventory();
                    for (int i = 0; i < playerItems.length; i++) {
                        if (playerItems[i].matches(t[1])) {
                            object = playerItems[i];
                            break;
                        }
                    }
                    if (object == null) {
                        // still no match, look in room
                        Item[] roomItems = room.getItems();
                        for (int i = 0; i < roomItems.length; i++) {
                            if (roomItems[i].matches(t[1])) {
                                object = roomItems[i];
                                break;
                            }
                        }
                    }
                    if (object != null) {
                        // found it!
                        if (object instanceof IEdible) {
                            // edible!
                            ((IEdible) object).eat(t[0]);
                        } else {
                            System.out.println("You can't " + t[0] + " that.");
                        }
                    } else {
                        System.out.println("I don't see that here.");
                    }
                }
                break;
            case "put":
                if(!trailing(t, 4)) {
                    if(t.length == 1) {
                        System.out.println(firstCapital(t[0])+" what?");
                        break;
                    }
                    else if(t.length == 2 || t.length == 3) {
                        System.out.println("wut");
                        break;
                    }
                    else {
                        t = new String[] {"give", t[1], "to", t[3]};
                    }
                }
                else {
                    break;
                }
            case "give":
            case "award":
            case "commit":
            case "grant":
            case "present":
            case "provide":
            case "donate":
            case "gift":
            case "entrust":
            case "furnish":
            case "dispense":
            case "administer":
            case "merge":
            case "bestow":
            case "bequeath":
            case "tip":
            case "transmit":
            case "bless":
                String rewarded;
                String reward;
                if (!trailing(t, 4)) {
                    if (t.length == 1) {
                        System.out.println(firstCapital(t[0] + " what?"));
                        break;
                    } else if (t.length == 2) {
                        System.out.println("wat");
                        break;
                    } else if (t.length == 3) {
                        // give character item
                        rewarded = t[1];
                        reward = t[2];
                    } else {
                        if (t[2].equals("to")) {
                            // give item to character
                            rewarded = t[3];
                            reward = t[1];
                        } else if (t[2].equals("with") || t[2].equals("the")) {
                            // bless character with item, give character the item
                            rewarded = t[1];
                            reward = t[3];
                        } else {
                            System.out.println("I don't understand.");
                            break;
                        }
                    }
                    // look for character
                    Character tophat = null;
                    Character[] roomChars = room.getCharacters();
                    for (int i = 0; i < roomChars.length; i++) {
                        if (roomChars[i].getName().equalsIgnoreCase(rewarded)) {
                            tophat = roomChars[i];
                            break;
                        }
                    }
                    if (tophat == null) {
                        System.out.println("I don't see that here.");
                        break;
                    }
                    Item item = null;
                    // look for item in inventory
                    Item[] playerItems = player.getInventory();
                    for (int i = 0; i < playerItems.length; i++) {
                        if (playerItems[i].matches(reward)) {
                            item = playerItems[i];
                            player.removeItem(item);
                            break;
                        }
                    }
                    if (item == null) {
                        // still no match, check room
                        Item[] roomItems = room.getItems();
                        for (int i = 0; i < roomItems.length; i++) {
                            if (roomItems[i].matches(reward)) {
                                item = room.takeItem(i);
                                break;
                            }
                        }
                    }
                    if (item == null) {
                        System.out.println("I don't see that here.");
                    } else {
                        if(!tophat.give(item)) {
                            player.addToInventory(item);
                        }
                    }
                }
                break;
            case "cheatoff":
                if(cheatMode) {
                    cheatMode = false;
                    System.out.println("Cheat mode off.");
                    break;
                }
            default:
                System.out.println("What would it mean to " + (go ? "go " : "") + t[0] + "?");
        }
    }

    /**
     * Go into a room and print info
     *
     * @param r the room to enter
     */
    public static void goTo(Room r) {
        if (r == null) {
            return;
        }
        room = r;
        boolean v = r.getVisited();
        r.visit();
        System.out.println(r.getFullDescription(v));
    }

    /**
     * Show death message
     */
    public static void kill() {
        System.out.println("You are dead.");
        System.out.println("Moves: " + moves);
        System.out.println("Score: " + score);
        System.exit(0);
    }

    /**
     * Check if the current room is lit
     *
     * @return whether the current room is lit
     */
    public static boolean isLight() {
        // check if room is always lit (outside, starting cell, etc.)
        if (room.isAlwaysLit()) {
            return true;
        }
        // check for light source in player's inventory
        Item[] playerItems = player.getInventory();
        for (Item i : playerItems) {
            if (i.matches("torch")) {
                // found the torch!
                return true;
            }
        }
        // check for light source on room floor
        Item[] roomItems = room.getItems();
        for (Item i : roomItems) {
            if (i.matches("torch")) {
                // found the torch
                return true;
            }
        }
        // check for light source in NPC's inventories
        Character[] roomChars = room.getCharacters();
        for (Character c : roomChars) {
            Item[] charItems = c.getInventory();
            for (Item i : charItems) {
                if (i.matches("torch")) {
                    // found the torch
                    return true;
                }
            }
        }
        // if not lit
        return false;
    }

    /**
     * Parse options
     * @param args      The command-line arguments
     * @param shortOpts Possible short options
     * @param longOpts  Possible long options and whether they require an argument
     * @return the parsed option map
     */
    public static HashMap<String,Object> parseOptions(String[] args, char[] shortOpts, HashMap<String, Boolean> longOpts) {
        HashMap<String,Object> tr = new HashMap<String,Object>();
        for(int i = 0; i < args.length; i++) {
            String s = args[i];
            if(s.length() < 2 || s.charAt(0) != '-') {
                // too short to be an option
                System.err.println("Unable to parse option: "+s);
                System.exit(-1);
            }
            else {
                if(s.charAt(1) == '-') {
                    // long option
                    String opt = s.substring(2);
                    if(longOpts.containsKey(opt)) {
                        if(longOpts.get(opt)) {
                            i++;
                            tr.put(opt, args[i]);
                        }
                        else {
                            tr.put(opt, true);
                        }
                    }
                    else {
                        System.err.println("Unrecognized option: "+opt);
                        System.exit(-2);
                    }
                }
                else {
                    // short options
                    for(int j = 1; j < s.length(); j++) {
                        char it = 0;
                        for(int c = 0; c < shortOpts.length; c++) {
                            if(shortOpts[c] == s.charAt(j)) {
                                it = shortOpts[c];
                            }
                        }
                        if(it == 0) {
                            System.err.println("Unrecognized option: "+s.charAt(j));
                            System.exit(-2);
                        }
                        else {
                            tr.put(it+"", true);
                        }
                    }
                }
            }
        }
        return tr;
    }
}
