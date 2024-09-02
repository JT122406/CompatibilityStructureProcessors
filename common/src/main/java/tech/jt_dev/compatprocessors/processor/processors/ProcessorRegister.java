package tech.jt_dev.compatprocessors.processor.processors;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;
import tech.jt_dev.compatprocessors.CompatibilityStructureProcessors;
import tech.jt_dev.compatprocessors.PlatformHandler;

public class ProcessorRegister<P extends StructureProcessor> {

    public static final StructureProcessorType<DirectionalBlockProcessor> DIRECTIONAL_BLOCK_PROCESSOR = register("directional_block", DirectionalBlockProcessor.CODEC);

    /**
     * Utility method for registering custom structure processor types.
     */
    private static <P extends StructureProcessor> StructureProcessorType<P> register(String id, @NotNull Codec<P> codec) {
        return PlatformHandler.PLATFORM_HANDLER.registerStructureProcessor(id, codec);
    }


    public static void registerProcessors() {
        CompatibilityStructureProcessors.LOGGER.info("Registering Structure Processors");
    }
}
