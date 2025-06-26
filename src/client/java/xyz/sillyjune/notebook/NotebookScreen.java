package xyz.sillyjune.notebook;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Colors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.*;
import static xyz.sillyjune.notebook.Notebook.*;

public class NotebookScreen extends Screen {

    public int bookOffset = 0;
    private static NotebookData DATA;
    public static ButtonWidget closeButton;
    public static ButtonWidget buttonRenameBook;
    public static ButtonWidget buttonNewBook;
    public static ButtonWidget buttonIncreaseOffset;
    public static ButtonWidget buttonDecreaseOffset;
    public ButtonWidget[] switchableBooks = { null, null, null, null, null };
    private int pageIndex;
    private int cursorIndex;
    private List<OrderedText> cachedPage;
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
        this.pageTurnSound = playPageTurnSound;
    }
    /// Creates a new blank page
    protected void newPage() {
        String[] new_pages = new String[DATA.content.length+1];
        int i = 0;
        for (String page : DATA.content) {
            new_pages[i] = page; i++;
        }
        new_pages[new_pages.length-1] = " ";
        DATA.content = new_pages; DATA.write();
        this.goToNextPage();
    }
    // Removes the last page
    protected void delPage() {
        String[] new_pages = new String[DATA.content.length-1];
        for (int i = 0; i < new_pages.length; i++) {
            new_pages[i] = DATA.content[i]; i++;
        }
        DATA.content = new_pages; DATA.write();
        this.goToPreviousPage();
    }
    // Reads an existing page from storage
    protected String readPage(int pagei) {
        if (pagei >= DATA.content.length) { newPage(); }
        // Prevents a crash if pages become corrupted somehow
        return Objects.requireNonNullElse(DATA.content[pagei], "");
    }
    void goto_book(String book) {
        int bIdx = 0;
        for (String iter : Objects.requireNonNull(new File(BOOK_FOLDER).list())) {
            if (Objects.equals(iter, book)) {
                break;
            }
            bIdx += 1;
        }
        DATA = NotebookData.read(Objects.requireNonNull(new File(BOOK_FOLDER).list())[bIdx]);
        this.bookNameField.setText(DATA.location.replace(".json", ""));
        this.pageIndex = 0;
        this.updatePageButtons();

    }
    protected void goToPreviousPage() {
        if (this.pageIndex > 0) { --this.pageIndex; }
        this.cursorIndex = readPage(pageIndex).length();
        this.updatePageButtons();
    }
    protected void goToNextPage() {
        if (this.pageIndex < DATA.content.length - 1) { ++this.pageIndex; }
        this.cursorIndex = readPage(pageIndex).length();
        this.updatePageButtons();
    }

    String getBookNameText() {
        return this.bookNameField.getText().replace(" ", "-");
    }

    void initBookSwitching() {
        String[] books = Objects.requireNonNull(new File(BOOK_FOLDER).list());
        int replaced = 0;
        for (int it = 0; it != 5; it++) {
            if (it+bookOffset > books.length - 1) {
                break;
            }
            replaced = it;
            String book = books[it+bookOffset];
            if (this.switchableBooks[it] != null) {
                this.switchableBooks[it].visible = false;
            }
            this.switchableBooks[it] = this.addDrawableChild(ButtonWidget.builder(Text.literal(book.replace(".json", "")), (button) -> goto_book(book)).dimensions(5, 30 + (25 * (it)), 108, 20).build());
        }
        for (int it = replaced+1; it != 5; it++) {
            if (this.switchableBooks[it] != null) {
                this.switchableBooks[it].visible = false;
                this.switchableBooks[it] = null;
            }
        }
    }

    // Innit mate
    protected void init() {
        DATA = NotebookData.read("default.json");
        pageIndex = 0;

        // Add done/close button
        closeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).dimensions(this.width / 2 - 100, 196, 200, 20).build());
        // Page buttons
        int i = (this.width - 192) / 2;
        this.nextPageButton = this.addDrawableChild(new PageTurnWidget(i + 116, 159, true, (button) -> this.goToNextPage(), this.pageTurnSound));
        this.previousPageButton = this.addDrawableChild(new PageTurnWidget(i + 43, 159, false, (button) -> this.goToPreviousPage(), this.pageTurnSound));
        this.newPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 119, 155, 20, 20, NEW_PAGE_ICON, (button) -> newPage()));
        this.delPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 99, 155, 20, 20, DEL_PAGE_ICON, (button) -> delPage()));
        // Top bar buttons
        this.bookNameField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, 5, 5, 108, 20, Text.translatable("notebook.text.field")));
        this.bookNameField.setEditable(true);
        this.bookNameField.setText("default");

        buttonIncreaseOffset = this.addDrawableChild(new TexturedButtonWidget(5, 155, 20, 20, LAST_BOOK_ICON, (button) -> {
            bookOffset += 1;
            initBookSwitching();
        }));
        buttonDecreaseOffset = this.addDrawableChild(new TexturedButtonWidget(30, 155, 20, 20, NEXT_BOOK_ICON, (button) -> {
            if (bookOffset > 0) {
                bookOffset -= 1;
                initBookSwitching();
            }
        }));

        buttonNewBook = this.addDrawableChild(new TexturedButtonWidget(143, 5, 20, 20, NEW_BOOK_ICON, (button) -> {
            String[] books = Objects.requireNonNull(new File(BOOK_FOLDER).list());
            boolean found = false;
            for (String iter : books) {
                if (iter.equals(getBookNameText() + ".json")) {
                    found = true;
                    break;
                }
            }
            if (!this.bookNameField.getText().isEmpty() && !found) {
                DATA = new NotebookData(new String[1], getBookNameText() + ".json");
                DATA.write();
            }
            initBookSwitching();
        }
        ));
        buttonRenameBook = this.addDrawableChild(new TexturedButtonWidget(118, 5, 20, 20, RENAME_BOOK_ICON, (button) -> {
            String[] books = Objects.requireNonNull(new File(BOOK_FOLDER).list());
            boolean found = false;
            for (String iter : books) {
                if (iter.equals(getBookNameText() + ".json")) {
                    found = true;
                    break;
                }
            }
            if (!this.bookNameField.getText().isEmpty() && !found) {
                String[] bookData = DATA.content;
                try {
                    delete(Path.of(BOOK_FOLDER + "/" + DATA.location));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DATA = new NotebookData(bookData, getBookNameText() + ".json");
                DATA.write();
            }
            initBookSwitching();
        }
        ));
        initBookSwitching();

        this.updatePageButtons();
        this.cursorIndex = readPage(pageIndex).length();
    }
    // Refresh page buttons
    private void updatePageButtons() {
        boolean onFinalPage = this.pageIndex == DATA.content.length - 1;
        this.nextPageButton.visible = !onFinalPage;
        this.newPageButton.visible = onFinalPage;
        this.delPageButton.visible = onFinalPage;
        this.previousPageButton.visible = this.pageIndex > 0;
    }
    // Special keys (delete, backspace, etc)
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.bookNameField.isSelected()) {
            if (CONFIG.debug()) { LOGGER.debug(String.valueOf(keyCode)); }
            switch (keyCode) {
                case 266 -> this.previousPageButton.onPress();
                case 267 -> this.nextPageButton.onPress();
                case 259 -> {
                    // Backspace
                    if (cursorIndex > 0) {
                        DATA.content[pageIndex] = DATA.content[pageIndex].substring(0, cursorIndex - 1) + DATA.content[pageIndex].substring(cursorIndex); DATA.write();
                        this.cursorIndex -= 1;
                    }
                }
                case 261 -> {
                    // Delete key
                    if (cursorIndex < DATA.content[pageIndex].length()) {
                        DATA.content[pageIndex] = DATA.content[pageIndex].substring(0, cursorIndex) + DATA.content[pageIndex].substring(cursorIndex + 1); DATA.write();
                    }
                }
                case 262 -> { if (cursorIndex < DATA.content[pageIndex].length()) { cursorIndex += 1; }  }
                case 263 -> { if (cursorIndex > 0) { cursorIndex -= 1; } }
                case 257 ->  {
                    DATA.content[pageIndex] = DATA.content[pageIndex].substring(0, cursorIndex) + "\n" + DATA.content[pageIndex].substring(cursorIndex);
                    this.cursorIndex += 1;
                }
                default -> { return false; }
            }
        } else {
            if (keyCode == 259 && !this.bookNameField.getText().isEmpty()) {
                this.bookNameField.setText(this.bookNameField.getText().substring(0, this.bookNameField.getText().length() - 1));
            } else if (keyCode == 257 && !this.bookNameField.getText().isEmpty()) {
                DATA = new NotebookData(DATA.content,this.bookNameField.getText() + ".json");
            }

        }
        return true;
    }
    // Normal typing
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.bookNameField.isSelected()) {
            if (DATA.content[pageIndex] == null) {
                DATA.content[pageIndex] = " "; // Prevents it from crashing when null values are created, though this shouldn't be able to happen
            }
            if (cursorIndex > DATA.content[pageIndex].length()) {
                cursorIndex = DATA.content[pageIndex].length();
            }
            DATA.content[pageIndex] = DATA.content[pageIndex].substring(0, cursorIndex) + chr + DATA.content[pageIndex].substring(cursorIndex);
            DATA.write();
            this.cursorIndex += 1;
            return true;
        } else {
            this.bookNameField.setText(this.bookNameField.getText() + chr);
        }
        return false;
    }
    // The code I am going to avoid like the plague
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        if (GAY) { context.drawText(this.textRenderer, Text.translatable("notebook.gay"), 5, this.height - 22, Colors.WHITE, true); }
        if (CONFIG.debug()) {
            context.drawText(this.textRenderer, Text.of("Notebook v" + VERSION + " " + Text.translatable("devwarning.info").getString()), 5, this.height - 10, Colors.WHITE, true);
        } else {
            context.drawText(this.textRenderer, Text.of("Notebook v" + VERSION), 5, this.height - 10, Colors.WHITE, true);
        }
    }
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        if (DATA.content.length > this.pageIndex) {
            String pageContent = readPage(pageIndex);
            // Cursor timing
            if (cursorIndex < pageContent.length()) { pageContent = pageContent.substring(0, cursorIndex)+"|" +pageContent.substring(cursorIndex); }
            else { cursorIndex = pageContent.length(); }
            if (cursorIndex == pageContent.length() && System.currentTimeMillis() % 2000 > 1000) {pageContent = pageContent + "_";}
            // Render cursor and book content
            this.cachedPage = this.textRenderer.wrapLines(StringVisitable.plain(pageContent), 114);
        }
        // Render book background
        context.drawTexture(RenderLayer::getGuiTextured, BOOK_TEXTURE, (this.width - 192) / 2, 2, 0.0F, 0.0F, 192, 192, 256, 256);
        //context.drawTexture(RenderLayer::getGuiTextured, BOOK_TEXTURE, (this.width - 192) / 2, 2, 0, 0, 192, 192);
        for(int m = 0; m < Math.min(128 / 9, this.cachedPage.size()); ++m) {
            context.drawText(this.textRenderer, this.cachedPage.get(m), ((this.width - 192) / 2) + 36, 32 + m * 9, 0, false);
        }
        // long
        context.drawText(this.textRenderer, Text.translatable("book.pageIndicator", this.pageIndex + 1, Math.max(DATA.content.length, 1)), ((this.width - 192) / 2) - this.textRenderer.getWidth(Text.translatable("book.pageIndicator", this.pageIndex + 1, Math.max(DATA.content.length, 1))) + 192 - 44, 18, 0, false);
    }
}
