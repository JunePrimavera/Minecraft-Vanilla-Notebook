package com.jwg.coord_book.util;

import com.jwg.coord_book.CoordBook;

import java.io.File;
import java.io.IOException;

public class generateConfig {
    public static void generate(int i) {
        boolean madeConfigFile;
        boolean madeConfigFolder;
        boolean madeBookmarkFile;

        ++i;

        madeConfigFolder = new File("config/coordinate-book/").mkdirs();
        try { madeConfigFile = new File("config/coordinate-book/config.cfg").createNewFile(); }
        catch (IOException e) { throw new RuntimeException(e); }
        try { madeBookmarkFile = new File("config/coordinate-book/config.cfg").createNewFile(); }
        catch (IOException e) { throw new RuntimeException(e); }

        if (i <= 5 && !new File("config/coordinate-book/").exists() || !new File("config/coordinate-book/config.cfg").exists()) {
            CoordBook.LOGGER.info("Couldn't create config folder or file.. Trying again, attempt {}/5", i);
            if (i == 5) { CoordBook.LOGGER.info("If this persists on startup, please make a bug report. The mod is unable to make the config files!"); }
            generate(i);
        }
    }
}