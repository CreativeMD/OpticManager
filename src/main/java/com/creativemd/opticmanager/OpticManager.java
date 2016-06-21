package com.creativemd.opticmanager;

import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = OpticManager.modid, version = OpticManager.version, name = "OpticManager")
public class OpticManager{
	
	public static final String modid = "opticmanager";
	public static final String version = "0.1";
	
	public static boolean renderNameTag = true;
	public static float brightness = 1F;
	public static EntityPlayer.EnumChatVisibility visibilty = EnumChatVisibility.FULL;
	
	private static int dayDuration = 12000;
	private static int nightDuration = 12000;
	
	public static void setDuration(int dayDuration, int nightDuration)
	{
		OpticManager.dayDuration = dayDuration;
		OpticManager.nightDuration = nightDuration;
	}
	
	public static int getTotalDayDuration()
	{
		return dayDuration + nightDuration;
	}
	
	public static int getDayDuration()
	{
		return dayDuration;
	}
	
	public static int getNightDuration()
	{
		return nightDuration;
	}
	
	public static ModTab tab;
	
	
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			//FMLCommonHandler.instance().bus().register(new OpticEventHandler());
			MinecraftForge.EVENT_BUS.register(new OpticEventHandler());
		}
		
		tab = new ModTab("Optic", new ItemStack(Items.CLOCK));
		tab.addBranch(new OpticManagerBranch());
		TabRegistry.registerModTab(tab);
	}
}
