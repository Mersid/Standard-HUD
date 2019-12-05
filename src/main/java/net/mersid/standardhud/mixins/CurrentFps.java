package net.mersid.standardhud.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface CurrentFps {
	@Accessor("currentFps")
	static int get() {
		throw new AssertionError();
	}
}