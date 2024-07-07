package com.jwg.coord_book.util;

import java.io.FileWriter;
import java.io.IOException;

public class resetConfig {
    public void reset() {
        try {
            FileWriter fileOverwriter = new FileWriter("config/coordinate-book/config.cfg");
            fileOverwriter.write("# You need to reload for these to take affect!\n# Directory for the pages to be stored in (e.g .minecraft/CoordinateBook)\npagedirectory=CoordinateBook\n# Set page limits, negative numbers mean no limit\npagelimit=-1\n# Page to start on after opening the book\nstartpage=0");
            fileOverwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}