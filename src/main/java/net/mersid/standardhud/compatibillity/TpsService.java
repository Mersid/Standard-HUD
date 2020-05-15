package net.mersid.standardhud.compatibillity;

import net.mersid.standardhud.events.PacketInputEvent;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

// With thanks to the Kami Client team!
public class TpsService {
	public static TpsService INSTANCE = new TpsService();

	private final float[] tickRates = new float[20];
	private int nextIndex = 0;
	private long timeLastTimeUpdate;

	public TpsService()
	{
		PacketInputEvent.EVENT.register(packet -> {if (packet instanceof WorldTimeUpdateS2CPacket) INSTANCE.onTimeUpdate();});
		reset();
	}

	public void reset()
	{
		this.nextIndex = 0;
		this.timeLastTimeUpdate = -1L;
		Arrays.fill(this.tickRates, 0.0F);
	}

	public float getTickRate()
	{
		float numTicks = 0.0F;
		float sumTickRates = 0.0F;
		for (float tickRate : this.tickRates) {
			if (tickRate > 0.0F)
			{
				sumTickRates += tickRate;
				numTicks += 1.0F;
			}
		}
		return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
	}

	public void onTimeUpdate()
	{
		if (this.timeLastTimeUpdate != -1L)
		{
			float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
			this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
			this.nextIndex += 1;
		}
		this.timeLastTimeUpdate = System.currentTimeMillis();
	}
}
