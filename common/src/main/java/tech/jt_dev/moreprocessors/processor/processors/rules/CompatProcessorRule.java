package tech.jt_dev.moreprocessors.processor.processors.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;

import javax.annotation.Nullable;

public class CompatProcessorRule  {
    public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
    public static final Codec<CompatProcessorRule> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            RuleTest.CODEC.fieldOf("input_predicate").forGetter(arg -> arg.inputPredicate),
                            RuleTest.CODEC.fieldOf("location_predicate").forGetter(arg -> arg.locPredicate),
                            PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(arg -> arg.posPredicate),
                            ResourceLocation.CODEC.fieldOf("output_location").forGetter(arg -> arg.outputLocation),
                            BlockState.CODEC.fieldOf("output_state").forGetter(arg -> arg.outputState),
                            RuleBlockEntityModifier.CODEC.optionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(arg -> arg.blockEntityModifier)
                    )
                    .apply(instance, CompatProcessorRule::new)
    );

    private final RuleTest inputPredicate;
    private final RuleTest locPredicate;
    private final PosRuleTest posPredicate;
    private final ResourceLocation outputLocation;
    private final BlockState outputState;
    private final RuleBlockEntityModifier blockEntityModifier;

    public CompatProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, ResourceLocation outputLocation, BlockState outputState, RuleBlockEntityModifier blockEntityModifier) {
        this.inputPredicate = inputPredicate;
        this.locPredicate = locPredicate;
        this.posPredicate = posPredicate;
        this.outputLocation = outputLocation;
        this.outputState = outputState;
        this.blockEntityModifier = blockEntityModifier;
    }

    public CompatProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, ResourceLocation outputLocation, BlockState outputState) {
        this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputLocation, outputState, DEFAULT_BLOCK_ENTITY_MODIFIER);
    }

    public CompatProcessorRule(RuleTest inputPredicate, ResourceLocation outputLocation, BlockState outputState) {
        this(inputPredicate, AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, outputLocation, outputState, DEFAULT_BLOCK_ENTITY_MODIFIER);
    }

    public CompatProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, Block outputBlock) {
        this(inputPredicate, locPredicate, BuiltInRegistries.BLOCK.getKey(outputBlock), outputBlock.defaultBlockState());
    }

    public CompatProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState) {
        this(inputPredicate, locPredicate, BuiltInRegistries.BLOCK.getKey(outputState.getBlock()), outputState);
    }

    public CompatProcessorRule(RuleTest inputPredicate, Block outputBlock) {
        this(inputPredicate, BuiltInRegistries.BLOCK.getKey(outputBlock), outputBlock.defaultBlockState());
    }

    public CompatProcessorRule(RuleTest inputPredicate, BlockState outputState) {
        this(inputPredicate, BuiltInRegistries.BLOCK.getKey(outputState.getBlock()), outputState);
    }

    public boolean test(BlockState inputState, BlockState existingState, BlockPos localPos, BlockPos relativePos, BlockPos structurePos, RandomSource random) {
        return BuiltInRegistries.BLOCK.containsKey(outputLocation)
                && this.inputPredicate.test(inputState, random)
                && this.locPredicate.test(existingState, random)
                && this.posPredicate.test(localPos, relativePos, structurePos, random);
    }

    public BlockState getOutputState() {
        return outputState;
    }

    @Nullable
    public CompoundTag getOutputTag(RandomSource random, @Nullable CompoundTag tag) {
        return this.blockEntityModifier.apply(random, tag);
    }
}
