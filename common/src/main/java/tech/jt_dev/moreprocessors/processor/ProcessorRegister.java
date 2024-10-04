package tech.jt_dev.moreprocessors.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;
import tech.jt_dev.moreprocessors.PlatformHandler;
import tech.jt_dev.moreprocessors.processor.processors.*;
import tech.jt_dev.moreprocessors.processor.processors.CompatRuleProcessor;

import java.util.function.Supplier;

public class ProcessorRegister {

    public static final Supplier<StructureProcessorType<DirectionalBlockProcessor>> DIRECTIONAL_BLOCK_PROCESSOR = register("directional_block", () -> DirectionalBlockProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceOnTopProcessor>> PLACE_ON_TOP_PROCESSOR = register("place_on_top", () -> PlaceOnTopProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceBelowProcessor>> PLACE_BELOW_PROCESSOR = register("place_below", () -> PlaceBelowProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCropAgeProcessor>> RANDOM_CROP_AGE_PROCESSOR = register("random_crop_age", () -> RandomCropAgeProcessor.CODEC);
    public static final Supplier<StructureProcessorType<CompatReplaceProcessor>> COMPAT_REPLACE_PROCESSOR = register("compat_replace", () -> CompatReplaceProcessor.CODEC);
    public static final Supplier<StructureProcessorType<CompatReplaceSameStateProcessor>> COMPAT_REPLACE_SAME_STATE_PROCESSOR = register("compat_replace_same_state", () -> CompatReplaceSameStateProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCompatReplaceSameStateProcessor>> RANDOM_COMPAT_REPLACE_SAME_STATE_PROCESSOR = register("random_compat_replace_same_state", () -> RandomCompatReplaceSameStateProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCompatProcessor>> RANDOM_COMPAT_PROCESSOR = register("random_compat", () -> RandomCompatProcessor.CODEC);
    public static final Supplier<StructureProcessorType<CompatRuleProcessor>> COMPAT_RULE = register("compat_rule", () -> CompatRuleProcessor.CODEC);

    /**
     * Utility method for registering custom structure processor types.
     */
    private static <P extends StructureProcessor> Supplier<StructureProcessorType<P>> register(String id, @NotNull Supplier<Codec<P>> codec) {
        return PlatformHandler.PLATFORM_HANDLER.register(BuiltInRegistries.STRUCTURE_PROCESSOR, id, () -> codec::get);
    }


    public static void registerProcessors() {
        MoreStructureProcessors.LOGGER.info("Registering Structure Processors");
    }
}
