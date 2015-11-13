package com.creativemd.opticmanager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLivingEvent;

public class OpticEventHandler {
	
	@SubscribeEvent
	public void onRenderNameTag(RenderLivingEvent.Specials.Pre event)
	{
		if(!OpticManager.renderNameTag)
			event.setCanceled(true);
	}
	
	public static float defaultGammaSetting;
	public Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void renderTick(RenderTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			defaultGammaSetting = mc.gameSettings.gammaSetting;
			mc.gameSettings.gammaSetting = OpticManager.brightness;
		}else{
			mc.gameSettings.gammaSetting = defaultGammaSetting;
			
			if(mc.gameSettings.chatVisibility != OpticManager.visibilty)
				mc.gameSettings.chatVisibility = OpticManager.visibilty;
		}
	}
	
}
