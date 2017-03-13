package com.creativemd.opticmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.collection.parallel.ParIterableLike.Min;
import scala.tools.cmd.Meta.Opt;

public class OpticEventHandler {
	
	public static final long vanillaDuration = 24000;
	public static final long vanillaHalfDuration = 12000;
	
	public long lastWorldTime = -1;
	public long lastTotalWorldTime = -1;
	public long realWorldTime;
	
	@SideOnly(Side.CLIENT)
	public long lastWorldTimeClient;
	@SideOnly(Side.CLIENT)
	public long lastTotalWorldTimeClient;
	@SideOnly(Side.CLIENT)
	public long realWorldTimeClient;
	
	
	public OpticEventHandler() {
		if(FMLCommonHandler.instance().getSide().isClient())
			initClient();
	}
	
	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		lastWorldTimeClient = -1;
		lastTotalWorldTimeClient = -1;
	}
	
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
	@SideOnly(Side.CLIENT)
	public void tick(ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.phase == Phase.END && mc.world != null)
			changeTick(mc.world);
	}
	
	@SideOnly(Side.CLIENT)
	public void assignTimeClient(long worldTime)
	{
		if(isDayVanilla(worldTime))
			realWorldTimeClient = (long) (worldTime/(float)vanillaHalfDuration*OpticManager.getDayDuration());
		else
			realWorldTimeClient = (long) ((worldTime-vanillaHalfDuration)/(float)vanillaHalfDuration*OpticManager.getNightDuration()+OpticManager.getDayDuration());
	}
	
	@SideOnly(Side.CLIENT)
	public void changeTick(World world)
	{
		
		//long timeBefore = System.currentTimeMillis();
		long expectedWorldTime = lastWorldTimeClient+1L;
		if(expectedWorldTime == world.getWorldTime())
		{
			realWorldTimeClient++;
			realWorldTimeClient = realWorldTimeClient % OpticManager.getTotalDayDuration();
			//System.out.println(realWorldTime);
			if(isDay(realWorldTimeClient, OpticManager.getDayDuration(), OpticManager.getNightDuration()))
				world.setWorldTime((long) (realWorldTimeClient/(float)OpticManager.getDayDuration()*(float)vanillaHalfDuration));
			else
				world.setWorldTime((long) ((realWorldTimeClient-OpticManager.getDayDuration())/(float)OpticManager.getNightDuration()*vanillaHalfDuration+vanillaHalfDuration));
			world.setTotalWorldTime(world.getTotalWorldTime() + expectedWorldTime-world.getWorldTime());
		}else{
			//System.out.println("expected: " + expectedWorldTime + " given:" + world.getWorldTime());
			assignTimeClient(world.getWorldTime());
		}
		lastWorldTimeClient = world.getWorldTime();
		lastTotalWorldTimeClient = world.getTotalWorldTime();
		//System.out.println("Client tick time=" + lastWorldTimeClient);
		//System.out.println("needed time: " + (System.currentTimeMillis()-timeBefore));
	}
	
	@SubscribeEvent
	public void tick(WorldTickEvent event)
	{
		if(event.phase == Phase.START && event.world.provider.getDimension() == 0 && event.world.getGameRules().getBoolean("doDaylightCycle"))
		{
			World world = event.world;
			long expectedWorldTime = lastWorldTime+1L;
			if(expectedWorldTime == world.getWorldTime())
			{
				realWorldTime++;
				realWorldTime = realWorldTime % OpticManager.getTotalDayDuration();
				//System.out.println(realWorldTime);
				if(isDay(realWorldTime, OpticManager.getDayDuration(), OpticManager.getNightDuration()))
					world.setWorldTime((long) (realWorldTime/(float)OpticManager.getDayDuration()*vanillaHalfDuration));
				else
					world.setWorldTime((long) ((realWorldTime-OpticManager.getDayDuration())/(float)OpticManager.getNightDuration()*vanillaHalfDuration+vanillaHalfDuration));
				world.getWorldInfo().setWorldTotalTime(world.getTotalWorldTime() + expectedWorldTime-world.getWorldTime());
			}else{
				//System.out.println("expected: " + expectedWorldTime + " given:" + event.world.getWorldTime());
				assignTime(world.getWorldTime());
			}
			lastWorldTime = world.getWorldTime();
			lastTotalWorldTime = world.getTotalWorldTime();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderNameTag(RenderLivingEvent.Specials.Pre event)
	{
		if(!OpticManager.renderPlayerNameTag && event.getEntity() instanceof EntityPlayer)
			event.setCanceled(true);
	}
	
	public static float defaultGammaSetting;
	//public Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTick(RenderTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.phase == Phase.START)
		{
			defaultGammaSetting = mc.gameSettings.gammaSetting;
			mc.gameSettings.gammaSetting = OpticManager.brightness;
		}else{
			mc.gameSettings.gammaSetting = defaultGammaSetting;
			
			/*if(OpticManager.visibility )
				OpticManager.visibility = EnumChatVisibility.FULL;*/
			//if(OpticManager.visibility != null && mc.gameSettings.chatVisibility != OpticManager.visibility)
			mc.gameSettings.chatVisibility = EnumChatVisibility.getEnumChatVisibility(OpticManager.visibility);
		}
	}
	
}
