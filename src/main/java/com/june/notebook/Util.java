package com.june.notebook;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static com.june.notebook.Notebook.LOGGER;
import static com.june.notebook.Notebook.pageLocation;
import static com.june.notebook.screens.menuScreen.*;

public class Util {
    public static class addCharacter {
        public static String add(String str, int position, String keyStr) {
            int len = str.length();
            char[] updatedArr = new char[len + 1];
            try {
                char ch = keyStr.charAt(0);
                str.getChars(0, len, updatedArr, 0);
                updatedArr[len - position] = ch;
                str.getChars(len - position, len, updatedArr, len - position + 1);
            } catch (IndexOutOfBoundsException e) {
                add(str, 0, keyStr);
            }
            return new String(updatedArr)+"";
        }
    }
    public static class removePage {
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
}


