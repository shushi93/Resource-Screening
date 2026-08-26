package net.shushi93.resource.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.shushi93.resource.client.util.Zip_Helper;

import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceScreeningClient implements ClientModInitializer {
    public static final Path pack_directory = Minecraft.getInstance().gameDirectory.toPath().resolve("resourcepacks");

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        if (!Files.exists(pack_directory.resolve("doogile.zip")))
            Zip_Helper.createPack("doogile");
    }
}