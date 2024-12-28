package tech.jt_dev.moreprocessors.processor.processors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;
import tech.jt_dev.moreprocessors.processor.processors.rules.FlowingFluidProcessorRule;

import java.util.List;

public class FlowingFluidRuleProcessor extends StructureProcessor {

    public static final MapCodec<FlowingFluidRuleProcessor> CODEC = FlowingFluidProcessorRule.CODEC
            .listOf()
            .fieldOf("rules")
            .xmap(FlowingFluidRuleProcessor::new, arg -> arg.rules);

    private final ImmutableList<FlowingFluidProcessorRule> rules;

    public FlowingFluidRuleProcessor(List<? extends FlowingFluidProcessorRule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        for (FlowingFluidProcessorRule processorRule : this.rules)
            if (processorRule.test(relativeBlockInfo.state(), relativeBlockInfo.state(), blockInfo.pos(), relativeBlockInfo.pos(), pos, RandomSource.create(Mth.getSeed(relativeBlockInfo.pos())))) {
                ((LevelAccessor)level).scheduleTick(relativeBlockInfo.pos(), processorRule.getOutputFluid(), 0);
                return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), processorRule.getOutputFluid().defaultFluidState().createLegacyBlock(), relativeBlockInfo.nbt());
            }

        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.FLOWING_FLUID_RULE.get();
    }
}
