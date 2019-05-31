package com.creativemd.opticmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		if (FMLCommonHandler.instance().getSide().isClient())
			initClient();
	}
	
	@SideOnly(Side.CLIENT)
	public void initClient() {
		lastWorldTimeClient = -1;
		lastTotalWorldTimeClient = -1;
	}
	
	public void assignTime(long worldTime) {
		long days = worldTime / vanillaDuration;
		realWorldTime = days * OpticManager.getTotalDayDuration();
		if (isDayVanilla(worldTime))
			realWorldTime += (long) ((worldTime % vanillaDuration) / (float) vanillaHalfDuration * OpticManager.getDayDuration());
		else
			realWorldTime += (long) (((worldTime % vanillaDuration) - vanillaHalfDuration) / (float) vanillaHalfDuration * OpticManager.getNightDuration() + OpticManager.getDayDuration());
	}
	
	public boolean isDayVanilla(long time) {
		return isDay(time, vanillaHalfDuration, vanillaHalfDuration);
	}
	
	public boolean isDay(long time, long dayDuration, long nightDuration) {
		return time % (dayDuration + nightDuration) <= dayDuration;
	}
	
	public boolean shouldAffectWorld(World world) {
		return world.provider.getDimension() == 0 && world.getGameRules().getBoolean("doDaylightCycle");
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void tick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (event.phase == Phase.END && mc.world != null)
			changeTick(mc.world);
	}
	
	@SideOnly(Side.CLIENT)
	public void assignTimeClient(long worldTime) {
		long days = worldTime / vanillaDuration;
		realWorldTimeClient = days * OpticManager.getTotalDayDuration();
		if (isDayVanilla(worldTime))
			realWorldTimeClient += (long) ((worldTime % vanillaDuration) / (float) vanillaHalfDuration * OpticManager.getDayDuration());
		else
			realWorldTimeClient += (long) (((worldTime % vanillaDuration) - vanillaHalfDuration) / (float) vanillaHalfDuration * OpticManager.getNightDuration() + OpticManager.getDayDuration());
	}
	
	@SideOnly(Side.CLIENT)
	public void changeTick(World world) {
		if (!shouldAffectWorld(world))
			return;
		//long timeBefore = System.currentTimeMillis();
		long expectedWorldTime = lastWorldTimeClient + 1L;
		if (expectedWorldTime == world.getWorldTime()) {
			realWorldTimeClient++;
			int days = (int) (realWorldTimeClient / OpticManager.getTotalDayDuration());
			//System.out.println(realWorldTimeClient + "." + world.getWorldTime());
			if (isDay(realWorldTimeClient, OpticManager.getDayDuration(), OpticManager.getNightDuration()))
				world.setWorldTime(days * vanillaDuration + (long) ((realWorldTimeClient % OpticManager.getTotalDayDuration()) / (float) OpticManager.getDayDuration() * vanillaHalfDuration));
			else
				world.setWorldTime((long) (days * vanillaDuration + ((realWorldTimeClient % OpticManager.getTotalDayDuration()) - OpticManager.getDayDuration()) / (float) OpticManager.getNightDuration() * vanillaHalfDuration + vanillaHalfDuration));
			world.setTotalWorldTime(world.getTotalWorldTime() + expectedWorldTime - world.getWorldTime());
		} else {
			//System.out.println("expected: " + expectedWorldTime + " given:" + world.getWorldTime());
			assignTimeClient(world.getWorldTime());
		}
		lastWorldTimeClient = world.getWorldTime();
		lastTotalWorldTimeClient = world.getTotalWorldTime();
		//System.out.println("Client tick time=" + lastWorldTimeClient);
		//System.out.println("needed time: " + (System.currentTimeMillis()-timeBefore));
	}
	
	@SubscribeEvent
	public void tick(WorldTickEvent event) {
		if (event.phase == Phase.START) {
			World world = event.world;
			
			if (!shouldAffectWorld(world))
				return;
			
			long expectedWorldTime = lastWorldTime + 1L;
			if (expectedWorldTime == world.getWorldTime()) {
				realWorldTime++;
				int days = (int) (realWorldTime / OpticManager.getTotalDayDuration());
				if (isDay(realWorldTime, OpticManager.getDayDuration(), OpticManager.getNightDuration()))
					world.setWorldTime(days * vanillaDuration + (long) ((realWorldTime % OpticManager.getTotalDayDuration()) / (float) OpticManager.getDayDuration() * vanillaHalfDuration));
				else
					world.setWorldTime((long) (days * vanillaDuration + ((realWorldTime % OpticManager.getTotalDayDuration()) - OpticManager.getDayDuration()) / (float) OpticManager.getNightDuration() * vanillaHalfDuration + vanillaHalfDuration));
				world.getWorldInfo().setWorldTotalTime(world.getTotalWorldTime() + expectedWorldTime - world.getWorldTime());
			} else {
				//System.out.println("expected: " + expectedWorldTime + " given:" + event.world.getWorldTime());
				assignTime(world.getWorldTime());
			}
			lastWorldTime = world.getWorldTime();
			lastTotalWorldTime = world.getTotalWorldTime();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderNameTag(RenderLivingEvent.Specials.Pre event) {
		if (!OpticManager.renderPlayerNameTag && event.getEntity() instanceof EntityPlayer)
			event.setCanceled(true);
	}
	
	public static float defaultGammaSetting;
	//public Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTick(RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (OpticManager.overrideBrightness) {
			if (event.phase == Phase.START) {
				defaultGammaSetting = mc.gameSettings.gammaSetting;
				mc.gameSettings.gammaSetting = OpticManager.brightness;
			} else {
				mc.gameSettings.gammaSetting = defaultGammaSetting;
			}
		}
		
		if (event.phase == Phase.END)
			mc.gameSettings.chatVisibility = EnumChatVisibility.getEnumChatVisibility(OpticManager.visibility);
	}
	
}
