package team.creative.opticmanager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public class OpticEventHandler {
    
    public static final long vanillaDuration = 24000;
    public static final long vanillaHalfDuration = 12000;
    
    public static boolean shouldAffectWorld(Level world) {
        return world.dimension().location().equals(BuiltinDimensionTypes.OVERWORLD.location()) && world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }
    
    public static boolean isDayVanilla(long time) {
        return isDay(time, vanillaHalfDuration, vanillaHalfDuration);
    }
    
    public static boolean isDay(long time, long dayDuration, long nightDuration) {
        return time % (dayDuration + nightDuration) <= dayDuration;
    }
    
    public long lastWorldTime = -1;
    public long lastTotalWorldTime = -1;
    public long realWorldTime;
    
    public void assignTime(long worldTime) {
        long days = worldTime / vanillaDuration;
        realWorldTime = days * OpticManager.CONFIG.getTotalDayDuration();
        if (isDayVanilla(worldTime))
            realWorldTime += (long) ((worldTime % vanillaDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.dayDuration);
        else
            realWorldTime += (long) (((worldTime % vanillaDuration) - vanillaHalfDuration) / (float) vanillaHalfDuration * OpticManager.CONFIG.nightDuration + OpticManager.CONFIG.dayDuration);
    }
    
    public void levelTick(ServerLevel level) {
        if (!shouldAffectWorld(level))
            return;
        
        long expectedWorldTime = lastWorldTime + 1L;
        if (expectedWorldTime == level.getDayTime()) {
            realWorldTime++;
            int days = (int) (realWorldTime / OpticManager.CONFIG.getTotalDayDuration());
            if (isDay(realWorldTime, OpticManager.CONFIG.dayDuration, OpticManager.CONFIG.nightDuration))
                level.setDayTime(days * vanillaDuration + (long) ((realWorldTime % OpticManager.CONFIG
                        .getTotalDayDuration()) / (float) OpticManager.CONFIG.dayDuration * vanillaHalfDuration));
            else
                level.setDayTime((long) (days * vanillaDuration + ((realWorldTime % OpticManager.CONFIG
                        .getTotalDayDuration()) - OpticManager.CONFIG.dayDuration) / (float) OpticManager.CONFIG.nightDuration * vanillaHalfDuration + vanillaHalfDuration));
        } else
            assignTime(level.getDayTime());
        lastWorldTime = level.getDayTime();
        lastTotalWorldTime = level.getGameTime();
    }
    
}
