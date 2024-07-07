package com.jwg.notebook.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ensureFileStructureExists {
    public static boolean exists(String folders) {
        return Files.exists(Path.of(folders));
    }
    public static void createFiles(boolean exists) { if (!exists) { try { Files.createDirectories(Paths.get("Notebook/")); } catch (IOException e) { throw new RuntimeException(e); } } }
}