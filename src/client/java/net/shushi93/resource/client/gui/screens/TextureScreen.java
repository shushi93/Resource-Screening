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
import net.shushi93.resource.client.gui.widgets.DropdownTextureRenderer;
import net.shushi93.resource.client.gui.widgets.filter;
import net.shushi93.resource.client.util.JsonWriter;
import net.shushi93.resource.client.util.Zip_Helper;

import java.util.Map;
import java.util.Objects;

/**
 * The main mod screen
 */
@Environment(EnvType.CLIENT)
public class TextureScreen extends Screen {
    public static boolean changed = false;
    public final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();
    private final PackRepository pr = mc.getResourcePackRepository();
    private final Map<String, String> saved_selections = JsonWriter.get_map();

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

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (b) -> onClose()).bounds(mc.screen.width / 2 - 60, mc.screen.height - 20, 120, 20).build();

        int i = 0;
        for (String texture : Objects.requireNonNull(Zip_Helper.get_textures("Better-Leaves-9.5"))) {
            int columns = Math.max(1, ((this.width - 20) + 5) / (120 + 5));

            this.addRenderableWidget(new DropdownTextureRenderer(
                    20 + (i % columns) * (120 + 5),
                    80 + (i / columns) * (20 + 30),
                    120, 20, texture,
                    saved_selections.getOrDefault(texture, Zip_Helper.get_all_pack_names().stream().findFirst().toString()),
                    (new_selection) -> {
                        saved_selections.put(texture, new_selection);
                        JsonWriter.write_json(saved_selections);
                    })
            );
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
}