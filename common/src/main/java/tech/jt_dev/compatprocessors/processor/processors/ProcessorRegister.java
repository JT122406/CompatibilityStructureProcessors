package tech.jt_dev.compatprocessors.processor.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;
import tech.jt_dev.compatprocessors.CompatibilityStructureProcessors;
import tech.jt_dev.compatprocessors.PlatformHandler;

import java.util.function.Supplier;

public class ProcessorRegister {

    public static final Supplier<StructureProcessorType<DirectionalBlockProcessor>> DIRECTIONAL_BLOCK_PROCESSOR = register("directional_block", () -> DirectionalBlockProcessor.CODEC);

    /**
     * Utility method for registering custom structure processor types.
     */
    private static <P extends StructureProcessor> Supplier<StructureProcessorType<P>> register(String id, @NotNull Supplier<Codec<P>> codec) {
        return PlatformHandler.PLATFORM_HANDLER.register(BuiltInRegistries.STRUCTURE_PROCESSOR, id, () -> codec::get);
    }


    public static void registerProcessors() {
        CompatibilityStructureProcessors.LOGGER.info("Registering Structure Processors");
    }
}
