package tech.jt_dev.moreprocessors.forge;

import com.google.auto.service.AutoService;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;
import tech.jt_dev.moreprocessors.PlatformHandler;

import java.util.Map;
import java.util.function.Supplier;

@AutoService(PlatformHandler.class)
public class ForgePlatformHandler implements PlatformHandler{
    public static final Map<ResourceKey<?>, DeferredRegister> CACHED = new Reference2ObjectOpenHashMap<>();

    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        return CACHED.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), MoreStructureProcessors.MOD_ID)).register(name, value);
    }

    public static void register(IEventBus bus) {
        CACHED.values().forEach(deferredRegister -> deferredRegister.register(bus));
    }
}
