package net.shushi93.resource.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.shushi93.resource.gui.components.filter;
import net.shushi93.resource.gui.widget.DropdownList;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class TextureScreen extends Screen {
    public final Screen parent;
    public static final int RETURN_LOCATION = 172;
    public TextureScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawString(this.font, Component.translatable("gui.screens.TextureScreen.filterButtonLabel"), 175, this.font.lineHeight, 0xFFFFFFFF);
    }

    @Override
    protected void init() {
        //TODO maybe make some inline documentation
        EditBox search = new EditBox(this.font, 40, 40 - this.font.lineHeight, 350, 20, Component.empty());
        this.addRenderableWidget(search);

        SpriteIconButton spriteIconButton2 = this.addRenderableWidget(
                filter.filter_btn(
                        20, button -> Minecraft.getInstance().setScreen(new TextureFilterScreen(Component.empty(), this)), true
                )
        );
        spriteIconButton2.setPosition(400, 40 - this.font.lineHeight);
        spriteIconButton2.setTooltip(Tooltip.create(Component.translatable("gui.screens.TextureScreen.filterTooltip")));

        DropdownList dropdown = new DropdownList(172, 112, 120, 20);
        this.addRenderableWidget(dropdown);

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (btn) -> Minecraft.getInstance().setScreen(this.parent)).bounds(RETURN_LOCATION, 225, 120, 20).build();
        this.addRenderableWidget(back);
    }
}
