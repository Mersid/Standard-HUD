package net.mersid.standardhud.modules;

import net.mersid.standardhud.Module;
import net.mersid.standardhud.compatibillity.FormattingCodes;
import net.mersid.standardhud.compatibillity.WMinecraft;
import net.mersid.standardhud.compatibillity.WPlayer;
import net.mersid.standardhud.events.OnRenderCallback;
import net.mersid.standardhud.mixins.CurrentFps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public class TCFModule extends Module {

	public TCFModule() {
		super("Time, Compass, and FPS");
		
		OnRenderCallback.EVENT.register(this::onRenderGUI);
		
	}
	
	public void onRenderGUI()
	{
		
		int fps = CurrentFps.get();
		Window mw = MinecraftClient.getInstance().getWindow();
		String colorcode = 
				fps > 100	?	FormattingCodes.DARK_GREEN	:
				fps > 60 	?	FormattingCodes.GREEN		:
				fps > 45	?	FormattingCodes.YELLOW		:
				fps > 30	?	FormattingCodes.GOLD		:
				fps > 15	?	FormattingCodes.RED			:
								FormattingCodes.DARK_RED	;
		
		WMinecraft.renderText("FPS: " + colorcode + Integer.toString(fps) + FormattingCodes.RESET, 3, mw.getScaledHeight() - 11);
		
		double compass = WPlayer.getCompass();
		WMinecraft.renderText("Compass: " + FormattingCodes.YELLOW + String.format("%.2f", compass) + FormattingCodes.RESET + " (" + FormattingCodes.YELLOW + WPlayer.getCompassDirection() + FormattingCodes.RESET + ")", 3, mw.getScaledHeight() - 21);
		
		// https://github.com/Lunatrius/InGame-Info-XML/blob/master/src/main/java/com/github/lunatrius/ingameinfo/tag/TagTime.java
		final long time = WMinecraft.getWorld().getTimeOfDay();
        final long hour = (time / 1000 + 6) % 24;
        final long minute = (time % 1000) * 60 / 1000;
        WMinecraft.renderText("Time: " + FormattingCodes.YELLOW + String.format("%02d:%02d", hour, minute) + FormattingCodes.RESET, 3, mw.getScaledHeight() - 31);
        
        
	}

}
