package net.shushi93.resource.client.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.shushi93.resource.client.gui.screens.TextureScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
@Mixin(OptionsScreen.class)
public abstract class ResourceScreenMixin extends Screen {

    protected ResourceScreenMixin(Component component) {
        super(component);
    }

    /**
     * Redirects the button to open my screen, instead of what the button usually opens
     * @param supplier The screen class to open
     * @return Returns the screen class to open
     */
    @Definition(id = "openScreenButton", method = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;openScreenButton(Lnet/minecraft/network/chat/Component;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/components/Button;")
    @Definition(id = "RESOURCEPACK", field = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;RESOURCEPACK:Lnet/minecraft/network/chat/Component;")
    @Expression("this.openScreenButton(RESOURCEPACK, ?)")
    @ModifyArg(method = "init", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Supplier redirectButton(@SuppressWarnings("ParameterCanBeLocal") Supplier supplier){
        supplier = () -> new TextureScreen(Component.empty(), this);
        return supplier;
    }
}