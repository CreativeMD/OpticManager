package com.creativemd.opticmanager;

import java.util.Arrays;

import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class OpticManager extends DummyModContainer{
	
	public static final String modid = "opticmanager"; 
	
	public OpticManager() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = modid;
		meta.name = "OpticManager";
		meta.version = "0.1"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "CreativeMD";
		meta.authorList = Arrays.asList("CreativeMD");
		meta.description = "This mod adds the ability to talk with people in a server.";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	public static boolean renderNameTag = true;
	public static float brightness = 1F;
	public static EntityPlayer.EnumChatVisibility visibilty;
	
	//public static String[] settingNames = {"-500%", "-200%", "-100%", "-50%", "-25%", "100%", "150%", "200%", "500%"};
	//public static float[] settings = {};
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
	
	public ModTab tab;
	
	@Subscribe
	public void init(FMLInitializationEvent evt) {
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			FMLCommonHandler.instance().bus().register(new OpticEventHandler());
			MinecraftForge.EVENT_BUS.register(new OpticEventHandler());
		}
		
		tab = new ModTab("Optic", new ItemStack(Items.clock));
		tab.addBranch(new OpticManagerBranch());
		TabRegistry.registerModTab(tab);
	}
}
