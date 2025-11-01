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
    public void apply(Project target) {
        var tasks = target.getTasks();
        tasks.register("expand_dep_archives").configure(new ExpandDevArchivesTranslater(target));
        tasks.register("update_dep_archives").configure(new UpdateDevArchivesTranslater(target));
    }
}
