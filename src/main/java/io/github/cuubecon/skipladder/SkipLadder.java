package io.github.cuubecon.skipladder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Main Class of the Mod
 *
 * @author CubeCon
 */
@Mod(SkipLadder.MODID)
public class SkipLadder
{
    public static final String MODID = "skipladder";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public SkipLadder()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
