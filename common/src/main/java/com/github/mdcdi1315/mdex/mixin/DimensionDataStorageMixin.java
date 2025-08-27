package com.github.mdcdi1315.mdex.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;

@Mixin(DimensionDataStorage.class)
public abstract class DimensionDataStorageMixin
{
    @Unique
    private static final String BASE_MDEX_DIMFILES_NAMES = "MDCDI1315_MDEX";

    @Invoker("isGzip")
    protected abstract boolean IsGZip(PushbackInputStream inputStream) throws IOException;

    @Invoker("getDataFile")
    protected abstract File GetDataFile(String name);

    @Inject(at = @At("HEAD") , method = "readTagFromDisk" , cancellable = true)
    public void readTagFromDisk(String name, int levelVersion, CallbackInfoReturnable<CompoundTag> ci) throws IOException
    {
        if (name.startsWith(BASE_MDEX_DIMFILES_NAMES))
        {
            ci.setReturnValue(MDEX$ReadTagFromDisk(name));
        }
    }

    @Unique
    private CompoundTag MDEX$ReadTagFromDisk(String filename) throws IOException
    {
        try (
                InputStream inputstream = new FileInputStream(GetDataFile(filename));
                PushbackInputStream pushbackinputstream = new PushbackInputStream(new FastBufferedInputStream(inputstream), 2);
        ) {
            CompoundTag compoundtag;
            if (IsGZip(pushbackinputstream)) {
                compoundtag = NbtIo.readCompressed(pushbackinputstream);
            } else {
                try (DataInputStream datainputstream = new DataInputStream(pushbackinputstream)) {
                    compoundtag = NbtIo.read(datainputstream);
                }
            }
            return compoundtag;
        }
    }
}