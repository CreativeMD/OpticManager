package team.creative.opticmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.fml.common.Mod;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.ICreativeLoader;
import team.creative.creativecore.client.ClientLoader;
import team.creative.creativecore.common.CommonLoader;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.opticmanager.client.OpticManagerClient;

@Mod(OpticManager.MODID)
public class OpticManager implements CommonLoader, ClientLoader {
    
    public static final Logger LOGGER = LogManager.getLogger(OpticManager.MODID);
    public static final String MODID = "opticmanager";
    public static OpticManagerConfig CONFIG;
    
    public OpticManager() {
        ICreativeLoader loader = CreativeCore.loader();
        loader.register(this);
        loader.registerClient(this);
    }
    
    @Override
    public void onInitialize() {
        CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new OpticManagerConfig());
    }
    
    @Override
    public void onInitializeClient() {
        OpticManagerClient.onInitializeClient();
    }
    
}
