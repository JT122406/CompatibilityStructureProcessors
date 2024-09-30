package tech.jt_dev.compatprocessors.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;
import tech.jt_dev.compatprocessors.CompatibilityStructureProcessors;
import tech.jt_dev.compatprocessors.PlatformHandler;
import tech.jt_dev.compatprocessors.processor.processors.*;

import java.util.function.Supplier;

public class ProcessorRegister {

    public static final Supplier<StructureProcessorType<DirectionalBlockProcessor>> DIRECTIONAL_BLOCK_PROCESSOR = register("directional_block", () -> DirectionalBlockProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceOnTopProcessor>> PLACE_ON_TOP_PROCESSOR = register("place_on_top", () -> PlaceOnTopProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceBelowProcessor>> PLACE_BELOW_PROCESSOR = register("place_below", () -> PlaceBelowProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCropAgeProcessor>> RANDOM_CROP_AGE_PROCESSOR = register("random_crop_age", () -> RandomCropAgeProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCompatProcessor>> RANDOM_COMPAT_PROCESSOR = register("random_compat", () -> RandomCompatProcessor.CODEC);

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
