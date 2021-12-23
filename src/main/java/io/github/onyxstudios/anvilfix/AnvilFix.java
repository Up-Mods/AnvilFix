/*
 * AnvilFix
 *
 * Copyright (C) 2019-2021 OnyxStudios
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

import dev.upcraft.mesh.api.util.config.ConfigHandler;
import io.github.onyxstudios.anvilfix.config.AnvilfixConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AnvilFix implements ModInitializer {

    public static final String MODID = "anvil_fix";

    public static final Tag<Item> FORCE_REPAIR_COST_TAG = TagRegistry.item(new Identifier(MODID, "force_incremental_repair_cost"));

    public static AnvilfixConfig getConfig() {
        return ConfigHandler.getConfig(AnvilfixConfig.class);
    }

    @Override
    public void onInitialize() {
        ConfigHandler.registerConfig(MODID, AnvilfixConfig.class);
    }
}
