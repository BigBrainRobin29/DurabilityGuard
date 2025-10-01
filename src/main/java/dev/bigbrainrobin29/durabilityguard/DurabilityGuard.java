package dev.bigbrainrobin29.durabilityguard;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurabilityGuard implements ClientModInitializer {
	public static final String MOD_ID = "durability-guard";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Durability Guard...");

		KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(
                //? if < 1.21.9
				/*new StickyKeyBinding("key.durability-guard.toggle", InputUtil.UNKNOWN_KEY.getCode(), "Durability Guard", () -> DurabilityGuardConfig.active)*/
                //? if >= 1.21.9
                new StickyKeyBinding("key.durability-guard.toggle", InputUtil.UNKNOWN_KEY.getCode(), new KeyBinding.Category(Identifier.of(MOD_ID, MOD_ID)), () -> DurabilityGuardConfig.active, false)
		);

		DurabilityGuardConfig.HANDLER.load();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("durabilityguard").executes((context -> {
				MinecraftClient client = MinecraftClient.getInstance();
				client.send(() -> client.setScreen(DurabilityGuardConfig.getScreen(null)));
                return 0;
            })));
		});


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				DurabilityGuardConfig.active = !DurabilityGuardConfig.active;
				client.player.sendMessage(Text.of("Durability Guard is now " + (DurabilityGuardConfig.active ? "enabled" : "disabled")), true);
			}
		});
	}
}