package xyz.sillyjune.notebook;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static xyz.sillyjune.notebook.Notebook.BOOK_FOLDER;
import static xyz.sillyjune.notebook.Notebook.LOGGER;

public record NotebookData(String[] content, String location) {
    public String[] content() {
        return content;
    }
    public String location() {
        return location;
    }

    public NotebookData read() { // Read the record from a file
        File config = new File(STR."notebook/\{location}.json");
        StringBuilder d = new StringBuilder();
        try {
            Scanner reader = new Scanner(config);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                d.append(data);
            }
            reader.close();
        } catch (FileNotFoundException e) { // If it fails, create a new one
            LOGGER.error("Failed to read book!");
            NotebookData data = new NotebookData(new String[0], STR."notebook/\{location}");
            write(data); // Write it to file
        }
        String json = d.toString();
        return new Gson().fromJson(json, NotebookData.class);
    }

    public static void write(NotebookData data) {
        String json = new Gson().toJson(data);
        try { // Write the record to a file
            FileWriter writer = new FileWriter(BOOK_FOLDER + STR."/\{data.location}");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Notebook.LOGGER.error(STR."Failed to write book!\n\{e}");
        }
    }
}
