package com.creativemd.opticmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import scala.tools.cmd.Meta.Opt;

public class OpticEventHandler {
	
	public static final long vanillaDuration = 24000;
	public static final long vanillaHalfDuration = 12000;
	
	public long lastWorldTime = -1;
	public long lastTotalWorldTime = -1;
	public long realWorldTime;
	
	public void assignTime(long worldTime)
	{
		if(isDayVanilla(worldTime))
			realWorldTime = (long) (worldTime/(float)vanillaHalfDuration*OpticManager.getDayDuration());
		else
			realWorldTime = (long) ((worldTime-vanillaHalfDuration)/(float)vanillaHalfDuration*OpticManager.getNightDuration()+OpticManager.getDayDuration());
	}
	
	public boolean isDayVanilla(long time)
	{
		return isDay(time, vanillaHalfDuration, vanillaHalfDuration);
	}
	
	public boolean isDay(long time, long dayDuration, long nightDuration)
	{
		return time <= dayDuration;
	}
	
	@SubscribeEvent
	public void tick(WorldTickEvent event)
	{
		if(event.phase == Phase.START && event.world.provider.getDimension() == 0 && event.world.getGameRules().getBoolean("doDaylightCycle"))
		{
			//long timeBefore = System.currentTimeMillis();
			long expectedWorldTime = lastWorldTime+1L;
			if(expectedWorldTime == event.world.getWorldTime())
			{
				realWorldTime++;
				realWorldTime = realWorldTime % OpticManager.getTotalDayDuration();
				//System.out.println(realWorldTime);
				if(isDay(realWorldTime, OpticManager.getDayDuration(), OpticManager.getNightDuration()))
					event.world.setWorldTime((long) (realWorldTime/(float)OpticManager.getDayDuration()*vanillaHalfDuration));
				else
					event.world.setWorldTime((long) ((realWorldTime-OpticManager.getDayDuration())/(float)OpticManager.getNightDuration()*vanillaHalfDuration+vanillaHalfDuration));
				event.world.setTotalWorldTime(event.world.getTotalWorldTime() + expectedWorldTime-event.world.getWorldTime());
			}else{
				//System.out.println("expected: " + expectedWorldTime + " given:" + event.world.getWorldTime());
				assignTime(event.world.getWorldTime());
			}
			lastWorldTime = event.world.getWorldTime();
			lastTotalWorldTime = event.world.getTotalWorldTime();
			//System.out.println("needed time: " + (System.currentTimeMillis()-timeBefore));
		}
	}
	
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
			
			if(OpticManager.visibilty == null)
				OpticManager.visibilty = EnumChatVisibility.FULL;
			if(OpticManager.visibilty != null && mc.gameSettings.chatVisibility != OpticManager.visibilty)
				mc.gameSettings.chatVisibility = OpticManager.visibilty;
		}
	}
	
}
