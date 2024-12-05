package team.creative.opticmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(ClientLevel.class)
public interface ClientLevelAccessor {
    
    @Accessor
    public boolean getTickDayTime();
}
