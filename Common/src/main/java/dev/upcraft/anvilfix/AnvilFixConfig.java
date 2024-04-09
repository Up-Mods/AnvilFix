package dev.upcraft.anvilfix;

import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;

@ConfigInfo(
        titleTranslation = "config.anvil_fix.title",
        descriptionTranslation = "config.anvil_fix.description",
        links = {
                @ConfigInfo.Link(
                        value = "https://modrinth.com/mod/EB79Qy8k",
                        icon = "modrinth",
                        text = "Modrinth"
                ),
                @ConfigInfo.Link(
                        value = "https://curseforge.com/projects/305480",
                        icon = "curseforge",
                        text = "Curseforge"
                ),
                @ConfigInfo.Link(
                        value = "https://github.com/Up-Mods/AnvilFix",
                        icon = "github",
                        text = "Github"
                )
        }
)
@Config(AnvilFix.MODID)
public final class AnvilFixConfig {

    @ConfigOption.Range(min = -1, max = 255)
    @ConfigEntry(id = "level_limit", type = EntryType.INTEGER, translation = "config.anvil_fix.level_limit")
    public static int levelLimit = -1;

    @ConfigOption.Range(min = -1, max = 255)
    @ConfigEntry(id = "global_enchantment_level_limit", type = EntryType.INTEGER, translation = "config.anvil_fix.global_enchantment_level_limit")
    public static int globalEnchantmentLevelLimit = -1;

    @ConfigEntry(id = "remove_incremental_repair_cost", type = EntryType.BOOLEAN, translation = "config.anvil_fix.remove_incremental_repair_cost")
    public static boolean removeIncrementalRepairCost = false;

    @ConfigEntry(id = "stop_anvil_breaking_on_craft", type = EntryType.BOOLEAN, translation = "config.anvil_fix.stop_anvil_breaking_on_craft")
    public static boolean stopAnvilBreakingOnCraft = true;

    @ConfigEntry(id = "stop_anvil_breaking_on_fall", type = EntryType.BOOLEAN, translation = "config.anvil_fix.stop_anvil_breaking_on_fall")
    public static boolean stopAnvilBreakingOnFall = false;

    public static int getLevelLimit() {
        return AnvilFixConfig.levelLimit >= 0 ? (AnvilFixConfig.levelLimit + 1) : Integer.MAX_VALUE;
    }

    public static int getGlobalEnchantmentLevelLimit(int original) {
        return AnvilFixConfig.globalEnchantmentLevelLimit >= 0 ? Math.min(AnvilFixConfig.globalEnchantmentLevelLimit, original) : original;
    }
}
