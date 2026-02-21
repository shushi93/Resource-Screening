package resource.modid.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
<<<<<<< HEAD
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
=======
>>>>>>> dbec9be (sync local code (redirect button to screen isnt working yet))
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
<<<<<<< HEAD
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
=======
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import resource.modid.gui.ExampleGui;
import resource.modid.gui.ExampleScreen;
>>>>>>> dbec9be (sync local code (redirect button to screen isnt working yet))

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("RETURN"), method = "createNormalMenuOptions")
    private void addModButton(int i, int j, CallbackInfoReturnable<Integer> cir) {
        this.addRenderableWidget(
<<<<<<< HEAD
                Button.builder(Component.translatable("mod.button"), button -> Minecraft.getInstance().setScreen(new SelectWorldScreen(this)))
=======
                Button.builder(Component.translatable("mixin.TitleScreenMixin.modButton"), button -> Minecraft.getInstance().setScreen(new ExampleScreen(new ExampleGui())))
>>>>>>> dbec9be (sync local code (redirect button to screen isnt working yet))
                        .bounds(this.width / 2 - 100 + 205, i, 20, 20)
                        .build()
        );
    }
}
