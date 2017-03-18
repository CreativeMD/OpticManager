package com.creativemd.opticmanager;

import com.creativemd.igcm.api.ConfigTab;

public class OpticIGCMLoader {
	
	public static void load()
	{
		ConfigTab.root.registerElement("optic", new OpticManagerBranch());
	}
	
}
