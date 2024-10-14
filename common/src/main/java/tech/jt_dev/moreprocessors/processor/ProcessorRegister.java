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

/**
 * Class for registering all custom structure processors.
 * @see StructureProcessorType
 * @author Joseph T. McQuigg
 */
public class ProcessorRegister {

    public static final Supplier<StructureProcessorType<DirectionalBlockProcessor>> DIRECTIONAL_BLOCK_PROCESSOR = register("directional_block", () -> DirectionalBlockProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceOnTopProcessor>> PLACE_ON_TOP_PROCESSOR = register("place_on_top", () -> PlaceOnTopProcessor.CODEC);
    public static final Supplier<StructureProcessorType<PlaceBelowProcessor>> PLACE_BELOW_PROCESSOR = register("place_below", () -> PlaceBelowProcessor.CODEC);
    public static final Supplier<StructureProcessorType<RandomCropAgeProcessor>> RANDOM_CROP_AGE_PROCESSOR = register("random_crop_age", () -> RandomCropAgeProcessor.CODEC);
    public static final Supplier<StructureProcessorType<CompatRuleProcessor>> COMPAT_RULE = register("compat_rule", () -> CompatRuleProcessor.CODEC);
    public static final Supplier<StructureProcessorType<SameStateCompatRuleProcessor>> SAME_STATE_COMPAT_RULE = register("same_state_compat_rule", () -> SameStateCompatRuleProcessor.CODEC);
    public static final Supplier<StructureProcessorType<SameStateRuleProcessor>> SAME_STATE_RULE = register("same_state_rule", () -> SameStateRuleProcessor.CODEC);
    public static final Supplier<StructureProcessorType<FlowingFluidRuleProcessor>> FLOWING_FLUID_RULE = register("flowing_fluid_rule", () -> FlowingFluidRuleProcessor.CODEC);
    public static final Supplier<StructureProcessorType<BiomeBasedProcessor>> BIOME_BASED_PROCESSOR = register("biome_based", () -> BiomeBasedProcessor.CODEC);
    public static final Supplier<StructureProcessorType<BiomeTagBasedProcessor>> BIOME_TAG_BASED_PROCESSOR = register("biome_tag_based", () -> BiomeTagBasedProcessor.CODEC);

    /**
     * Utility method for registering custom structure processor types.
     * @see PlatformHandler
     */
    private static <P extends StructureProcessor> Supplier<StructureProcessorType<P>> register(String id, @NotNull Supplier<Codec<P>> codec) {
        return PlatformHandler.PLATFORM_HANDLER.register(BuiltInRegistries.STRUCTURE_PROCESSOR, id, () -> codec::get);
    }


    public static void registerProcessors() {
        MoreStructureProcessors.LOGGER.info("Registering Structure Processors");
    }
}
