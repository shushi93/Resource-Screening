package net.shushi93.resource.client.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public class DropdownTextureRenderer extends DropdownList {

    public DropdownTextureRenderer(int x, int y, int w, int h, String src_texture, String current_selection, Consumer<String> selection_callback) {
        super(x, y, w, h, src_texture, current_selection, selection_callback);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
    }
}
