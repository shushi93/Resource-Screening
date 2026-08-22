package net.shushi93.resource.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.shushi93.resource.ResourceScreening;
import net.shushi93.resource.client.gui.components.Pack_Creator;
import net.shushi93.resource.client.gui.widget.DropdownList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
    private final List<String> selected_packs = new ArrayList<>(pr.getSelectedIds());
    private final List<String> hacking_noises = new ArrayList<>(pr.getAvailableIds());

    public TextureScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        Pack_Creator.createPack("doogile");
        Pack_Creator.createPack("riley");
        mc.reloadResourcePacks();
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
        LOGGER.info("Available IDs: {}", pr.getAvailableIds());
//        LOGGER.info("Selected IDs: {}", pr.getSelectedIds());

        EditBox search = new EditBox(this.font, 40, 40 - this.font.lineHeight, 350, 20, Component.empty());

//        SpriteIconButton spriteIconButton2 = this.addRenderableWidget(
//                filter.filter_btn(
//                        20, b -> mc.setScreen(new TextureFilterScreen(Component.empty(), this)), true
//                )
//        );
//        spriteIconButton2.setPosition(400, 40 - this.font.lineHeight);
//        spriteIconButton2.setTooltip(Tooltip.create(Component.translatable("gui.screens.TextureScreen.filterTooltip")));

        DropdownList dropdown = new DropdownList(172, 112, 120, 20);

        Button back = Button.builder(Component.translatable("gui.screens.TextureScreen.backButton"), (b) -> onClose()).bounds(TextureScreen.RETURN_LOCATION, 224, 120, 20).build();
        Button b1 = Button.builder(Component.literal("B1"), b -> onClick("file/doogile")).bounds(120, 112, 20, 20).build();
        Button b2 = Button.builder(Component.literal("B2"), b -> onClick("file/riley")).bounds(120, 144, 20, 20).build();

        this.addRenderableWidget(b1);
        this.addRenderableWidget(b2);
        this.addRenderableWidget(search);
        this.addRenderableWidget(back);
        //this.addRenderableWidget(dropdown);
    }

    @Override
    public void onClose() {
        mc.setScreen(this.parent);
    }

    private void reset_hacking_noises() {
        hacking_noises.clear();
        hacking_noises.addAll(selected_packs);
    }

    private void onClick(String name) {
        reset_hacking_noises();
        hacking_noises.add(name);
        LOGGER.info("hackingnoises: {}", hacking_noises);
//        LOGGER.info("available: {}", pr.getAvailableIds());
        pr.setSelected(hacking_noises);
        mc.reloadResourcePacks();
    }
}
