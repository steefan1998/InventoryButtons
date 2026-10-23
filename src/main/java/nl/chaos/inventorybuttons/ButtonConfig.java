package nl.chaos.inventorybuttons;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Client config: where the buttons sit and how WeatherStory weather is spawned. */
public final class ButtonConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.IntValue OFFSET_X;
    public static final ModConfigSpec.IntValue OFFSET_Y;

    public static final ModConfigSpec.BooleanValue WEATHERSTORY;
    public static final ModConfigSpec.IntValue WEATHERSTORY_INTENSITY;
    public static final ModConfigSpec.IntValue WEATHERSTORY_DURATION;
    public static final ModConfigSpec.IntValue WEATHERSTORY_RADIUS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ENABLED = builder
            .comment("Show the buttons in the survival and creative inventory.")
            .translation("inventorybuttons.config.enabled")
            .define("enabled", true);
        OFFSET_X = builder
            .comment("Distance of the button panel from the left edge of the screen, in GUI pixels.")
            .translation("inventorybuttons.config.offset_x")
            .defineInRange("offsetX", 2, 0, 1000);
        OFFSET_Y = builder
            .comment("Distance of the button panel from the top edge of the screen, in GUI pixels.")
            .translation("inventorybuttons.config.offset_y")
            .defineInRange("offsetY", 2, 0, 1000);

        builder.comment("Only used when WeatherStory is installed.")
            .translation("inventorybuttons.config.weatherstory")
            .push("weatherstory");
        WEATHERSTORY = builder
            .comment("Use WeatherStory's local weather for the weather buttons and show its extra weather types.")
            .translation("inventorybuttons.config.weatherstory.enabled")
            .define("enabled", true);
        WEATHERSTORY_INTENSITY = builder
            .comment("Strength of spawned weather, in percent.")
            .translation("inventorybuttons.config.weatherstory.intensity")
            .defineInRange("intensity", 100, 0, 100);
        WEATHERSTORY_DURATION = builder
            .comment("How long spawned weather lasts, in seconds (WeatherStory allows at most 600).")
            .translation("inventorybuttons.config.weatherstory.duration")
            .defineInRange("durationSeconds", 600, 1, 600);
        WEATHERSTORY_RADIUS = builder
            .comment("Radius of spawned weather around the player, in blocks.")
            .translation("inventorybuttons.config.weatherstory.radius")
            .defineInRange("radius", 192, 16, 192);
        builder.pop();

        SPEC = builder.build();
    }

    private ButtonConfig() {
    }
}
