package com.github.mdcdi1315.mdex.structures;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.util.MDEXException;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;

import net.blay09.mods.balm.api.Balm;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.lang.reflect.Modifier;

/**
 * Defines the base class for all Mining Dimension: EX hardcoded structure types. <br />
 * This class also adopts the Distributed Compilation Object logic.
 */
public abstract class AbstractStructure
    extends Structure
    implements Compilable
{
    /**
     * A list of mod id's that are required for actually creating the structure. <br />
     * Will be determined only once during the creation of the structure type,
     * then this list will be deleted for performance reasons.
     */
    public List<String> ModIds;
    // We need this for codec referencing, even if that does mean that we have to keep a second field for doing the job.
    private final StructureSettings settings;
    private byte state;
    private static final byte STATE_IS_COMPILED = 1 << 0 , STATE_IS_INVALID = 1 << 1 , HAS_RUN_AT_LEAST_ONCE = STATE_IS_COMPILED | STATE_IS_INVALID;

    protected AbstractStructure(StructureSettings settings) {
        super(settings);
        this.settings = settings;
        ModIds = List.of();
    }

    protected AbstractStructure(List<String> modids, StructureSettings settings) {
        this(settings);
        ArgumentNullException.ThrowIfNull(modids , "modids");
        ModIds = modids;
    }

    private void DestroyInternalState()
    {
        try {
            if (MDEXBalmLayer.LoggingFlags.StructureProcessors())
            {
                MDEXBalmLayer.LOGGER.info("Destroying invalid structure class instance named as {}" , getClass().getName());
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
     * Compiles this instance. <br />
     * This method call should be the last executable code in your constructor definition.
     */
    public final void Compile()
    {
        if ((state & HAS_RUN_AT_LEAST_ONCE) != 0) { return; }
        try {
            if (MDEXBalmLayer.LoggingFlags.StructureProcessors())
            {
                MDEXBalmLayer.LOGGER.info("Attempting to compile structure DCO {} with hash code {}." , getClass().getName() , hashCode());
            }
            for (var m : ModIds)
            {
                if (!Balm.isModLoaded(m)) {
                    MDEXBalmLayer.LOGGER.warn("Cannot compile DCO with hash code {} because the mod list is invalid." , hashCode());
                    SetInstanceAsInvalid();
                    return;
                }
            }
            ModIds = null;
            CompileInstanceData();
            if ((state & STATE_IS_INVALID) == 0) // May found errors during compilation
            {
                state |= STATE_IS_COMPILED;
                if (MDEXBalmLayer.LoggingFlags.StructureProcessors()) {
                    MDEXBalmLayer.LOGGER.info("Structure DCO with hash code {} succeeded with compilation." , hashCode());
                }
            }
        } catch (MDEXException e) {
            SetInstanceAsInvalid();
            MDEXBalmLayer.LOGGER.error("Cannot compile an instance of the DCO '{}' because an exception of type {} has been occurred: {}", getClass().getName() , e.getClass().getName() , e.getMessage());
        }
    }

    public final boolean IsCompiled()
    {
        return (state & STATE_IS_COMPILED) != 0 && (state & STATE_IS_INVALID) == 0;
    }

    public final boolean IsInvalid()
    {
        return (state & STATE_IS_INVALID) != 0;
    }

    /**
     * Marks this instance as invalid, and asynchronously destroys all the public fields of the derived class.
     * <p>
     *     This invalidity is required when your instance is elsewise invalid due to a missing dependency in the configuration data.
     * </p>
     * <p>
     *     You can retrieve whether this instance is invalid by using the {@link #IsInvalid()} method.
     * </p>
     */
    public final void SetInstanceAsInvalid()
    {
        if ((state & STATE_IS_INVALID) != 0) { return; }
        state |= STATE_IS_INVALID;
        if (MDEXBalmLayer.LoggingFlags.StructureProcessors()) {
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
            MDEXBalmLayer.LOGGER.info("IsInvalid was run for structure configuration object {}. Stack Trace: \n{}" , getClass().getName() , sb);
        } else {
            MDEXBalmLayer.LOGGER.info("IsInvalid was run for structure configuration object {}. To get stack trace information, set the DebugStructureConfigurations option to true." , getClass().getName());
        }
        MDEXBalmLayer.RunTaskAsync(this::DestroyInternalState);
    }

    /**
     * Compiles the data that can be compiled for the current structure type instance. <br />
     * This method can report exceptions , it is enough that such exceptions are derived from the {@link MDEXException} class. <br />
     * If additionally determined that the config is invalid (but no exceptions occured), you can also call from here the {@link #SetInstanceAsInvalid()} method.
     */
    protected abstract void CompileInstanceData();

    protected abstract Optional<GenerationStub> FindGenerationPoint(GenerationContext gc);

    protected abstract AbstractStructureType<?> GetStructureType();

    @Override
    protected final Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext)
    {
        return (state & STATE_IS_COMPILED) == 0 ? Optional.empty() : FindGenerationPoint(generationContext);
    }

    public final StructureSettings GetSettings() {
        return settings;
    }

    @Override
    public final StructureType<?> type() {
        return GetStructureType();
    }
}
