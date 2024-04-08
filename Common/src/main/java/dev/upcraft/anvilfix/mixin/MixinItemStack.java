package dev.upcraft.anvilfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.upcraft.anvilfix.AnvilFix;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @ModifyReturnValue(method = "getBaseRepairCost", at = @At(value = "RETURN"))
    private int anvilfix$getRepairCost(int original) {

        if(original > 0 && AnvilFix.removeIncrementalRepairCost((ItemStack)(Object)this)) {
            return 0;
        }

        return original;
    }
}
