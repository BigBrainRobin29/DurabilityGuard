package bigbrainrobin29.durabilityguard;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static bigbrainrobin29.durabilityguard.DurabilityGuard.MOD_ID;

public class DurabilityGuardConfig {
    public static ConfigClassHandler<DurabilityGuardConfig> HANDLER = ConfigClassHandler.createBuilder(DurabilityGuardConfig.class)
            .id(Identifier.of(MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json"))
                    .build())
            .build();

    @SerialEntry
    public static boolean active = true;
    @SerialEntry
    public static int minPercentage = 10;
    @SerialEntry
    public static int minDurability = 30;
    @SerialEntry
    public static LimitType limitType = LimitType.PERCENTAGE;
    @SerialEntry
    public static boolean useIgnoredAsWhitelist = false;
    @SerialEntry
    public static List<String> ignoredItems = new ArrayList<>();

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Durability Guard"))
                .save(HANDLER::save)
                .category(
                        ConfigCategory.createBuilder()
                                .name(Text.literal("General"))
                                .option(
                                        Option.<Integer>createBuilder()
                                                .name(Text.literal("Minimum Percentage"))
                                                .description(OptionDescription.of(Text.literal("The mod will stop you from mining if the durability percentage is lower than this.")))
                                                .binding(
                                                        10,
                                                        () -> minPercentage,
                                                        (newVal) -> minPercentage = newVal
                                                )
                                                .controller((opt) -> IntegerSliderControllerBuilder.create(opt)
                                                        .range(0, 100)
                                                        .formatValue((value -> Text.literal(value.toString() + "%")))
                                                        .step(1))
                                                .build()
                                )
                                .option(
                                        Option.<Integer>createBuilder()
                                                .name(Text.literal("Minimum Durability"))
                                                .description(OptionDescription.of(Text.literal("The mod will stop you from mining if the tool's durability is lower than this.")))
                                                .binding(
                                                        30,
                                                        () -> minDurability,
                                                        newVal -> minDurability = newVal
                                                )
                                                .controller(IntegerFieldControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Text.literal("Active"))
                                                .description(OptionDescription.of(Text.literal("Determines if the mod should be active.")))
                                                .binding(
                                                        true,
                                                        () -> active,
                                                        newVal -> active = newVal
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build()

                                )
                                .option(
                                        Option.<LimitType>createBuilder()
                                                .name(Text.literal("Limit type"))
                                                .binding(
                                                        LimitType.PERCENTAGE,
                                                        () -> limitType,
                                                        (newVal) -> limitType = newVal
                                                )
                                                .description(OptionDescription.of(Text.literal("Percentage: The limit scales with the maximum durability of the tool\n\nNumber: The limit is a fixed number\n\nBoth: If the durability reaches one of the other limits, the mod will stop you")))
                                                .controller((opt) -> EnumControllerBuilder.create(opt)
                                                        .enumClass(LimitType.class))
                                                .build()
                                )
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Text.literal("Use ignored items as whitelist"))
                                                .description(OptionDescription.of(Text.literal("Determines if the mod should ignore everything but the items listed in ignored tools.")))
                                                .binding(
                                                        false,
                                                        () -> useIgnoredAsWhitelist,
                                                        newVal -> useIgnoredAsWhitelist = newVal
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        ListOption.<String>createBuilder()
                                                .name(Text.literal("Ignored items"))
                                                .binding(
                                                        new ArrayList<>(),
                                                        () -> ignoredItems,
                                                        (newV) -> ignoredItems = newV
                                                )
                                                .controller(StringControllerBuilder::create)
                                                .description(OptionDescription.of(Text.literal("The mod will ignore these items.")))
                                                .initial("minecraft:")
                                                .build()
                                )
                                .build()
                ).build().generateScreen(parent);
    }

    public enum LimitType implements NameableEnum {
        PERCENTAGE,
        NUMBER,
        BOTH;


        @Override
        public Text getDisplayName() {
            return switch (this) {
                case PERCENTAGE -> Text.literal("Percentage");
                case BOTH -> Text.literal("Both");
                case NUMBER -> Text.literal("Number");
            };
        }
    }
}
