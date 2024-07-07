package com.june.notebook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Notebook implements ModInitializer {

    @Override
    public void onInitialize() {
        if (!new File("Notebook").exists()) {
            try {
                Path path = Paths.get("Notebook");
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Failed to create directory! Make a bug report if you see this\n " + e.getMessage());
            }
        }

        System.out.println("June is a silly goober");
    }

    public static final Identifier MAIN_BUTTON_ICON = new Identifier("notebook:textures/gui/book.png");
    public static final Identifier NEW_PAGE_ICON = new Identifier("notebook:textures/gui/new_page.png");
    public static final boolean DEV_ONLY = true;
}
