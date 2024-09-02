package tech.jt_dev.compatprocessors;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface PlatformHandler {

    PlatformHandler PLATFORM_HANDLER = load(PlatformHandler.class);

    private static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }


    <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value);

    <P extends StructureProcessor> StructureProcessorType<P> registerStructureProcessor(String id, Codec<P> codec);

}
