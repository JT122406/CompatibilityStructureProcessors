package tech.jt_dev.compatprocessors;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.function.Supplier;

@AutoService(PlatformHandler.class)
public class FabricPlatformHandler implements PlatformHandler{
    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        T value1 = Registry.register(registry, CompatibilityStructureProcessors.id(name), value.get());
        return () -> value1;
    }

    @Override
    public <P extends StructureProcessor> StructureProcessorType<P> registerStructureProcessor(String id, Codec<P> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, CompatibilityStructureProcessors.id(id), () -> codec);
    }
}
