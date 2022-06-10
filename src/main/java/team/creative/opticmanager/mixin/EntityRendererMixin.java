package team.creative.opticmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.creative.opticmanager.OpticManager;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    
    @Inject(at = @At("HEAD"),
            method = "renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            cancellable = true)
    public void renderNameTag(Entity entity, CallbackInfoReturnable<Void> callback) {
        if (!OpticManager.CONFIG.renderPlayerNameTag && entity instanceof Player)
            callback.cancel();
    }
    
}
