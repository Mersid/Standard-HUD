/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.mersid.standardhud.compatibillity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.world.World;

public final class WPlayer
{
	public static World getWorld(PlayerEntity player)
	{
		return player.world;
	}
	
	/**
	 * Returns the player's look direction between 0-360, without negatives or other weird stuff.
	 * @return
	 */
	public static double getCompass()
	{
		// Compass can go over 360, so modulo sets it between -360 and 360. If -360, do subtraction, because it does not automatically wrap around.
		double compass = WMinecraft.getPlayer().headYaw + 180; // Some reason the head's yaw was facing in the opposite direction.
		compass %= 360;
		compass = compass < 0 ? 360 + compass : compass;
		return compass;
	}

	/**
	 * Gets the elevation of the player's head, from 90 (straight up) to -90 (straight down)
	 * @return
	 */
	public static double getAngle()
	{
		return -WMinecraft.getPlayer().pitch;
	}
	
	/**
	 * Returns based on angle
	 * 
	 * @return
	 */
	public static String getCompassDirection()
	{
		double compass = getCompass();
		return
				compass >= 0 && compass <= 22.5			?	"N"		:
				compass >= 22.5 && compass <= 67.5		?	"NE"	:
				compass >= 67.5 && compass <= 112.5		?	"E"		:
				compass >= 112.5 && compass <= 157.5	?	"SE"	:
				compass	>= 157.5 && compass <= 202.5	?	"S"		:
				compass >= 202.5 && compass <= 247.5	?	"SW"	:
				compass >= 247.5 && compass <= 292.5	?	"W"		:
				compass >= 292.5 && compass <= 337.5	?	"NW"	:
				compass >= 337.5						?	"N"		:
															"ERROR"	;
	}
	
	/**
	 * Gets a count of all arrows in inventory
	 * @return
	 */
	public static int getArrowCount()
	{
		int arrows = 0;
		PlayerInventory inventory = WMinecraft.getPlayer().inventory;
		for (int i = 0; i < inventory.size(); i++)
		{
			if (inventory.getStack(i).getItem() instanceof ArrowItem)
			{
				arrows += inventory.getStack(i).getCount();
			}
		}
		return arrows;
	}

	public static int getPing()
	{
		MinecraftClient mc = MinecraftClient.getInstance();
		ClientPlayNetworkHandler clientPlayNetworkHandler = mc.getNetworkHandler();

		if (clientPlayNetworkHandler == null) // Single player
			return 0;

		if (mc.player == null)
			return -1;

		try {
			return clientPlayNetworkHandler.getPlayerListEntry(mc.player.getUuid()).getLatency();
		} catch (NullPointerException ignored) {
			// Return -1 on error below
		}
		return -1;
	}
}
