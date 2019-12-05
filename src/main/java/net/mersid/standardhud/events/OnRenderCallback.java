package net.mersid.standardhud.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface OnRenderCallback {
    Event<OnRenderCallback> EVENT = EventFactory.createArrayBacked(OnRenderCallback.class,
            (listeners) -> () -> {
                for (OnRenderCallback event : listeners) {
                    event.onRender();
                }
        });
	
	void onRender();
}
