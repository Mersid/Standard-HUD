package net.mersid.standardhud;

import net.fabricmc.api.ModInitializer;

public class StandardHUD implements ModInitializer {
	
	public ModuleLoader moduleLoader;
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		moduleLoader = new ModuleLoader();
	}
}
