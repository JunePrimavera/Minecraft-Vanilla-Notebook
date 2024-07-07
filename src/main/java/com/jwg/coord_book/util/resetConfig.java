package com.jwg.coord_book.util;

import java.io.FileWriter;
import java.io.IOException;

public class resetConfig {
    public void reset() {
        try {
            FileWriter fileOverwriter = new FileWriter("config/coordinate-book/config.cfg");
            fileOverwriter.write("pagedirectory=CoordinateBook\npagelimit=-1");
            fileOverwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}