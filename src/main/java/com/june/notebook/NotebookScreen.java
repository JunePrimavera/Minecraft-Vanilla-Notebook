package com.june.notebook;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


import static com.june.notebook.Notebook.NEW_PAGE_ICON;

public class NotebookScreen extends Screen {
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");

    private String BookFolder = "Notebook";
    private int pageIndex;
    private int totalPages;
    private int cursorIndex;

    private List<OrderedText> cachedPage;
    private int cachedPageIndex;
    private Text pageIndexText;
    private ButtonWidget closeButton;
    private ButtonWidget newPageButton;
    private PageTurnWidget nextPageButton;
    private PageTurnWidget previousPageButton;
    private final boolean pageTurnSound;


    public NotebookScreen() {
        this(false);
    }

    private NotebookScreen(boolean playPageTurnSound) {
        super(NarratorManager.EMPTY);
        this.cachedPage = Collections.emptyList();
        this.cachedPageIndex = -1;
        this.pageIndexText = ScreenTexts.EMPTY;
        this.pageTurnSound = playPageTurnSound;
    }

    // Returns the amount of pages stored
    protected int getPageCount(String path) {
        return Objects.requireNonNull(new File(path).list()).length;
    }
    /// Creates a new blank page
    protected void newPage(String path, int pagei) {
        try {
            if (!new File(path + "/" + pagei + ".notebookpage").createNewFile()) {
                System.err.println("Couldn't create page! Report on the bug tracker");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.totalPages = getPageCount(BookFolder);
    }
    // Reads an existing page from storage
    protected String readPage(String path, int pagei) {
        StringBuilder text = new StringBuilder();
        try {
            File f = new File(path + "/" + pagei + ".notebookpage");
            Scanner r = new Scanner(f);
            while (r.hasNextLine()) {
                String d = r.nextLine();
                text.append(d);
            }
            r.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return text.toString();
    }
    // Overwrites an existing page
    protected void writePage(String data, String path, int pagei) {
        try {
            FileWriter w = new FileWriter(path + "/" + pagei + ".notebookpage");
            w.write(data);
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void init() {
        this.cursorIndex = readPage(BookFolder, pageIndex).length();;
        this.totalPages = getPageCount(BookFolder);
        // Add done/close button
        this.closeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).dimensions(this.width / 2 - 100, 196, 200, 20).build());

        // Page buttons
        int i = (this.width - 192) / 2;
        this.nextPageButton = this.addDrawableChild(new PageTurnWidget(i + 116, 159, true, (button) -> {
            this.goToNextPage();
        }, this.pageTurnSound));
        this.previousPageButton = this.addDrawableChild(new PageTurnWidget(i + 43, 159, false, (button) -> {
            this.goToPreviousPage();
        }, this.pageTurnSound));
        this.newPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 119, 155, 20, 20, 0, 0, 20, NEW_PAGE_ICON, 32, 64, (button) -> {
            newPage(BookFolder, totalPages);
            this.goToNextPage();
        }, Text.translatable("jwg.button.close")));

        this.updatePageButtons();
    }
    public boolean charTyped(char chr, int modifiers) {
        String pageContent = this.readPage(BookFolder, pageIndex);
        if (cursorIndex > pageContent.length()) {
            cursorIndex = pageContent.length();
        }
        pageContent = pageContent.substring(0, cursorIndex) + chr + pageContent.substring(cursorIndex);

        this.writePage(pageContent, BookFolder, pageIndex);
        this.cursorIndex += 1;
        return true;
    }

    // Button related functions
    protected void goToPreviousPage() {
        if (this.pageIndex > 0) {
            --this.pageIndex;
        }
        this.cursorIndex = readPage(BookFolder, pageIndex).length();
        this.updatePageButtons();
    }
    protected void goToNextPage() {
        if (this.pageIndex < this.totalPages- 1) {
            ++this.pageIndex;
        }
        this.cursorIndex = readPage(BookFolder, pageIndex).length();
        this.updatePageButtons();
    }
    private void updatePageButtons() {
        boolean onFinalPage = this.pageIndex == totalPages - 1;
        this.nextPageButton.visible = !onFinalPage;
        this.newPageButton.visible = onFinalPage;
        this.previousPageButton.visible = this.pageIndex > 0;
    }

    // Special keys (delete, backspace, etc)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            System.out.println(keyCode);
            switch (keyCode) {
                case 266 -> {
                    this.previousPageButton.onPress();
                    return true;
                }
                case 267 -> {
                    this.nextPageButton.onPress();
                    return true;
                }
                case 259 -> {
                    String pageContent = this.readPage(BookFolder, pageIndex);
                    if (cursorIndex > 0) {
                        pageContent = pageContent.substring(0, cursorIndex - 1) + pageContent.substring(cursorIndex);
                        this.writePage(pageContent, BookFolder, pageIndex);
                        this.cursorIndex -= 1;
                    }
                    return true;
                }
                case 261 -> {
                    String pageContent = this.readPage(BookFolder, pageIndex);
                    if (cursorIndex < pageContent.length()) {
                        pageContent = pageContent.substring(0, cursorIndex) + pageContent.substring(cursorIndex + 1);
                        this.writePage(pageContent, BookFolder, pageIndex);
                    }
                    return true;
                }
                case 262 -> {
                    String pageContent = this.readPage(BookFolder, pageIndex);
                    if (cursorIndex < pageContent.length()) {
                        cursorIndex += 1;
                    }
                    return true;
                }
                case 263 -> {
                    if (cursorIndex > 0) {
                        cursorIndex -= 1;
                    }
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }

    }

    // The code I am going to avoid like the plague
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int i = (this.width - 192) / 2;
        context.drawTexture(BOOK_TEXTURE, i, 2, 0, 0, 192, 192);
        if (this.totalPages > this.pageIndex) {
            String pageContent = readPage(BookFolder, pageIndex);
            // Cursor rendering
            // TODO : Make blink
            assert this.client != null;

            if (cursorIndex < pageContent.length()) {pageContent = pageContent.substring(0, cursorIndex) + "|" + pageContent.substring(cursorIndex);
            } else {cursorIndex = pageContent.length();}
            if (cursorIndex == pageContent.length()) {pageContent = pageContent + "_";}

            StringVisitable stringVisitable = StringVisitable.plain(pageContent);
            this.cachedPage = this.textRenderer.wrapLines(stringVisitable, 114);
            this.pageIndexText = Text.translatable("book.pageIndicator", this.pageIndex + 1, Math.max(this.totalPages, 1));
        }

        this.cachedPageIndex = this.pageIndex;
        int k = this.textRenderer.getWidth(this.pageIndexText);


        context.drawText(this.textRenderer, Text.of("Alpha Build - Expect bugs or missing features!"), 5, this.height - 22, Colors.RED, true);
        if (Notebook.DEV_ONLY) {
            context.drawText(this.textRenderer, Text.of("Notebook v3.0.0 - Development build"), 5, this.height - 10, Colors.WHITE, true);
        } else {
            context.drawText(this.textRenderer, Text.of("Notebook v3.0.0"), 5, this.height - 10, Colors.WHITE, true);
        }

        context.drawText(this.textRenderer, this.pageIndexText, i - k + 192 - 44, 18, 0, false);
        Objects.requireNonNull(this.textRenderer);
        int l = Math.min(128 / 9, this.cachedPage.size());

        for(int m = 0; m < l; ++m) {
            OrderedText orderedText = this.cachedPage.get(m);
            TextRenderer var10001 = this.textRenderer;
            int var10003 = i + 36;
            Objects.requireNonNull(this.textRenderer);
            context.drawText(var10001, orderedText, var10003, 32 + m * 9, 0, false);
        }

        super.render(context, mouseX, mouseY, delta);
    }


}
