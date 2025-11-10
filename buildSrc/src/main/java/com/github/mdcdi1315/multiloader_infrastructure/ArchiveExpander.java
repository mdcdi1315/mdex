package com.github.mdcdi1315.multiloader_infrastructure;

import org.gradle.api.Task;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.IOException;

public final class ArchiveExpander
    implements Plugin<Project>
{
    private record ExpandDevArchivesTranslater(Project target)
        implements Action<Task>
    {
        @Override
        public void execute(Task task)
        {
            try {
                Archive_Expander_Internal.ExpandArchives(target , task.getLogger(), false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private record UpdateDevArchivesTranslater(Project target)
            implements Action<Task>
    {
        @Override
        public void execute(Task task)
        {
            try {
                Archive_Expander_Internal.ExpandArchives(target , task.getLogger(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void apply(Project target)
    {
        // Task t;
        var tasks = target.getTasks();
        var expand_archives_task = tasks.register("expand_dep_archives");
        expand_archives_task.configure(new ExpandDevArchivesTranslater(target));
        // (t = expand_archives_task.get()).setDescription("Expands all the Developer archives found on the 'deps' directory. For initial project setup only.");
        // t.setGroup("build setup");
        var update_archives_task = tasks.register("update_dep_archives");
        update_archives_task.configure(new UpdateDevArchivesTranslater(target));
        // (t = update_archives_task.get()).setDescription("Updates all the Developer archives found on the 'deps' directory.");
        // t.setGroup("build setup");
    }
}
