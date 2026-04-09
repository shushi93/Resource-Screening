package net.shushi93.resource.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.shushi93.resource.client.gui.screens.TextureScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Quick access during dev

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("RETURN"), method = "createNormalMenuOptions")
    private void addModButton(int i, int j, CallbackInfoReturnable<Integer> cir) {
        this.addRenderableWidget(
            Button.builder(Component.translatable("mixin.TitleScreenMixin.modButton"), _ -> Minecraft.getInstance().setScreen(new TextureScreen(Component.empty(), this)))
                    .bounds(this.width / 2 - 100 + 205, i, 20, 20)
                    .build()
        );
    }
}
