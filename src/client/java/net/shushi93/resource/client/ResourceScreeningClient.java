package net.shushi93.resource.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.shushi93.resource.client.util.Pack_Creator;

import java.nio.file.Files;

public class ResourceScreeningClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        if (Files.exists(Minecraft.getInstance().gameDirectory.toPath().resolve("resourcepacks").resolve("doogile.zip")))
            Pack_Creator.createPack("doogile");
    }
}