package net.mersid.standardhud;


public abstract class Module {
	protected boolean isEnabled = false;

	public String name;
	
	public Module(String name)
	{
		this.name = name;
	}
	
	protected void onEnable()
	{
		//MinecraftForge.EVENT_BUS.register(this);
	}
	
	protected void onDisable()
	{
		//MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	public void enable()
	{
		isEnabled = true;
		onEnable();
	}
	
	public void disable()
	{
		isEnabled = false;
		onDisable();
	}
}
