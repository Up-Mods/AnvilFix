/*
 * AnvilFix
 *
 * Copyright (C) 2019 NerdhubMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.onyxstudios.anvilfix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.onyxstudios.anvilfix.config.AnvilFixConfigReloadListener;
import io.github.onyxstudios.anvilfix.config.AnvilfixConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AnvilFix implements ModInitializer {

    public static final String MODID = "anvil_fix";
    private static final Logger LOG = LogManager.getLogger(MODID);
    private static final String FABRIC_ID = "fabric";
    private static AnvilfixConfig config;

    public static AnvilfixConfig getConfig() {
        return config;
    }

    @Override
    public void onInitialize() {
        reloadConfig();
        if(FabricLoader.getInstance().isModLoaded(FABRIC_ID)) {
            LOG.info("found fabric API, registering reload listener");
            AnvilFixConfigReloadListener.init();
        }
    }

    public static void reloadConfig() {
        LOG.info("[{}] Reloading config", MODID);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), MODID + ".json");
        if(!configFile.exists()) {
            try(FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(new AnvilfixConfig(), writer);
            }
            catch (IOException e) {
                LOG.error("unable to write config file to " + configFile.getAbsolutePath(), e);
            }
        }
        try(FileReader reader = new FileReader(configFile)) {
            config = gson.fromJson(reader, AnvilfixConfig.class);
        }
        catch (IOException e) {
            LOG.error("unable to read config file from " + configFile.getAbsolutePath() + ", falling back to default values!", e);
            config = new AnvilfixConfig();
        }
    }
}
