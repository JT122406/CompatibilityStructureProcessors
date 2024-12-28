package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
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
 * Processor that places a block below another block.
 * @see StructureProcessor
 * @author Joseph T. McQuigg
 */
public class PlaceBelowProcessor extends StructureProcessor {

    public static final MapCodec<PlaceBelowProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("above").forGetter((below) -> below.above),
            BlockState.CODEC.fieldOf("below").forGetter((block) -> block.below),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter((chance) -> chance.chance),
            Codec.BOOL.optionalFieldOf("air", false).forGetter((air) -> air.air)
    ).apply(instance, PlaceBelowProcessor::new));

    /** The block that must be above the block to be placed */
    private final Block above;
    /** The block that will be placed below the given above block */
    private final BlockState below;
    /** The chance that the block will be placed */
    private final float chance;
    /** If true, the block will only be placed if the block below is air */
    private final boolean air;

    public PlaceBelowProcessor(Block above, BlockState below, float chance, boolean air) {
        this.above = above;
        this.below = below;
        this.chance = chance;
        this.air = air;
    }

    public PlaceBelowProcessor(Block above, Block below, float chance, boolean air) {
        this(above, below.defaultBlockState(), chance, air);
    }

    public PlaceBelowProcessor(Block above, Block below, float chance) {
        this(above, below, chance, false);
    }

    public PlaceBelowProcessor(Block above, BlockState below, float chance) {
        this(above, below, chance, false);
    }

    public PlaceBelowProcessor(Block above, Block below) {
        this(above, below, 1.0F);
    }

    public PlaceBelowProcessor(Block above, BlockState below) {
        this(above, below, 1.0F);
    }

    @Override
    public @NotNull List<StructureTemplate.StructureBlockInfo> finalizeProcessing(@NotNull ServerLevelAccessor serverLevel, @NotNull BlockPos offset, @NotNull BlockPos pos, @NotNull List<StructureTemplate.StructureBlockInfo> originalBlockInfos, @NotNull List<StructureTemplate.StructureBlockInfo> processedBlockInfos, @NotNull StructurePlaceSettings settings) {
        List<StructureTemplate.StructureBlockInfo> newInfo = new java.util.ArrayList<>(List.copyOf(processedBlockInfos));

        processedBlockInfos.stream().filter(blockInfo -> blockInfo.state().is(this.above)).forEach(above -> {
            BlockPos abovePos = above.pos();
            newInfo.stream().filter(blockInfo -> blockInfo.pos().equals(abovePos.below())).findFirst().ifPresent(spot -> {
                if (!air || spot.state().isAir())
                    if (serverLevel.getRandom().nextFloat() < chance) {
                        newInfo.remove(spot);
                        newInfo.add(new StructureTemplate.StructureBlockInfo(spot.pos(), this.below, spot.nbt()));
                    }
            });
        });

        return newInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.PLACE_BELOW_PROCESSOR.get();
    }
}
