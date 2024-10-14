package tech.jt_dev.moreprocessors.processor.processors.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;

import javax.annotation.Nullable;

public class BiomeProcessorRule {
	public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;

	public static final Codec<BiomeProcessorRule> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							RuleTest.CODEC.fieldOf("input_predicate").forGetter(arg -> arg.inputPredicate),
							RuleTest.CODEC.fieldOf("location_predicate").forGetter(arg -> arg.locPredicate),
							PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(arg -> arg.posPredicate),
							BlockState.CODEC.fieldOf("output_state").forGetter(arg -> arg.outputState),
							RuleBlockEntityModifier.CODEC.optionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(arg -> arg.blockEntityModifier),
							ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(arg -> arg.biome)
					)
					.apply(instance, BiomeProcessorRule::new)
	);

	private final RuleTest inputPredicate;
	private final RuleTest locPredicate;
	private final PosRuleTest posPredicate;
	private final BlockState outputState;
	private final RuleBlockEntityModifier blockEntityModifier;
	private final ResourceKey<Biome> biome;

	public BiomeProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState, RuleBlockEntityModifier blockEntityModifier, ResourceKey<Biome> biome) {
		this.inputPredicate = inputPredicate;
		this.locPredicate = locPredicate;
		this.posPredicate = posPredicate;
		this.outputState = outputState;
		this.blockEntityModifier = blockEntityModifier;
		this.biome = biome;
	}

	public BiomeProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState, ResourceKey<Biome> biome) {
		this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputState, DEFAULT_BLOCK_ENTITY_MODIFIER, biome);
	}

	public BiomeProcessorRule(RuleTest inputPredicate, BlockState outputState, ResourceKey<Biome> biome) {
		this(inputPredicate, AlwaysTrueTest.INSTANCE, outputState, biome);
	}

	public BiomeProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, Block outputBlock, ResourceKey<Biome> biome) {
		this(inputPredicate, locPredicate, outputBlock.defaultBlockState(), biome);
	}

	public BiomeProcessorRule(RuleTest inputPredicate, Block outputBlock, ResourceKey<Biome> biome) {
		this(inputPredicate, outputBlock.defaultBlockState(), biome);
	}

	public boolean test(LevelReader level, BlockState inputState, BlockState existingState, BlockPos localPos, BlockPos relativePos, BlockPos structurePos, RandomSource random) {
		return  level.getBiome(relativePos).is(biome)
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
