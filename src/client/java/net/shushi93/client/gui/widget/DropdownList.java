package net.shushi93.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.shushi93.ResourceScreening;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates the dropdown list widget and handles functionality
 */
public class DropdownList extends AbstractWidget {
    private boolean isExpanded = false;
    protected final Font font = Minecraft.getInstance().font;
    private int scroll;
    private boolean isEntryHovered;
    public final List<String> packs = new ArrayList<>();
    public List<Integer> mouseCords;
//    private List<String> PacksToRemove = new ArrayList<>(Arrays.asList(
//            "fabric", "fabric-api", "fabric-api-base", "fabric-api-lookup-api-v1", "fabric-biome-api-v1", "fabric-block-api-v1", "fabric-block-view-api-v2", "fabric-client-gametest-api-v1", "fabric-command-api-v2", "fabric-content-registries-v0", "fabric-convention-tags-v1", "fabric-convention-tags-v2", "fabric-crash-report-info-v1", "fabric-data-attachment-api-v1", "fabric-data-generation-api-v1", "fabric-dimensions-v1", "fabric-entity-events-v1", "fabric-events-interaction-v0", "fabric-game-rule-api-v1", "fabric-gametest-api-v1", "fabric-item-api-v1", "fabric-item-group-api-v1", "fabric-item-group-api-v1_programmer_art", "fabric-key-binding-api-v1", "fabric-lifecycle-events-v1", "fabric-loot-api-v2", "fabric-loot-api-v3", "fabric-message-api-v1", "fabric-model-loading-api-v1", "fabric-networking-api-v1", "fabric-object-builder-api-v1", "fabric-particles-v1", "fabric-recipe-api-v1", "fabric-registry-sync-v0", "fabric-renderer-api-v1", "fabric-renderer-indigo", "fabric-rendering-fluids-v1", "fabric-rendering-v1", "fabric-resource-conditions-api-v1", "fabric-resource-loader-v0", "fabric-resource-loader-v1", "fabric-screen-api-v1", "fabric-screen-handler-api-v1", "fabric-serialization-api-v1", "fabric-sound-api-v1", "fabric-tag-api-v1", "fabric-transfer-api-v1", "fabric-transitive-access-wideners-v1", "fabricloader", "resource-screening"
//    ));

    /**
     * Overloaded Constructor
     */
    public DropdownList(int x, int y, int w, int h) {
//        List<String> packs = Minecraft.getInstance()
//                .getResourcePackRepository()
//                .getAvailablePacks()
//                .stream()
//                .map(Pack::getId)
//                .toList();
//        ResourceScreening.LOGGER.info(String.valueOf(packs));
//        packs.removeIf(
//                new ArrayList<>(Arrays.asList(
//                "fabric", "fabric-api", "fabric-api-base", "fabric-api-lookup-api-v1", "fabric-biome-api-v1", "fabric-block-api-v1", "fabric-block-view-api-v2", "fabric-client-gametest-api-v1", "fabric-command-api-v2", "fabric-content-registries-v0", "fabric-convention-tags-v1", "fabric-convention-tags-v2", "fabric-crash-report-info-v1", "fabric-data-attachment-api-v1", "fabric-data-generation-api-v1", "fabric-dimensions-v1", "fabric-entity-events-v1", "fabric-events-interaction-v0", "fabric-game-rule-api-v1", "fabric-gametest-api-v1", "fabric-item-api-v1", "fabric-item-group-api-v1", "fabric-item-group-api-v1_programmer_art", "fabric-key-binding-api-v1", "fabric-lifecycle-events-v1", "fabric-loot-api-v2", "fabric-loot-api-v3", "fabric-message-api-v1", "fabric-model-loading-api-v1", "fabric-networking-api-v1", "fabric-object-builder-api-v1", "fabric-particles-v1", "fabric-recipe-api-v1", "fabric-registry-sync-v0", "fabric-renderer-api-v1", "fabric-renderer-indigo", "fabric-rendering-fluids-v1", "fabric-rendering-v1", "fabric-resource-conditions-api-v1", "fabric-resource-loader-v0", "fabric-resource-loader-v1", "fabric-screen-api-v1", "fabric-screen-handler-api-v1", "fabric-serialization-api-v1", "fabric-sound-api-v1", "fabric-tag-api-v1", "fabric-transfer-api-v1", "fabric-transitive-access-wideners-v1", "fabricloader", "resource-screening"
//        ))::contains);
        this(x, y, w, h, List.of("temp"));
    }

    /**
     * Creates the dropdown list
     * @param x x-coordinate of the top-left point of the dropdown list
     * @param y y-coordinate of the top-left point of the dropdown list
     * @param w width of the dropdown box
     * @param h height of the dropdown box
     * @param items The names of all the entries
     */
    public DropdownList(int x, int y, int w, int h, List<String> items) {
        super(x, y, w, h, Component.literal(items.getFirst()));
    }

