package com.github.mdcdi1315.mdex.api.saveddata;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;

/**
 * Defines the common header for all saved data provided by the mod. <br />
 * Can have a custom format version, as well as the data specifying the saved information. <br />
 * Thus, every decoding and encoding is performed through instances of this class.
 * @since 1.7.0
 */
public final class SavedDataCommonHeader
{
    private static final String VERSION_HEADER = "version" , DATA_HEADER = "data";
    public static final short INVALID_VERSION = -127;

    private short version;
    private CompoundTag tag;

    public static Codec<SavedDataCommonHeader> GetCodec()
    {
        return CodecUtils.CreateCodecDirect(
                Codec.SHORT.fieldOf(VERSION_HEADER).forGetter((h) -> h.version),
                CompoundTag.CODEC.fieldOf(DATA_HEADER).forGetter(SavedDataCommonHeader::GetData),
                SavedDataCommonHeader::CreateHeader
        );
    }

    public SavedDataCommonHeader(CompoundTag tag)
    {
        if (tag.contains(VERSION_HEADER , Tag.TAG_SHORT)) {
            version = tag.getShort(VERSION_HEADER);
        } else {
            throw new IncorrectSavedDataFormatException("Cannot find the saved data versioning field!");
        }
        if (tag.contains(DATA_HEADER , Tag.TAG_COMPOUND)) {
            this.tag = tag.getCompound(DATA_HEADER);
        } else {
            throw new IncorrectSavedDataFormatException("Cannot find the saved data data field!");
        }
    }

    private SavedDataCommonHeader() {
        version = 0;
        tag = null;
    }

    /**
     * Creates a new header, providing the version and the initial data to store. <br />
     * The data to store can be null.
     * @param version The custom version of your saved data.
     * @param data The data to save. Can be null or empty if not known in advance.
     * @return A new saved data header to be used to the {@link ISavedData} interface implementation.
     */
    public static SavedDataCommonHeader CreateHeader(short version, @MaybeNull CompoundTag data)
    {
        SavedDataCommonHeader s = new SavedDataCommonHeader();
        s.version = version;
        s.tag = data;
        return s;
    }

    public short GetVersion() {
        return version;
    }

    public void SetVersion(short newversion) {
        version = newversion;
    }

    /**
     * Gets a value whether the last saved data were failed to be saved. <br />
     * This method identifies that by whether the versioning field holds the invalid version.
     * @return A value whether the currently held version is invalid, meaning that the saved data were not saved in the last time due to an error.
     */
    public boolean IsInvalidVersion() {
        return version == INVALID_VERSION;
    }

    @NotNull
    public CompoundTag GetData() {
        return tag == null ? new CompoundTag() : tag;
    }

    public void SetData(@MaybeNull CompoundTag t) {
        tag = t;
    }

    /**
     * Creates a new {@link CompoundTag} that can be used to save the data to the drive.
     * @return A valid {@link CompoundTag} instance ready to be saved on disk.
     */
    public CompoundTag GenerateFinalData() {
        CompoundTag ct = new CompoundTag();
        ct.putShort(VERSION_HEADER , version);
        ct.put(DATA_HEADER , tag == null ? new CompoundTag() : tag);
        return ct;
    }
}
