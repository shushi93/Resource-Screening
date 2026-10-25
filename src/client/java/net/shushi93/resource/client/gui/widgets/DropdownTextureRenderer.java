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
import java.util.Map;
import java.util.function.Consumer;

public class DropdownTextureRenderer extends DropdownList {
    private static final WidgetSprites RENDERER_BOX = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "renderer/renderer"),
            ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, "renderer/renderer_highlighted")
    );
    private final String src_texture;
    private String current_selection;
    /**
     * Stores the dynamic texture to be registered to Minecraft's texture manager, so that textures can be hot-swapped.
     */
    private DynamicTexture d;
    /**
     * Stores whether texture was registered successfully, so if not, the default texture can be rendered.
     */
    private boolean b;

    public DropdownTextureRenderer(int x, int y, int w, int h, String src_texture, String current_selection, Consumer<String> selection_callback) {
        super(x, y, w, h * 2, src_texture, current_selection, selection_callback, true);
        this.src_texture = src_texture;
        this.current_selection = current_selection;
        register_texture();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);

        if (!this.current_selection.equals(getCurrentSelection())) {
            this.current_selection = getCurrentSelection();
            register_texture();
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                RENDERER_BOX.get(this.isActive(), this.isTextureHovered(i, j)),
                this.getX(), this.getY() - this.getHeight(), this.getWidth(), this.getHeight(), 0xFFFFFFFF);

        if (!b) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ResourceLocation.fromNamespaceAndPath(ResourceScreening.MOD_ID, src_texture),
                    super.getXWithOffset(50) - 8, getCenteredY() - getHeight() * 2 + 12, 0, 0, 16, 16, 16, 16, 0xFFFFFFFF);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ResourceLocation.withDefaultNamespace("textures/" + src_texture),
                    super.getXWithOffset(50) - 8, getCenteredY() - getHeight() * 2 + 12, 0, 0, 16, 16, 16, 16, 0xFFFFFFFF);
        }
    }

    /**
     * Closes dynamic texture to prevent memory leaks
     */
    public void close() {
        try {
            if (d != null) d.close();
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(e.getMessage());
        }
    }

    /**
     * Registers the texture to the Minecraft's texture manager, by copying the original texture
     */
    private void register_texture() {
        close();
        try (FileSystem fs = FileSystems.newFileSystem(ResourceScreeningClient
                .pack_directory
                .resolve(this.current_selection), Map.of())) {
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
                this.b = false;
            } catch (Exception e) {
                this.b = true;
                LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(e.getMessage());
            }
        } catch (Exception e) {
            this.b = true;
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error(e.getMessage());
        }
    }

    /**
     * Checks if the mouse is hovering over the texture renderer box
     */
    private boolean isTextureHovered(int d, int e) {
        return this.areCoordinatesInRectangle(d, e, getX(), getY() - getHeight(), getWidth(), getHeight());
    }
}
