package com.creativemd.opticmanager;

import java.util.Arrays;

import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = OpticManager.modid, version = OpticManager.version, name = "OpticManager")
public class OpticManager{
	
	public static final String modid = "opticmanager";
	public static final String version = "0.1";
	
	public static boolean renderNameTag = true;
	public static float brightness = 1F;
	public static EntityPlayer.EnumChatVisibility visibilty;
	
	public ModTab tab;
	
	@EventHandler
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
