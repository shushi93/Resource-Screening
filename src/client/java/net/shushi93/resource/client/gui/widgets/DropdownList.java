package net.shushi93.resource.client.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
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
    private final Consumer<String> selection_callback;
    private final boolean b;
    private String current_selection;
    private int selectedOption;
    private int scroll_amount = 0;
    private boolean isExpanded = false;

    /**
     * Creates DropdownList with (optional) boolean to check whether class was invoked via DropdownTextureRenderer
     *
     * @param src_texture        The texture the dropdown will be changing
     * @param current_selection  The current texture selected
     * @param selection_callback Callback to store selection
     * @param b                  Whether class was invoked via DropdownTextureRenderer
     */
    public DropdownList(int x, int y, int w, int h, String src_texture, String current_selection, Consumer<String> selection_callback, boolean b) {
        super(x, y, w, h, Component.literal(src_texture));
        this.b = b;
        this.selectedOption = Math.max(0, options.indexOf(current_selection));
        this.current_selection = current_selection;
        this.selection_callback = selection_callback;
        this.height = h / 2;
    }

    public String getCurrentSelection() {
        return this.current_selection;
    }

    /**
     * Renders the dropdown box
     *
     * @param i mouse x
     * @param j mouse y
     */
    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                HEADER_BOX.get(this.isActive(), this.isHoveredOrFocused()),
                this.getX(), this.getY(), this.width, this.height, 0xFFFFFFFF);

        guiGraphics.drawCenteredString(this.font, this.isExpanded ? "▲" : "▼", getXWithOffset(90), getCenteredY(-3), /*color*/ 0xFFFFFFFF); //dropdown arrow
        AbstractWidget.renderScrollingString(
                guiGraphics, this.font, Component.literal(options.get(selectedOption)),
                getXWithOffset(5), getY(), getXWithOffset(85),
                getY() + this.height, 0xFFFFFFFF
        );
    }

    /**
     * Checks whether an option was selected, saves the selection if selected and closes the dropdown
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean isDoubleClick) {
        if (mouseButtonEvent.button() == 0) { // Left mouse button
            if (isExpanded && !super.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y())) {
                selectedOption = (int) ((mouseButtonEvent.y() - (getY() + this.height) + scroll_amount) / this.height);
                this.selection_callback.accept(options.get(selectedOption));
                this.current_selection = options.get(selectedOption);
                onClick(selectedOption);
            }
            this.isExpanded = !isExpanded;
        }
        return super.mouseClicked(mouseButtonEvent, isDoubleClick);
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return super.isMouseOver(d, e) || (isExpanded && areCoordinatesInRectangle(d, e, getX(), getY() + this.height, this.width, this.height * 2));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    /**
     * Adjust y-coordinate if class invoked by DropdownTextureRenderer, due to its texture renderer box
     */
    @Override
    public int getY() {
        return b ? super.getY() + this.height : super.getY();
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        this.scroll_amount += (int) (-g * 3);
        this.scroll_amount = Math.clamp(this.scroll_amount, 0, this.height * (options.size() - 2));
        return super.mouseScrolled(d, e, f, g);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (!this.isActive()) {
            return false;
        } else if (event.isSelection()) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.isExpanded = !this.isExpanded;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get x-coordinate with offset based on percentage of width
     */
    public int getXWithOffset(int percent) {
        return getX() + this.width * percent / 100;
    }

    public int getCenteredY() {
        return getCenteredY(0);
    }

    /**
     * Get y-coordinate centered in the dropdown, with an optional height translation
     */
    public int getCenteredY(int translate) {
        return getY() + this.height / 2 + translate;
    }

    /**
     * Renders dropdown. In a separate method to allow dropdown to appear over other widgets
     */
    public void renderDecorations(GuiGraphics guiGraphics, int i, int j) {
        if (this.isExpanded) {
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
                        getXWithOffset(5), drop_y, getXWithOffset(95),
                        getY() + this.height * (k + 2) - this.scroll_amount, 0xFFFFFFFF
                );
            }
            guiGraphics.disableScissor();
        }
    }

    /**
     * @param d x-coordinate of mouse
     * @param e y-coordinate of mouse
     */
    public boolean areCoordinatesInRectangle(double d, double e, int x, int y, int w, int h) {
        return d >= x && e >= y && d < x + w && e < y + h;
    }

    public boolean superIsMouseOver(double d, double e) {
        return super.isMouseOver(d, e);
    }

    /**
     * Called on option selection. Removes texture pack from currently active selection, and adds/removes texture
     * to/from the pack.
     *
     * @param selectedOption the index of the selected option
     */
    private void onClick(int selectedOption) {
        String src_pack = options.get(selectedOption);
        String src_texture = super.getMessage().getString();
        Zip_Helper.removeIfSelected();
        if (Zip_Helper.does_texture_exist(src_texture)) {
            Zip_Helper.remove_from_pack(src_texture);
        }
        if (Zip_Helper.does_texture_exist(src_pack, src_texture)) Zip_Helper.add_to_pack(src_pack, src_texture);
        TextureScreen.changed = true;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }
}