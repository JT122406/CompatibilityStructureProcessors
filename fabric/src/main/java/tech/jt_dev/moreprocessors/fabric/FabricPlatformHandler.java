package tech.jt_dev.moreprocessors.fabric;

import com.google.auto.service.AutoService;
import net.minecraft.core.Registry;
import tech.jt_dev.moreprocessors.MoreStructureProcessors;
import tech.jt_dev.moreprocessors.PlatformHandler;

import java.util.function.Supplier;

@AutoService(PlatformHandler.class)
public class FabricPlatformHandler implements PlatformHandler{
    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
        T value1 = Registry.register(registry, MoreStructureProcessors.id(name), value.get());
        return () -> value1;
    }
}
