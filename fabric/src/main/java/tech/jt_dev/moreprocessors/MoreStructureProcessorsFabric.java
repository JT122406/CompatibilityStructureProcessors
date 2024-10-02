package tech.jt_dev.moreprocessors;

import net.fabricmc.api.ModInitializer;

/**
 * This class is the entrypoint for the mod on the Fabric platform.
 */
public class MoreStructureProcessorsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MoreStructureProcessors.init();
    }
}
