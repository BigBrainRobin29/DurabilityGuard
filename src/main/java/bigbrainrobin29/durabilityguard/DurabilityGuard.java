package bigbrainrobin29.durabilityguard;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurabilityGuard implements ClientModInitializer {
	public static final String MOD_ID = "durability-guard";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Durability Guard...");

		DurabilityGuardConfig.HANDLER.load();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("durabilityguardconfig").executes((context -> {
				context.getSource().getClient().tell(() -> context.getSource().getClient().setScreen(DurabilityGuardConfig.getScreen(null)));
                return 0;
            })));
		});

	}
}