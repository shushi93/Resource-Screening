package net.shushi93.resource.gui.widget;

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

public class DropdownList extends AbstractWidget {
    private boolean isExpanded = false;
    protected Font font = Minecraft.getInstance().font;
    private String message;
    private List<String> m;

    public DropdownList(int x, int y, int w, int h) {
        List<String> m = List.of("Item 1 but Super duper longer", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6");
        this.m = m;
        this.message = m.getFirst();
        super(x, y, w, h, Component.literal(m.getFirst()));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int i, int j, float f) {
        int outline_color = 0xFF8B837E;

        if (isHovered) {
            outline_color = 0xFFFFFFFF; //change from grey to white
        }
        graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, outline_color);
        graphics.fill(getX(), getY(),getX() + this.width, getY() + this.height, /*color*/ 0xFF000000);
        if(isHovered) {graphics.fill(getX() - 1, getY() - 1, getX() + this.width + 1, getY() + this.height + 1, 0x19FFFFFF);}//make it grey-er when hovered (layering is pain)
        graphics.drawCenteredString(this.font, this.isExpanded ? "▲" : "▼", getXwithOffset(90), getYwithOffset(-3), /*color*/ 0xFFFFFFFF);
        graphics.drawString(this.font, shortenEntryString(this.message), getXwithOffset(10), getYwithOffset(-4), 0xFFFFFFFF);

        if (this.isExpanded) {
            //graphics.enableScissor(getX(), getY() + this.height, getX() + this.width, getY() + this.height * 3);
            for (int c = 1; c < this.m.size(); c++) {
                graphics.fill(getX(), getY() + this.height * c, getX() + this.width, getY() + this.height, 0xFF000000 + (c * 100));
                graphics.drawString(this.font, shortenEntryString(this.m.get(c)), getXwithOffset(10), getYwithOffset(-4) + this.height * c, 0xFFFFFFFF);
            }
            //graphics.disableScissor();
        }

        this.setTooltip(Tooltip.create(Component.translatable("gui.widget.DropdownList.tooltip")));
    }

    public int getXwithOffset(int percent){
        return getX() + this.width * percent / 100;
    }

    public int getYwithOffset(int translate) {
        return getY() + this.height / 2 + translate;
    }

    public String shortenEntryString(@NotNull String message) {
        if (message.length() > 12) {
            String y = "";
            for (int c = 0; c < 12; c++){
                y += message.charAt(c);
            }
            y += "...";
            return y;
        }
        else{
            return message;
        }
    }

    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent) {
        this.isExpanded = !this.isExpanded;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        //TODO add narration
    }
}

//TODO after this is done, update version to 0.2.0