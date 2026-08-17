package net.shushi93.resource.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.shushi93.resource.client.gui.widget.DropdownList;

/**
 * The main mod screen
 */
@Environment(EnvType.CLIENT)
public class TextureScreen extends Screen {
    public static final int RETURN_LOCATION = 172;
    public final Screen parent;

    public TextureScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    /**
     * Renders the title text
     */
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawString(this.font, Component.translatable("gui.screens.TextureScreen.filterButtonLabel"), 175, this.font.lineHeight, 0xFFFFFFFF);
    }

    /**
     * Renders all the main widgets
     */
    @Override
    protected void init() {
        EditBox search = new EditBox(this.font, 40, 40 - this.font.lineHeight, 350, 20, Component.empty());

//        SpriteIconButton spriteIconButton2 = this.addRenderableWidget(
//                filter.filter_btn(
//                        20, b -> Minecraft.getInstance().setScreen(new TextureFilterScreen(Component.empty(), this)), true
//                )
//        );
//        spriteIconButton2.setPosition(400, 40 - this.font.lineHeight);
//        spriteIconButton2.setTooltip(Tooltip.create(Component.translatable("gui.screens.TextureScreen.filterTooltip")));

        DropdownList dropdown = new DropdownList(172, 112, 120, 20);

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (b) -> onClose()).bounds(TextureScreen.RETURN_LOCATION, 224, 120, 20).build();
        Button b1 = Button.builder(Component.literal("B1"), b -> onClose()).bounds(120, 112, 20, 20).build();
        Button b2 = Button.builder(Component.literal("B2"), b -> onClose()).bounds(180, 112, 20, 20).build();

        this.addRenderableOnly(b1);
        this.addRenderableOnly(b2);
        this.addRenderableWidget(search);
        this.addRenderableWidget(back);
        //this.addRenderableWidget(dropdown);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }
}
