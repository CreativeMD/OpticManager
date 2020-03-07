package com.creativemd.opticmanager;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.sync.ConfigSynchronization;

import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;

public class OpticManagerConfig {
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public boolean renderPlayerNameTag = true;
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public EnumChatVisibility visibility = EnumChatVisibility.FULL;
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public boolean overrideBrightness = false;
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	@CreativeConfig.DecimalRange(min = -1, max = 1)
	public float brightness = 0F;
	
	public float getRealBrightness() {
		if (brightness > 0)
			return brightness + 1;
		return brightness;
	}
	
	@CreativeConfig
	public boolean overrideDayNightBrightness = true;
	
	@CreativeConfig
	@CreativeConfig.DecimalRange(min = 0, max = 1)
	public float dayBrightness = 1F;
	@CreativeConfig
	@CreativeConfig.DecimalRange(min = 0, max = 1)
	public float nightBrightness = 0F;
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public boolean overrideDayNightRenderedBrightness = true;
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	@CreativeConfig.DecimalRange(min = 0, max = 1)
	public float dayBrightnessRendered = 1F;
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	@CreativeConfig.DecimalRange(min = 0, max = 1)
	public float nightBrightnessRendered = 0.2F;
	
	@CreativeConfig
	public int dayDuration = 12000;
	@CreativeConfig
	public int nightDuration = 12000;
	
	public int getTotalDayDuration() {
		return dayDuration + nightDuration;
	}
}
