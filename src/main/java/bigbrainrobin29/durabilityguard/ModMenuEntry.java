package bigbrainrobin29.durabilityguard;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfigBuilder;
import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static bigbrainrobin29.durabilityguard.DurabilityGuard.MOD_ID;

public class ModMenuEntry implements ModMenuApi {
    public static SimpleModConfigBuilder builder;
    public static ConfigHandler<DurabilityGuardConfig> configHandler;
    @Override
    public ConfigScreenFactory<ConfigScreen> getModConfigScreenFactory()
    {
        Text displayName = Text.of("Durability Guard");
        configHandler = new ConfigHandler<>(MOD_ID, new DurabilityGuardConfig());
        builder = new SimpleModConfigBuilder(configHandler, displayName.getString());
        return new ConfigScreenFactory<ConfigScreen>()
        {
            @Override
            public ConfigScreen create(Screen screen)
            {
                return new ConfigScreen(builder.configHandler, screen);
            }
        };
    }
}
