package com.creativemd.opticmanager;

import java.util.Arrays;

import com.creativemd.igcm.api.core.TabRegistry;
import com.creativemd.igcm.api.tab.ModTab;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class OpticManager extends DummyModContainer {
	
	public static final String modid = "opticmanager";
	public static final String version = "1.0";
	
	public static boolean renderPlayerNameTag = true;
	public static float brightness = 1F;
	
	/**0: FULL, 1:SYSTEM, 2:HIDDEN*/
	public static int visibility = 0;
	//public static EntityPlayer.EnumChatVisibility visibilty = EnumChatVisibility.FULL;
	
	public static float dayBrightness = 1F;
	public static float nightBrightness = 1F;
	
	public static float dayBrightnessClient = 1F;
	public static float nightBrightnessClient = 1F;
	
	private static int dayDuration = 12000;
	private static int nightDuration = 12000;
	
	public OpticManager() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = modid;
		meta.name = "OpticManager";
		meta.version = version; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "CreativeMD";
		meta.authorList = Arrays.asList("CreativeMD");
		meta.description = "";
		meta.url = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
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
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void init(FMLInitializationEvent evt) {
		
		MinecraftForge.EVENT_BUS.register(new OpticEventHandler());
		
		tab = new ModTab("Optic", new ItemStack(Items.CLOCK));
		tab.addBranch(new OpticManagerBranch());
		TabRegistry.registerModTab(tab);
	}
}
