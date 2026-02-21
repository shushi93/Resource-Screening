package resource.modid.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;


@Mixin(OptionsScreen.class)
public abstract class ResourceScreenMixin extends Screen {
    protected ResourceScreenMixin(Component component) {
        super(component);
    }

    @Definition(id = "openScreenButton", method = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;openScreenButton(Lnet/minecraft/network/chat/Component;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/components/Button;")
    @Definition(id = "RESOURCEPACK", field = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;RESOURCEPACK:Lnet/minecraft/network/chat/Component;")
    @Expression("this.openScreenButton(RESOURCEPACK, ?)")
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "MIXINEXTRAS:EXPRESSIONS")))
    private Supplier<Screen> redirectButton(Component component, Supplier<Screen> supplier){
        return supplier;
    }
}