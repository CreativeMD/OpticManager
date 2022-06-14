package team.creative.opticmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
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
    public static OpticEventHandler EVENTS;
    
    public OpticManager() {
        ICreativeLoader loader = CreativeCore.loader();
        loader.register(this);
        loader.registerClient(this);
    }
    
    @Override
    public void onInitialize() {
        ICreativeLoader loader = CreativeCore.loader();
        CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new OpticManagerConfig());
        EVENTS = new OpticEventHandler();
        loader.registerLevelTickStart(EVENTS::levelTick);
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(value = Dist.CLIENT)
    public void onInitializeClient() {
        OpticManagerClient.onInitializeClient();
    }
    
}
