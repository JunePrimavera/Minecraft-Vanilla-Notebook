package com.june.notebook;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.june.notebook.Notebook.*;

public class NotebookScreen extends Screen {
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");

    private final String BookFolder = "Notebook";
    public static String BookName = "Default";
    private int pageIndex;
    private int totalPages;
    private int cursorIndex;
    private List<OrderedText> cachedPage;
    private Text pageIndexText;
    private TextFieldWidget bookNameField;
    private ButtonWidget newPageButton;
    private ButtonWidget delPageButton;
    private PageTurnWidget nextPageButton;
    private PageTurnWidget previousPageButton;
    private final boolean pageTurnSound;


    public NotebookScreen() {
        this(false);
    }

    private NotebookScreen(boolean playPageTurnSound) {
        super(NarratorManager.EMPTY);
        this.cachedPage = Collections.emptyList();
        this.pageIndexText = ScreenTexts.EMPTY;
        this.pageTurnSound = playPageTurnSound;
    }

    // Returns the amount of pages stored
    protected int getPageCount(String path) {
        if (new File(path).exists()) {
            return Objects.requireNonNull(new File(path).list()).length;
        }
        return 0;
    }
    /// Creates a new blank page
    protected void newPage(String path, int pagei) {
        try {
            if (!new File(path).exists()) {
                if (!new File(path).mkdirs()) {
                    System.err.println("Couldn't create page! Report on the bug tracker");
                }
            }
            if (!new File(path + "/" + pagei + ".notebookpage").createNewFile()) {
                System.err.println("Couldn't create page! Report on the bug tracker");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.totalPages = getPageCount(BookFolder + "/" + BookName);
    }
    // Removes the last page
    protected void delPage(String path) {
        if (!new File(path + "/" + (totalPages-1) + ".notebookpage").delete()) {
            System.err.println("Couldn't remove page! Report on the bug tracker");
        }
        this.totalPages = getPageCount(BookFolder + "/" + BookName);
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

    // Get index of book in folder
    protected int getBookIndex() {
        for (int i = 0; i < Objects.requireNonNull(new File(BookFolder + "/").list()).length; i++) {
            if (Objects.equals(Objects.requireNonNull(new File(BookFolder + "/").list())[i], BookName)) {
                return i;
            }
        }
        return 0;
    }
    // Innit mate
    protected void init() {
        this.totalPages = getPageCount(BookFolder + "/" + BookName);
        if (totalPages < 1) {
            newPage(BookFolder + "/" + BookName, 0);
        }
        this.cursorIndex = readPage(BookFolder + "/" + BookName, pageIndex).length();
        // Add done/close button
        ButtonWidget closeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
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
            newPage(BookFolder + "/" + BookName, totalPages);
            this.goToNextPage();
        }, Text.translatable("notebook.button.new")));
        this.delPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 99, 155, 20, 20, 0, 0, 20, DEL_PAGE_ICON, 32, 64, (button) -> {
            if (totalPages > 1) {
                delPage(BookFolder + "/" + BookName);
                this.goToPreviousPage();
            }
        }, Text.translatable("notebook.button.delete")));

        // Top bar buttons
        this.bookNameField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, 5, 5, 108, 20, Text.translatable("notebook.text.field")));
        this.bookNameField.setEditable(true);
        this.bookNameField.setText(BookName);

        ButtonWidget nextBookButton = this.addDrawableChild(new TexturedButtonWidget(5, 30, 20, 20, 0, 0, 20, NEXT_BOOK_ICON, 32, 64, (button) -> {
            int bookIndex = getBookIndex();
            if (bookIndex < Objects.requireNonNull(new File(BookFolder + "/").list()).length - 1) {
                BookName = Objects.requireNonNull(new File(BookFolder + "/").list())[bookIndex + 1];
                this.bookNameField.setText(BookName);
                this.updatePageButtons();
            }
        }, Text.translatable("notebook.button.next")));
        ButtonWidget lastBookButton = this.addDrawableChild(new TexturedButtonWidget(30, 30, 20, 20, 0, 0, 20, LAST_BOOK_ICON, 32, 64, (button) -> {
            int bookIndex = getBookIndex();
            if (bookIndex > 0) {
                BookName = Objects.requireNonNull(new File(BookFolder + "/").list())[bookIndex - 1];
                this.bookNameField.setText(BookName);
                this.updatePageButtons();
            }
        }, Text.translatable("notebook.button.last")));
        this.updatePageButtons();
    }


    // Button related functions
    protected void goToPreviousPage() {
        if (this.pageIndex > 0) { --this.pageIndex; }
        this.cursorIndex = readPage(BookFolder + "/" + BookName, pageIndex).length();
        this.updatePageButtons();
    }
    protected void goToNextPage() {
        if (this.pageIndex < this.totalPages- 1) { ++this.pageIndex; }
        this.cursorIndex = readPage(BookFolder + "/" + BookName, pageIndex).length();
        this.updatePageButtons();
    }
    private void updatePageButtons() {
        boolean onFinalPage = this.pageIndex == totalPages - 1;
        this.nextPageButton.visible = !onFinalPage;
        this.newPageButton.visible = onFinalPage;
        this.delPageButton.visible = onFinalPage;
        this.previousPageButton.visible = this.pageIndex > 0;
    }

    // Special keys (delete, backspace, etc)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (!this.bookNameField.isSelected()) {
            if (super.keyPressed(keyCode, scanCode, modifiers)) { return true;
            } else {
                if (Notebook.DEV_ONLY) {
                    System.out.println(keyCode);
                }
                switch (keyCode) {
                    case 266 -> { this.previousPageButton.onPress(); return true; }
                    case 267 -> { this.nextPageButton.onPress(); return true; }
                    case 259 -> {
                        String pageContent = this.readPage(BookFolder + "/" + BookName, pageIndex);
                        if (cursorIndex > 0) {
                            pageContent = pageContent.substring(0, cursorIndex - 1) + pageContent.substring(cursorIndex);
                            this.writePage(pageContent, BookFolder + "/" + BookName, pageIndex);
                            this.cursorIndex -= 1;
                        }
                        return true;
                    }
                    case 261 -> {
                        String pageContent = this.readPage(BookFolder + "/" + BookName, pageIndex);
                        if (cursorIndex < pageContent.length()) {
                            pageContent = pageContent.substring(0, cursorIndex) + pageContent.substring(cursorIndex + 1);
                            this.writePage(pageContent, BookFolder + "/" + BookName, pageIndex);
                        }
                        return true;
                    }
                    case 262 -> { String pageContent = this.readPage(BookFolder + "/" + BookName, pageIndex); if (cursorIndex < pageContent.length()) { cursorIndex += 1; } return true; }
                    case 263 -> { if (cursorIndex > 0) { cursorIndex -= 1; } return true; }
                    default -> { return false; }
                }
            }
        } else {
            if (keyCode == 257) {
                this.bookNameField.setFocused(false);
            } else if (keyCode == 259 && this.bookNameField.getText().length() > 0) {
                this.bookNameField.setText(this.bookNameField.getText().substring(0, this.bookNameField.getText().length() - 1));
            }
            return true;
        }
    }
    // Normal typing
    public boolean charTyped(char chr, int modifiers) {
        if (!this.bookNameField.isSelected()) {
            String pageContent = this.readPage(BookFolder + "/" + BookName, pageIndex);
            if (cursorIndex > pageContent.length()) { cursorIndex = pageContent.length(); }
            pageContent = pageContent.substring(0, cursorIndex) + chr + pageContent.substring(cursorIndex);
            this.writePage(pageContent, BookFolder + "/" + BookName, pageIndex);
            this.cursorIndex += 1;
            return true;
        } else {
            this.bookNameField.setText(this.bookNameField.getText() + chr);
        }
        return false;
    }

    // The code I am going to avoid like the plague
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int i = (this.width - 192) / 2;
        context.drawTexture(BOOK_TEXTURE, i, 2, 0, 0, 192, 192);
        if (this.totalPages > this.pageIndex) {
            String pageContent = readPage(BookFolder + "/" + BookName, pageIndex);
            // Cursor rendering
            assert this.client != null;
            if (cursorIndex < pageContent.length()) {pageContent = pageContent.substring(0, cursorIndex) + "|" + pageContent.substring(cursorIndex);
            } else {cursorIndex = pageContent.length();}
            if (cursorIndex == pageContent.length() && System.currentTimeMillis() % 2000 > 1000) {pageContent = pageContent + "_";}

            StringVisitable stringVisitable = StringVisitable.plain(pageContent);
            this.cachedPage = this.textRenderer.wrapLines(stringVisitable, 114);
            this.pageIndexText = Text.translatable("book.pageIndicator", this.pageIndex + 1, Math.max(this.totalPages, 1));
        }
        int k = this.textRenderer.getWidth(this.pageIndexText);
        context.drawText(this.textRenderer, Text.of("Alpha Build - Expect bugs or missing features!"), 5, this.height - 22, Colors.RED, true);
        if (Notebook.DEV_ONLY) {
            context.drawText(this.textRenderer, Text.of("Notebook v3.0.0 - " + Text.translatable("devwarning.info").getString()), 5, this.height - 10, Colors.WHITE, true);
        } else {
            context.drawText(this.textRenderer, Text.of("Notebook v3.0.0"), 5, this.height - 10, Colors.WHITE, true);
        }
        if (!Objects.equals(this.bookNameField.getText(), BookName) && !Objects.equals(this.bookNameField.getText(), "")) {
           boolean bookExists = false;
            for (int it = 0; it == Objects.requireNonNull(new File(BookFolder).list()).length; it++) {
                if (Objects.equals(BookName, Objects.requireNonNull(new File(BookFolder).list())[it])) {
                    bookExists = true;
                }
            }
            if (!bookExists) {
                if (!new File(BookFolder + "/" + BookName).renameTo(new File(BookFolder + "/" + this.bookNameField.getText()))) {
                    System.err.println("Couldn't change book name! Make a bug report.");
                } else {
                    BookName = bookNameField.getText();
                }

            }
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
