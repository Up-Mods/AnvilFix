package dev.upcraft.anvilfix.quilt.entrypoints;

import dev.upcraft.anvilfix.AnvilFix;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class Main implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        AnvilFix.init();
    }
}
