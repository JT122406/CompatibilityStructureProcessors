package tech.jt_dev.moreprocessors;

import net.minecraft.core.Registry;

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
}
