package net.shushi93.resource.client.gui.widgets;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.shushi93.resource.ResourceScreening;
import net.shushi93.resource.client.ResourceScreeningClient;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class DropdownTextureRenderer extends DropdownList {
    private static final WidgetSprites RENDERER_BOX = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "renderer/renderer"),
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "renderer/renderer_highlighted")
    );
    private final String src_texture;
    private String current_selection;
    private DynamicTexture d;
    private boolean b;

    public DropdownTextureRenderer(int x, int y, int w, int h, String src_texture, String current_selection, Consumer<String> selection_callback) {
        super(x, y, w, h, src_texture, current_selection, selection_callback);
        this.src_texture = src_texture;
        this.current_selection = current_selection;
        register_texture();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);

        if (!current_selection.equals(getCurrent_selection())) {
            current_selection = getCurrent_selection();
            register_texture();
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                RENDERER_BOX.get(this.isActive(), this.isHovered()),
                this.getX(), this.getY() - getHeight(), this.width, this.height, 0xFFFFFFFF);

        if (!b) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, src_texture),
                    super.getXwithOffset(50) - 8, getCenteredY() - getHeight() * 2 + 12, 0, 0, 16, 16, 16, 16, 0xFFFFFFFF);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ResourceLocation.withDefaultNamespace("textures/" + src_texture),
                    super.getXwithOffset(50) - 8, getCenteredY() - getHeight() * 2 + 12, 0, 0, 16, 16, 16, 16, 0xFFFFFFFF);
        }
    }

    public void close() {
        try {
            d.close();
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(Arrays.toString(e.getStackTrace()));
        }
    }

    private void register_texture() {
        close();
        try (FileSystem fs = FileSystems.newFileSystem(ResourceScreeningClient
                .pack_directory
                .resolve(getCurrent_selection()), Map.of())) {
            try {
                this.d = new DynamicTexture(
                        () -> src_texture,
                        NativeImage.read(
                                Files.newInputStream(
                                        fs.getPath("assets", "minecraft", "textures")
                                                .resolve(src_texture)
                                )
                        )
                );
                Minecraft.getInstance().getTextureManager().register(
                        ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, src_texture),
                        d
                );
            } catch (Exception e) {
                this.b = true;
                LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(Arrays.toString(e.getStackTrace()));
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(Arrays.toString(e.getStackTrace()));
            this.b = true;
        }
    }
}
