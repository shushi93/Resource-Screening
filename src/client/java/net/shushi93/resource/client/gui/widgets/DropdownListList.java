package net.shushi93.resource.client.gui.widgets;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DropdownListList extends ContainerObjectSelectionList<DropdownListList.Entry> {
    private List<DropdownList> oldChildren;

    public DropdownListList(Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    public void addEntry(List<DropdownList> renderer) {
        this.addEntry(new Entry(renderer));
    }

    public void updateEntries(String filter) {
        if (filter == null || filter.isEmpty()) {
            return;
        }

        if (this.oldChildren == null) {
            this.oldChildren = this.children().stream()
                    .map(e -> e.renderer)
                    .flatMap(List::stream)
                    .toList();
        }

        this.clearEntries();

        List<DropdownList> dropdowns = new ArrayList<>();
        for (DropdownList d :
                oldChildren.stream()
                        .filter(d -> d.getMessage().getString().contains(filter))
                        .toList()
        ) {
            dropdowns.add(d);
            if (dropdowns.size() == 2) {
                this.addEntry(dropdowns);
                dropdowns.clear();
            }
        }
        this.addEntry(dropdowns);
    }

    /**
     * {@link net.shushi93.resource.client.gui.widgets.DropdownList#renderDecorations(GuiGraphics, int, int)} <br>
     */
    public void renderDecorations(GuiGraphics guiGraphics, int i, int j) {
        for (Entry e : this.children()) {
            for (DropdownList d : e.renderer) {
                d.renderDecorations(guiGraphics, i, j);
            }
        }
    }

    /**
     * Sets position of scrollbar
     */
    @Override
    public int getRowWidth() {
        return 310;
    }

    /**
     * Allows scrolling to pass through to the dropdowns when expanded
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (Entry e : this.children()) {
            for (DropdownList d : e.renderer) {
                if (d.isMouseOver(mouseX, mouseY) && !d.superIsMouseOver(mouseX, mouseY))
                    return d.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    /**
     * @param event Mouse Button event
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        boolean bl = false;
        for (Entry e : this.children()) {
            for (DropdownList d : e.renderer) {
                if (!d.isMouseOver(event.x(), event.y())) {
                    d.setExpanded(false);
                } else {
                    bl = d.mouseClicked(event, isDoubleClick);
                }
            }
        }
        return bl;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        boolean bl = false;
        for (Entry e : this.children()) {
            for (DropdownList d : e.renderer) {
                if (!d.isFocused()) {
                    d.setExpanded(false);
                } else {
                    bl = d.keyPressed(event);
                }
            }
        }
        return bl;
    }

    /**
     * Each entry stores an immutable list of 2 dropdowns
     */
    protected static class Entry extends ContainerObjectSelectionList.Entry<DropdownListList.Entry> {

        private final List<DropdownList> renderer;

        Entry(List<DropdownList> renderer) {
            this.renderer = ImmutableList.copyOf(renderer);
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            int i = 0;
            int j = (Minecraft.getInstance().screen != null ? Minecraft.getInstance().screen.width : 0) / 2 - 165;

            for (DropdownList d : this.renderer) {
                d.setPosition(j + i, this.getContentY());
                d.render(guiGraphics, mouseX, mouseY, partialTick);
                i += 160;
            }
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return renderer;
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return renderer;
        }
    }
}
