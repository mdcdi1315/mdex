package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.AllowNull;

import com.github.mdcdi1315.basemodslib.BaseModsLib;
import com.github.mdcdi1315.basemodslib.codecs.ListCodec;
import com.github.mdcdi1315.basemodslib.codecs.CodecUtils;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.util.MDEXException;
import com.github.mdcdi1315.mdex.dco_logic.Compilable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Base class for all modded feature configurations.
 * This class implementation adopts the Distributed Compilation Object logic.
 */
public class ModdedFeatureConfiguration<TD extends IModdedFeatureConfigurationDetails>
    implements FeatureConfiguration , Compilable
{
    private static final byte STATE_NONE = 0;
    private static final byte STATE_IS_INVALID = 1 << 0;
    private static final byte STATE_MODLIST_IS_VALID = 1 << 1;
    private static final byte STATE_MODLIST_IS_INVALID = 1 << 2;
    private static final byte STATE_IS_COMPILED = 1 << 3;

    /**
     A list of Mod ID's required to generate the feature itself.
     */
    @AllowNull
    public List<String> ModIds;
    /**
     * The information allocated by the feature configuration.
     */
    @AllowNull
    public TD Details;
    private byte state;

    private void STATE_AddFlag(byte flag)
    {
        state |= flag;
    }

    private void STATE_RemoveFlag(byte flag) { state &= ~flag; }

    private boolean STATE_HasFlag(byte flag)
    {
        return (state & flag) != 0;
    }

    private boolean STATE_HasFlag(byte flg1 , byte flg2)
    {
        int compiledflags = flg1 | flg2;
        return (state & compiledflags) == compiledflags;
    }

    public static <TD extends IModdedFeatureConfigurationDetails> Codec<ModdedFeatureConfiguration<TD>> GetCodec(MapCodec<TD> map_codec)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(map_codec, "map_codec");
        return CodecUtils.CreateCodecDirect(
                new ListCodec<>(Codec.STRING).fieldOf("modids").forGetter((cfg) -> cfg.ModIds),
                map_codec.forGetter((cfg) -> cfg.Details),
                ModdedFeatureConfiguration::new
        );
    }

    /**
     * Creates a default {@link ModdedFeatureConfiguration} instance.
     */
    public ModdedFeatureConfiguration()
    {
        // Create an array list if this is created through code.
        ModIds = new ArrayList<>();
        // No flags are being referenced at the early beginning
        state = STATE_NONE;
    }

    /**
     * Creates a new {@link ModdedFeatureConfiguration} from an already-defined mod id list.
     * The list is validated for null. If null, an empty list is created.
     * @param modids The mod list to append to ModIds field before returning.
     */
    public ModdedFeatureConfiguration(List<String> modids)
    {
        state = STATE_NONE;
        ModIds = Objects.requireNonNullElseGet(modids, List::of);
    }

    public ModdedFeatureConfiguration(List<String> modids, TD instance)
    {
        state = STATE_NONE;
        Details = instance;
        ModIds = Objects.requireNonNullElseGet(modids, List::of);
        Compile();
    }

    /**
     * Gets a boolean whether the mod id list specified in {@literal ModIds} field is valid and the feature config is not 'stale'.
     * <p>
     *     Note that if the list is valid or not, the list is destroyed after the final conclusion is reached.
     * </p>
     * @return true when the required mod list is valid, or false when the list is not valid.
     */
    public final boolean GetAllModIdsAreLoaded()
    {
        if (STATE_HasFlag(STATE_MODLIST_IS_VALID)) { return true; }
        if (STATE_HasFlag(STATE_MODLIST_IS_INVALID)) { return false; }
        // Discover whether the specified mods are loaded.
        // The search runs only once, and then the id list is invalidated.
        // The result of this search pass is stored by the state flags and kept there until the end of the instance life.
        for (String mod : ModIds)
        {
            if (!BaseModsLib.IsModLoaded(mod)) {
                STATE_AddFlag(STATE_MODLIST_IS_INVALID);
                if (MDEXModInstance.LoggingFlags.Feature())
                {
                    MDEXModInstance.LOGGER.warn("Feature configuration of type {} will not be applied to the current world because the mod named as '{}' is not present in the mod lifecycle." , getClass().getName() , mod);
                }
                ModIds = null;
                return false;
            }
        }
        STATE_AddFlag(STATE_MODLIST_IS_VALID);
        ModIds = null;
        return true;
    }

    /**
     * Returns a value indicating that the current configuration instance is invalid.
     * @return true when the current instance is invalid; during this stage, all public
     * config fields are not considered to be safe to be accessed because the internal
     * destroyer may be running. If this returns false, then the instance is still valid
     * and can be manipulated.
     */
    public boolean IsInvalid()
    {
        return STATE_HasFlag(STATE_IS_INVALID) || STATE_HasFlag(STATE_MODLIST_IS_INVALID);
    }

    /**
     * Runs the implementation of compileConfigData to compile and transforms the configuration data for a small mem footprint as possible.
     * <p>
     *     Automatically invalidates the config if the instance is determined as invalid during compilation.
     * </p>
     * <p>
     *     This method should be run in the constructor context. This allows to run on the render thread only once and avoid the concurrency issues introduced by the chunk builder.
     * </p>
     */
    public final void Compile()
    {
        try {
            if (STATE_HasFlag(STATE_IS_COMPILED)) { return; }
            if (MDEXModInstance.LoggingFlags.Feature())
            {
                MDEXModInstance.LOGGER.info("Attempting to compile DCO {} with hash code {}." , getClass().getName() , hashCode());
            }
            if (GetAllModIdsAreLoaded()) {
                Details.Compile();
            } else {
                MDEXModInstance.LOGGER.warn("Cannot compile DCO with hash code {} because the mod list is invalid." , hashCode());
                SetConfigAsInvalid();
                return;
            }
            STATE_AddFlag(STATE_IS_COMPILED);
            if (MDEXModInstance.LoggingFlags.Feature()) {
                MDEXModInstance.LOGGER.info("DCO with hash code {} succeeded with compilation." , hashCode());
            }
        } catch (MDEXException e) {
            SetConfigAsInvalid();
            MDEXModInstance.LOGGER.error("Cannot compile an instance of the DCO '{}' because an exception of type {} has been occurred: {}", getClass().getName() , e.getClass().getName() , e.getMessage());
        }
    }

    /**
     * Gets a value whether this configuration is ready to be used directly by the feature's logic.
     * @return true, if the current instance can be used by a feature logic; otherwise, false.
     */
    public final boolean IsCompiled()
    {
        return STATE_HasFlag(STATE_MODLIST_IS_VALID , STATE_IS_COMPILED);
    }

    /**
     * Marks this configuration instance as invalid, and asynchronously destroys all the public fields of the derived class.
     * <p>
     *     This invalidity is required when your instance is elsewise invalid due to a missing dependency in the configuration data.
     * </p>
     * <p>
     *     You can retrieve whether this configuration instance is invalid by using the isInvalid() method.
     * </p>
     */
    public void SetConfigAsInvalid()
    {
        if (STATE_HasFlag(STATE_IS_INVALID)) { return; }
        STATE_AddFlag(STATE_IS_INVALID);
        STATE_RemoveFlag(STATE_IS_COMPILED);
        if (MDEXModInstance.LoggingFlags.Feature()) {
            StringBuilder sb = StackWalker.getInstance().walk((Stream<StackWalker.StackFrame> s) -> {
                StringBuilder sbi = new StringBuilder(2048);
                s.limit(8).forEach(
                        (StackWalker.StackFrame i) -> {
                            sbi.append(i.toStackTraceElement().toString());
                            sbi.append('\n');
                        }
                );
                return sbi;
            });
            MDEXModInstance.LOGGER.info("IsInvalid was run for feature configuration object {}. Stack Trace: \n{}" , getClass().getName() , sb);
        } else {
            MDEXModInstance.LOGGER.info("IsInvalid was run for feature configuration object {}. To get stack trace information, set the DebugFeatureConfigurations to true." , getClass().getName());
        }
        Details = null;
    }
}
