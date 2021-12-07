package io.github.cuubecon.skipladder.config;

import com.mojang.datafixers.util.Pair;
import io.github.cuubecon.skipladder.SkipLadder;

public class SkipLadderConfig
{
    public static int MAX_AMOUNT;
    public static int HUNGER_AMOUNT;
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;
    public static boolean REMOVE_HUNGER;
    public static boolean PLAY_TELEPORTSOUND;
    public static boolean SKIP_STAIRS;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(SkipLadder.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("hunger-amount", 10), "For every block of teleportation, there is a counter. This counter divide by this amount is the hungerlevels to remove, if 'remove hunger' is true.");
        configs.addKeyValuePair(new Pair<>("max-amount", 150), "The max amount of remove hunger devide hunger amount. If divider = max amount, teleportation always cots 1 hungerlevel (0.5).");
        configs.addKeyValuePair(new Pair<>("remove-hunger", true), "Should teleport cost hunger.");
        configs.addKeyValuePair(new Pair<>("play-teleportsound", true), "Should play sound on teleport.");
        configs.addKeyValuePair(new Pair<>("skip-stairs", true), "Should play sound on teleport.");
    }

    private static void assignConfigs() {
        // And that's it! Now you can request values from the config:
        HUNGER_AMOUNT = CONFIG.getOrDefault( "hunger-amount", 10 );
        MAX_AMOUNT = CONFIG.getOrDefault( "max-amount", 150 );
        REMOVE_HUNGER = CONFIG.getOrDefault( "remove-hunger", true );
        PLAY_TELEPORTSOUND = CONFIG.getOrDefault( "play-teleportsound", true );
        SKIP_STAIRS = CONFIG.getOrDefault( "skip-stairs", true );

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }


}
