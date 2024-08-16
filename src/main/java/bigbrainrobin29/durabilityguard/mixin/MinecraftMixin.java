package bigbrainrobin29.durabilityguard.mixin;

import bigbrainrobin29.durabilityguard.DurabilityGuardConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public LocalPlayer player;

    @Unique
    SystemToast.SystemToastId systemToastId = new SystemToast.SystemToastId(500);

    @Shadow @Nullable public HitResult hitResult;

    @Shadow public abstract ToastComponent getToasts();

    @Inject(method = "startAttack", at = @At(value = "HEAD"), cancellable = true)
    void check(CallbackInfoReturnable<Boolean> cir) {
        checkDamage(() -> cir.setReturnValue(false));
    }

    @Inject(method = "continueAttack", at = @At(value = "HEAD"), cancellable = true)
    void check(boolean bl, CallbackInfo ci) {
        if (bl && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
            checkDamage(ci::cancel);
        }
    }

    @Inject(method = "startUseItem", at = @At(value = "HEAD"), cancellable = true)
    void check(CallbackInfo ci) {
        checkDamage(ci::cancel);
    }

    @Unique
    void checkDamage(Runnable cancel) {
        if (!player.getMainHandItem().isEmpty()) {
            boolean shouldStop = player.getMainHandItem().isDamageableItem() && DurabilityGuardConfig.active && (!DurabilityGuardConfig.ignoredItems.contains(BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString())) && switch (DurabilityGuardConfig.limitType) {
                case PERCENTAGE -> (float)(player.getMainHandItem().getMaxDamage() - (player.getMainHandItem().getDamageValue() + 1)) / (float)player.getMainHandItem().getMaxDamage() * 100f < DurabilityGuardConfig.minPercentage;
                case NUMBER -> player.getMainHandItem().getMaxDamage() - (player.getMainHandItem().getDamageValue() + 1) < DurabilityGuardConfig.minDurability;
                case BOTH -> (float)(player.getMainHandItem().getMaxDamage() - (player.getMainHandItem().getDamageValue() + 1)) / (float)player.getMainHandItem().getMaxDamage() * 100f < DurabilityGuardConfig.minPercentage || player.getMainHandItem().getMaxDamage() - (player.getMainHandItem().getDamageValue() + 1) < DurabilityGuardConfig.minDurability;
            };
            if (shouldStop) {
                SystemToast.addOrUpdate(this.getToasts(), systemToastId, Component.literal("Durability Guard"), Component.literal("Your tool has low durability!").withStyle(ChatFormatting.RED));
                cancel.run();
            }
        }
    }
}
