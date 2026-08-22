package net.shushi93.resource.client.gui.components;

import net.minecraft.client.Minecraft;
import net.shushi93.resource.ResourceScreening;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class Pack_Creator {

    public static void createPack(String pack_name) {
        Path pack = Minecraft.getInstance().gameDirectory.toPath().resolve("resourcepacks").resolve(pack_name);

        try {
            Files.createDirectories(pack.resolve("assets/minecraft"));
            Files.writeString(
                    pack.resolve("pack.mcmeta"),
                    """
                            {
                              "pack": {
                                "description": "custom textures by resource-screening",
                                "min_format": [69, 0],
                                "max_format": [2147483647, 0]
                              }
                            }
                            """
            );
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error("Failed to create pack: {}", e.getMessage());
        }
    }
}