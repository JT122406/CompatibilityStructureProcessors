package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

public class BiomeBasedProcessor extends StructureProcessor {

	public static final Codec<BiomeBasedProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter((processor) -> processor.biome),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace").forGetter((processor) -> processor.replace),
			BlockState.CODEC.fieldOf("new_block").forGetter((processor) -> processor.newBlock)
	).apply(instance, BiomeBasedProcessor::new));

	private final ResourceKey<Biome> biome;
	private final Block replace;
	private final BlockState newBlock;

	public BiomeBasedProcessor(ResourceKey<Biome> biome, Block replace, Block newBlock) {
		this.biome = biome;
		this.replace = replace;
		this.newBlock = newBlock.defaultBlockState();
	}

	public BiomeBasedProcessor(ResourceKey<Biome> biome, Block replace, BlockState newBlock) {
		this.biome = biome;
		this.replace = replace;
		this.newBlock = newBlock;
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
		if (level.getBiome(pos).is(biome) && level.getBlockState(relativeBlockInfo.pos()).is(replace))
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlock, relativeBlockInfo.nbt());
		return relativeBlockInfo;
	}

	@Override
	protected @NotNull StructureProcessorType<?> getType() {
		return ProcessorRegister.BIOME_BASED_PROCESSOR.get();
	}
}
