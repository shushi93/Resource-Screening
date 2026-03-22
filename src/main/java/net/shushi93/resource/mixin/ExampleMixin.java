package net.shushi93.resource.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//for my reference incase I forget how to make a mixin
@SuppressWarnings("UnusedMixin")
@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@SuppressWarnings("EmptyMethod")
    @Inject(at = @At("HEAD"), method = "loadLevel")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}
}