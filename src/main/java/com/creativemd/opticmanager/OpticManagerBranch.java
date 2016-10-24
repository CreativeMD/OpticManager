package com.creativemd.opticmanager;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.common.branch.ConfigSegmentCollection;
import com.creativemd.igcm.api.common.segment.BooleanSegment;
import com.creativemd.igcm.api.common.segment.FloatSliderSegment;
import com.creativemd.igcm.api.common.segment.IntegerSegment;
import com.creativemd.igcm.api.common.segment.SelectSegment;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpticManagerBranch extends ConfigBranch{

	public OpticManagerBranch() {
		super("Optic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected Avatar getAvatar() {
		return new AvatarItemStack(new ItemStack(Items.CLOCK));
	}

	@Override
	public void loadCore() {
		
	}

	@Override
	public void createConfigSegments() {
		segments.add(new BooleanSegment("nametag", "Show Player Nametag", false));
		segments.add(new FloatSliderSegment("brightness", "Brightness", 0F, -1F, 1));
		segments.add(new SelectSegment("chat", "Chat Visibilty", 0, "Shown", "Only Commands", "Hidden"));
		segments.add(new IntegerSegment("dayDuration", "Day duration", 12000, 1, Integer.MAX_VALUE));
		segments.add(new IntegerSegment("nightDuration", "Night duration", 12000, 1, Integer.MAX_VALUE));
		segments.add(new FloatSliderSegment("dayBrightness", "Sunlight Factor", 1F, 0, 1).setToolTip("Will be used in calculating light value.", "Will effect monster spawning."));
		segments.add(new FloatSliderSegment("nightBrightness", "Moonlight Factor", 0F, 0, 1).setToolTip("Will be used in calculating light value.", "Will effect monster spawning."));
		segments.add(new FloatSliderSegment("dayBrightnessClient", "Sunlight Brightness", 1F, 0, 1).setToolTip("Client side only", "Does not effect monster spawning."));
		segments.add(new FloatSliderSegment("nightBrightnessClient", "Moonlight Brightness", 0.2F, 0, 1).setToolTip("Client side only", "Does not effect monster spawning."));
	}

	@Override
	public boolean needPacket() {
		return true;
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		OpticManager.renderPlayerNameTag = (Boolean) collection.getSegmentValue("nametag");
		OpticManager.brightness = (Float) collection.getSegmentValue("brightness");
		if(OpticManager.brightness > 0)
			OpticManager.brightness++;
		OpticManager.visibility = ((SelectSegment) collection.getSegmentByID("chat")).getIndex();
		OpticManager.setDuration((Integer) collection.getSegmentValue("dayDuration"), (Integer) collection.getSegmentValue("nightDuration"));
		OpticManager.dayBrightness = (Float) collection.getSegmentValue("dayBrightness");
		OpticManager.nightBrightness = (Float) collection.getSegmentValue("nightBrightness");
		OpticManager.dayBrightnessClient = (Float) collection.getSegmentValue("dayBrightnessClient");
		OpticManager.nightBrightnessClient = (Float) collection.getSegmentValue("nightBrightnessClient");
		if(FMLCommonHandler.instance().getSide().isClient())
			receiveClient();
	}
	
	@SideOnly(Side.CLIENT)
	public void receiveClient()
	{
		Minecraft.getMinecraft().gameSettings.chatVisibility = EnumChatVisibility.getEnumChatVisibility(OpticManager.visibility);
	}
	
}