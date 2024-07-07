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

    public static class ports {
        public static void exports(String pagelocation) {
            // Format for export
            int len = Objects.requireNonNull(new File(pagelocation).list()).length;
            StringBuilder exported_file = new StringBuilder();
            for (int i = 0 ; i != len ; i++) {
                try {
                    File f = new File(pagelocation + "/" + Objects.requireNonNull(new File(pagelocation).list())[i]);
                    Scanner r = new Scanner(f);
                    while (r.hasNextLine()) {
                        String data = r.nextLine();
                        exported_file.append(data);
                    }
                    r.close();
                } catch (FileNotFoundException e) { e.printStackTrace(); }
                exported_file.append("\n{_ENDOFPAGE_}\n");
            }

            // Create export file
            try { boolean b = new File("Notebook/book.export").createNewFile();
            } catch (IOException e) { throw new RuntimeException(e); }

            // Write to file
            try {
                FileWriter f = new FileWriter("Notebook/book.export");
                f.write(String.valueOf(exported_file));
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void imports(String pagelocation, String fileToImport) {
            if (new File(pagelocation).exists()) {
                boolean b = new File(pagelocation).delete();
                b = new File(pagelocation).mkdirs();
            } else {
                boolean b = new File(pagelocation).mkdirs();
            }
            StringBuilder import_file_data = new StringBuilder();

            try {
                File fi = new File(fileToImport);
                Scanner r = new Scanner(fi);
                int it = 0;
                while (r.hasNextLine()) {
                    String data = r.nextLine();
                    System.out.println(data);
                    if (Objects.equals(data, "{_ENDOFPAGE_}")) {
                        boolean b = new File(pagelocation + "/" + it + ".jdat").createNewFile();
                        System.out.println("WEWEEW");
                        try {

                            FileWriter f = new FileWriter(pagelocation + "/" + it + ".jdat");
                            f.write(String.valueOf(import_file_data));
                            f.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        it += 1;
                        import_file_data = new StringBuilder();
                    } else {
                        import_file_data.append(data);
                    }
                }
                r.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}