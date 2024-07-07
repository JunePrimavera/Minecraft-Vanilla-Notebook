package com.jwg.notebook.gui;

import com.jwg.notebook.screens.menuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Objects;

import static com.jwg.notebook.Notebook.pageLocation;
import static com.jwg.notebook.screens.menuScreen.bookmarkedpage;
import static com.jwg.notebook.screens.menuScreen.page;

public class sidebar {
    public static TexturedButtonWidget addSidebarButton(int pos, Identifier BUTTON_TEXTURE, Screen BOOK_MENU, String buttonID, int size) {
        return new TexturedButtonWidget(BOOK_MENU.width/2 +90, 18+(12*pos), 8, 8, 0, 0, size, BUTTON_TEXTURE, size, size*2, button -> buttonAction(buttonID), Text.translatable("jwg.button."+buttonID));
    }
    static void buttonAction(String buttonID) {
        if (Objects.equals(buttonID, "delete")) {
            menuScreen.removePage(page);
        } else if (Objects.equals(buttonID, "bookmark")) {
            if (page != bookmarkedpage && bookmarkedpage >= 0) {
                if (new File(pageLocation+"/"+bookmarkedpage+".jdat").exists()) { page = bookmarkedpage;
                } else { bookmarkedpage = -1; }
            }
        }
    }

}
