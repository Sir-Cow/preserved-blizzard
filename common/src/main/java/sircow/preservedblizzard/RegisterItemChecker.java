package sircow.preservedblizzard;

import java.util.Set;

public class RegisterItemChecker {
    // catches name of item being registered
    public static boolean flip;
    public static String itemName;

    public static final Set<String> SWORDS = Set.of(
            "wooden_sword", "golden_sword", "stone_sword", "iron_sword"
    );
    public static final Set<String> AXES = Set.of(
            "wooden_axe", "golden_axe", "stone_axe", "iron_axe"
    );
}
