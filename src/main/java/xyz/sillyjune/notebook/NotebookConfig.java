package xyz.sillyjune.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public record NotebookConfig(boolean debug, int button_offset) {
    public boolean debug() {
        return debug;
    }
    public int button_offset() {
        return button_offset;
    }

    static String read_config() {
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
            System.out.println("Couldn't read config!");
            return null;
        }
    }
    static NotebookConfig default_config() {
        return new NotebookConfig(false, 0);
    }
}
