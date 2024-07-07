package xyz.sillyjune.notebook;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static xyz.sillyjune.notebook.Notebook.BOOK_FOLDER;
import static xyz.sillyjune.notebook.Notebook.LOGGER;

public class NotebookData {
    String[] content;
    String location;
    NotebookData(String[] content, String location) {
        this.content = content;
        this.location = location;
    }

    public static NotebookData read(String location) { // Read the record from a file
        File jsondata = new File(BOOK_FOLDER + "/" + location);
        StringBuilder d = new StringBuilder();
        try {
            Scanner reader = new Scanner(jsondata);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                d.append(data);
            }
            reader.close();
        } catch (FileNotFoundException e) { // If it fails, create a new one
            LOGGER.error("Failed to read book!\n" + e);
            NotebookData data = new NotebookData(new String[0], location);
            data.write(); // Write to file
        }
        String json = d.toString();
        return new Gson().fromJson(json, NotebookData.class);
    }

    public void write() {
        String json = new Gson().toJson(this);
        try { // Write the record to a file
            FileWriter writer = new FileWriter(BOOK_FOLDER + "/" + this.location);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Notebook.LOGGER.error("Failed to write book!\n" + e);
        }
    }
}
