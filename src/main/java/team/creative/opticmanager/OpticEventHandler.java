package team.creative.opticmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OpticEventHandler {
    
    public static final long vanillaDuration = 24000;
    public static final long vanillaHalfDuration = 12000;
    
    public long lastWorldTime = -1;
    public long lastTotalWorldTime = -1;
    public long realWorldTime;
    
    @OnlyIn(value = Dist.CLIENT)
    public long lastWorldTimeClient;
    @OnlyIn(value = Dist.CLIENT)
    public long lastTotalWorldTimeClient;
    @OnlyIn(value = Dist.CLIENT)
    public long realWorldTimeClient;
    
    @OnlyIn(value = Dist.CLIENT)
    public void initClient() {
        lastWorldTimeClient = -1;
        lastTotalWorldTimeClient = -1;
    }
    
    public void assignTime(long worldTime) {
        long days = worldTime / vanillaDuration;
        realWorldTime = days * OpticManager.CONFIG.getTotalDayDuration();
        if (isDayVanilla(worldTime))
            realWorldTime += (long) ((worldTime % vanillaDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.dayDuration);
        else
            realWorldTime += (long) (((worldTime % vanillaDuration) - vanillaHalfDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.nightDuration + OpticManager.CONFIG.dayDuration);
    }
    
    public boolean isDayVanilla(long time) {
        return isDay(time, vanillaHalfDuration, vanillaHalfDuration);
    }
    
    public boolean isDay(long time, long dayDuration, long nightDuration) {
        return time % (dayDuration + nightDuration) <= dayDuration;
    }
    
    public boolean shouldAffectWorld(World world) {
        return world.dimension().location().equals(DimensionType.OVERWORLD_LOCATION.location()) && world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }
    
    @SubscribeEvent
    @OnlyIn(value = Dist.CLIENT)
    public void tick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.phase == Phase.END && mc.level != null)
            changeTick(mc.level);
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public void assignTimeClient(long worldTime) {
        long days = worldTime / vanillaDuration;
        realWorldTimeClient = days * OpticManager.CONFIG.getTotalDayDuration();
        if (isDayVanilla(worldTime))
            realWorldTimeClient += (long) ((worldTime % vanillaDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.dayDuration);
        else
            realWorldTimeClient += (long) (((worldTime % vanillaDuration) - vanillaHalfDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.nightDuration + OpticManager.CONFIG.dayDuration);
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public void changeTick(World world) {
        if (!shouldAffectWorld(world))
            return;
        long expectedWorldTime = lastWorldTimeClient + 1L;
        if (expectedWorldTime == world.getDayTime()) {
            realWorldTimeClient++;
            int days = (int) (realWorldTimeClient / OpticManager.CONFIG.getTotalDayDuration());
            if (isDay(realWorldTimeClient, OpticManager.CONFIG.dayDuration, OpticManager.CONFIG.nightDuration))
                ((ClientWorld) world).setDayTime(days * vanillaDuration + (long) ((realWorldTimeClient % OpticManager.CONFIG
                        .getTotalDayDuration()) / (float) OpticManager.CONFIG.dayDuration * vanillaHalfDuration));
            else
                ((ClientWorld) world).setDayTime((long) (days * vanillaDuration + ((realWorldTimeClient % OpticManager.CONFIG
                        .getTotalDayDuration()) - OpticManager.CONFIG.dayDuration) / (float) OpticManager.CONFIG.nightDuration * vanillaHalfDuration + vanillaHalfDuration));
            ((ClientWorld) world).setGameTime(world.getGameTime() + expectedWorldTime - world.getDayTime());
        } else
            assignTimeClient(world.getDayTime());
        lastWorldTimeClient = world.getDayTime();
        lastTotalWorldTimeClient = world.getGameTime();
    }
    
    @SubscribeEvent
    public void tick(WorldTickEvent event) {
        if (event.phase == Phase.START) {
            World world = event.world;
            
            if (!shouldAffectWorld(world))
                return;
            
            long expectedWorldTime = lastWorldTime + 1L;
            if (expectedWorldTime == world.getDayTime()) {
                realWorldTime++;
                int days = (int) (realWorldTime / OpticManager.CONFIG.getTotalDayDuration());
                if (isDay(realWorldTime, OpticManager.CONFIG.dayDuration, OpticManager.CONFIG.nightDuration))
                    ((ServerWorld) world).setDayTime(days * vanillaDuration + (long) ((realWorldTime % OpticManager.CONFIG
                            .getTotalDayDuration()) / (float) OpticManager.CONFIG.dayDuration * vanillaHalfDuration));
                else
                    ((ServerWorld) world).setDayTime((long) (days * vanillaDuration + ((realWorldTime % OpticManager.CONFIG
                            .getTotalDayDuration()) - OpticManager.CONFIG.dayDuration) / (float) OpticManager.CONFIG.nightDuration * vanillaHalfDuration + vanillaHalfDuration));
            } else
                assignTime(world.getDayTime());
            lastWorldTime = world.getDayTime();
            lastTotalWorldTime = world.getGameTime();
        }
    }
    
    public static double defaultGammaSetting;
    
    @SubscribeEvent
    @OnlyIn(value = Dist.CLIENT)
    public void renderTick(RenderTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (OpticManager.CONFIG.overrideBrightness) {
            if (event.phase == Phase.START) {
                defaultGammaSetting = mc.options.gamma;
                mc.options.gamma = OpticManager.CONFIG.getRealBrightness();
            } else
                mc.options.gamma = defaultGammaSetting;
        }
        
        if (event.phase == Phase.END)
            mc.options.chatVisibility = OpticManager.CONFIG.visibility;
    }
    
    public static boolean shouldHideNames(Entity entity) {
        return !OpticManager.CONFIG.renderPlayerNameTag && entity instanceof PlayerEntity;
    }
    
}
