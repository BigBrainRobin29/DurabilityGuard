package bigbrainrobin29.durabilityguard;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfigBuilder;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class DurabilityGuard implements ModInitializer {
	public static final String MOD_ID = "durability-guard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static SimpleModConfigBuilder builder;
	public static ConfigHandler<DurabilityGuardConfig> configHandler;

	public static void initConfig()
	{
		Text displayName = Text.of("Durability Guard");
		configHandler = new ConfigHandler<>(MOD_ID, new DurabilityGuardConfig());
		builder = new SimpleModConfigBuilder(configHandler, displayName.getString());
	}



	@Override
	public void onInitialize() {

		LOGGER.info("Initializing Durability Guard Config...");
		initConfig();

		LOGGER.info("Initializing Durability Guard...");
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			configHandler.load();
			if (configHandler.getConfig().active) {
			ClientPlayerEntity player = client.player;
            if (player != null) {
            ItemStack heldItem = player.getMainHandStack();
			if (heldItem != null && heldItem.isDamageable() && heldItem.getDamage() >= (heldItem.getMaxDamage() - (configHandler.getConfig().minDurability))) {
				client.options.attackKey.setPressed(false);
			}}
		}});
		LOGGER.info("Initializing Durability Guard Config...");
		initConfig();
		/*AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			ItemStack heldItem = player.getMainHandStack();
			if (heldItem != null && heldItem.isDamageable() && heldItem.getDamage() >= (heldItem.getMaxDamage() - 6)) {
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;

		});*/
	}
}