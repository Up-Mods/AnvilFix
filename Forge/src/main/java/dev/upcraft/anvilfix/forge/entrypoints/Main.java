package dev.upcraft.anvilfix.forge.entrypoints;

import dev.upcraft.anvilfix.AnvilFix;
import net.minecraftforge.fml.common.Mod;

@Mod(AnvilFix.MODID)
public class Main {

    public Main() {
        AnvilFix.init();
    }
}
