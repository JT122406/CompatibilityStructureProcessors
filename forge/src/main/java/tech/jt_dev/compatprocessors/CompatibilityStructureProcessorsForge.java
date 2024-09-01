package tech.jt_dev.compatprocessors;

import net.minecraftforge.fml.common.Mod;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(CompatibilityStructureProcessors.MOD_ID)
public class CompatibilityStructureProcessorsForge {
    public CompatibilityStructureProcessorsForge() {
        CompatibilityStructureProcessors.init();
    }
}
