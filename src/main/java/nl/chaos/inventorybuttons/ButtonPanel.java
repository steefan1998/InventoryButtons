package nl.chaos.inventorybuttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;

/**
 * The dark rounded panel behind the buttons; {@link PanelLayout} sets its position and size.
 * It is added before the buttons so it draws underneath them, and it is inactive so clicks fall through to them.
 */
public final class ButtonPanel extends AbstractWidget {
    private static final int FILL = 0xE0101420;
    private static final int BORDER = 0x50FFFFFF;

    public ButtonPanel() {
        super(0, 0, 0, 0, CommonComponents.EMPTY);
        this.active = false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x0 = this.getX();
        int y0 = this.getY();
        int x1 = x0 + this.width;
        int y1 = y0 + this.height;
        // fill with the corner pixels cut off, then a 1px border that follows the same rounded shape
        graphics.fill(x0 + 1, y0 + 1, x1 - 1, y1 - 1, FILL);
        graphics.fill(x0 + 2, y0, x1 - 2, y0 + 1, BORDER);
        graphics.fill(x0 + 2, y1 - 1, x1 - 2, y1, BORDER);
        graphics.fill(x0, y0 + 2, x0 + 1, y1 - 2, BORDER);
        graphics.fill(x1 - 1, y0 + 2, x1, y1 - 2, BORDER);
        graphics.fill(x0 + 1, y0 + 1, x0 + 2, y0 + 2, BORDER);
        graphics.fill(x1 - 2, y0 + 1, x1 - 1, y0 + 2, BORDER);
        graphics.fill(x0 + 1, y1 - 2, x0 + 2, y1 - 1, BORDER);
        graphics.fill(x1 - 2, y1 - 2, x1 - 1, y1 - 1, BORDER);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
