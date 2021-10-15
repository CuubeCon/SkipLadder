package io.github.cuubecon.skipladder.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkipLadderConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> remove_hunger;

    static {
        BUILDER.push("Config for Skip Ladder Mod!");

        remove_hunger = BUILDER.comment("Should teleport cost hunger. Default: true").define("Remove hunger", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
