package com.jwg.coord_book.util;

import com.jwg.coord_book.CoordBook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ensureFileStructureExists {
    public static boolean exists(String folders, String file) {
        return Files.exists(Path.of(folders)) && Files.exists(Path.of(file));
    }
    public static void createFiles(boolean create) {
        if (!create) {
            CoordBook.LOGGER.info("Going through first-time setup (Generating files/folders).");

            try {
                Files.createDirectories(Paths.get("CoordinateBook/"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                File files = new File("CoordinateBook/coords.json");
                if (files.createNewFile()) {
                    CoordBook.LOGGER.info("Created file (CoordinateBook/coords.json)");
                } else {
                    CoordBook.LOGGER.info("File already exists (CoordinateBook/coords.json)");
                }
            } catch (IOException e) {
                CoordBook.LOGGER.error("WARNING: Unable to create file CoordinateBook/coords.json");
            }
        } else {
            CoordBook.LOGGER.info("Files/Folders are already created.");
        }

    }
}