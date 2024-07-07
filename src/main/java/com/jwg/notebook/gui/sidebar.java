package com.jwg.notebook.gui;

import com.jwg.notebook.screens.menuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.File;
import java.util.Objects;

import static com.jwg.notebook.Notebook.pageLocation;
import static com.jwg.notebook.screens.menuScreen.bookmarkedpage;
import static com.jwg.notebook.screens.menuScreen.page;

public class sidebar {
    public static TexturedButtonWidget addSidebarButton(int pos, Identifier BUTTON_TEXTURE, Screen BOOK_MENU, String buttonID, int x, int y, ButtonWidget.PressAction pressAction) {
        return new TexturedButtonWidget(BOOK_MENU.width/2 +90, 18+(12*pos), 8, 8, 0, 0, y, BUTTON_TEXTURE, x, y*2, pressAction, new TranslatableText("jwg.button."+buttonID));
    }

}
