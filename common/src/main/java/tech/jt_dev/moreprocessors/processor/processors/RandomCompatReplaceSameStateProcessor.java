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

public class RandomCompatReplaceSameStateProcessor extends StructureProcessor {

    public static final Codec<RandomCompatReplaceSameStateProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((block) -> block.block),
            ResourceLocation.CODEC.fieldOf("replace").forGetter((replace) -> replace.replace),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter((chance) -> chance.chance)
    ).apply(instance, RandomCompatReplaceSameStateProcessor::new));

    private final Block block;
    private final ResourceLocation replace;
    private final float chance;

    public RandomCompatReplaceSameStateProcessor(Block block, Block replace, float chance) {
        this.block = block;
        this.replace = BuiltInRegistries.BLOCK.getKey(replace);
        this.chance = chance;
    }

    public RandomCompatReplaceSameStateProcessor(Block block, ResourceLocation replace, float chance) {
        this.block = block;
        this.replace = replace;
        this.chance = chance;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        if (relativeBlockInfo.state().is(block) && BuiltInRegistries.BLOCK.containsKey(replace) && settings.getRandom(relativeBlockInfo.pos()).nextFloat() < chance)
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), BuiltInRegistries.BLOCK.get(replace).withPropertiesOf(relativeBlockInfo.state()), relativeBlockInfo.nbt());
        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.RANDOM_COMPAT_REPLACE_SAME_STATE_PROCESSOR.get();
    }
}
