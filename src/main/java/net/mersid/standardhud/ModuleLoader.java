package net.mersid.standardhud;

import net.mersid.standardhud.modules.InventoryModule;
import net.mersid.standardhud.modules.PotionModule;
import net.mersid.standardhud.modules.TCFModule;

public class ModuleLoader extends AbstractModuleLoader{

	TCFModule testModule = register(new TCFModule());
	InventoryModule inventoryModule = register(new InventoryModule());
	PotionModule potionModule = register(new PotionModule());

	public ModuleLoader()
	{
		loadModules();
	}

	private void loadModules() {
		for (Module module : getModules())
		{
			module.enable();
		}
		
	}
	

}
