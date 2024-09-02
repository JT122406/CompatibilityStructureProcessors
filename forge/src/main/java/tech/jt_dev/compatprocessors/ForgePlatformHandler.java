package tech.jt_dev.compatprocessors;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;

@AutoService(PlatformHandler.class)
public class ForgePlatformHandler implements PlatformHandler{
    public static final Map<ResourceKey<?>, DeferredRegister> CACHED = new Reference2ObjectOpenHashMap<>();

    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        return CACHED.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), CompatibilityStructureProcessors.MOD_ID)).register(name, value);
    }

    private static final DeferredRegister<StructureProcessorType<? extends StructureProcessor>> STRUCTURE_PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, CompatibilityStructureProcessors.MOD_ID);

    @Override
    public <P extends StructureProcessor> StructureProcessorType<P> registerStructureProcessor(String id, Codec<P> codec) {
        //var obj = STRUCTURE_PROCESSORS.register(id, () -> codec);
        return null;
    }

    public static void register(IEventBus bus) {
        CACHED.values().forEach(deferredRegister -> deferredRegister.register(bus));
        STRUCTURE_PROCESSORS.register(bus);
    }
}
