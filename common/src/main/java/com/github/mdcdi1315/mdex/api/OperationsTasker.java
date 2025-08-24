package com.github.mdcdi1315.mdex.api;

import com.github.mdcdi1315.DotNetLayer.System.IDisposable;
import com.github.mdcdi1315.DotNetLayer.System.SystemException;
import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public final class OperationsTasker
    implements IDisposable
{
    private Thread thread;
    private boolean acceptsdispatches;
    private Queue<Runnable> runnables;
    private java.util.concurrent.Semaphore smp;

    public OperationsTasker()
    {
        runnables = new java.util.ArrayDeque<>();
        smp = new Semaphore(0);
        acceptsdispatches = true;
        CreateThread();
    }

    public void Add(Runnable item)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(item , "item");
        if (acceptsdispatches)
        {
            runnables.add(item);
            if (smp.availablePermits() == 0) {
                smp.release();
            }
        }
    }

    private void UncaughtExceptionHandling(Thread t , Throwable exception)
    {
        MDEXBalmLayer.LOGGER.warn("OperationsTasker: Uncaught exception occurred on the tasker thread. The thread will be re-started." , exception);
        MDEXBalmLayer.LOGGER.warn("OperationsTasker: Cleaning execution queue.");
        runnables.clear();
        CreateThread();
    }

    private void CreateThread()
    {
        if (!acceptsdispatches) { return; }
        MDEXBalmLayer.LOGGER.info("OperationsTasker: Creating new operations tasker thread.");
        thread = new Thread(null , this::ThreadCode , "MDEX: OperationsTasker");
        thread.setUncaughtExceptionHandler(this::UncaughtExceptionHandling);
        thread.start();
        MDEXBalmLayer.LOGGER.info("OperationsTasker: Operations tasker thread created with ID {}." , thread.getId());
    }

    private void ThreadCode()
    {
        Runnable ru;
        while (acceptsdispatches)
        {
            while ((ru = runnables.poll()) != null) {
                MDEXBalmLayer.LOGGER.trace("Attempting to run tasker operation with hash code {}" , ru.hashCode());
                try {
                    ru.run();
                } catch (Exception any) {
                    MDEXBalmLayer.LOGGER.warn("Tasker operation with hash code {} failed.\nException data: {}" , ru.hashCode() , any);
                    continue;
                }
                MDEXBalmLayer.LOGGER.trace("Tasker operation with hash code {} completed." , ru.hashCode());
            }
            try {
                smp.acquire();
            } catch (InterruptedException ie) {
                throw new SystemException(String.format("Interrupted exception reported\nException Data: %s" , ie));
            }
        }
    }

    @Override
    public void Dispose()
    {
        acceptsdispatches = false;
        runnables.clear();
        smp.release();
        smp = null;
        runnables = null;
        try {
            MDEXBalmLayer.LOGGER.info("OperationsTasker: Attempting to destroy the operations tasking thread with ID {}" , thread.getId());
            thread.join();
        } catch (InterruptedException e) {
            // Just catch it
        }
        MDEXBalmLayer.LOGGER.info("OperationsTasker: Thread destroyed, disposal completed successfully.");
        thread = null;
    }
}
