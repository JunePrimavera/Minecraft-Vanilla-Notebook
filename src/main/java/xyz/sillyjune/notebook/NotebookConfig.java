package xyz.sillyjune.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public record NotebookConfig(boolean debug, int button_offset) {
    public boolean debug() { // Is debug mode enabled?
        return debug;
    }
    public int button_offset() { // Button offset config option for compatibility with mods with buttons in the same place (e.g. create)
        return button_offset;
    }
    static String read_config() { // Read config from file
        try {
            File config = new File("config/notebook.json");
            Scanner reader = new Scanner(config);
            StringBuilder d = new StringBuilder();
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                d.append(data);
            }
            reader.close();
            return d.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    static NotebookConfig default_config() {
        return new NotebookConfig(false, 0);
    }
}
