package net.mersid.standardhud.modules;

import net.mersid.standardhud.Module;
import net.mersid.standardhud.compatibillity.FormattingCodes;
import net.mersid.standardhud.compatibillity.TpsService;
import net.mersid.standardhud.compatibillity.WMinecraft;
import net.mersid.standardhud.compatibillity.WPlayer;
import net.mersid.standardhud.events.OnRenderCallback;
import net.mersid.standardhud.events.PacketInputEvent;
import net.mersid.standardhud.mixins.CurrentFps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TCFModule extends Module {

	public TCFModule() {
		super("Time, Compass, and FPS");
		
		OnRenderCallback.EVENT.register(this::onRenderGUI);
	}
	
	public void onRenderGUI(MatrixStack matrixStack, float partialTicks)
	{
		renderTime(matrixStack);
		renderCoords(matrixStack);
		renderCompass(matrixStack);
		renderFps(matrixStack);
	}

	private void renderCoords(MatrixStack matrixStack)
	{
		Window mw = MinecraftClient.getInstance().getWindow();
		ClientPlayerEntity player = WMinecraft.getPlayer();
		double compass = WPlayer.getCompass();
		double angle = WPlayer.getAngle();

		// Color codes. Green for HEADING positive, red for negative, yellow for neutral (perfect alignment to one axis)
		// Note: North is negative Z, east is positive X

		String xFormattingCode =
				compass > 0 && compass < 45 || compass > 135 && compass < 180       ?   FormattingCodes.GREEN       :
				compass >= 45 && compass <= 135                                     ?   FormattingCodes.DARK_GREEN  :
				compass > 180 && compass < 225 || compass > 315 && compass < 360    ?   FormattingCodes.RED         :
				compass >= 225 && compass <= 315                                    ?   FormattingCodes.DARK_RED    :
				FormattingCodes.YELLOW;

		String yFormattingCode =
				angle >= 45 ? FormattingCodes.DARK_GREEN    :
				angle > 0 ? FormattingCodes.GREEN           :
				angle <= -45 ? FormattingCodes.DARK_RED     :
				angle < 0 ? FormattingCodes.RED             :
				FormattingCodes.YELLOW;

		String zFormattingCode =
				compass > 270 && compass < 315 || compass > 45 && compass < 90  ?   FormattingCodes.RED         :
				compass >= 315 || compass <= 45                                 ?   FormattingCodes.DARK_RED    :
				compass > 90 && compass < 135 || compass > 225 && compass < 270 ?   FormattingCodes.GREEN       :
				compass >= 135 && compass <= 270                                ?   FormattingCodes.DARK_GREEN  :
				FormattingCodes.YELLOW;

		WMinecraft.renderText(
				matrixStack,
				"Coords: X: " + xFormattingCode + String.format("%.3f", player.getX()) + FormattingCodes.RESET +
				", Y: " + yFormattingCode + String.format("%.3f", player.getY()) + FormattingCodes.RESET +
				", Z: " + zFormattingCode + String.format("%.3f", player.getZ()) + FormattingCodes.RESET ,
				3, mw.getScaledHeight() - 31);
	}

	private void renderFps(MatrixStack matrixStack)
	{
		int fps = CurrentFps.get();
		Window mw = MinecraftClient.getInstance().getWindow();
		String fpsFormattingCode =
				fps > 100	?	FormattingCodes.DARK_GREEN	:
				fps > 60 	?	FormattingCodes.GREEN		:
				fps > 45	?	FormattingCodes.YELLOW		:
				fps > 30	?	FormattingCodes.GOLD		:
				fps > 15	?	FormattingCodes.RED			:
				FormattingCodes.DARK_RED;

		float tps = TpsService.INSTANCE.getTickRate();
		String tpsFormattingCode =
				tps >= 18   ?   FormattingCodes.DARK_GREEN  :
				tps >= 14   ?   FormattingCodes.GREEN       :
				tps >= 10   ?   FormattingCodes.YELLOW      :
				tps >= 6    ?   FormattingCodes.GOLD        :
				tps >= 3    ?   FormattingCodes.RED         :
				FormattingCodes.DARK_RED;

		WMinecraft.renderText(
				matrixStack,
				"FPS: " + fpsFormattingCode + fps + FormattingCodes.RESET +
				" | TPS: " + tpsFormattingCode + String.format("%.2f", tps)  + FormattingCodes.RESET
				, 3, mw.getScaledHeight() - 11);
	}

	private void renderCompass(MatrixStack matrixStack)
	{
		double compass = WPlayer.getCompass();
		Window mw = MinecraftClient.getInstance().getWindow();
		WMinecraft.renderText(
				matrixStack,
				"Compass: " + FormattingCodes.YELLOW + String.format("%.2f", compass) +
				FormattingCodes.RESET + " (" + FormattingCodes.YELLOW + WPlayer.getCompassDirection() +
				FormattingCodes.RESET + ") | Angle: " + FormattingCodes.YELLOW + String.format("%.2f", WPlayer.getAngle()) +
				FormattingCodes.RESET, 3, mw.getScaledHeight() - 21);
	}


	private void renderTime(MatrixStack matrixStack)
	{
		// https://github.com/Lunatrius/InGame-Info-XML/blob/master/src/main/java/com/github/lunatrius/ingameinfo/tag/TagTime.java
		final long time = WMinecraft.getWorld().getTimeOfDay();
		final long hour = (time / 1000 + 6) % 24;
		final long minute = (time % 1000) * 60 / 1000;
		Window mw = MinecraftClient.getInstance().getWindow();
		WMinecraft.renderText(matrixStack, "Time: " + FormattingCodes.YELLOW + String.format("%02d:%02d", hour, minute) + FormattingCodes.RESET, 3, mw.getScaledHeight() - 41);
	}
}
