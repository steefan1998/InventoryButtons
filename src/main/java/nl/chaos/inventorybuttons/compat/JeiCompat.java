package nl.chaos.inventorybuttons.compat;

import java.util.Collection;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGlobalGuiHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.Identifier;
import nl.chaos.inventorybuttons.InventoryButtons;

/**
 * Tells JEI where the button panel is, so its bookmark and ingredient lists move out of the way instead of
 * drawing over the buttons. Only loaded by JEI itself, so the mod still runs without JEI installed.
 */
@JeiPlugin
public final class JeiCompat implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return InventoryButtons.id("jei");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGlobalGuiHandler(new IGlobalGuiHandler() {
            @Override
            public Collection<Rect2i> getGuiExtraAreas() {
                return InventoryButtons.panelAreas();
            }
        });
    }
}
