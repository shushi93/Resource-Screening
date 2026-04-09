package net.shushi93.resource.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shushi93.resource.ResourceScreening;

/**
 * Creates the filter button with the filter image
 */
@Environment(EnvType.CLIENT)
public class filter {
    public static SpriteIconButton filter_btn(int i, Button.OnPress onPress, boolean bl) {
        return SpriteIconButton.builder(Component.translatable("gui.components.filter.filterbtn"), onPress, bl)
                .width(i)
                .sprite(ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "filter"), 20, 20) //TODO add highlighted and disabled images
                .build();
    }
}
