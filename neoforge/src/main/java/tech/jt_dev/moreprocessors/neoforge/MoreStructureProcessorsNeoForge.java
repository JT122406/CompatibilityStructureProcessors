package tech.jt_dev.moreprocessors.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;

/**
 * Main class for the mod on the Forge platform.
 */
@Mod(MoreStructureProcessors.MOD_ID)
public class MoreStructureProcessorsNeoForge {
    public MoreStructureProcessorsNeoForge(final IEventBus eventBus) {
        MoreStructureProcessors.init();
        NeoForgePlatformHandler.register(eventBus);
    }
}
