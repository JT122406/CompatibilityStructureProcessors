package tech.jt_dev.compatprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Replaces a block with another block based on chance
 * Places the block in the same direction as the original block
 * Useful for replacing blocks like furnaces with blast furnaces
 */
public class DirectionalBlockProcessor extends StructureProcessor {

    public static final Codec<DirectionalBlockProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("old").forGetter((old) -> old.old),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((block) -> block.block),
            Codec.FLOAT.fieldOf("chance").forGetter((chance) -> chance.chance)
    ).apply(instance, DirectionalBlockProcessor::new));

    private final Block old;
    private final Block block;
    private final float chance;

    public DirectionalBlockProcessor(Block old, Block block, float chance) {
        this.old = old;
        this.block = block;
        this.chance = chance;
    }


    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        if (relativeBlockInfo.state().is(old)) {
            if (block.defaultBlockState().hasProperty(BlockStateProperties.FACING) && old.defaultBlockState().hasProperty(BlockStateProperties.FACING) &&
                    settings.getRandom(relativeBlockInfo.pos()).nextFloat() < chance)
                return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), block.defaultBlockState().setValue(BlockStateProperties.FACING, relativeBlockInfo.state().getValue(BlockStateProperties.FACING)), relativeBlockInfo.nbt());
        }return super.processBlock(level, offset, pos, blockInfo, relativeBlockInfo, settings);
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.DIRECTIONAL_BLOCK_PROCESSOR.get();
    }
}
