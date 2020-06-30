/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.mersid.standardhud.compatibillity;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public final class WMinecraft
{
	
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public static ClientPlayerEntity getPlayer()
	{
		return mc.player;
	}
	
	public static ClientWorld getWorld()
	{
		return mc.world;
	}
	
	public static TextRenderer getFontRenderer()
	{
		return mc.textRenderer;
	}

	public static void renderText(MatrixStack matrixStack, String string, int x, int y)
	{
		renderText(matrixStack, new LiteralText(string), x, y);
	}
	
	public static void renderText(MatrixStack matrixStack, Text text, int x, int y)
	{
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		getFontRenderer().drawWithShadow(matrixStack, text, x, y, 0xFFFFFF);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
