package dev.upcraft.anvilfix.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.upcraft.anvilfix.AnvilFixConfig;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen {

    @ModifyExpressionValue(method = "renderLabels", at = @At(value = "CONSTANT", args = "intValue=40", ordinal = 0))
    private int anvilfix$modifyLevelLimit(int original) {
        return AnvilFixConfig.getLevelLimit();
    }
}
