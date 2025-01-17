package tech.jt_dev.moreprocessors.processor.processors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;
import tech.jt_dev.moreprocessors.processor.processors.rules.SameStateCompatProcessorRule;

import java.util.List;

public class SameStateCompatRuleProcessor extends StructureProcessor {

    public static final Codec<SameStateCompatRuleProcessor> CODEC = SameStateCompatProcessorRule.CODEC
            .listOf()
            .fieldOf("rules")
            .xmap(SameStateCompatRuleProcessor::new, arg -> arg.rules)
            .codec();

    private final ImmutableList<SameStateCompatProcessorRule> rules;

    public SameStateCompatRuleProcessor(List<? extends SameStateCompatProcessorRule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        RandomSource randomSource = RandomSource.create(Mth.getSeed(relativeBlockInfo.pos()));
        BlockState blockState = level.getBlockState(relativeBlockInfo.pos());

        for (SameStateCompatProcessorRule processorRule : this.rules)
            if (processorRule.test(relativeBlockInfo.state(), blockState, blockInfo.pos(), relativeBlockInfo.pos(), pos, randomSource))
                return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), processorRule.getOutputBlock().withPropertiesOf(relativeBlockInfo.state()), processorRule.getOutputTag(randomSource, relativeBlockInfo.nbt()));

        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.SAME_STATE_COMPAT_RULE.get();
    }
}
