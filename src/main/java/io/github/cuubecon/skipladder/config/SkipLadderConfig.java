package io.github.cuubecon.skipladder.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkipLadderConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> remove_hunger;
    public static final ForgeConfigSpec.ConfigValue<Boolean> play_teleportsound;
    public static final ForgeConfigSpec.ConfigValue<Integer> hunger_amount;
    public static final ForgeConfigSpec.ConfigValue<Integer> max_amount;

    static {
        BUILDER.push("Config for Skip Ladder Mod!");

        remove_hunger = BUILDER.comment("Should teleport cost hunger. Default: true").define("remove hunger", true);
        play_teleportsound = BUILDER.comment("Should play sound on teleport. Default: true").define("play sound", true);
        hunger_amount = BUILDER.comment("For every block of teleportation, there is a counter. This counter divide by this amount is the hungerlevels to remove, if 'remove hunger' is true. Default: 10").define("divider", 10);
        max_amount = BUILDER.comment("The max amount of remove hunger devide hunger amount. If divider = max amount, teleportation always cots 1 hungerlevel (0.5). Default: 150").define("max counter amount", 150);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
