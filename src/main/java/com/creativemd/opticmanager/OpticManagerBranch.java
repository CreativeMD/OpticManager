package com.creativemd.opticmanager;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.BooleanSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.FloatSegment;
import com.creativemd.ingameconfigmanager.api.common.segment.FloatSliderSegment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
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
	}

}
