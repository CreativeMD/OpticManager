package com.creativemd.opticmanager;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.FloatSliderSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.creativemd.igcm.api.segments.SelectSegment;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpticManagerBranch extends ConfigBranch{

	public OpticManagerBranch() {
		super("Optic", new ItemStack(Items.CLOCK));
	}
	
	@SideOnly(Side.CLIENT)
	public void receiveClient()
	{
		Minecraft.getMinecraft().gameSettings.chatVisibility = EnumChatVisibility.getEnumChatVisibility(OpticManager.visibility);
	}

	@Override
	public void createChildren() {
		
		registerElement("nametag", new BooleanSegment("Show Player Nametag", false));
		registerElement("brightness", new FloatSliderSegment("Brightness", 0F, -1F, 1));
		registerElement("chat", new SelectSegment("Chat Visibilty", 0, "Shown", "Only Commands", "Hidden"));
		registerElement("dayDuration", new IntegerSegment("Day duration", 12000, 1, Integer.MAX_VALUE));
		registerElement("nightDuration", new IntegerSegment("Night duration", 12000, 1, Integer.MAX_VALUE));
		registerElement("dayBrightness", new FloatSliderSegment("Sunlight Factor", 1F, 0, 1).setToolTip("Will be used in calculating light value.", "Will effect monster spawning."));
		registerElement("nightBrightness", new FloatSliderSegment("Moonlight Factor", 0F, 0, 1).setToolTip("Will be used in calculating light value.", "Will effect monster spawning."));
		registerElement("dayBrightnessClient", new FloatSliderSegment("Sunlight Brightness", 1F, 0, 1).setToolTip("Client side only", "Does not effect monster spawning."));
		registerElement("nightBrightnessClient", new FloatSliderSegment("Moonlight Brightness", 0.2F, 0, 1).setToolTip("Client side only", "Does not effect monster spawning."));
	}

	@Override
	public boolean requiresSynchronization() {
		return true;
	}

	@Override
	public void onRecieveFrom(Side side) {
		OpticManager.renderPlayerNameTag = (Boolean) getValue("nametag");
		OpticManager.brightness = (Float) getValue("brightness");
		if(OpticManager.brightness > 0)
			OpticManager.brightness++;
		OpticManager.visibility = ((SelectSegment) getChildByKey("chat")).getIndex();
		OpticManager.setDuration((Integer) getValue("dayDuration"), (Integer) getValue("nightDuration"));
		OpticManager.dayBrightness = (Float) getValue("dayBrightness");
		OpticManager.nightBrightness = (Float) getValue("nightBrightness");
		OpticManager.dayBrightnessClient = (Float) getValue("dayBrightnessClient");
		OpticManager.nightBrightnessClient = (Float) getValue("nightBrightnessClient");
		if(side.isClient())
			receiveClient();
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		
	}
	
}