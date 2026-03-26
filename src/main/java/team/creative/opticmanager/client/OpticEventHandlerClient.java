package team.creative.opticmanager.client;

import net.minecraft.client.Minecraft;
import team.creative.opticmanager.OpticManager;

public class OpticEventHandlerClient {
    
    public static double defaultGammaSetting;
    
    public void renderStart() {
        Minecraft mc = Minecraft.getInstance();
        if (OpticManager.CONFIG.overrideBrightness) {
            defaultGammaSetting = mc.options.gamma().get();
            mc.options.gamma().set(OpticManager.CONFIG.getRealBrightness());
        }
    }
    
    public void render() {
        Minecraft mc = Minecraft.getInstance();
        if (OpticManager.CONFIG.overrideBrightness)
            mc.options.gamma().set(defaultGammaSetting);
        mc.options.chatVisibility().set(OpticManager.CONFIG.visibility);
    }
    
}
