package dev.upcraft.anvilfix.fabric.entrypoints;

import dev.upcraft.anvilfix.AnvilFix;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        AnvilFix.init();
    }
}
