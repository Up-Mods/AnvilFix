package p455w0rd.anvilfix.init;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * @author p455w0rd
 *
 */
public class ModConfig {

	public static Configuration CONFIG;

	public static void init() {
		if (CONFIG == null) {
			File configFile = new File(ModGlobals.CONFIG_FILE);
			CONFIG = new Configuration(configFile);
			CONFIG.load();
		}

		Options.ignoreRepairCost = CONFIG.getBoolean("ignore_incremental_repair_cost", CATEGORY_GENERAL, true, "Ignore the cumulative repair cost");
		Options.levelLimit = CONFIG.getInt("anvil_level_limit", CATEGORY_GENERAL, -1, -1, Integer.MAX_VALUE, "Sets the level limit of combining Items in an Anvil (vanilla default is 40)");
		if (Options.levelLimit <= -1) {
			Options.levelLimit = Integer.MAX_VALUE;
		}
		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}
	}

	public static class Options {

		public static boolean ignoreRepairCost = true;
		public static int levelLimit = -1;
	}

}
