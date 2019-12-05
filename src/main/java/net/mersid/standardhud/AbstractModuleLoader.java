package net.mersid.standardhud;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModuleLoader {
	private List<Module> modules = new ArrayList<Module>();
	
	protected <T extends Module> T register(T module)
	{
		modules.add(module);
		return module;
	}
	
	public Module getModuleWithName(String name)
	{
		for (Module module : modules)
		{
			if (module.name.equals(name))
			{
				return module;
			}
		}
		return null;
	}
	
	public List<Module> getModules()
	{
		return modules;
	}
}
