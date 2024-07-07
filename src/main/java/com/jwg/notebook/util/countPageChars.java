package com.jwg.notebook.util;

import java.io.*;

import static com.jwg.notebook.Notebook.pageLocation;

public class countPageChars {
    public static int countPg(int pg) throws IOException {
        File file = new File(pageLocation+"/"+pg+".jdat");
        FileInputStream fileInputStream;

        { try { fileInputStream = new FileInputStream(file); } catch (FileNotFoundException e) { throw new RuntimeException(e); } }
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int characterCount = 1;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            characterCount += line.length();

        }
        return characterCount;
    }
}
