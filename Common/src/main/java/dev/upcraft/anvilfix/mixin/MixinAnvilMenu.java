package dev.upcraft.anvilfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.upcraft.anvilfix.AnvilFixConfig;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class MixinAnvilMenu {

    @ModifyExpressionValue(method = "createResult", at = @At(value = "CONSTANT", args = "intValue=40", ordinal = 2))
    private int anvilfix$modifyLevelLimit(int original) {
        return AnvilFixConfig.getLevelLimit();
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private int anvilfix$modifyGlobalEnchantmentLevelLimit(int original) {
        return AnvilFixConfig.getGlobalEnchantmentLevelLimit(original);
    }

    @ModifyExpressionValue(method = "lambda$onTake$2", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"))
    private static boolean anvilfix$stopAnvilBreaking(boolean original) {
        return original || AnvilFixConfig.stopAnvilBreakingOnCraft;
    }
}
