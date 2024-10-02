package tech.jt_dev.moreprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.moreprocessors.processor.ProcessorRegister;

public class CompatReplaceSameStateProcessor extends StructureProcessor {

    public static final Codec<CompatReplaceSameStateProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((block) -> block.block),
            ResourceLocation.CODEC.fieldOf("replace").forGetter((replace) -> replace.replace)
    ).apply(instance, CompatReplaceSameStateProcessor::new));

    private final Block block;
    private final ResourceLocation replace;

    public CompatReplaceSameStateProcessor(Block block, Block replace) {
        this.block = block;
        this.replace = BuiltInRegistries.BLOCK.getKey(replace);
    }

    private CompatReplaceSameStateProcessor(Block block, ResourceLocation replace) {
        this.block = block;
        this.replace = replace;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        if (relativeBlockInfo.state().is(block) && BuiltInRegistries.BLOCK.containsKey(replace))
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), BuiltInRegistries.BLOCK.get(replace).withPropertiesOf(relativeBlockInfo.state()), relativeBlockInfo.nbt());
        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.COMPAT_REPLACE_SAME_STATE_PROCESSOR.get();
    }
}
