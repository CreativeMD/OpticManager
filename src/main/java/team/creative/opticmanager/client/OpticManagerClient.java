package team.creative.opticmanager.client;

import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.ICreativeLoader;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.opticmanager.OpticManager;

public class OpticManagerClient {
    
    public static OpticEventHandlerClient EVENT;
    
    public static void onInitializeClient() {
        EVENT = new OpticEventHandlerClient();
        CreativeCoreClient.registerClientConfig(OpticManager.MODID);
        
        ICreativeLoader loader = CreativeCore.loader();
        loader.registerClientTick(EVENT::tick);
        loader.registerClientRenderStart(EVENT::renderStart);
        loader.registerClientRender(EVENT::render);
    }
    
}
