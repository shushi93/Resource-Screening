package net.shushi93.resource.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Creates the dropdown list widget and handles functionality
 */
public class DropdownList extends AbstractWidget {
    private boolean isExpanded = false;
    protected final Font font = Minecraft.getInstance().font;
    private final List<String> m;
    private int scroll;
    private boolean isEntryHovered;
    private boolean isMainHover;

    /**
     * Overloaded Constructor
     */
    public DropdownList(int x, int y, int w, int h) {
        final List<String> m = List.of("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6");
        this(x, y, w, h, m);
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
        this.m = items;
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
        this.isMainHover = areCoordinatesInRectangle(i, j, getX(), getY(), getWidth(), getHeight());

        int outline_color = 0xFF8B837E;
        int highlight_color = 0x19FFFFFF;
        int white = 0xFFFFFFFF;
        int black = 0xFF000000;

        if (isHovered) outline_color = 0xFFFFFFFF; //change from grey to white
        graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, outline_color); //outline
        graphics.fill(getX(), getY(),getX() + this.width, getY() + this.height, /*color*/ black); //black box

        if(isHovered) graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, highlight_color);//highlight on hover
        graphics.drawCenteredString(this.font, this.isExpanded ? "▲" : "▼", getXwithOffset(90), getCenteredY(-3), /*color*/ white); //dropdown arrow
        graphics.drawString(this.font, shortenEntryString(this.m.getFirst()), getXwithOffset(10), getCenteredY(-4), white); //Entry name

        if (this.isExpanded) {
            graphics.enableScissor(getX() - 1, getY() + this.height + 1, getX() + this.width + 1, getY() + this.height * 3 + 1);

            graphics.fill(getX() - 1, getY() + this.height, getX() + this.width + 1, getY() + this.height * 3 + 1, 0x2BFFFFFF); //translucent outline

            for (int c = 1; c < this.m.size(); c++) {
                graphics.fill(getX(), getY() + this.height * c + 1 - this.scroll, getX() + this.width, (getY() + this.height * (c + 1)) - this.scroll, black); //black box
                graphics.drawString(this.font, shortenEntryString(this.m.get(c)), getXwithOffset(10), (getCenteredY(-4) + this.height * c + 1) - this.scroll, white); //Entry name

                this.isEntryHovered = areCoordinatesInRectangle(i, j, getX(), getY() + this.height * c + 1 - this.scroll, this.width, this.height);
                if (this.isEntryHovered) graphics.fill(getX(), getY() + this.height * c + 1 - this.scroll, getX() + this.width, getY() + this.height * (c + 1) - this.scroll, highlight_color); //highlight
            }

            graphics.disableScissor();
        }

        this.setTooltip(Tooltip.create(Component.translatable("gui.widget.DropdownList.tooltip")));
    }


    /**
     * Allows an easy & resuable way to position an item inside the widget
     * @param percent How far right the item's x-coordinate should be
     * @return x-coordinate
     */
    public int getXwithOffset(int percent){
        return getX() + this.width * percent / 100;
    }

    /**
     * Allows an easy & reusable way to center items inside the widget
     * @param translate How far up or down the item should me offset by
     * @return y-coordinate
     */
    public int getCenteredY(int translate) {
        return getY() + this.height / 2 + translate;
    }

    /**
     * Shortens the name of the text that should be rendered inside the widget so it is no longer than 12 characters
     * @param message The string that should (or not) be shortened
     * @return The message, shortened with elpisis if needed
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
        if (this.isHovered) this.isExpanded = !this.isExpanded;
    }

    //TODO add widget narration
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}

//TODO after this is done, update version to 0.2.0