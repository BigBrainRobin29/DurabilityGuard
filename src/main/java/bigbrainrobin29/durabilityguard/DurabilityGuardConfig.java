package bigbrainrobin29.durabilityguard;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static bigbrainrobin29.durabilityguard.DurabilityGuard.MOD_ID;

public class DurabilityGuardConfig {
    public static ConfigClassHandler<DurabilityGuardConfig> HANDLER = ConfigClassHandler.createBuilder(DurabilityGuardConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(MOD_ID, "config"))
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
    public static List<String> ignoredItems = new ArrayList<>();

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Durability Guard"))
                .save(HANDLER::save)
                .category(
                        ConfigCategory.createBuilder()
                                .name(Component.literal("General"))
                                .option(
                                        Option.<Integer>createBuilder()
                                                .name(Component.literal("Minimum Percentage"))
                                                .description(OptionDescription.of(Component.literal("The mod will stop you from mining if the durability percentage is lower than this.")))
                                                .binding(
                                                        10,
                                                        () -> minPercentage,
                                                        (newVal) -> minPercentage = newVal
                                                )
                                                .controller((opt) -> IntegerSliderControllerBuilder.create(opt)
                                                        .range(0, 100)
                                                        .formatValue((value -> Component.literal(value.toString() + "%")))
                                                        .step(1))
                                                .build()
                                )
                                .option(
                                        Option.<Integer>createBuilder()
                                                .name(Component.literal("Minimum Durability"))
                                                .description(OptionDescription.of(Component.literal("The mod will stop you from mining if the tool's durability is lower than this.")))
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
                                                .name(Component.literal("Active"))
                                                .description(OptionDescription.of(Component.literal("Determines if the mod should be active.")))
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
                                                .name(Component.literal("Limit type"))
                                                .binding(
                                                        LimitType.PERCENTAGE,
                                                        () -> limitType,
                                                        (newVal) -> limitType = newVal
                                                )
                                                .description(OptionDescription.of(Component.literal("Percentage: The limit scales with the maximum durability of the tool\n\nNumber: The limit is a fixed number\n\nBoth: If the durability reaches one of the other limits, the mod will stop you")))
                                                .controller((opt) -> EnumControllerBuilder.create(opt)
                                                        .enumClass(LimitType.class))
                                                .build()
                                )
                                .option(
                                        ListOption.<String>createBuilder()
                                                .name(Component.literal("Ignored items"))
                                                .binding(
                                                        new ArrayList<>(),
                                                        () -> ignoredItems,
                                                        (newV) -> ignoredItems = newV
                                                )
                                                .controller(StringControllerBuilder::create)
                                                .description(OptionDescription.of(Component.literal("The mod will ignore these items.")))
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
        public Component getDisplayName() {
            return switch (this) {
                case PERCENTAGE -> Component.literal("Percentage");
                case BOTH -> Component.literal("Both");
                case NUMBER -> Component.literal("Number");
            };
        }
    }
}
