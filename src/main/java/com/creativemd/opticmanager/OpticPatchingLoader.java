package com.creativemd.opticmanager;

import java.io.File;
import java.util.Map;

import com.creativemd.creativecore.transformer.TransformerNames;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;

public class OpticPatchingLoader implements IFMLLoadingPlugin {
	
	public static File location;

	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{OpticTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return OpticManager.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
