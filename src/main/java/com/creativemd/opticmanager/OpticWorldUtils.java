package com.creativemd.opticmanager;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpticWorldUtils {
	
	public static float getSunBrightness(World world, float brightness) {
		float f = world.getCelestialAngle(brightness);
		float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F);
		f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) ((double) f1 * (1.0D - (double) (world.getRainStrength(brightness) * 5.0F) / 16.0D));
		f1 = (float) ((double) f1 * (1.0D - (double) (world.getThunderStrength(brightness) * 5.0F) / 16.0D));
		
		return f1 * (OpticManager.dayBrightness - OpticManager.nightBrightness) + OpticManager.nightBrightness;
	}
	
	@SideOnly(Side.CLIENT)
	public static float getSunBrightnessClient(World world, float brightness) {
		float f = world.getCelestialAngle(brightness);
		float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.2F);
		f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
		f1 = 1.0F - f1;
		f1 = (float) ((double) f1 * (1.0D - (double) (world.getRainStrength(brightness) * 5.0F) / 16.0D));
		f1 = (float) ((double) f1 * (1.0D - (double) (world.getThunderStrength(brightness) * 5.0F) / 16.0D));
		//return f1 * 0.8F + 0.2F;
		return f1 * (OpticManager.dayBrightnessClient - OpticManager.nightBrightnessClient) + OpticManager.nightBrightnessClient;
	}
	
}
