package com.june.notebook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import static com.june.notebook.NotebookKeybind.openBookKeybindRegister;

public class Notebook implements ModInitializer {

    @Override
    public void onInitialize() {

        // Create config file if it doesn't exist
        if (!new File("config/notebook.json").exists()) {
            try {
                if (!new File("config/notebook.json").createNewFile()) {
                    System.err.println("Failed to create config file! Make a bug report if you see this");
                } else {
                    FileWriter w = new FileWriter("config/notebook.json");
                    w.write("0\nNotebook");
                    w.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to create config file! Make a bug report if you see this\n " + e.getMessage());
            }
        }
        openBookKeybindRegister();

        // Read config file
        StringBuilder text = new StringBuilder();
        try {
            File f = new File("config/notebook.json");
            Scanner r = new Scanner(f);
            int line = 0;
            while (r.hasNextLine()) {
                String d = r.nextLine();
                if (line == 0) { if (Objects.equals(d, "0")) { DEV_ONLY = false; } else { DEV_ONLY = true; } }
                else if (line == 1) { BOOK_FOLDER = d; }
                line += 1;
            }
            r.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Create book folders if they don't exist
        if (!new File(BOOK_FOLDER).exists() || !new File(BOOK_FOLDER + "/Default").exists()) {
            try {
                Path path = Paths.get(BOOK_FOLDER + "/Default");
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Failed to create directory! Make a bug report if you see this\n " + e.getMessage());
            }
        }
        if (DEV_ONLY) {
            System.out.println("June is a silly goober");
        }
    }

    public static final Identifier MAIN_BUTTON_ICON = new Identifier("notebook:textures/gui/book.png");
    public static final Identifier NEW_PAGE_ICON = new Identifier("notebook:textures/gui/new_page.png");
    public static final Identifier DEL_PAGE_ICON = new Identifier("notebook:textures/gui/delete_page.png");
    public static final Identifier LAST_BOOK_ICON = new Identifier("notebook:textures/gui/last_book.png");
    public static final Identifier NEXT_BOOK_ICON = new Identifier("notebook:textures/gui/next_book.png");
    public static String BOOK_FOLDER = "Notebook";
    public static boolean DEV_ONLY = true;
}
