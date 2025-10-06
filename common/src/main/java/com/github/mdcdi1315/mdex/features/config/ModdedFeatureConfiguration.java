package com.github.mdcdi1315.mdex.features.config;

import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.util.MDEXException;

import net.blay09.mods.balm.api.Balm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.lang.reflect.Modifier;

/**
 * Base class for all modded feature configurations.
 * This class implementation adopts the Distributed Compilation Object logic.
 */
public abstract class ModdedFeatureConfiguration
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
    public List<String> ModIds;
    private byte state;

    private void STATE_AddFlag(byte flag)
    {
        state |= flag;
    }

    private boolean STATE_HasFlag(byte flag)
    {
        return (state & flag) != 0;
    }

    private boolean STATE_HasFlag(byte flg1 , byte flg2)
    {
        int compiledflags = flg1 | flg2;
        return (state & compiledflags) == compiledflags;
    }

    /**
     * Gets a codec instance capable of handling the Mod ID's pattern in JSON data.
     * @return A codec builder instance to further chain other generation properties.
     * @param <T> The derived {@link ModdedFeatureConfiguration} class.
     */
    public static <T extends ModdedFeatureConfiguration> RecordCodecBuilder<T, List<String>> GetBaseCodec()
    {
        return Codec.STRING.listOf().optionalFieldOf("modids" , List.of()).forGetter((T inst) -> inst.ModIds);
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

    /**
     * Gets a boolean whether the mod id list specified in {@literal ModIds} field is valid and the feature config is not 'stale'.
     * <p>
     *     Note that if the list is valid or not, the list is destroyed after the final conclusion is reached.
     * </p>
     * @return true when the required mod list is valid, or false when the list is not valid.
     */
    public final boolean getAllModIdsAreLoaded()
    {
        if (STATE_HasFlag(STATE_MODLIST_IS_VALID)) { return true; }
        if (STATE_HasFlag(STATE_MODLIST_IS_INVALID)) { return false; }
        // Discover whether the specified mods are loaded.
        // The search runs only once, and then the id list is invalidated.
        // The result of this search pass is stored by the state flags and kept there until the end of the instance life.
        for (String mod : ModIds)
        {
            if (!Balm.isModLoaded(mod)) {
                STATE_AddFlag(STATE_MODLIST_IS_INVALID);
                if (MDEXBalmLayer.LoggingFlags.Feature())
                {
                    MDEXBalmLayer.LOGGER.warn("Feature configuration of type {} will not be applied to the current world because the mod named as '{}' is not present in the mod lifecycle." , getClass().getName() , mod);
                }
                ModIds = null;
                return false;
            }
        }
        STATE_AddFlag(STATE_MODLIST_IS_VALID);
        ModIds = null;
        return true;
    }

    private void DestroyConfigurationState()
    {
        try {
            if (MDEXBalmLayer.LoggingFlags.Feature())
            {
                MDEXBalmLayer.LOGGER.info("Destroying invalid config class instance named as {}" , getClass().getName());
            }
            for (var f : getClass().getFields())
            {
                int mod = f.getModifiers();
                // Public && Not Static && field type NOT a primitive
                // (Note that we cannot delete primitives through typed code)
                if ((mod & Modifier.PUBLIC) != 0 &&
                    (mod & Modifier.STATIC) == 0 &&
                        !f.getType().isPrimitive())
                {
                    f.set(this, null);
                }
            }
        } catch (IllegalAccessException aex)
        {
            throw new InvalidOperationException(String.format("Field DESTROY operation failed because of an access failure.\nInternal data: %s" , aex.getMessage()));
        }
    }

    /**
     * Returns a value indicating that the current configuration instance is invalid.
     * @return true when the current instance is invalid; during this stage, all public
     * config fields are not considered to be safe to be accessed because the internal
     * destroyer may be running. If this returns false, then the instance is still valid
     * and can be manipulated.
     */
    public boolean isInvalid()
    {
        return STATE_HasFlag(STATE_IS_INVALID) || STATE_HasFlag(STATE_MODLIST_IS_INVALID);
    }

    /**
     * Called by Compile method.
     * <p>
     *     This method signature provides the actual compilation algorithm to run.
     * </p>
     * <p>
     *     This method should throw exceptions derived from {@link MDEXException} if something incorrect occurred.
     * </p>
     */
    protected abstract void compileConfigData();

    /**
     * Must be used by your code to destroy all the un-compiled references
     * that have been compiled and do not need additional processing.
     * This allows to reclaim some memory for other resources that the game WILL need.
     */
    protected abstract void invalidateUntransformedFields();

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
            if (MDEXBalmLayer.LoggingFlags.Feature())
            {
                MDEXBalmLayer.LOGGER.info("Attempting to compile DCO {} with hash code {}." , getClass().getName() , hashCode());
            }
            if (getAllModIdsAreLoaded()) {
                compileConfigData();
            } else {
                MDEXBalmLayer.LOGGER.warn("Cannot compile DCO with hash code {} because the mod list is invalid." , hashCode());
                setConfigAsInvalid();
                return;
            }
            STATE_AddFlag(STATE_IS_COMPILED);
            if (MDEXBalmLayer.LoggingFlags.Feature())
            {
                MDEXBalmLayer.LOGGER.info("DCO with hash code {} succeeded with compilation." , hashCode());
            }
            try {
                invalidateUntransformedFields();
            } catch (Exception e) {
                MDEXBalmLayer.LOGGER.warn("DCO with hash code {} cannot destroy all the un-compiled fields due to a failure: {}\nThis might cause increased memory footprints in the game." , hashCode() , e.getMessage());
            }
        } catch (MDEXException e) {
            setConfigAsInvalid();
            MDEXBalmLayer.LOGGER.error("Cannot compile an instance of the DCO '{}' because an exception of type {} has been occurred: {}", getClass().getName() , e.getClass().getName() , e.getMessage());
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
    public void setConfigAsInvalid()
    {
        if (STATE_HasFlag(STATE_IS_INVALID)) { return; }
        STATE_AddFlag(STATE_IS_INVALID);
        if (MDEXBalmLayer.LoggingFlags.Feature()) {
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
            MDEXBalmLayer.LOGGER.info("IsInvalid was run for feature configuration object {}. Stack Trace: \n{}" , getClass().getName() , sb);
        } else {
            MDEXBalmLayer.LOGGER.info("IsInvalid was run for feature configuration object {}. To get stack trace information, set the DebugFeatureConfigurations to true." , getClass().getName());
        }
        MDEXBalmLayer.RunTaskAsync(this::DestroyConfigurationState);
    }
}
