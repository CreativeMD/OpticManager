package team.creative.opticmanager.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.opticmanager.OpticEventHandler;
import team.creative.opticmanager.OpticManager;

@Environment(EnvType.CLIENT)
@OnlyIn(Dist.CLIENT)
public class OpticEventHandlerClient {
    
    public long lastWorldTimeClient = -1;
    public long lastTotalWorldTimeClient = -1;
    public long realWorldTimeClient;
    
    public static double defaultGammaSetting;
    
    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            ClientLevel level = mc.level;
            if (!OpticEventHandler.shouldAffectWorld(level))
                return;
            long expectedWorldTime = lastWorldTimeClient + 1L;
            if (expectedWorldTime == level.getDayTime()) {
                realWorldTimeClient++;
                int days = (int) (realWorldTimeClient / OpticManager.CONFIG.getTotalDayDuration());
                if (OpticEventHandler.isDay(realWorldTimeClient, OpticManager.CONFIG.dayDuration, OpticManager.CONFIG.nightDuration))
                    level.setDayTime(days * OpticEventHandler.vanillaDuration + (long) ((realWorldTimeClient % OpticManager.CONFIG
                            .getTotalDayDuration()) / (float) OpticManager.CONFIG.dayDuration * OpticEventHandler.vanillaHalfDuration));
                else
                    level.setDayTime((long) (days * OpticEventHandler.vanillaDuration + ((realWorldTimeClient % OpticManager.CONFIG
                            .getTotalDayDuration()) - OpticManager.CONFIG.dayDuration) / (float) OpticManager.CONFIG.nightDuration * OpticEventHandler.vanillaHalfDuration + OpticEventHandler.vanillaHalfDuration));
                level.setGameTime(level.getGameTime() + expectedWorldTime - level.getDayTime());
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
