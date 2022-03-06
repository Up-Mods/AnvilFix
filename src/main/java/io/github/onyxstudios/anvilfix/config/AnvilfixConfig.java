/*
 * AnvilFix
 *
 * Copyright (C) 2019-2022 OnyxStudios
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
package io.github.onyxstudios.anvilfix.config;

import io.github.onyxstudios.anvilfix.AnvilFix;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@SuppressWarnings("ALL")
@Config(name = AnvilFix.MODID)
public class AnvilfixConfig implements ConfigData {

    private int levelLimit = -1;

    private int globalEnchantmentLevelLimit = -1;

    private boolean removeIncrementalRepairCost = false;

    private boolean stopAnvilBreaking = true;

    public int getLevelLimit() {
        return levelLimit != -1 ? levelLimit + 1 : Integer.MAX_VALUE;
    }

    public int getEnchantmentLimit(Enchantment enchantment) {
        return globalEnchantmentLevelLimit >= 0 ? globalEnchantmentLevelLimit : enchantment.getMaxLevel();
    }

    public boolean removeIncrementalCost(ItemStack stack) {
        return removeIncrementalRepairCost && !stack.isIn(AnvilFix.FORCE_REPAIR_COST_TAG);
    }

    public boolean shouldStopAnvilBreaking() {
        return stopAnvilBreaking;
    }
}
