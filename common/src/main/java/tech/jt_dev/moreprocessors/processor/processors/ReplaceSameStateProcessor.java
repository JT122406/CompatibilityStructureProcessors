package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

public class ReplaceSameStateProcessor extends StructureProcessor {

    public static final Codec<ReplaceSameStateProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((block) -> block.block),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace").forGetter((replace) -> replace.replace)
    ).apply(instance, ReplaceSameStateProcessor::new));

    private final Block block;
    private final Block replace;

    public ReplaceSameStateProcessor(Block block, Block replace) {
        this.block = block;
        this.replace = replace;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        if (relativeBlockInfo.state().is(block))
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), replace.withPropertiesOf(relativeBlockInfo.state()), relativeBlockInfo.nbt());
        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.REPLACE_SAME_STATE_PROCESSOR.get();
    }
}
