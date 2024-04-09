package dev.upcraft.anvilfix;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AnvilFix {

    public static final String MODID = "anvil_fix";
    public static final Configurator CONFIGURATOR = new Configurator(MODID);
    private static final TagKey<Item> FORCE_REPAIR_COST_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(MODID, "force_incremental_repair_cost"));

    public static void init() {
        CONFIGURATOR.register(AnvilFixConfig.class);
    }

    public static boolean removeIncrementalRepairCost(ItemStack itemStack) {
        return AnvilFixConfig.removeIncrementalRepairCost && !itemStack.is(FORCE_REPAIR_COST_TAG);
    }
}
