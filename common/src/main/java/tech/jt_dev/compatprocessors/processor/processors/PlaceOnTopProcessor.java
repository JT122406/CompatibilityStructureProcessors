package tech.jt_dev.compatprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.compatprocessors.CompatibilityStructureProcessors;
import tech.jt_dev.compatprocessors.processor.ProcessorRegister;

public class PlaceOnTopProcessor extends StructureProcessor {
	public static final Codec<PlaceOnTopProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("below").forGetter((below) -> below.below),
			BlockState.CODEC.fieldOf("above").forGetter((block) -> block.above),
			Codec.FLOAT.fieldOf("chance").forGetter((chance) -> chance.chance)
	).apply(instance, PlaceOnTopProcessor::new));

	private final Block below;
	private final BlockState above;
	private final float chance;

	public PlaceOnTopProcessor(Block below, BlockState above, float chance) {
		this.below = below;
		this.above = above;
		this.chance = chance;
	}

	public PlaceOnTopProcessor(Block below, Block above, float chance) {
		this.below = below;
		this.above = above.defaultBlockState();
		this.chance = chance;
	}

	public PlaceOnTopProcessor(Block below, Block above) {
		this(below, above, 1.0F);
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
		BlockPos relPos = relativeBlockInfo.pos();
        CompatibilityStructureProcessors.LOGGER.info("PlaceOnTopProcessor: Processing block at {}", relPos);
		if (level.getBlockState(relPos.below()).is(below) && settings.getRandom(relPos).nextFloat() < chance) {
            CompatibilityStructureProcessors.LOGGER.info("PlaceOnTopProcessor: Placing block at {}", relPos);
            CompatibilityStructureProcessors.LOGGER.info("PlaceOnTopProcessor: Block state: {}", above);
			return new StructureTemplate.StructureBlockInfo(relPos, above, relativeBlockInfo.nbt());
		}return relativeBlockInfo;
	}

	@Override
	protected @NotNull StructureProcessorType<?> getType() {
		return ProcessorRegister.PLACE_ON_TOP_PROCESSOR.get();
	}
}
