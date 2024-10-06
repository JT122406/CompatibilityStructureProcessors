package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

import java.util.List;

/**
 * Processor that places a block above another block.
 * @see StructureProcessor
 * @author Joseph T. McQuigg
 */
@Deprecated(forRemoval = true)
public class PlaceOnTopProcessor extends StructureProcessor {

	public static final Codec<PlaceOnTopProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("below").forGetter((below) -> below.below),
			BlockState.CODEC.fieldOf("above").forGetter((block) -> block.above),
			Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter((chance) -> chance.chance),
			Codec.BOOL.optionalFieldOf("air", false).forGetter((air) -> air.air)
	).apply(instance, PlaceOnTopProcessor::new));

	/** The block that must be below the block to be placed */
	private final Block below;
	/** The block that will be placed above the given below block */
	private final BlockState above;
	/** The chance that the block will be placed */
	private final float chance;
	/** If true, the block will only be placed if the block above is air */
	private final boolean air;

	public PlaceOnTopProcessor(Block below, BlockState above, float chance, boolean air) {
		this.below = below;
		this.above = above;
		this.chance = chance;
		this.air = air;
	}

	public PlaceOnTopProcessor(Block below, Block above, float chance, boolean air) {
		this(below, above.defaultBlockState(), chance, air);
	}

	public PlaceOnTopProcessor(Block below, Block above, float chance) {
		this(below, above, chance, false);
	}

	public PlaceOnTopProcessor(Block below, BlockState above, float chance) {
		this(below, above, chance, false);
	}

	public PlaceOnTopProcessor(Block below, Block above) {
		this(below, above, 1.0F);
	}

	public PlaceOnTopProcessor(Block below, BlockState above) {
		this(below, above, 1.0F);
	}

	@Override
	public @NotNull List<StructureTemplate.StructureBlockInfo> finalizeProcessing(@NotNull ServerLevelAccessor serverLevel, @NotNull BlockPos offset, @NotNull BlockPos pos, @NotNull List<StructureTemplate.StructureBlockInfo> originalBlockInfos, @NotNull List<StructureTemplate.StructureBlockInfo> processedBlockInfos, @NotNull StructurePlaceSettings settings) {
		List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(processedBlockInfos));

		processedBlockInfos.stream().filter(blockInfo -> blockInfo.state().is(this.below)).forEach(below -> {
			BlockPos belowPos = below.pos();
				newInfo.stream().filter(blockInfo -> blockInfo.pos().equals(belowPos.above())).findFirst().ifPresent(spot -> {
					if (!air || spot.state().isAir())
						if (serverLevel.getRandom().nextFloat() < chance) {
							newInfo.remove(spot);
							newInfo.add(new StructureTemplate.StructureBlockInfo(spot.pos(), above, spot.nbt()));
						}
				});
		});
		return newInfo;
	}

	@Override
	protected @NotNull StructureProcessorType<?> getType() {
		return ProcessorRegister.PLACE_ON_TOP_PROCESSOR.get();
	}
}
