package com.github.mdcdi1315.multiloader_infrastructure;

import org.codehaus.groovy.runtime.IOGroovyMethods;

import org.gradle.api.Project;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.Directory;
import org.gradle.api.logging.Logger;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public final class Archive_Expander_Internal
{
    private static Map<String, String> ReadPropertiesFile(File f)
            throws java.io.IOException
    {
        String temp;
        HashMap<String, String> map = new HashMap<>();

        try (
                FileInputStream fis = new FileInputStream(f);
                InputStreamReader reader = new InputStreamReader(fis)
        )
        {
            int line = 0, sep_index;
            while ((temp = IOGroovyMethods.readLine(reader)) != null)
            {
                if (temp.startsWith("#") || temp.isBlank()) {
                    // Empty
                } else if ((sep_index = temp.indexOf('=')) < 0) {
                    throw new IllegalArgumentException(String.format("This is not a properties file - the line %s is not properly formatted property.", line));
                } else {
                    map.put(
                            temp.substring(0, sep_index),
                            temp.substring(sep_index+1)
                    );
                }
                line++;
            }
        }
        return map;
    }

    private static boolean ArchiveFileFilterImpl(File t) {
        return t.getName().endsWith(".zip");
    }

    private static boolean DirectoryFileFilterImpl(File t) { return t.isDirectory(); }

    private static void ExtractFileTo(File from, File to)
            throws IOException
    {
        try (
                var source = new FileInputStream(from);
                var dest = new FileOutputStream(to);
        ) {
            int rb;
            byte[] bytes = new byte[2048];
            while ((rb = source.read(bytes)) > -1) {
                dest.write(bytes, 0 , rb);
            }
        }
    }

    public static void ExpandArchives(Project p, Logger logger, boolean update)
            throws IOException
    {
        var root_dir = p.getLayout().getProjectDirectory().dir("deps");

        String base_mods_lib_version = (String)p.findProperty("base_mods_lib_version");

        if (base_mods_lib_version == null) {
            throw new IllegalStateException("Cannot find project property base_mods_lib_version!");
        }

        var list = root_dir.getAsFile().listFiles(Archive_Expander_Internal::ArchiveFileFilterImpl);

        if (list == null) {
            throw new IllegalStateException("I/O error occurred!");
        }

        var project_mv = p.findProperty("minecraft_version");

        if (project_mv == null) {
            throw new IllegalStateException("Cannot find the Minecraft Version project property!");
        }

        if (update)
        {
            // Update task is running instead, we need to update our dependencies.
            // To do that we will delete our generated directories (if any).
            var list_update = root_dir.getAsFile().listFiles(Archive_Expander_Internal::DirectoryFileFilterImpl);
            if (list_update == null) {
                throw new IllegalStateException("I/O error occurred!");
            }
            File[] tf;
            for (File f : list_update)
            {
                tf = f.listFiles();
                if (tf == null) {
                    logger.lifecycle(String.format("I/O error occurred attempting to delete directory %s. Deletion will be ignored for the in question directory.", f.getName()));
                } else {
                    for (var fi : tf) { fi.delete(); }
                }
            }
        }

        for (File f : list)
        {
            FileTree ft = p.zipTree(f);
            Map<String, String> properties_map = null;
            for (File zip_archive_f : ft)
            {
                if (zip_archive_f.getName().equalsIgnoreCase("mappings.properties")) {
                    properties_map = ReadPropertiesFile(zip_archive_f);
                    break;
                }
            }
            if (properties_map == null) {
                throw new IllegalStateException("Cannot find the mapping properties file!");
            }

            String mv = properties_map.get("minecraft_version");

            if (!project_mv.equals(mv)) {
                logger.lifecycle(String.format("Minecraft version mismatch. Expected %s while this package is %s." , project_mv, mv));
                continue;
            }

            String jdocs = properties_map.get("javadoc");

            if (jdocs != null)
            {
                var dir = root_dir.dir("java_docs");
                File other = dir.getAsFile();
                if (!other.exists()) { other.mkdir(); }
                for (File fo : ft)
                {
                    if (fo.getName().equals(jdocs)) {
                        ExtractFileTo(fo , dir.file(jdocs).getAsFile());
                        break;
                    }
                }
            }

            String k, v;
            int mapping_index;
            Directory base_dir;
            for (var entry : properties_map.entrySet())
            {
                k = entry.getKey();
                v = entry.getValue();
                if ((mapping_index = k.indexOf("_mapping")) > -1)
                {
                    (base_dir = root_dir.dir(k.substring(0 , mapping_index))).getAsFile().mkdir();
                    for (File zf : ft)
                    {
                        if (zf.getName().equals(v)) {
                            ExtractFileTo(zf , base_dir.file(v).getAsFile());
                            break;
                        }
                    }
                } else if (k.equals("version")) {
                    logger.lifecycle(String.format("Reading archive %s with version %s", f.getName(), v));
                    if (
                            f.getName().equals("mdcdi1315_base_mods_lib_dev_package.zip") &&
                                    !base_mods_lib_version.equals(v)
                    ) {
                        throw new IllegalStateException(String.format("Base mods library version mismatch!\nExpected %s while found %s." , base_mods_lib_version, v));
                    }
                }
            }
            logger.lifecycle(String.format("Completed archive %s!" , f.getName()));
        }
    }
}
