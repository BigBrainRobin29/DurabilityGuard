package bigbrainrobin29.durabilityguard.mixin;

import bigbrainrobin29.durabilityguard.DurabilityGuardConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Pattern;

@Mixin(MinecraftClient.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Unique
    SystemToast.Type systemToastId = new SystemToast.Type(500);

    @Shadow @Nullable public HitResult crosshairTarget;


    @Shadow public abstract ToastManager getToastManager();


    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "doAttack", at = @At(value = "HEAD"), cancellable = true)
    void check(CallbackInfoReturnable<Boolean> cir) {
        checkDamage(() -> cir.setReturnValue(false));
    }

    @Inject(method = "handleBlockBreaking", at = @At(value = "HEAD"), cancellable = true)
    void check(boolean bl, CallbackInfo ci) {
        if (bl && this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            checkDamage(ci::cancel);
        }
    }

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), cancellable = true)
    void check(CallbackInfo ci) {
        checkDamage(ci::cancel);
    }

    @Unique
    void checkDamage(Runnable cancel) {
        if (!player.getMainHandStack().isEmpty()) {
            boolean damageable = player.getMainHandStack().isDamageable();
            boolean shouldStop = damageable && !player.isInCreativeMode() && DurabilityGuardConfig.active && !isIgnored() &&
                switch (DurabilityGuardConfig.limitType) {
                    case PERCENTAGE -> (float)(player.getMainHandStack().getMaxDamage() - (player.getMainHandStack().getDamage() + 1)) / (float)player.getMainHandStack().getMaxDamage() * 100f < DurabilityGuardConfig.minPercentage;
                    case NUMBER -> player.getMainHandStack().getMaxDamage() - (player.getMainHandStack().getDamage() + 1) < DurabilityGuardConfig.minDurability;
                    case BOTH -> (float)(player.getMainHandStack().getMaxDamage() - (player.getMainHandStack().getDamage() + 1)) / (float)player.getMainHandStack().getMaxDamage() * 100f < DurabilityGuardConfig.minPercentage || player.getMainHandStack().getMaxDamage() - (player.getMainHandStack().getDamage() + 1) < DurabilityGuardConfig.minDurability;
                };
            if (shouldStop) {
                SystemToast.show(this.getToastManager(), systemToastId, Text.literal("Durability Guard"), Text.literal("Your tool has low durability!").formatted(Formatting.RED));
                cancel.run();
            }
        }
    }

    @Unique
    private boolean isIgnored() {
        boolean isIgnored = false;

        String id = Registries.ITEM.getId(player.getMainHandStack().getItem()).toString();

        LOGGER.info("Checking if item is ignored: " + id);

        if (DurabilityGuardConfig.ignoredItems.contains(id)) {
            LOGGER.info("Item is ignored: " + id);
            isIgnored = true;
        }

        for (String ignoredItem : DurabilityGuardConfig.ignoredItems) {
            Pattern pattern = Pattern.compile(ignoredItem.replace("*", ".*"), Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(id).matches()) {
                LOGGER.info("Item is ignored: " + id);
                isIgnored = true;
                break;
            }
        }

        return DurabilityGuardConfig.useIgnoredAsWhitelist != isIgnored;
    }
}
