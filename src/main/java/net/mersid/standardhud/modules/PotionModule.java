package net.mersid.standardhud.modules;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import net.mersid.standardhud.Module;
import net.mersid.standardhud.compatibillity.WMinecraft;
import net.mersid.standardhud.events.OnRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

import java.util.List;

public class PotionModule extends Module {
	
	private static final int X_OFFSET = 3; // Offset from the right of the screen
	private static final int Y_INCREMENT = 21; // Increments upwards
	
	private TextRenderer textRenderer;
	private ClientPlayerEntity player;
	private StatusEffectSpriteManager statusEffectSpriteManager;
	private TextureManager textureManager;
	private Window window;
	
	private int y;

	public PotionModule() {
		super("Potion");
		OnRenderCallback.EVENT.register(this::onRenderGUI);
	}

	private void onRenderGUI(MatrixStack matrixStack, float partialTicks)
	{
		textRenderer = WMinecraft.getFontRenderer();
		player = WMinecraft.getPlayer();
		statusEffectSpriteManager = MinecraftClient.getInstance().getStatusEffectSpriteManager();
		textureManager = MinecraftClient.getInstance().getTextureManager();
		window = MinecraftClient.getInstance().getWindow();
		y = window.getScaledHeight() - 18; // Because 18 is the sprite size.
		
		// A lot of the following code is taken from AbstractInventoryScreen.class.
		List<StatusEffectInstance> sortedStatusEffects = Ordering.natural().sortedCopy(player.getStatusEffects());
		StatusEffectSpriteManager statusEffectSpriteManager = MinecraftClient.getInstance().getStatusEffectSpriteManager();
		//GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);

		for (StatusEffectInstance effect : sortedStatusEffects)
		{
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			// Render sprite
			//textureManager.bindTexture(SpriteAtlasTexture.STATUS_EFFECT_ATLAS_TEX);
			StatusEffect statusEffect = effect.getEffectType();
			Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
			MinecraftClient.getInstance().getTextureManager().bindTexture(sprite.getAtlas().getId());
			DrawableHelper.drawSprite(matrixStack, window.getScaledWidth() - X_OFFSET - 18, y, 0, 18, 18, statusEffectSpriteManager.getSprite(effect.getEffectType()));
			
			// Render text. effectEnchantmentLevel shows the Roman numerals after the name, but only if the level is between 2 and 10.
			// Code portially borrowed from AbstractInventoryScreen.drawStatusEffectDescriptions
			String effectName = I18n.translate(effect.getTranslationKey());
			String effectEnchantmentLevel = effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9 ? " " + I18n.translate("enchantment.level." + (effect.getAmplifier() + 1)) : "";
			String effectDurationStr = StatusEffectUtil.durationToString(effect, 1.0f);

			
			WMinecraft.renderText(matrixStack, effectName + effectEnchantmentLevel, window.getScaledWidth() - X_OFFSET - 23 - textRenderer.getWidth(effectName) - textRenderer.getWidth((effectEnchantmentLevel)), y - 4); // Some magic numbers for fine tuning... sorry.
			WMinecraft.renderText(matrixStack, effectDurationStr, window.getScaledWidth() - X_OFFSET - 23 - textRenderer.getWidth(effectDurationStr), y + 6);
			
			
			y -= Y_INCREMENT;
		}

		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GL11.glPopMatrix();
	}

}
