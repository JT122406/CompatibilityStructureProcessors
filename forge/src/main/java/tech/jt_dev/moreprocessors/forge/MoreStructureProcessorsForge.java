package tech.jt_dev.moreprocessors.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(MoreStructureProcessors.MOD_ID)
public class MoreStructureProcessorsForge {
    public MoreStructureProcessorsForge(final FMLJavaModLoadingContext context) {
        MoreStructureProcessors.init();
        ForgePlatformHandler.register(context.getModEventBus());
    }
}
