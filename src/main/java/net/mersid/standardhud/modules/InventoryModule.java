package net.mersid.standardhud.modules;

import net.mersid.standardhud.Module;
import net.mersid.standardhud.compatibillity.FormattingCodes;
import net.mersid.standardhud.compatibillity.WMinecraft;
import net.mersid.standardhud.compatibillity.WPlayer;
import net.mersid.standardhud.events.OnRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import org.lwjgl.opengl.GL11;

public class InventoryModule extends Module {

	private static final int X_OFFSET = 3;
	private static final int Y_INCREMENT = 21;
	
	private ItemRenderer itemRenderer;
	private TextRenderer textRenderer;
	private ClientPlayerEntity player;
	private int y;
	
	public InventoryModule() {
		super("Inventory");
		OnRenderCallback.EVENT.register(() -> onRenderGUI());
	}
	
	public void onRenderGUI()
	{
		Window mw = MinecraftClient.getInstance().getWindow();
		y = mw.getScaledHeight() / 2 - 60;
		
		itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		textRenderer = MinecraftClient.getInstance().textRenderer;
		player = WMinecraft.getPlayer();
		
		renderSlot(EquipmentSlot.HEAD);
		renderSlot(EquipmentSlot.CHEST);
		renderSlot(EquipmentSlot.LEGS);
		renderSlot(EquipmentSlot.FEET);
		renderSlot(EquipmentSlot.MAINHAND);
		renderSlot(EquipmentSlot.OFFHAND);
	}
	
	/**
	 * Renders an armor or item slot on the side of the screen, with corresponding text.
	 * @param slot
	 */
	private void renderSlot(EquipmentSlot slot)
	{
		if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen) return;

		ItemStack itemStack = player.getEquippedStack(slot);
		GL11.glColor4f(1, 1, 1, 1);

		// If slot is not empty, render name and dura.
		if (!(itemStack.getItem() instanceof AirBlockItem))
		{
			DiffuseLighting.method_24211();
			itemRenderer.renderGuiItem(itemStack, X_OFFSET, y);

			if (itemStack.isDamageable())
			{
				itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, X_OFFSET, y);
			}
			else
			{
				if ((slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) && !(itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem))
				{
					int itemCountInInventory = itemsInInventory(itemStack.getItem());
					WMinecraft.renderText(Integer.toString(itemCountInInventory), X_OFFSET + 17 - textRenderer.getStringWidth(Integer.toString(itemCountInInventory)), y + 9);
				}

			}


			if (itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem)
			{
		        //GlStateManager.disableDepthTest();
				int arrowCount = WPlayer.getArrowCount();
				WMinecraft.renderText(Integer.toString(arrowCount), X_OFFSET + 17 - textRenderer.getStringWidth(Integer.toString(arrowCount)), y + 9);
			}
			DiffuseLighting.disable();
		
		

			WMinecraft.renderText(itemStack.getName().asFormattedString(), X_OFFSET + 20, y);
			
			if (itemStack.isDamageable())
			{
				int durability = itemStack.getMaxDamage() - itemStack.getDamage();
				int maxDurability = itemStack.getMaxDamage();
				double percent = ((double)durability) / maxDurability; // Double cast on durability to fix integer division. Between 0 and 1.
				
				String colorcode = 
						percent > 0.8	?	FormattingCodes.DARK_GREEN	:
						percent > 0.6 	?	FormattingCodes.GREEN		:
						percent > 0.4	?	FormattingCodes.YELLOW		:
						percent > 0.25	?	FormattingCodes.GOLD		:
						percent > 0.1	?	FormattingCodes.RED			:
											FormattingCodes.DARK_RED	;
				WMinecraft.renderText(colorcode + Integer.toString(durability) + "/" + Integer.toString(maxDurability) + FormattingCodes.RESET, X_OFFSET + 20, y + 10);
			}
			
		}
		
		
		y += Y_INCREMENT;
		
	}
	
	/**
	 * Returns a count of all items in the inventory for that item.
	 * @return
	 */
	private int itemsInInventory(Item item)
	{
		int itemCount = 0;
		PlayerInventory inventory = WMinecraft.getPlayer().inventory;
		for (int i = 0; i < inventory.getInvSize(); i++)
		{
			if (inventory.getInvStack(i).getItem().equals(item))
			{
				itemCount += inventory.getInvStack(i).getCount();
			}
		}
		return itemCount;
	}

}
