package team.creative.opticmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;

@Mod(OpticManager.MODID)
public class OpticManager {
    
    public static final Logger LOGGER = LogManager.getLogger(OpticManager.MODID);
    public static final String MODID = "opticmanager";
    public static OpticManagerConfig CONFIG;
    public static OpticEventHandler EVENTS;
    
    public OpticManager() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
    }
    
    @OnlyIn(value = Dist.CLIENT)
    private void client(final FMLClientSetupEvent event) {
        CreativeCoreClient.registerClientConfig(MODID);
        EVENTS.initClient();
    }
    
    private void init(final FMLCommonSetupEvent event) {
        CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new OpticManagerConfig());
        MinecraftForge.EVENT_BUS.register(EVENTS = new OpticEventHandler());
    }
    
}
