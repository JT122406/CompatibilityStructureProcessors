package tech.jt_dev.compatprocessors;

import net.fabricmc.api.ModInitializer;

/**
 * This class is the entrypoint for the mod on the Fabric platform.
 */
public class CompatibilityStructureProcessorsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CompatibilityStructureProcessors.init();
    }
}
