package tech.jt_dev.compatprocessors.processor.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.jt_dev.compatprocessors.processor.ProcessorRegister;

public class RandomCropAgeProcessor extends StructureProcessor {

    public static final Codec<RandomCropAgeProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("crop").forGetter((block) -> block.crop)
    ).apply(instance, RandomCropAgeProcessor::new));

    private final CropBlock crop;

    private RandomCropAgeProcessor(Block block) {
        if (!(block instanceof CropBlock cropBlock))
            throw new IllegalArgumentException("Block must be a crop block");
        this.crop = cropBlock;
    }

    public RandomCropAgeProcessor(CropBlock crop) {
        this.crop = crop;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader level, @NotNull BlockPos offset, @NotNull BlockPos pos, StructureTemplate.@NotNull StructureBlockInfo blockInfo, StructureTemplate.@NotNull StructureBlockInfo relativeBlockInfo, @NotNull StructurePlaceSettings settings) {
        BlockPos relPos = relativeBlockInfo.pos();
        if (level.getBlockState(relPos).is(crop))
            return new StructureTemplate.StructureBlockInfo(relPos, crop.getStateForAge(settings.getRandom(relPos).nextInt(crop.getMaxAge())), relativeBlockInfo.nbt());
        return relativeBlockInfo;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ProcessorRegister.RANDOM_CROP_AGE_PROCESSOR.get();
    }
}
