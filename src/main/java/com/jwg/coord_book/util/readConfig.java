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
    public static void read() {
        LOGGER.info("Reading config...");

        try {
            pageLocation = readCfg("pagedirector", 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            pageLimit = Integer.parseInt(readCfg("pagelimi", 4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println(readCfg("startpag", 6));
            page = Integer.parseInt(readCfg("startpag", 6));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println(readCfg("deletebutto", 8));
            deletePageButtonShown = Boolean.parseBoolean(readCfg("deletebutto", 8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (developerMode) {
            System.out.println(pageLocation + ", " + pageLimit + ", " + page);
        }
    }
}