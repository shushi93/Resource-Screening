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
import net.shushi93.resource.client.gui.widgets.DropdownList;
import net.shushi93.resource.client.gui.widgets.DropdownListList;
import net.shushi93.resource.client.gui.widgets.DropdownTextureRenderer;
import net.shushi93.resource.client.gui.widgets.filter;
import net.shushi93.resource.client.util.JsonWriter;
import net.shushi93.resource.client.util.Zip_Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The main mod screen
 */
@Environment(EnvType.CLIENT)
public class TextureScreen extends Screen {
    private static final int ROW_SIZE = 2;
    /**
     * {@link #onClose()}
     */
    public static boolean changed = false;
    /**
     * The screen this screen was opened from, is used to return to that screen {@link #onClose()}
     */
    public final Screen parent;
    private final Minecraft mc = Minecraft.getInstance();
    private final PackRepository pr = mc.getResourcePackRepository();
    /**
     * Stores a map of the selected textures, for persistence. <br>
     * src_texture <-> pack name (current_selection)
     */
    private final Map<String, String> saved_selections = JsonWriter.get_map();
    private final ArrayList<DropdownTextureRenderer> all_dropdowns = new ArrayList<>();
    private DropdownListList dll;

    public TextureScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    /**
     * Renders the title text, and the dropdown rows
     */
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawString(this.font, Component.translatable("gui.screens.TextureScreen.titleText"), 175, this.font.lineHeight, 0xFFFFFFFF);

        dll.renderDecorations(context, mouseX, mouseY);
    }

    /**
     * Checks for {@link changed} textures.
     * Persists and saves changes if necessary, and runs {@link DropdownTextureRenderer#close()} on each dropdown. <br>
     * Returns to the previous screen
     */
    @Override
    public void onClose() {
        if (changed) {
            Zip_Helper.addIfNotSelected();
            mc.options.save();
            mc.reloadResourcePacks();
            changed = false;

            for (DropdownTextureRenderer d : all_dropdowns) {
                d.close();
            }
        }
        mc.setScreen(this.parent);
    }

    /**
     * Renders all the main widgets
     */
    @Override
    protected void init() {
        EditBox search = new EditBox(this.font, 40, 40 - this.font.lineHeight, 350, 20, Component.empty());

        SpriteIconButton spriteIconButton2 = this.addRenderableWidget( // Filter button
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

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (b) -> onClose()).bounds(width / 2 - 60, height - 20, 120, 20).build();

        dll = new DropdownListList(mc, width, height - 60 - 30, 60, 50);
        //                             width,   height,             y,    y-spacing

        List<DropdownList> dropdowns = new ArrayList<>();
        for (String texture : Objects.requireNonNull(Zip_Helper.get_textures("Better-Leaves-9.5"))) {
            DropdownTextureRenderer d = new DropdownTextureRenderer(
                    0,
                    0,
                    120, 20, texture,
                    saved_selections.getOrDefault(
                            texture,
                            Objects.requireNonNullElse(
                                            Zip_Helper.get_all_pack_names(), List.of("vanilla")
                                    ).stream().findFirst()
                                    .orElse("vanilla")
                    ), //get texture's current selection, or default to the first pack if not found
                    (new_selection) -> {
                        saved_selections.put(texture, new_selection);
                        JsonWriter.write_json(saved_selections);
                    });
            dropdowns.add(d);

            if (dropdowns.size() >= ROW_SIZE) {
                dll.addEntry(dropdowns);
                dropdowns.clear();
            }
            all_dropdowns.add(d);
        }
        if (!dropdowns.isEmpty()) {
            dll.addEntry(dropdowns);
        }

        search.setResponder(s -> dll.updateEntries(s.toLowerCase()));

        //h: 250, w: 400
        this.addRenderableWidget(search);
        this.addRenderableWidget(back);
        this.addRenderableWidget(dll);
    }
}