package com.github.mdcdi1315.mdex.mixin;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import net.minecraft.server.dedicated.DedicatedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedServer.class)
public class DedicatedMinecraftServerMixin
{
    @Inject(at = @At("HEAD") , method = "onServerExit")
    public void onServerExit(CallbackInfo ci)
    {
        MDEXBalmLayer.LOGGER.info("Stopping Mining Dimension: EX mod.");
        MDEXBalmLayer.DestroyModInstanceData();
    }
}
