package tech.jt_dev.compatprocessors;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import tech.jt_dev.compatprocessors.processor.ProcessorRegister;

public class CompatibilityStructureProcessors {

    /** The mod id for  examplemod. */
    public static final String MOD_ID = "compatprocessors";

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
