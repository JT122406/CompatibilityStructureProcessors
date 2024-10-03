package tech.jt_dev.moreprocessors.fabric;

import net.fabricmc.api.ModInitializer;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;

/**
 * This class is the entrypoint for the mod on the Fabric platform.
 */
public class MoreStructureProcessorsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MoreStructureProcessors.init();
    }
}
