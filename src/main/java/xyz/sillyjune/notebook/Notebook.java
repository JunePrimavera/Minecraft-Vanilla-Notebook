package xyz.sillyjune.notebook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.ButtonTextures;
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

public class Notebook implements ModInitializer {

    @Override
    public void onInitialize() {

        // Create config file if it doesn't exist
        if (!new File("config").exists()) {
            boolean b = new File("config").mkdir();
        }
        if (!new File("config/notebook.conf").exists()) {
            try {
                if (!new File("config/notebook.conf").createNewFile()) {
                    System.err.println("Failed to create config file! Make a bug report if you see this");
                } else {
                    FileWriter w = new FileWriter("config/notebook.conf");
                    w.write("0\nNotebook");
                    w.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to create config file! Make a bug report if you see this\n " + e.getMessage());
            }
        }
        NotebookKeybind.openBookKeybindRegister();

        // Read config file
        try {
            File f = new File("config/notebook.conf");
            Scanner r = new Scanner(f);
            int line = 0;
            while (r.hasNextLine()) {
                String d = r.nextLine();
                if (line == 0) { if (Objects.equals(d, "0")) { DEV_ONLY = false; } else { DEV_ONLY = true; } }
                else if (line == 1) { BOOK_FOLDER = d; }
                else if (line == 2) { BUTTON_OFFSET = Integer.parseInt(d); }
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

    public static final ButtonTextures MAIN_BUTTON_ICON = new ButtonTextures(
            new Identifier("notebook:book/unfocused"),
            new Identifier("notebook:book/focused")
    );
    public static final ButtonTextures NEW_PAGE_ICON = new ButtonTextures(
            new Identifier("notebook:new_page/unfocused"),
            new Identifier("notebook:new_page/focused")
    );
    public static final ButtonTextures DEL_PAGE_ICON = new ButtonTextures(
            new Identifier("notebook:delete_page/unfocused"),
            new Identifier("notebook:delete_page/focused")
    );
    public static final ButtonTextures LAST_BOOK_ICON = new ButtonTextures(
            new Identifier("notebook:last_book/unfocused"),
            new Identifier("notebook:last_book/focused")
    );
    public static final ButtonTextures NEXT_BOOK_ICON =  new ButtonTextures(
            new Identifier("notebook:next_book/unfocused"),
            new Identifier("notebook:next_book/focused")
    );
    public static String BOOK_FOLDER = "Notebook";
    public static boolean DEV_ONLY = false;
    public static int BUTTON_OFFSET = 0;
}
