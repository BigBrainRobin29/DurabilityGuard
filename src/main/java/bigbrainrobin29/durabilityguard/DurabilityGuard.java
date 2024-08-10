package bigbrainrobin29.durabilityguard;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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