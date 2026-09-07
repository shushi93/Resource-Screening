package net.shushi93.resource.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.shushi93.resource.ResourceScreening;
import net.shushi93.resource.client.gui.widgets.filter;
import net.shushi93.resource.client.util.Zip_Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main mod screen
 */
@Environment(EnvType.CLIENT)
public class TextureScreen extends Screen {
    public static final int RETURN_LOCATION = 172;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceScreening.MOD_ID);
    public final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();
    private final PackRepository pr = mc.getResourcePackRepository();
    private boolean changed = false;

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

        SpriteIconButton spriteIconButton2 = this.addRenderableWidget(
                filter.filter_btn(
                        20, b -> mc.setScreen(new PackSelectionScreen(pr, repo -> {
                            pr.setSelected(repo.getSelectedIds());
                            mc.options.save();
                            mc.reloadResourcePacks();
                            mc.setScreen(this);
                        }, mc.gameDirectory.toPath().resolve("resourcepacks"), Component.translatable("options.resourcepack"))), true
                )
        );
        spriteIconButton2.setPosition(400, 40 - this.font.lineHeight);
        spriteIconButton2.setTooltip(Tooltip.create(Component.translatable("gui.screens.TextureScreen.filterTooltip")));

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (b) -> onClose()).bounds(TextureScreen.RETURN_LOCATION, 224, 120, 20).build();

        int i = 0;
        for (String texture : Zip_Helper.get_textures("Better-Leaves-9.5")) {
            Button b = Button.builder(Component.literal(String.format("B%d", i)), b1_ -> onClick("Better-Leaves-9.5", texture)).bounds(20 * i % 200, 120 + 20 * (i / 10) + 20, 20, 20).build();
            this.addRenderableWidget(b);
            i++;
        }
        //h: 250, w: 400
        this.addRenderableWidget(search);
        this.addRenderableWidget(back);
    }

    @Override
    public void onClose() {
        if (changed) {
            Zip_Helper.addIfNotSelected();
            mc.options.save();
            mc.reloadResourcePacks();
            changed = false;
        }
        mc.setScreen(this.parent);
    }

    private void onClick(String src_pack, String src_texture) {
        Zip_Helper.removeIfSelected();
        if (Zip_Helper.does_texture_exist(src_texture)) {
            Zip_Helper.remove_from_pack(src_texture);
        } else {
            Zip_Helper.add_to_pack(src_pack, src_texture);
        }

        LOGGER.debug(Boolean.toString(Zip_Helper.does_texture_exist(src_texture)));

        changed = true;
    }
}