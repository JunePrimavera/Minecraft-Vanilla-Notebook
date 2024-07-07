package com.jwg.coord_book.util;

import com.jwg.coord_book.CoordBook;
import com.jwg.coord_book.screens.menuScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jwg.coord_book.CoordBook.*;
import static com.jwg.coord_book.screens.menuScreen.*;

public class readConfig {
    public static String readCfg(String entry, int cfgLine) throws IOException {
        String dat = Files.readAllLines(Paths.get("config/coordinate-book/config.cfg")).get(cfgLine);
        return dat.substring(entry.length()+2);
    }
    public static void getBookmarkPage() throws IOException {
        String dat = Files.readAllLines(Paths.get("config/coordinate-book/bookmark.cfg")).get(1);
        bookmarkedpage = Integer.parseInt(dat);
    }
    public static void read() {
        LOGGER.info("Reading config...");

        try {
            pageLocation = readCfg("pagedirector", 2);
        } catch (IOException e) { throw new RuntimeException(e); }

        try {
            pageLimit = Integer.parseInt(readCfg("pagelimi", 4));
        } catch (IOException e) { throw new RuntimeException(e); }

        try {
            page = Integer.parseInt(readCfg("startpag", 6));
        } catch (IOException e) { throw new RuntimeException(e); }

        try {
            deletePageButtonShown = Boolean.parseBoolean(readCfg("deletebutto", 8));
        } catch (IOException e) { throw new RuntimeException(e); }

        try {
            getBookmarkPage();
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}