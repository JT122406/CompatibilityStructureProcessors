package tech.jt_dev.compatprocessors;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(CompatibilityStructureProcessors.MOD_ID)
public class CompatibilityStructureProcessorsForge {
    public CompatibilityStructureProcessorsForge() {
        CompatibilityStructureProcessors.init();
        ForgePlatformHandler.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
