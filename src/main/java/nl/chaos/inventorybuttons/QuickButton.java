package nl.chaos.inventorybuttons;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** A round 20x20 icon button; the "selected" state draws a gold ring, e.g. for the current gamemode. */
public final class QuickButton extends Button {
    public static final int SIZE = 20;

    private static final Identifier NORMAL = InventoryButtons.id("button");
    private static final Identifier HOVERED = InventoryButtons.id("button_hovered");
    private static final Identifier SELECTED = InventoryButtons.id("button_selected");
    private static final Identifier DISABLED = InventoryButtons.id("button_disabled");

    private final Identifier icon;
    private final BooleanSupplier selected;

    public QuickButton(String icon, Component message, BooleanSupplier selected, Runnable action) {
        super(0, 0, SIZE, SIZE, message, button -> action.run(), DEFAULT_NARRATION);
        this.icon = InventoryButtons.id("icon/" + icon);
        this.selected = selected;
        this.setTooltip(Tooltip.create(message));
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Identifier background;
        if (!this.isActive()) {
            background = DISABLED;
        } else if (this.isHoveredOrFocused()) {
            background = HOVERED;
        } else if (this.selected.getAsBoolean()) {
            background = SELECTED;
        } else {
            background = NORMAL;
        }
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, this.getX(), this.getY(), SIZE, SIZE);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.icon, this.getX() + 2, this.getY() + 2, 16, 16, this.isActive() ? 1.0F : 0.5F);
    }
}
