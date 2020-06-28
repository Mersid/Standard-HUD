package net.mersid.standardhud.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;

public interface OnRenderCallback {
	Event<OnRenderCallback> EVENT = EventFactory.createArrayBacked(OnRenderCallback.class,
			(listeners) -> (matrixStack, partialTicks) -> {
				for (OnRenderCallback event : listeners) {
					event.onRender(matrixStack, partialTicks);
				}
			});

	void onRender(MatrixStack matrixStack, float partialTicks);
}
