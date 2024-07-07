package com.jwg.notebook.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static com.jwg.notebook.Notebook.LOGGER;
import static com.jwg.notebook.Notebook.pageLocation;
import static com.jwg.notebook.screens.menuScreen.goToPreviousPage;

public class removePage {
    public static void remove(int rmpage) {
        int files = Objects.requireNonNull(new File(pageLocation + "/").list()).length;
        int pagesToRename = files - rmpage;
        boolean tmp = false;
        if (rmpage == 0) { LOGGER.warn("Can't delete first page"); }
        else if (rmpage == files-1) { goToPreviousPage(); tmp = new File(pageLocation +"/"+ rmpage + ".jdat").delete(); }
        else {
            int i = 0;
            tmp = new File(pageLocation +"/"+ rmpage + ".jdat").delete();
            tmp = new File(pageLocation +"/"+ (files-1) + ".jdat").delete();

            while (i < files-rmpage) {
                StringBuilder fulldata = new StringBuilder();
                try {
                    if (new File(pageLocation +"/"+ (rmpage+i) + ".jdat").exists()) {
                        Scanner readPageContent = new Scanner(new File(pageLocation +"/"+ (rmpage+i) + ".jdat"));
                        while (readPageContent.hasNextLine()) {
                            String data = readPageContent.nextLine();
                            if (!fulldata.toString().equals("")) data = "\n" + data;
                            fulldata.append(data);
                        }
                        readPageContent.close();
                    }
                } catch (FileNotFoundException e) { e.printStackTrace(); }
                try {
                    FileWriter f = new FileWriter(pageLocation +"/"+ (rmpage+i-1) + ".jdat", false);
                    f.write(String.valueOf(fulldata));
                    f.close();
                } catch (IOException e) { e.printStackTrace(); }

                i++;
            }
        }
    }
}