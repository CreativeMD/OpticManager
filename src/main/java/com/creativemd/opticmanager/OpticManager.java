package com.creativemd.opticmanager;

import java.util.Arrays;

import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class OpticManager extends DummyModContainer {
	
	public static final String modid = "opticmanager";
	public static final String version = "1.0";
	
	public static OpticManagerConfig CONFIG;
	
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
	
	@Override
	public String getGuiClassName() {
		return "com.creativemd.opticmanager.OpticManagerSettings";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void init(FMLInitializationEvent evt) {
		CreativeConfigRegistry.ROOT.registerValue(modid, CONFIG = new OpticManagerConfig());
		MinecraftForge.EVENT_BUS.register(new OpticEventHandler());
	}
}
