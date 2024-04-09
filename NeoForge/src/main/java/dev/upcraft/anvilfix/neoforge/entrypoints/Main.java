package dev.upcraft.anvilfix.neoforge.entrypoints;

import dev.upcraft.anvilfix.AnvilFix;
import net.neoforged.fml.common.Mod;

@Mod(AnvilFix.MODID)
public class Main {

    public Main() {
        AnvilFix.init();
    }
}
