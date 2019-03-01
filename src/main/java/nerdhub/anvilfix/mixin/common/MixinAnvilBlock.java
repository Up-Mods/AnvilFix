package nerdhub.anvilfix.mixin.common;

import nerdhub.anvilfix.AnvilFix;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class MixinAnvilBlock {

    @Inject(method = "getLandingState", at = @At("HEAD"), cancellable = true)
    private static void getLandingState(BlockState orignalState, CallbackInfoReturnable<BlockState> cir) {
        if(AnvilFix.getConfig().shouldStopAnvilBreaking()) {
            cir.setReturnValue(orignalState);
        }
    }
}
