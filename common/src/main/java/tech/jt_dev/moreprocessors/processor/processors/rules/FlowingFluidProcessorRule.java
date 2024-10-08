package tech.jt_dev.moreprocessors.processor.processors.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
import net.minecraft.world.level.material.Fluid;

public class FlowingFluidProcessorRule {

    public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
    public static final Codec<FlowingFluidProcessorRule> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RuleTest.CODEC.fieldOf("input_predicate").forGetter(arg -> arg.inputPredicate),
                    RuleTest.CODEC.fieldOf("location_predicate").forGetter(arg -> arg.locPredicate),
                    PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(arg -> arg.posPredicate),
                    BuiltInRegistries.FLUID.byNameCodec().fieldOf("output_location").forGetter(arg -> arg.outputFluid),
                    RuleBlockEntityModifier.CODEC.optionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(arg -> arg.blockEntityModifier)
            ).apply(instance, FlowingFluidProcessorRule::new)
    );

    private final RuleTest inputPredicate;
    private final RuleTest locPredicate;
    private final PosRuleTest posPredicate;
    private final Fluid outputFluid;
    private final RuleBlockEntityModifier blockEntityModifier;

    public FlowingFluidProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, Fluid outputFluid, RuleBlockEntityModifier blockEntityModifier) {
        this.inputPredicate = inputPredicate;
        this.locPredicate = locPredicate;
        this.posPredicate = posPredicate;
        this.outputFluid = outputFluid;
        this.blockEntityModifier = blockEntityModifier;
    }

    public FlowingFluidProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, Fluid outputFluid) {
        this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputFluid, DEFAULT_BLOCK_ENTITY_MODIFIER);
    }


    public boolean test(BlockState inputState, BlockState existingState, BlockPos localPos, BlockPos relativePos, BlockPos structurePos, RandomSource random) {
        return  this.inputPredicate.test(inputState, random)
                && this.locPredicate.test(existingState, random)
                && this.posPredicate.test(localPos, relativePos, structurePos, random);
    }

    public Fluid getOutputFluid() {
        return outputFluid;
    }
}
