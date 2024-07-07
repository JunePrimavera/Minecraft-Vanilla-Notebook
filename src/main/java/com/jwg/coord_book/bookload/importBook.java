package com.jwg.coord_book.bookload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.jwg.coord_book.CoordBook.LOGGER;
import static com.jwg.coord_book.CoordBook.pageLocation;

public class importBook {
    public static void imprt() throws IOException {
        LOGGER.info("Importing");
        String fileZip = "export.zip";
        File destDir = new File("");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = null;
        try {
            zipEntry = zis.getNextEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            zis.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        zis.close();
        LOGGER.info("Done");
    }
}
