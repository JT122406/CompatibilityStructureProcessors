package tech.jt_dev.moreprocessors;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(MoreStructureProcessors.MOD_ID)
public class MoreStructureProcessorsForge {
    public MoreStructureProcessorsForge() {
        MoreStructureProcessors.init();
        ForgePlatformHandler.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
