package com.jwg.notebook.gui.button;

import java.io.File;
import java.util.Objects;

import static com.jwg.notebook.Notebook.pageLocation;
import static com.jwg.notebook.screens.menuScreen.page;
import static com.jwg.notebook.screens.menuScreen.bookmarkedpage;

public class bookmark {
    public static void onPress() {
        if (bookmarkedpage <= Objects.requireNonNull(new File(pageLocation + "/").list()).length) {
            page = bookmarkedpage;
        }
    }


}
