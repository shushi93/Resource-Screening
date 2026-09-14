package net.shushi93.resource.client.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shushi93.resource.ResourceScreening;
import net.shushi93.resource.client.gui.screens.TextureScreen;
import net.shushi93.resource.client.util.Zip_Helper;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Creates the dropdown list widget and handles functionality
 */
public class DropdownList extends AbstractWidget {
    private static final WidgetSprites HEADER_BOX = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/text_field"),
            ResourceLocation.withDefaultNamespace("widget/text_field_highlighted")
    );
    private static final WidgetSprites DROPDOWN_BOX = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "dropdown/dropdown"),
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "dropdown/dropdown_highlighted")
    );
    private final Font font = Minecraft.getInstance().font;
    private final ArrayList<String> options = new ArrayList<>(Objects.requireNonNull(Zip_Helper.get_all_pack_names()));
    private final String src_texture;
    private final Consumer<String> selection_callback;
    private int selectedOption;
    private int scroll_amount = 0;
    private boolean isExpanded = false;

    public DropdownList(int x, int y, int w, int h, String src_texture, String current_selection, Consumer<String> selection_callback) {
        super(x, y, w, h, Component.literal("Test"));
        this.src_texture = src_texture;
        this.selectedOption = Math.max(0, options.indexOf(current_selection));
        this.selection_callback = selection_callback;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                HEADER_BOX.get(this.isActive(), this.isHovered()),
                this.getX(), this.getY(), this.width, this.height, 0xFFFFFFFF);

        guiGraphics.drawCenteredString(this.font, this.isExpanded ? "▲" : "▼", getXwithOffset(90), getCenteredY(-3), /*color*/ 0xFFFFFFFF); //dropdown arrow
        AbstractWidget.renderScrollingString(
                guiGraphics, this.font, Component.literal(options.get(selectedOption)),
                getXwithOffset(5), getY(), getXwithOffset(85),
                getY() + this.height, 0xFFFFFFFF
        );

        if (isExpanded) {
            guiGraphics.enableScissor(getX() - 1, getY() + this.height + 1, getX() + this.width + 1, getY() + this.height * 3 + 1);
            guiGraphics.fill(getX() - 1, getY() + this.height, getX() + this.width + 1, getY() + this.height * 3 + 1, 0x2BFFFFFF); //translucent outline box

            for (int k = 0; k < options.size(); k++) {
                int drop_y = getY() + this.height * (k + 1) - this.scroll_amount;
                if (areCoordinatesInRectangle(i, j, getX(), drop_y, this.width, this.height)) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                            DROPDOWN_BOX.get(this.isActive(), true),
                            getX(), drop_y, this.width, this.height, 0xFFFFFFFF);
                } else {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                            DROPDOWN_BOX.get(this.isActive(), false),
                            getX(), drop_y, this.width, this.height, 0xFFFFFFFF);
                }

                AbstractWidget.renderScrollingString(
                        guiGraphics, this.font, Component.literal(options.get(k)),
                        getXwithOffset(5), drop_y, getXwithOffset(95),
                        getY() + this.height * (k + 2) - this.scroll_amount, 0xFFFFFFFF
                );
            }
            guiGraphics.disableScissor();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public int getXwithOffset(int percent) {
        return getX() + this.width * percent / 100;
    }

    public int getCenteredY(int translate) {
        return getY() + this.height / 2 + translate;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() == 0) { // Left mouse button
            if (isExpanded && !super.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y())) {
                selectedOption = (int) ((mouseButtonEvent.y() - (getY() + this.height) + scroll_amount) / this.height);
                this.selection_callback.accept(options.get(selectedOption));
                onClick(selectedOption);
            }
            this.isExpanded = !isExpanded;
            return true;
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    public boolean areCoordinatesInRectangle(double d, double e, int x, int y, int w, int h) {
        return d >= x && e >= y && d < x + w && e < y + h;
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return super.isMouseOver(d, e) || (isExpanded && areCoordinatesInRectangle(d, e, getX(), getY() + this.height, this.width, this.height * 2));
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        this.scroll_amount += (int) (-g * 3);
        this.scroll_amount = Math.clamp(this.scroll_amount, 0, this.height * (options.size() - 2));
        return super.mouseScrolled(d, e, f, g);
    }

    private void onClick(int selectedOption) {
        String src_pack = options.get(selectedOption);
        Zip_Helper.removeIfSelected();
        if (Zip_Helper.does_texture_exist(src_texture)) {
            Zip_Helper.remove_from_pack(src_texture);

        }
        if (Zip_Helper.does_texture_exist(src_pack, src_texture)) Zip_Helper.add_to_pack(src_pack, src_texture);
        TextureScreen.changed = true;
    }
}