    /**
     * @param d mouseX
     * @param e mouseY
     * @return Whether the mouse is on top of the widget
     */
    @Override
    public boolean isMouseOver(double d, double e) {
        return this.isHovered;
    }

    /**
     * Renders the main widget
     */
    @Override
    protected void renderWidget(GuiGraphics graphics, int i, int j, float f) {
        this.isHovered = areCoordinatesInRectangle(i, j, getX(), getY(), getWidth(), getHeight() * 3 + 1);
        this.mouseCords = List.of(i, j);

        int outline_color = 0xFF8B837E;
        int highlight_color = 0x19FFFFFF;
        int white = 0xFFFFFFFF;
        int black = 0xFF000000;

        if (isHovered) outline_color = 0xFFFFFFFF; //change from grey to white
        graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, outline_color); //outline
        graphics.fill(getX(), getY(),getX() + this.width, getY() + this.height, /*color*/ black); //black box

        if(isHovered) graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, highlight_color);//highlight on hover
        graphics.drawCenteredString(this.font, this.isExpanded ? "▲" : "▼", getXwithOffset(90), getCenteredY(-3), /*color*/ white); //dropdown arrow
        graphics.drawString(this.font, shortenEntryString(this.packs.getFirst()), getXwithOffset(10), getCenteredY(-4), white); //Entry name

        if (this.isExpanded) {
            graphics.enableScissor(getX() - 1, getY() + this.height + 1, getX() + this.width + 1, getY() + this.height * 3 + 1);

            graphics.fill(getX() - 1, getY() + this.height, getX() + this.width + 1, getY() + this.height * 3 + 1, 0x2BFFFFFF); //translucent outline
            for (int c = 1; c < this.packs.size(); c++) {
                graphics.fill(getX(), getY() + this.height * c + 1 - this.scroll, getX() + this.width, (getY() + this.height * (c + 1)) - this.scroll, black); //black box
                graphics.drawString(this.font, shortenEntryString(this.packs.get(c)), getXwithOffset(10), (getCenteredY(-4) + this.height * c + 1) - this.scroll, white); //Entry name

                this.isEntryHovered = areCoordinatesInRectangle(i, j, getX(), getY() + this.height * c + 1 - this.scroll, this.width, this.height);
                if (this.isEntryHovered) graphics.fill(getX(), getY() + this.height * c + 1 - this.scroll, getX() + this.width, getY() + this.height * (c + 1) - this.scroll, highlight_color); //highlight
            }

            graphics.disableScissor();
        }

        this.setTooltip(Tooltip.create(Component.translatable("gui.widget.DropdownList.tooltip")));
    }


    /**
     * Allows an easy & reusable way to position an item inside the widget
     * @param percent How far right the item's x-coordinate should be
     * @return x-coordinate
     */
    public int getXwithOffset(int percent){
        return getX() + this.width * percent / 100;
    }

    /**
     * Allows an easy & reusable way to center items inside the widget
     * @param translate How far up or down the item should be offset by
     * @return y-coordinate
     */
    public int getCenteredY(int translate) {
        return getY() + this.height / 2 + translate;
    }

    /**
     * Shortens the name of the text that should be rendered inside the widget so it is no longer than 12 characters
     * @param message The string that should (or not) be shortened
     * @return The message, shortened with ellipsis if needed
     */
    public String shortenEntryString(@NotNull String message) {
        if (message.length() > 12) {
            StringBuilder y = new StringBuilder();
            for (int c = 0; c < 12; c++){
                y.append(message.charAt(c));
            }
            y.append("...");
            return y.toString();
        }
        else{
            return message;
        }
    }

    /**
     * Checks whether coordinates are in a Rectangle, based on areCoordinatesInRectangle from AbstractWidget
     * @param d x-coordinate
     * @param e y-coordinate
     * x, y, w, h are the same as those in
     * @return Returns a boolean that is true if the coordinates are inside the rectangle, false if not
     */
    private boolean areCoordinatesInRectangle(double d, double e, int x, int y, int w, int h) {
        return d >= x && e >= y && d < x + w && e < y + h;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        if (!this.isHovered) return false;
        else{
            this.scroll += g * 3;
            return true;
        }
    }

    /**
     * Opens the dropdown on click release
     */
    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent) {
        ResourceScreening.LOGGER.info(String.valueOf(this.isEntryHovered));
        if (!this.isHovered) {
            this.isExpanded = false;
        } else if (areCoordinatesInRectangle(this.mouseCords.getFirst(), this.mouseCords.getLast(), getX(), getY(), getWidth(), getHeight())) {
            ResourceScreening.LOGGER.info(String.valueOf(this.mouseCords.getFirst()));
            ResourceScreening.LOGGER.info(packs.getFirst());
            this.isExpanded = false;
        }
        else {
            this.isExpanded = !this.isExpanded;
            ResourceScreening.LOGGER.info(String.valueOf(this.isEntryHovered));
        }

    }

    //TODO add widget narration
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}