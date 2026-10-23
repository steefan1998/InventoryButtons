package nl.chaos.inventorybuttons;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jspecify.annotations.Nullable;

/**
 * Adds gamemode, time and weather buttons to the survival and creative inventory.
 * Everything goes through the same packets and commands a player could use, so the server decides
 * whether the player is allowed; the buttons are greyed out for players without cheats/operator rights.
 */
@Mod(value = InventoryButtons.MOD_ID, dist = Dist.CLIENT)
public final class InventoryButtons {
    public static final String MOD_ID = "inventorybuttons";

    private static @Nullable PanelLayout layout;

    public InventoryButtons(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ButtonConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.addListener(InventoryButtons::addButtons);
        NeoForge.EVENT_BUS.addListener(InventoryButtons::positionButtons);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void addButtons(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen) && !(event.getScreen() instanceof CreativeModeInventoryScreen)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (!ButtonConfig.ENABLED.get() || player == null || minecraft.gameMode == null) {
            return;
        }
        boolean allowed = GameModeCommand.PERMISSION_CHECK.check(player.permissions());
        boolean weatherStory = ButtonConfig.WEATHERSTORY.get() && ModList.get().isLoaded("weatherstory");

        List<QuickButton> gameModes = List.of(
            gameMode(GameType.SURVIVAL, "survival"),
            gameMode(GameType.CREATIVE, "creative"),
            gameMode(GameType.ADVENTURE, "adventure"),
            gameMode(GameType.SPECTATOR, "spectator")
        );
        List<QuickButton> world = weatherStory
            ? List.of(
                command("day", "time set day"),
                command("night", "time set night"),
                weatherStory("clear", "clear"),
                weatherStory("rain", "rain"),
                new QuickButton("thunder", tooltip("thunder"), () -> false, () -> {
                    send("weather thunder");
                    send(spawnCommand("rain"));
                })
            )
            : List.of(
                command("day", "time set day"),
                command("night", "time set night"),
                vanillaWeather("clear", level -> !level.isRaining()),
                vanillaWeather("rain", level -> level.isRaining() && !level.isThundering()),
                vanillaWeather("thunder", level -> level.isThundering())
            );
        List<QuickButton> extraWeather = weatherStory
            ? List.of(
                weatherStory("snow", "snow"),
                weatherStory("snowstorm", "snowstorm"),
                weatherStory("hail", "hail"),
                weatherStory("fog", "fog"),
                weatherStory("sandstorm", "sandstorm"),
                // removes WeatherStory's override and hands the weather back to its climate
                command("natural", "weatherstory weather clear")
            )
            : List.of();

        List<List<QuickButton>> groups = extraWeather.isEmpty() ? List.of(gameModes, world) : List.of(gameModes, world, extraWeather);
        ButtonPanel panel = new ButtonPanel();
        event.addListener(panel);
        for (List<QuickButton> group : groups) {
            for (QuickButton button : group) {
                button.active = allowed;
                event.addListener(button);
            }
        }
        layout = new PanelLayout(event.getScreen(), panel, groups);
        layout.apply();
    }

    /** The screen area of the button panel on the open screen, for mods like JEI that lay out around it. */
    public static List<Rect2i> panelAreas() {
        return layout != null && layout.belongsTo(Minecraft.getInstance().screen) ? List.of(layout.area()) : List.of();
    }

    private static void positionButtons(ScreenEvent.Render.Pre event) {
        if (layout != null && layout.belongsTo(event.getScreen())) {
            layout.apply();
        }
    }

    private static QuickButton gameMode(GameType mode, String icon) {
        Minecraft minecraft = Minecraft.getInstance();
        return new QuickButton(icon, mode.getLongDisplayName(), () -> minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() == mode, () -> {
            // the same packet the F3+F4 gamemode switcher sends
            if (minecraft.player != null && minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != mode) {
                minecraft.player.connection.send(new ServerboundChangeGameModePacket(mode));
            }
        });
    }

    private static QuickButton command(String icon, String command) {
        return new QuickButton(icon, tooltip(icon), () -> false, () -> send(command));
    }

    private static QuickButton vanillaWeather(String kind, java.util.function.Predicate<net.minecraft.client.multiplayer.ClientLevel> current) {
        Minecraft minecraft = Minecraft.getInstance();
        return new QuickButton(kind, tooltip(kind), () -> minecraft.level != null && current.test(minecraft.level), () -> send("weather " + kind));
    }

    private static QuickButton weatherStory(String icon, String kind) {
        return new QuickButton(icon, tooltip(icon), () -> false, () -> send(spawnCommand(kind)));
    }

    /** WeatherStory's local weather override: {@code /weatherstory weather spawn <kind> <intensity> <durationSeconds> <radius>}. */
    private static String spawnCommand(String kind) {
        return String.format(
            Locale.ROOT, "weatherstory weather spawn %s %.2f %d %d",
            kind,
            ButtonConfig.WEATHERSTORY_INTENSITY.get() / 100.0,
            ButtonConfig.WEATHERSTORY_DURATION.get(),
            ButtonConfig.WEATHERSTORY_RADIUS.get()
        );
    }

    private static Component tooltip(String icon) {
        return Component.translatable("inventorybuttons.button." + icon);
    }

    private static void send(String command) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.connection.sendCommand(command);
        }
    }
}
