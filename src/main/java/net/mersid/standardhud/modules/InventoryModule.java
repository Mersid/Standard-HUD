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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
		OnRenderCallback.EVENT.register(this::onRenderGUI);
	}
	
	public void onRenderGUI(MatrixStack matrixStack, float partialTicks)
	{
		Window mw = MinecraftClient.getInstance().getWindow();
		y = mw.getScaledHeight() / 2 - 60;
		
		itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		textRenderer = MinecraftClient.getInstance().textRenderer;
		player = WMinecraft.getPlayer();
		
		renderSlot(matrixStack, EquipmentSlot.HEAD);
		renderSlot(matrixStack, EquipmentSlot.CHEST);
		renderSlot(matrixStack, EquipmentSlot.LEGS);
		renderSlot(matrixStack, EquipmentSlot.FEET);
		renderSlot(matrixStack, EquipmentSlot.MAINHAND);
		renderSlot(matrixStack, EquipmentSlot.OFFHAND);
	}
	
	/**
	 * Renders an armor or item slot on the side of the screen, with corresponding text.
	 * @param slot
	 */
	private void renderSlot(MatrixStack matrixStack, EquipmentSlot slot)
	{
		if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen) return;

		ItemStack itemStack = player.getEquippedStack(slot);
		GL11.glColor4f(1, 1, 1, 1);

		// If slot is not empty, render name and dura.
		if (!(itemStack.getItem() instanceof AirBlockItem))
		{
			DiffuseLighting.enable();
			itemRenderer.renderGuiItemIcon(itemStack, X_OFFSET, y);

			if (itemStack.isDamageable())
			{
				itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, X_OFFSET, y);
			}
			else
			{
				if ((slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) && !(itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem))
				{
					int itemCountInInventory = itemsInInventory(itemStack.getItem());
					WMinecraft.renderText(matrixStack, Integer.toString(itemCountInInventory), X_OFFSET + 17 - textRenderer.getWidth(Integer.toString(itemCountInInventory)), y + 9);
				}

			}


			if (itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem)
			{
		        //GlStateManager.disableDepthTest();
				int arrowCount = WPlayer.getArrowCount();
				WMinecraft.renderText(matrixStack, Integer.toString(arrowCount), X_OFFSET + 17 - textRenderer.getWidth(Integer.toString(arrowCount)), y + 9);
			}
			DiffuseLighting.disable();
		
		

			WMinecraft.renderText(matrixStack, ((MutableText)itemStack.getName()).formatted(itemStack.getRarity().formatting), X_OFFSET + 20, y);

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
				WMinecraft.renderText(matrixStack, colorcode + Integer.toString(durability) + "/" + Integer.toString(maxDurability) + FormattingCodes.RESET, X_OFFSET + 20, y + 10);
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
		for (int i = 0; i < inventory.size(); i++)
		{
			if (inventory.getStack(i).getItem().equals(item))
			{
				itemCount += inventory.getStack(i).getCount();
			}
		}
		return itemCount;
	}

}
