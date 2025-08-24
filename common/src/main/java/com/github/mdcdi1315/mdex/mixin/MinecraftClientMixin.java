package com.github.mdcdi1315.mdex.mixin;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import net.minecraft.client.Minecraft;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin
{
    @Inject(at = @At("HEAD") , method = "destroy")
    public void destroy(CallbackInfo ci)
    {
        MDEXBalmLayer.LOGGER.info("Stopping Mining Dimension: EX mod.");
        MDEXBalmLayer.DestroyModInstanceData();
    }
}
