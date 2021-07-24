package team.creative.opticmanager;

import net.minecraft.world.entity.player.ChatVisiblity;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;

public class OpticManagerConfig {
    
    @CreativeConfig(type = ConfigSynchronization.CLIENT)
    public boolean renderPlayerNameTag = true;
    
    @CreativeConfig(type = ConfigSynchronization.CLIENT)
    public ChatVisiblity visibility = ChatVisiblity.FULL;
    
    @CreativeConfig(type = ConfigSynchronization.CLIENT)
    public boolean overrideBrightness = false;
    @CreativeConfig(type = ConfigSynchronization.CLIENT)
    @CreativeConfig.DecimalRange(min = -1, max = 1)
    public double brightness = 0F;
    
    public double getRealBrightness() {
        if (brightness > 0)
            return brightness + 1;
        return brightness;
    }
    
    @CreativeConfig
    public int dayDuration = 12000;
    @CreativeConfig
    public int nightDuration = 12000;
    
    public int getTotalDayDuration() {
        return dayDuration + nightDuration;
    }
}
