package resource.modid.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextureScreen extends Screen {
    public Screen parent;
    private static final ResourceLocation FILTER = ResourceLocation.withDefaultNamespace("textures/gui/filters_button.png");

    public TextureScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button filter = Button.builder(Component.translatable("gui.widget.TextureScreen.filterButton"), (btn) -> Minecraft.getInstance().setScreen(
                new TextureFilterScreen(Component.empty(), this)
        )).bounds(343, 40 - this.font.lineHeight - 10, 120, 20).build();
        this.addRenderableWidget(filter);

        Button back = Button.builder(Component.translatable("gui.widget.TextureScreen.backButton"), (btn) -> Minecraft.getInstance().setScreen(this.parent)).bounds(343, 225, 120, 20).build();
        this.addRenderableWidget(back);

        ImageButton button = ImageButton.
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawString(this.font, Component.translatable("gui.widget.TextureScreen.filterButtonLabel"), 235, 10 - this.font.lineHeight, 0xFFFFFFFF);
    }
}
