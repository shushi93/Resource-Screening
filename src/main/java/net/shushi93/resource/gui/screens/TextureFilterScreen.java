//Forget about this for now
package net.shushi93.resource.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TextureFilterScreen extends Screen {
    public final Screen parent;
    public TextureFilterScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }
    @Override
    protected void init() {
        Button temp = Button.builder(Component.translatable("gui.screens.TextureFilterScreen.temp"), (btn) -> Minecraft.getInstance().getToastManager().addToast(
                SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.translatable("gui.screens.TextureFilterScreen.tempLabelTitle"), Component.translatable("gui.screens.TextureFilterScreen.tempLabelMessage"))
        )).bounds(40, 40, 120, 20).build();
        this.addRenderableWidget(temp);

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (btn) -> Minecraft.getInstance().setScreen(this.parent)).bounds(TextureScreen.RETURN_LOCATION, 120, 120, 20).build();
        this.addRenderableWidget(back);
    }
}
//TODO not sure what I'm going to do with this