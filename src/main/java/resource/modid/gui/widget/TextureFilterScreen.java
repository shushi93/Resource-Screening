//Forget about this for now
package resource.modid.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TextureFilterScreen extends Screen {
    public Screen parent;
    public TextureFilterScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }
    @Override
    protected void init() {
        Button temp = Button.builder(Component.translatable("gui.widget.TextureFilterScreen.temp"), (btn) ->{
            assert this.minecraft != null; //stupid IntelliJ bothering me with ts, not needed
            this.minecraft.getToastManager().addToast(
                    SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.translatable("gui.widget.TextureFilterScreen.tempLabelTitle"), Component.translatable("gui.widget.TextureFilterScreen.tempLabelMessage"))
            );
        }).bounds(40, 40, 120, 20).build();
        this.addRenderableWidget(temp);

        Button back = Button.builder(Component.translatable("gui.widget.TextureScreen.backButton"), (btn) -> {
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(40, 120, 120, 20).build();
        this.addRenderableWidget(back);
    }
}
