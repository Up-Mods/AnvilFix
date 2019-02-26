package p455w0rd.anvilfix.init;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import p455w0rd.anvilfix.AnvilFix;
import p455w0rd.anvilfix.init.ModConfig.Options;

/**
 * @author p455w0rd
 *
 */
@EventBusSubscriber(modid = ModGlobals.MODID)
public class ModEvents {

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent event) {
		if (ModConfig.CONFIG.hasChanged()) {
			ModConfig.CONFIG.save();
		}
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		if (Options.ignoreRepairCost && event.getCost() > 0) {
			event.setCost(0);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event) {
		if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.ANVIL) {
			if (!event.getWorld().isRemote) {
				event.getEntityPlayer().openGui(AnvilFix.INSTANCE, 0, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
				event.setCancellationResult(EnumActionResult.FAIL);
				event.setCanceled(true);
			}
		}
	}

}
