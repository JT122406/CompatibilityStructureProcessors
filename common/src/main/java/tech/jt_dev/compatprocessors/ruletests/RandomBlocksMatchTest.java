package tech.jt_dev.compatprocessors.ruletests;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RandomBlocksMatchTest extends RuleTest {

    public static final Codec<RandomBlocksMatchTest> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("blocks").forGetter((test) -> {
                    return test.blocks;
                }), Codec.FLOAT.listOf().optionalFieldOf("probabilities").forGetter((test) -> {
                    return test.probabilities;
                })).apply(instance, RandomBlocksMatchTest::new);
    });

    private final List<Block> blocks;
    private final Optional<List<Float>> probabilities;

    public RandomBlocksMatchTest(List<Block> blocks, Optional<List<Float>> probabilities) {
        this.blocks = blocks;
        this.probabilities = Optional.empty();
    }

    public RandomBlocksMatchTest(List<Pair<Block, Float>> blocks) {
        this.blocks = blocks.stream().map(Pair::getFirst).toList();
        this.probabilities = Optional.of(blocks.stream().map(Pair::getSecond).toList());
    }

    public RandomBlocksMatchTest(Block... blocks) {
        this.blocks = List.of(blocks);
        this.probabilities = Optional.empty();
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return false;
    }

    @Override
    protected @NotNull RuleTestType<?> getType() {
        return null;
    }
}
