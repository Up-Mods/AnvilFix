package dev.upcraft.anvilfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.upcraft.anvilfix.AnvilFixConfig;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingBlockEntity.class)
public class MixinFallingBlockEntity {

    @Shadow
    private BlockState blockState;

    @ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/AnvilBlock;damage(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState anvilfix$stopAnvilBreaking(BlockState original) {
        if (AnvilFixConfig.stopAnvilBreakingOnFall) {
            return this.blockState;
        }

        return original;
    }
}
