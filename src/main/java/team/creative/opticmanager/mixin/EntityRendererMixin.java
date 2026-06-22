package team.creative.opticmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.creative.opticmanager.OpticManager;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    
    @Inject(at = @At("HEAD"), method = "extractNameTags(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FDD)V", cancellable = true)
    protected final void extractNameTags(Entity entity, EntityRenderState state, float partialTicks, double nameTagDistance, double belowNameDistance, CallbackInfo info) {
        if (!OpticManager.CONFIG.renderPlayerNameTag && entity instanceof Player)
            info.cancel();
    }
    
}
