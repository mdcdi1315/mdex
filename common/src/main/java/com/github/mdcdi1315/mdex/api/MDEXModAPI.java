package com.github.mdcdi1315.mdex.api;

// Mod Balm layer
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.mdex.MDEXBalmLayer;

// Everything Balm
import net.blay09.mods.balm.api.Balm;

// Java common API
import java.lang.reflect.InvocationTargetException;

public final class MDEXModAPI
{
    public static final String MODID = MDEXBalmLayer.MODID;

    private static ModLoaderMethods methods;

    // So that it cannot be instantiated.
    private MDEXModAPI() {}

    public static void InitializeMethods()
    {
        MDEXBalmLayer.LOGGER.info("Initializing platform methods layer.");
        String platclass = String.format("com.github.mdcdi1315.mdex.%s.api.ModLoaderMethodsImplementation" , Balm.getPlatform().toLowerCase());
        MDEXBalmLayer.LOGGER.info("Loader expects to find the class named as {} in order for the layering to work." , platclass);
        try {
            methods = (ModLoaderMethods) Class.forName(platclass).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        MDEXBalmLayer.LOGGER.info("Loader has loaded platform methods, ready to continue.");
    }

    public static void UninitializeMethods()
    {
        if (methods != null)
        {
            MDEXBalmLayer.LOGGER.info("Disposing platform methods...");
            try {
                methods.Dispose();
            } catch (Exception any) {
                MDEXBalmLayer.LOGGER.error("Cannot call Dispose on the platform methods. Execution failed unexpectedly." , any);
            }
            methods = null;
            MDEXBalmLayer.LOGGER.info("Done!");
        }
    }

    // By convention, this is not null during normal execution.
    @NotNull
    public static ModLoaderMethods getMethodImplementation()
    {
        return methods;
    }
}
