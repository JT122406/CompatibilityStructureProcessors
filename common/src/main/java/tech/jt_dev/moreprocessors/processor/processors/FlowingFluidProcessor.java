package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

@Deprecated
public class FlowingFluidProcessor extends StructureProcessor {

    public static final Codec<FlowingFluidProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((block) -> block.block),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter((fluid) -> fluid.fluid)
            ).apply(instance, FlowingFluidProcessor::new));

    private final Block block;

    private final Fluid fluid;

    public FlowingFluidProcessor(Block block, Fluid fluid) {
        this.block = block;
        this.fluid = fluid;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        if (relativeBlockInfo.state().is(block)) {
            ((LevelAccessor)level).scheduleTick(relativeBlockInfo.pos(), fluid, 0);
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), fluid.defaultFluidState().createLegacyBlock(), relativeBlockInfo.nbt());
        }
        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.FLOWING_FLUID_PROCESSOR.get();
    }
}
