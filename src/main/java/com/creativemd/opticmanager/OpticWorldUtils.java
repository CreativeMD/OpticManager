package com.creativemd.opticmanager;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpticWorldUtils {
	
	public static float getSunBrightness(World world, float partialTicks) {
		float f = world.getCelestialAngle(partialTicks);
		float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F);
		f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) (f1 * (1.0D - world.getRainStrength(partialTicks) * 5.0F / 16.0D));
		f1 = (float) (f1 * (1.0D - world.getThunderStrength(partialTicks) * 5.0F / 16.0D));
		if (OpticManager.CONFIG.overrideDayNightBrightness)
			return f1 * (OpticManager.CONFIG.dayBrightness - OpticManager.CONFIG.nightBrightness) + OpticManager.CONFIG.nightBrightness;
		return f1;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getSunBrightnessClient(World world, float partialTicks) {
		float f = world.getCelestialAngle(partialTicks);
		float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.2F);
		f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) (f1 * (1.0D - world.getRainStrength(partialTicks) * 5.0F / 16.0D));
		f1 = (float) (f1 * (1.0D - world.getThunderStrength(partialTicks) * 5.0F / 16.0D));
		//return f1 * 0.8F + 0.2F;
		if (OpticManager.CONFIG.overrideDayNightRenderedBrightness)
			return f1 * (OpticManager.CONFIG.dayBrightnessRendered - OpticManager.CONFIG.nightBrightnessRendered) + OpticManager.CONFIG.nightBrightnessRendered;
		return f1;
	}
	
}
