package com.jwg.notebook.util;

import com.jwg.notebook.Notebook;

import java.io.File;
import java.io.IOException;

public class generateConfig {
    public static void generate(int i) {
        boolean madeConfigFile;
        boolean madeConfigFolder;
        boolean madeBookmarkFile;

        ++i;

        madeConfigFolder = new File("config/vanilla-notebook/").mkdirs();
        try { madeConfigFile = new File("config/vanilla-notebook/config.cfg").createNewFile(); }
        catch (IOException e) { throw new RuntimeException(e); }
        try { madeBookmarkFile = new File("config/vanilla-notebook/config.cfg").createNewFile(); }
        catch (IOException e) { throw new RuntimeException(e); }

        if (i <= 5 && !new File("config/vanilla-notebook/").exists() || !new File("config/vanilla-notebook/config.cfg").exists()) {
            Notebook.LOGGER.info("Couldn't create config folder or file.. Trying again, attempt {}/5", i);
            if (i == 5) { Notebook.LOGGER.info("If this persists on startup, please make a bug report. The mod is unable to make the config files!"); }
            generate(i);
        }
    }
}