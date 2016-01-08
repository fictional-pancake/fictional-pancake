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
                                new ItemKey(new String[]{"a keycard", "keycard", "card", "scglgd"}, "It's a white keycard, labeled in red marker with \"SCGLGD\"", 5, "scanner")
                        }, 10).setBlockingExits(),
                        new CharacterDoor("door", "It is a metal door.", Side.NORTH, cellDoor)
                },
                new Item[]{},
                true
        );

        Character hoboHole = new CharacterScenery("hole", "It is quite narrow, but it looks like you could fit through.");
        Room corridor = new Room("East end of corridor",
                "You are in a small corridor going west.  There is a open door to the south, a hole to the east, and an opening to the north.",
                new Item[]{new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.", 15)},
                new Character[]{
                        new CharacterDoor("door", "It is a metal door.", Side.SOUTH, cellDoor),
                        hoboHole
                },
                new Item[]{}
        );
        corridor.connectTo(cell, Side.SOUTH);

        Room danielsCardCollection = new Room("Dirt Room",
                "You are in a room that is completely empty except for a pile of dirt.  There is an exit to the south.",
                new Item[]{},
                new Character[] {
                        new CharacterScenery(new String[]{"pile of dirt", "dirt", "pile"}, "It is a pile of dirt.  Hooray!"),
                },
                new Item[]{
                        new Item(new String[] {"a slip of paper", "paper", "slip", "invitation"}, "It says:\nTo whoever finds this note:\nYou just found this note.\nI have resorted to a life as a hobo underground in a cave.\nIf you care, you will care.\nBy reading this note, you have read this note.\nSincerely,\nRobert Richard Stallman Schreiber"),
                        new Item(new String[] {"some dirt", "dirt", "sediment"}, "It looks slightly fish-like."),
                        new ItemKey(new String[] {"a painting", "painting"}, "It is a lovely painting of a pineapple.", 10, "scanner")
                }
        );
        danielsCardCollection.connectTo(corridor, Side.SOUTH);

        Room hoboCave = new Room(
                "The Hobo's Cave",
                "You are in a damp cave. There is a hole in the west wall and a long tunnel to the south with a faint light at the end.",
                new Item[]{},
                new Character[]{new CharacterHobo("hobo", "He is an old man sitting in the corner, dressed in rags, holding a torch. He looks hungry.",
                        new Item[]{
                                new Weapon(new String[]{"a steel sword", "sword", "steel"}, "The Hobo's steel sword.", 10, 40),
                                new Item(new String[]{"a torch", "torch"}, "It is a simple stick with coal on the end.  It is alight with a small flame.", 10),
                                new ItemLeaf("purple")
                        }, 20),
                        hoboHole
                },
                new Item[]{
                        new ItemLeaf("magenta")
                }
        );
        hoboCave.connectTo(corridor, Side.WEST);

        Room hallway = new Room(
                "Temple Hallway",
                "You are in a long hallway with a bright light at the south end. To the north is an archway leading to a darker room.",
                new Item[]{
                        new Weapon(new String[]{"a shovel", "shovel", "digger", "trowel", "scoop", "spade", "tool"}, "It is a simple digging implement, twice as long as it is wide.", 7, 100)
                },
                new Character[]{
                        new CharacterScenery("archway", "There isn't much to say about it.")
                },
                new Item[]{},
                true
        );
        hallway.connectTo(hoboCave, Side.NORTH);

        Room goatGuard = new Room(
                "Lamb Room",
                "You are in a room with artificial grass, lit by an opening to the south.  There is also a hallway to the north.",
                new Item[]{},
                new Character[]{
                        new CharacterLamb("lamb", "It has a tag on its ear that says \"The Lamb Who's Gonna Kill You Someday\".", new Item[]{}, 77, Side.SOUTH)
                },
                new Item[]{},
                true
        );
        goatGuard.connectTo(hallway, Side.NORTH);

        Room temple = new Room(
                "Holey Temple",
                "You are in a huge room with a very high ceiling. There is no discernible light source, and you can't find a shadow anywhere. In the center is a bronze tree. Around the base of the tree are eight elliptical holes.",
                new Item[]{},
                new Character[]{
                        new CharacterScenery("tree", "It is a large bronze tree.  The leaves are quite colorful.")
                },
                new Item[]{},
                true
        );
        temple.connectTo(goatGuard, Side.NORTH);

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
        temple.addCharacter(new CharacterLeafHole(8, temple, winRoom));

        Room ladderRoom = new Room(
                "West end of corridor",
                "You are in a small corridor going east.  There is a ladder on the wall here, leading through a hole in the ceiling.",
                new Item[]{},
                new Character[]{
                        new Character(new String[] {"bat", "jal", "jai"}, "It is a small black bat, flying around the room. Upon closer inspection, you see it has the word \"Jal\" written on it in white. Whatever that means.", new Item[]{new ItemKey(new String[]{"a key", "key", "brass"}, "It is a very large and heavy brass key. Seems to be very old and well-used.", 10, "lock")}),
                        new CharacterScenery("ladder", "It is an old-looking wooden ladder, firmly attached to the wall.")
                },
                new Item[]{}
        );
        ladderRoom.connectTo(corridor, Side.EAST);

        Character labStairs = new CharacterScenery(new String[]{"staircase", "stairs", "stair"}, "It is a long concrete staircase.  You can't tell where it leads.");
        Room dangerousItemStorage = new Room(
                "Dangerous item storage room",
                "You are in a small storage room with a damp concrete floor. There are a variety of dangerous looking implements attached to the walls. The ceiling is so low that you can climb into the room above. A staircase leads downwards into the darkness.",
                new Item[]{
                        new Weapon(new String[]{"a salt shaker", "salt", "shaker", "sodium"}, "The description on the side reads: \"Microsoft Assault Shaker: Saltier than Daniels in Hearthstone.\"", 2, 5),
                        new ItemPineappleBomb(new String[]{"a pineapple bomb", "pineapple", "bomb", "fruit"}, "It is a delicious looking pineapple. You would assume it was harmless if it weren't for the fuse coming out of the top.", 100)
                },
                new Character[]{
                        labStairs
                },
                null
        );
        dangerousItemStorage.setDir(Side.UP, ladderRoom);

        LinkableBoolean labDoorOpen = new LinkableBoolean(false);
        LinkableBoolean labDoorLocked = new LinkableBoolean(true);
        CharacterDoor labEntranceDoor = new CharacterDoor("door", "It is a fancy glass door that you can't quite see through. Labeled \"Microsoft R&D\", they must do some pretty impressive stuff in here.", Side.EAST, labDoorOpen, labDoorLocked);
        Room labEntrance = new Room(
                "Lab Entrance",
                "You are in front of a fancy translucent glass door on the east wall with a keycard scanner next to it. The staircase leads back upward.",
                new Item[]{},
                new Character[]{
                        labEntranceDoor,
                        new CharacterLock("scanner", "It is a high-tech keycard scanner that looks as though it can never be fooled. Labelled \"Aperture Science, Inc.\"", labEntranceDoor),
                        labStairs
                },
                null
        );
        labEntrance.connectTo(dangerousItemStorage, Side.UP);

        CharacterDoor labExitDoor = new CharacterDoor("door", "It is a fancy glass door that you can't quite see through.", Side.WEST, labDoorOpen, labDoorLocked);
        Room labRoomOne = new Room(
                "Lab Room One",
                "You are in a large room in what appears to be a laboratory. There is a glass door on the west wall controlled by a keycard scanner. There are exits to the north and south.",
                new Item[]{
                        new Item(new String[] {"a beaker", "beaker", "vinegar", "acid", "liquid", "smelly"}, "It is a 1 Liter beaker filled with an unknown liquid. Smells terrible.", 30)
                },
                new Character[]{
                        labExitDoor,
                        new CharacterLock("scanner", "It is a high-tech keycard scanner that looks as though it can never be fooled. Labelled \"Aperture Science, Inc.\"", labExitDoor)
                },
                null
        );
        labRoomOne.connectTo(labEntrance, Side.WEST);

        Room labRoomTwo = new Room(
                "Lab Room Two",
                "You are in a small room in the lab.  There is a sign here.  There is an exit to the north.",
                new Item[]{
                        new Item(new String[] {"a powder", "powder", "baking", "soda"}, "It is a large heap of white powder.", 20)
                },
                new Character[]{
                        new CharacterCollector("collector", "He is a shady-looking character with a fine collection of Native American arrowheads.", new Item[]{new ItemLeaf("orange")}),
                        new CharacterScenery("sign", "It says:\nSomeone has stolen all the potato batteries.\nIf you have any information that could lead to their capture,\nplease discard it.")
                },
                null
        );
        labRoomTwo.connectTo(labRoomOne, Side.NORTH);

        Room labRoomThree = new Room(
                "Lab Room Three",
                "You are in a small room in the lab. There is an exit to the south.",
                new Item[]{},
                new Character[]{
                        new CharacterVolcano(new String[] {"model volcano", "volcano", "model"}, "An incredibly elaborate clay model of a volcano.", new Item[]{})
                },
                null
        );
        labRoomThree.connectTo(labRoomOne, Side.SOUTH);

        Room museumBasement = new Room(
                "Museum Basement",
                "You are in what appears to be the basement of a museum. There are many exhibits here which are not currently on display. There is a hole in the floor with a ladder leading down into the darkness. Stairs in the corner lead upward.",
                new Item[]{
                        new ItemOrb(new String[]{"an orb", "orb", "shiny"}, "A small orb. It is glowing softly but doesn't seem to light up anything around it.", 100)
                },
                new Character[]{},
                null
        );
        museumBasement.connectTo(ladderRoom, Side.DOWN);

        Room closet = new Room(
                "Museum Closet",
                "You are in a small side closet. Stairs lead down out of sight. There is an entryway on the east wall that looks like it used to contain a door.",
                new Item[]{
                        new ItemShirt(new String[]{"a shirt", "shirt", "Hawaiian", "fancy"}, "A fancy Hawaiian shirt. It's exactly your size!", new Item[]{new ItemLeaf("blue")}, 30)
                },
                new Character[]{},
                null
        );
        closet.connectTo(museumBasement, Side.DOWN);

        Room lobby = new Room(
                "Museum Lobby",
                "You are in the lobby of a museum. It appears to be abandoned. Hallways go off to the south and north, likely leading to various exhibits, and there is a small room to the west.",
                new Item[]{},
                new Character[]{},
                null
        );
        lobby.connectTo(closet, Side.WEST);

        Room nativeAmericanExhibit = new Room(
                "Native American Exhibit",
                "You are in a museum exhibit room highlighting history and culture of Native American people.  The exit is to the south.",
                new Item[]{
                        new Weapon(new String[]{"a spear", "spear"}, "It is a spear with a wooden handle.  You aren't sure what the tip is made of.", 15, 75),
                        new Item(new String[]{"an arrowhead", "arrowhead", "arrow", "head", "obsidian"}, "It is an arrowhead, similar to the tip of the spear.", 25)
                },
                new Character[]{
                        new CharacterBuffalo("buffalo", "It is a brownish-green buffalo.", null)
                },
                null
        );
        nativeAmericanExhibit.connectTo(lobby, Side.SOUTH);

        LinkableBoolean scrollDoorOpen = new LinkableBoolean(false);
        LinkableBoolean scrollDoorLocked = new LinkableBoolean(true);
        CharacterDoor scrollEntranceDoor = new CharacterDoor("door", "A large, old-looking door.", Side.EAST, scrollDoorOpen, scrollDoorLocked);

        Room dinosaurExhibit = new Room(
                "Dinosaur Exhibit",
                "You are in a museum exhibit room celebrating the discovery of dinosaur bones.  There are several reconstructions of dinosaur skeletons around the room.  There are exits to the north and east.",
                new Item[]{
                },
                new Character[]{
                        scrollEntranceDoor,
                        new CharacterLock("lock", "A large, old lock that looks like it would fit a massive key.", scrollEntranceDoor),
                        new CharacterDinoButton("button", "It is a circular red button, about half an inch in diameter.", ladderRoom, dangerousItemStorage),
                        new Character("slug", "An extremely large slug that is almost as tall as you. It smells of potatoes.", new Item[]{new WeaponPotato(), new WeaponPotato(), new WeaponPotato(), new WeaponPotato(), new WeaponPotato(), new ItemLeaf("pink")}, 2)
                },
                null
        );
        dinosaurExhibit.connectTo(lobby, Side.NORTH);

        Room scrollRoom = new Room(
                "Scroll Room",
                "You are in what appears to be an unfinished room of the museum.  Large colorful leaves are painted on the walls.  The exit is to the west.",
                new Item[]{
                        new Item(new String[]{"a scroll", "scroll", "proclamation", "letter", "legend", "prophecy"}, "The scroll says:\n\"A very long time ago,\nwhen this town was new,\nsomeone came and hid some leaves,\nwhose name nobody knew.\nThe legends say that someone\nwho finds these leaves of old\nmay find the greatest treasure\nthat man could ever hold.\"", 10)
                },
                new Character[]{
                        new CharacterDoor("door", "A large, old-looking door.", Side.WEST, scrollDoorOpen, scrollDoorLocked)
                },
                null
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
                        if (c.matches(t[1])) {
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
                if (!trailing(t, 3)) {
                    if (t.length == 1) {
                        System.out.println(firstCapital(t[0]) + " what?");
                        break;
                    } else if (t.length == 2) {
                        t = new String[]{"attack", t[1], "with", "hands"};
                    } else if (t.length == 3) {
                        if (t[1].equals("the")) {
                            t = new String[]{"attack", t[2], "with", "hands"};
                        } else {
                            System.out.println("Too much information!");
                            break;
                        }
                    }
                } else {
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
                } else {
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
                                if (c.matches(t[1])) {
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
                                    if (roomChars[i].matches(t[3])) {
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
                    if(player.getInventoryWeight() != 0) {
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
                    else {
                        System.out.println("You don't have any items.");
                        System.out.println("Your load is nonexistent.");
                    }
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
                if(t.length == 9 && str.indexOf("what does the scouter say about his power level") == 0) {
                    System.out.println("It's OVER 9000!");
                    break;
                }
                if (t.length == 3 && t[1].equals("at")) {
                    t = new String[]{t[0], t[2]};
                }
                if (trailing(t, 2)) {
                } else if (t.length == 1 || (t.length == 2 && (t[1].equals("room") || t[1].equals("around") || t[1].equals("cell")))) {
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
                                if (c.matches(t[1])) {
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
                if (t.length == 1) {
                    // just the word "drop"
                    System.out.println("Drop what?");
                    break;
                } else if (t.length > 2) {
                    // statement of type drop x y (attempted to drop multiple or used 2-word name)
                    System.out.println("Drop things one at a time or use 'drop all'.");
                } else {
                    // valid drop
                    Item[] playerItems = player.getInventory();
                    if (t[1].equalsIgnoreCase("all")) {
                        // drop everything
                        for (int i = 0; i < playerItems.length; i++) {
                            Item it = player.takeItem(0);
                            room.addItem(it);
                            System.out.println("Dropped " + it.getName() + ".");
                        }
                    } else {
                        // drop one item
                        for (int i = 0; i < playerItems.length; i++) {
                            if (playerItems[i].matches(t[1])) {
                                // found it!
                                Item it = player.takeItem(i);
                                room.addItem(it);
                                System.out.println("Dropped " + it.getName() + ".");
                                return;
                            }
                        }
                        System.out.println("You don't have that!");
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
                if (!trailing(t, 4)) {
                    if (t.length == 1) {
                        System.out.println(firstCapital(t[0]) + " what?");
                        break;
                    } else if (t.length == 2 || t.length == 3) {
                        System.out.println("wut");
                        break;
                    } else {
                        t = new String[]{"give", t[1], "to", t[3]};
                    }
                } else {
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
            case "feed":
                String rewarded;
                String reward;
                if (!trailing(t, 4)) {
                    if (t.length == 1) {
                        System.out.println(firstCapital(t[0] + " what?"));
                        break;
                    } else if (t.length == 2) {
                        System.out.println("That's not specific enough.");
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
                        if (roomChars[i].matches(rewarded)) {
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
                        if (!tophat.give(item)) {
                            player.addToInventory(item);
                        }
                    }
                }
                break;
            case "dig":
            case "excavate":
            case "unearth":
            case "quarry":
            case "mine":
            case "scoop":
                if (player.hasItem("shovel")) {
                    if (!room.isDiggable()) {
                        System.out.println("You can't dig here.");
                    } else {
                        Item[] dug = room.dig();
                        if (dug == null) {
                            System.out.println("Digging here reveals nothing.");
                        } else {
                            for (int i = 0; i < dug.length; i++) {
                                room.addItem(dug[i]);
                            }
                            System.out.println("I think you found something.");
                        }
                    }
                } else {
                    System.out.println("You don't have a shovel.");
                }
                break;
            case "cheatoff":
                if (cheatMode) {
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
        if(!v) {
            r.afterVisit();
        }
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
