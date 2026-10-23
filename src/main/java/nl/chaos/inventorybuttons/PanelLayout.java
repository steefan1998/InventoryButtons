package nl.chaos.inventorybuttons;

import java.util.List;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.Rect2i;

/**
 * Places the button groups in the top-left corner: one group per row when that fits left of the inventory,
 * otherwise one group per column so the panel never covers the inventory or the open recipe book.
 * Runs every frame because opening the recipe book moves the inventory without re-initialising the screen.
 */
final class PanelLayout {
    private static final int SPACING = QuickButton.SIZE + 2;
    private static final int PADDING = 3;
    private static final int MARGIN = 4;

    private final Screen screen;
    private final ButtonPanel panel;
    private final List<List<QuickButton>> groups;

    PanelLayout(Screen screen, ButtonPanel panel, List<List<QuickButton>> groups) {
        this.screen = screen;
        this.panel = panel;
        this.groups = groups;
    }

    boolean belongsTo(Screen screen) {
        return this.screen == screen;
    }

    void apply() {
        int longest = this.groups.stream().mapToInt(List::size).max().orElse(0);
        int x = ButtonConfig.OFFSET_X.get();
        int y = ButtonConfig.OFFSET_Y.get();
        boolean rows = x + span(longest) + MARGIN <= this.leftObstacle();

        this.panel.setPosition(x, y);
        this.panel.setSize(rows ? span(longest) : span(this.groups.size()), rows ? span(this.groups.size()) : span(longest));
        for (int group = 0; group < this.groups.size(); group++) {
            List<QuickButton> buttons = this.groups.get(group);
            for (int i = 0; i < buttons.size(); i++) {
                int column = rows ? i : group;
                int row = rows ? group : i;
                buttons.get(i).setPosition(x + PADDING + column * SPACING, y + PADDING + row * SPACING);
            }
        }
    }

    Rect2i area() {
        return new Rect2i(this.panel.getX(), this.panel.getY(), this.panel.getWidth(), this.panel.getHeight());
    }

    /** Width or height of the panel holding {@code count} buttons side by side. */
    private static int span(int count) {
        return count * SPACING - 2 + 2 * PADDING;
    }

    /** Left edge of whatever the screen draws in the middle: the inventory, or the recipe book next to it. */
    private int leftObstacle() {
        if (!(this.screen instanceof AbstractContainerScreen<?> container)) {
            return this.screen.width;
        }
        int left = container.getLeftPos();
        for (GuiEventListener child : this.screen.children()) {
            if (child instanceof RecipeBookComponent<?> recipeBook && recipeBook.isVisible()) {
                // the open book sits directly left of the inventory (see RecipeBookComponent#updateScreenPosition)
                left = Math.min(left, (this.screen.width - RecipeBookComponent.IMAGE_WIDTH) / 2 - 86);
            }
        }
        return left;
    }
}
