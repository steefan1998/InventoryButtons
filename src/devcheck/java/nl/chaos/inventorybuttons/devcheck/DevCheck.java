package nl.chaos.inventorybuttons.devcheck;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import nl.chaos.inventorybuttons.QuickButton;
import org.slf4j.Logger;

/** Loads into the test world, opens the inventory, presses buttons and takes screenshots, then quits. */
@Mod(value = "inventorybuttons_devcheck", dist = Dist.CLIENT)
public final class DevCheck {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static int ticks;

    public DevCheck() {
        NeoForge.EVENT_BUS.addListener(DevCheck::tick);
    }

    private static void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        ticks++;
        switch (ticks) {
            case 20 -> setup(mc);
            case 100 -> open(mc);
            case 139 -> LOGGER.info("DevCheck JEI exclusion areas {}", nl.chaos.inventorybuttons.InventoryButtons.panelAreas().stream().map(r -> r.getX() + "," + r.getY() + " " + r.getWidth() + "x" + r.getHeight()).toList());
            case 120 -> bookmarkJeiItems(mc);
            case 140 -> shot(mc, "ib_1_open");
            case 150 -> {
                press(mc, "Set time to night");
                press(mc, "Snow (WeatherStory)");
            }
            case 220 -> shot(mc, "ib_2_night_snow");
            case 230 -> press(mc, "Adventure Mode");
            case 270 -> open(mc);
            case 310 -> shot(mc, "ib_3_adventure");
            case 320 -> {
                press(mc, "Set time to day");
                press(mc, "Thunderstorm");
            }
            case 400 -> shot(mc, "ib_4_day_thunder");
            case 410 -> press(mc, "Creative Mode");
            case 450 -> open(mc);
            case 490 -> shot(mc, "ib_5_creative");
            case 520 -> mc.stop();
            default -> {
            }
        }
    }

    // the test world has cheats off: allow cheats and start in survival, as the integrated server
    private static void setup(Minecraft mc) {
        var server = mc.getSingleplayerServer();
        server.execute(() -> {
            server.getPlayerList().setAllowCommandsForAllPlayers(true);
            server.getPlayerList().getPlayers().forEach(p -> server.getPlayerList().sendPlayerPermissionLevel(p));
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "gamemode survival @a");
        });
    }

    // hovers items in JEI's ingredient list (right side) and presses A, like a player bookmarking them
    private static void bookmarkJeiItems(Minecraft mc) {
        try {
            var onMove = net.minecraft.client.MouseHandler.class.getDeclaredMethod("onMove", long.class, double.class, double.class);
            var keyPress = net.minecraft.client.KeyboardHandler.class.getDeclaredMethod("keyPress", long.class, int.class, net.minecraft.client.input.KeyEvent.class);
            onMove.setAccessible(true);
            keyPress.setAccessible(true);
            long handle = mc.getWindow().handle();
            int scale = mc.getWindow().getGuiScale();
            int guiWidth = mc.getWindow().getGuiScaledWidth();
            int scancode = org.lwjgl.glfw.GLFW.glfwGetKeyScancode(org.lwjgl.glfw.GLFW.GLFW_KEY_A);
            for (int i = 0; i < 12; i++) {
                // JEI's list starts near the top right, one 18px slot per ingredient
                double x = (guiWidth - 100 + (i % 6) * 18 + 9) * scale;
                double y = (40 + (i / 6) * 18 + 9) * scale;
                onMove.invoke(mc.mouseHandler, handle, x, y);
                keyPress.invoke(mc.keyboardHandler, handle, 1, new net.minecraft.client.input.KeyEvent(org.lwjgl.glfw.GLFW.GLFW_KEY_A, scancode, 0));
                keyPress.invoke(mc.keyboardHandler, handle, 0, new net.minecraft.client.input.KeyEvent(org.lwjgl.glfw.GLFW.GLFW_KEY_A, scancode, 0));
            }
            onMove.invoke(mc.mouseHandler, handle, 0.0, (double) mc.getWindow().getScreenHeight() - 1);
            LOGGER.info("DevCheck bookmarked JEI items");
        } catch (ReflectiveOperationException e) {
            LOGGER.error("DevCheck could not bookmark JEI items", e);
        }
    }

    private static void open(Minecraft mc) {
        mc.setScreen(new InventoryScreen(mc.player));
        LOGGER.info("DevCheck open -> {} gamemode={}", mc.screen == null ? null : mc.screen.getClass().getSimpleName(), mc.gameMode.getPlayerMode());
    }

    private static void press(Minecraft mc, String label) {
        if (mc.screen == null) {
            LOGGER.info("DevCheck no screen open for '{}'", label);
            return;
        }
        boolean found = false;
        for (GuiEventListener child : mc.screen.children()) {
            if (child instanceof QuickButton button) {
                if (button.getMessage().getString().equals(label)) {
                    found = true;
                    LOGGER.info("DevCheck press '{}' at {},{} active={}", label, button.getX(), button.getY(), button.active);
                    button.onPress(null);
                }
            }
        }
        if (!found) {
            mc.screen.children().stream().filter(c -> c instanceof QuickButton)
                .forEach(c -> LOGGER.info("DevCheck available '{}'", ((QuickButton) c).getMessage().getString()));
            LOGGER.info("DevCheck button '{}' not found", label);
        }
    }

    private static void shot(Minecraft mc, String name) {
        Screenshot.grab(mc.gameDirectory, name + ".png", mc.getMainRenderTarget(), 1, m -> LOGGER.info("DevCheck shot {}", m.getString()));
    }
}
