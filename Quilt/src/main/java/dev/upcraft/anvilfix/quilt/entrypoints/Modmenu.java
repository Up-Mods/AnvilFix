package dev.upcraft.anvilfix.quilt.entrypoints;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.upcraft.anvilfix.AnvilFix;
import dev.upcraft.anvilfix.AnvilFixConfig;

public class Modmenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, null, AnvilFix.CONFIGURATOR.getConfig(AnvilFixConfig.class));
    }
}
