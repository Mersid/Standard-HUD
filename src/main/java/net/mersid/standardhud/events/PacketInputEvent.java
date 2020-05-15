package net.mersid.standardhud.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Packet;

public interface PacketInputEvent {
	Event<PacketInputEvent> EVENT = EventFactory.createArrayBacked(PacketInputEvent.class,
			(listeners) -> (packet) -> {
				for (PacketInputEvent event : listeners) {
					event.onPacketReceived(packet);
				}
			});

	void onPacketReceived(Packet<?> packet);
}
