package bigbrainrobin29.durabilityguard;


import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;
import org.apache.logging.log4j.core.config.builder.api.Component;


public class DurabilityGuardConfig {
    @SimpleConfig(displayName = "Minimum Durability", description = "Lowest durability the tool can have before mining is stopped.")
    public int minDurability = 5;
    @SimpleConfig(index = 1, displayName = "Active", description = "If Durability Guard is active or not.")
    public boolean active = true;

}
