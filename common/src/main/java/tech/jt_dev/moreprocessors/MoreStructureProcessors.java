package tech.jt_dev.moreprocessors;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

/**
 * Main class for the More Structure Processors.
 * @author Joseph T. McQuigg
 */
public class MoreStructureProcessors {

    /** The mod id for More Structure Processors. */
    public static final String MOD_ID = "moreprocessors";

    /** The logger for examplemod. */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Initializes the mod.
     */
    public static void init() {
        ProcessorRegister.registerProcessors();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
