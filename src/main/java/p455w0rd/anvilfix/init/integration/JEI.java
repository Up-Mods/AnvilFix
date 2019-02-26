package p455w0rd.anvilfix.init.integration;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import p455w0rd.anvilfix.client.GuiRepairHacked;
import p455w0rd.anvilfix.container.ContainerRepairHacked;

/**
 * @author p455w0rd
 *
 */
@JEIPlugin
public class JEI implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeClickArea(GuiRepairHacked.class, 102, 48, 22, 15, VanillaRecipeCategoryUid.ANVIL);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRepairHacked.class, VanillaRecipeCategoryUid.ANVIL, 0, 2, 3, 36);
	}

}
