package com.jwg.notebook.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class readPage {
    public static StringBuilder read(String pageloc, int page) {
        StringBuilder fulldata = new StringBuilder();
        try {
            Scanner readPageContent = new Scanner(new File(pageloc+"/"+page+".jdat"));
            while (readPageContent.hasNextLine()) {
                String data = readPageContent.nextLine();
                fulldata.append(data);
            }
            readPageContent.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fulldata;
    }
}
