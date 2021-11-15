package io.github.cuubecon.skipladder;

import io.github.cuubecon.skipladder.config.SkipLadderConfig;
import io.github.cuubecon.skipladder.event.ClickLadderEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkipLadder implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(SkipLadder.MOD_ID);
	public static String MOD_ID = "skipladder";
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		UseBlockCallback.EVENT.register(new ClickLadderEvent());

		SkipLadderConfig.registerConfigs();
	}
}
