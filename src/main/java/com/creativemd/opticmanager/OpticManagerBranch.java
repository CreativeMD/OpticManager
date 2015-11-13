package com.creativemd.opticmanager;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.BooleanSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.FloatSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.FloatSliderSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.SelectSegment;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class OpticManagerBranch extends ConfigBranch{

	public OpticManagerBranch() {
		super("Optic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected Avatar getAvatar() {
		return new AvatarItemStack(new ItemStack(Items.clock));
	}

	@Override
	public void loadCore() {
		
	}

	@Override
	public void createConfigSegments() {
		segments.add(new BooleanSegment("nametag", "Show Nametag", false));
		segments.add(new FloatSliderSegment("brightness", "Brightness", 0F, -1F, 1));
		segments.add(new SelectSegment("chat", "Chat Visibilty", 0, "Shown", "Only Commands", "Hidden"));
	}

	@Override
	public boolean needPacket() {
		return true;
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		OpticManager.renderNameTag = (Boolean) collection.getSegmentValue("nametag");
		OpticManager.brightness = (Float) collection.getSegmentValue("brightness");
		if(OpticManager.brightness > 0)
			OpticManager.brightness++;
		OpticManager.visibilty = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(((SelectSegment) collection.getSegmentByID("chat")).getIndex());
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			receiveClient();
	}
	
	@SideOnly(Side.CLIENT)
	public void receiveClient()
	{
		Minecraft.getMinecraft().gameSettings.chatVisibility = OpticManager.visibilty;
	}
	
}
