package team.creative.opticmanager.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import team.creative.opticmanager.OpticEventHandler;
import team.creative.opticmanager.OpticManager;
import team.creative.opticmanager.mixin.ClientLevelAccessor;

public class OpticEventHandlerClient {
    
    public static boolean shouldAffectWorld(Level level) {
        return level.dimension().location().equals(BuiltinDimensionTypes.OVERWORLD.location()) && level instanceof ClientLevelAccessor c && c.getTickDayTime();
    }
    
    public long lastWorldTimeClient = -1;
    public long lastTotalWorldTimeClient = -1;
    public long realWorldTimeClient;
    
    public static double defaultGammaSetting;
    
    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            ClientLevel level = mc.level;
            if (!shouldAffectWorld(level))
                return;
            long expectedWorldTime = lastWorldTimeClient + 1L;
            if (expectedWorldTime == level.getDayTime()) {
                realWorldTimeClient++;
                int days = (int) (realWorldTimeClient / OpticManager.CONFIG.getTotalDayDuration());
                if (OpticEventHandler.isDay(realWorldTimeClient, OpticManager.CONFIG.dayDuration, OpticManager.CONFIG.nightDuration))
                    level.setTimeFromServer(level.getGameTime(), days * OpticEventHandler.vanillaDuration + (long) ((realWorldTimeClient % OpticManager.CONFIG
                            .getTotalDayDuration()) / (float) OpticManager.CONFIG.dayDuration * OpticEventHandler.vanillaHalfDuration), true);
                else
                    level.setTimeFromServer(level.getGameTime(), (long) (days * OpticEventHandler.vanillaDuration + ((realWorldTimeClient % OpticManager.CONFIG
                            .getTotalDayDuration()) - OpticManager.CONFIG.dayDuration) / (float) OpticManager.CONFIG.nightDuration * OpticEventHandler.vanillaHalfDuration + OpticEventHandler.vanillaHalfDuration),
                        true);
            } else
                assignTimeClient(level.getDayTime());
            lastWorldTimeClient = level.getDayTime();
            lastTotalWorldTimeClient = level.getGameTime();
        }
    }
    
    public void assignTimeClient(long worldTime) {
        long days = worldTime / OpticEventHandler.vanillaDuration;
        realWorldTimeClient = days * OpticManager.CONFIG.getTotalDayDuration();
        if (OpticEventHandler.isDayVanilla(worldTime))
            realWorldTimeClient += (long) ((worldTime % OpticEventHandler.vanillaDuration) / (float) OpticEventHandler.vanillaHalfDuration * OpticManager.CONFIG.dayDuration);
        else
            realWorldTimeClient += (long) (((worldTime % OpticEventHandler.vanillaDuration) - OpticEventHandler.vanillaHalfDuration) / (float) OpticEventHandler.vanillaHalfDuration * OpticManager.CONFIG.nightDuration + OpticManager.CONFIG.dayDuration);
    }
    
    public void renderStart() {
        Minecraft mc = Minecraft.getInstance();
        if (OpticManager.CONFIG.overrideBrightness) {
            defaultGammaSetting = mc.options.gamma().get();
            mc.options.gamma().set(OpticManager.CONFIG.getRealBrightness());
        }
    }
    
    public void render() {
        Minecraft mc = Minecraft.getInstance();
        if (OpticManager.CONFIG.overrideBrightness)
            mc.options.gamma().set(defaultGammaSetting);
        mc.options.chatVisibility().set(OpticManager.CONFIG.visibility);
    }
    
}
