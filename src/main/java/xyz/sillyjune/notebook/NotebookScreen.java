package xyz.sillyjune.notebook;

import net.minecraft.client.MinecraftClient;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static xyz.sillyjune.notebook.Notebook.*;

public class NotebookScreen extends Screen {
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");
    private static NotebookData DATA;

    public static ButtonWidget closeButton;
    public static ButtonWidget buttonGo;
    public static ButtonWidget buttonNext;
    public static ButtonWidget buttonLast;
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
    protected int getPageCount() {
        return DATA.content().length;
    }
    /// Creates a new blank page
    protected void newPage() {
        String[] pages = DATA.content();
        String[] new_pages = new String[pages.length+1];
        int i = 0;
        for (String page : pages) {
            new_pages[i] = page;
            i++;
        }
        new_pages[new_pages.length-1] = " ";
        DATA = new NotebookData(new_pages, DATA.location());
        NotebookData.write(DATA);
        totalPages += 1;
    }
    // Removes the last page
    protected void delPage() {
        String[] pages = DATA.content();
        String[] new_pages = new String[pages.length-1];
        int i = 0;
        for (String _ : new_pages) {
            new_pages[i] = pages[i];
            i++;
        }
        DATA = new NotebookData(new_pages, DATA.location());
        NotebookData.write(DATA);
        totalPages -= 1;
    }
    // Reads an existing page from storage
    protected String readPage(int pagei) {
        if (pagei >= DATA.content().length) {
            newPage();
        }
        String content = DATA.content()[pagei];
        if (content == null) {
            return "";
        }
        return content;
    }
    // Overwrites an existing page
    protected void writePages() {
        NotebookData.write(DATA);
    }

    // Get index of book in folder
    protected int getBookIndex() {
        for (int i = 0; i < Objects.requireNonNull(new File(STR."\{BOOK_FOLDER}/").list()).length; i++) {
            if (Objects.equals(Objects.requireNonNull(new File(STR."\{BOOK_FOLDER}/").list())[i], DATA.location())) {
                return i;
            }
        }
        return 0;
    }
    // Innit mate
    protected void init() {
        DATA = NotebookData.read("default.json");
        pageIndex = 0;
        this.cursorIndex = readPage(pageIndex).length();
        this.totalPages = getPageCount();
        // Add done/close button
        closeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (_) -> {
            this.close();
        }).dimensions(this.width / 2 - 100, 196, 200, 20).build());

        // Page buttons
        int i = (this.width - 192) / 2;
        this.nextPageButton = this.addDrawableChild(new PageTurnWidget(i + 116, 159, true, (_) -> {
            this.goToNextPage();
        }, this.pageTurnSound));
        this.previousPageButton = this.addDrawableChild(new PageTurnWidget(i + 43, 159, false, (_) -> {
            this.goToPreviousPage();
        }, this.pageTurnSound));

        this.newPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 119, 155, 20, 20, NEW_PAGE_ICON, (_) -> {
            newPage();
            this.goToNextPage();
        }, Text.translatable("notebook.button.new")));
        this.delPageButton = this.addDrawableChild(new TexturedButtonWidget(i + 99, 155, 20, 20, DEL_PAGE_ICON, (_) -> {
            if (totalPages > 1) {
                delPage();
                this.goToPreviousPage();
            }
        }, Text.translatable("notebook.button.delete")));

        // Top bar buttons
        this.bookNameField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, 5, 5, 108, 20, Text.translatable("notebook.text.field")));
        this.bookNameField.setEditable(true);
        this.bookNameField.setText("default");
       buttonNext = this.addDrawableChild(new TexturedButtonWidget(5, 30, 20, 20, NEXT_BOOK_ICON, (button) -> {
            int bookIndex = getBookIndex();
            if (bookIndex < Objects.requireNonNull(new File(STR."\{BOOK_FOLDER}/").list()).length - 1) {
                DATA = NotebookData.read(Objects.requireNonNull(new File(STR."\{BOOK_FOLDER}/").list())[bookIndex + 1]);
                this.bookNameField.setText(DATA.location().replace(".json", ""));
                this.pageIndex = 0;
                this.totalPages = DATA.content().length;
                this.updatePageButtons();
            }
        }, Text.translatable("notebook.button.next")));
        buttonLast = this.addDrawableChild(new TexturedButtonWidget(30, 30, 20, 20, LAST_BOOK_ICON, (_) -> {
            int bookIndex = getBookIndex();
            if (bookIndex > 0) {
                DATA = NotebookData.read(Objects.requireNonNull(new File(STR."\{BOOK_FOLDER}/").list())[bookIndex - 1]);
                this.bookNameField.setText(DATA.location().replace(".json", ""));
                this.pageIndex = 0;
                this.totalPages = DATA.content().length;
                this.updatePageButtons();
            }
        }, Text.translatable("notebook.button.rename")));
        buttonGo = this.addDrawableChild(new TexturedButtonWidget(55, 30, 20, 20, RENAME_BOOK_ICON, (_) -> {
            DATA = new NotebookData(DATA.content(), STR."\{this.bookNameField.getText()}.json");
        }, Text.translatable("notebook.button.rename")));



        this.updatePageButtons();
    }


    // Button related functions
    protected void goToPreviousPage() {
        if (this.pageIndex > 0) { --this.pageIndex; }
        this.cursorIndex = readPage(pageIndex).length();
        this.updatePageButtons();
    }
    protected void goToNextPage() {
        if (this.pageIndex < this.totalPages- 1) { ++this.pageIndex; }
        this.cursorIndex = readPage(pageIndex).length();
        this.updatePageButtons();
    }
    private void updatePageButtons() {
        boolean onFinalPage = this.pageIndex == totalPages - 1;
        this.nextPageButton.visible = !onFinalPage;
        this.newPageButton.visible = onFinalPage;
        this.delPageButton.visible = onFinalPage;
        this.previousPageButton.visible = this.pageIndex > 0;
    }
    // Deselects all buttons


    // Special keys (delete, backspace, etc)
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.bookNameField.isSelected()) {
            if (CONFIG.debug()) {
                System.out.println(keyCode);
            }
            switch (keyCode) {
                case 266 -> { this.previousPageButton.onPress(); return true; }
                case 267 -> { this.nextPageButton.onPress(); return true; }
                case 259 -> {
                    String pageContent = this.readPage(pageIndex);
                    if (cursorIndex > 0) {
                        pageContent = pageContent.substring(0, cursorIndex - 1) + pageContent.substring(cursorIndex);
                        String[] content = DATA.content();
                        content[pageIndex] = pageContent;
                        DATA = new NotebookData(content, DATA.location());
                        NotebookData.write(DATA);
                        this.cursorIndex -= 1;
                    }
                    return true;
                }
                case 261 -> {
                    String pageContent = this.readPage(pageIndex);
                    if (cursorIndex < pageContent.length()) {
                        pageContent = pageContent.substring(0, cursorIndex) + pageContent.substring(cursorIndex + 1);
                        String[] content = DATA.content();
                        content[pageIndex] = pageContent;
                        DATA = new NotebookData(content, DATA.location());
                        NotebookData.write(DATA);
                    }
                    return true;
                }
                case 262 -> {
                    String pageContent = this.readPage( pageIndex);
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
                default -> { return false; }
            }
        } else {
            if (keyCode == 259 && !this.bookNameField.getText().isEmpty()) {
                this.bookNameField.setText(this.bookNameField.getText().substring(0, this.bookNameField.getText().length() - 1));
            }
            return true;
        }
    }
    // Normal typing
    @Override
    public boolean charTyped(char chr, int modifiers) {
        System.out.println(STR."\{chr} \{modifiers}");
        if (!this.bookNameField.isSelected()) {
            String pageContent = this.readPage(pageIndex);
            if (cursorIndex > pageContent.length()) { cursorIndex = pageContent.length(); }
            pageContent = pageContent.substring(0, cursorIndex) + chr + pageContent.substring(cursorIndex);
            String[] content = DATA.content();
            content[pageIndex] = pageContent;
            DATA = new NotebookData(content, DATA.location());
            NotebookData.write(DATA);
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
        context.drawText(this.textRenderer, Text.of("Beta Build - Expect minor bugs or missing features!"), 5, this.height - 22, Colors.RED / 2, true);
        if (CONFIG.debug()) {
            context.drawText(this.textRenderer, Text.of(STR."Notebook v4.0.0 - \{Text.translatable("devwarning.info").getString()}"), 5, this.height - 10, Colors.WHITE, true);
        } else {
            context.drawText(this.textRenderer, Text.of("Notebook v4.0.0"), 5, this.height - 10, Colors.WHITE, true);
        }
    }
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = (this.width - 192) / 2;
        super.renderBackground(context, mouseX, mouseY, delta);
        if (this.totalPages > this.pageIndex) {
            String pageContent = readPage(pageIndex);
            // Cursor rendering
            assert this.client != null;
            if (cursorIndex < pageContent.length()) {pageContent = STR."\{pageContent.substring(0, cursorIndex)}|\{pageContent.substring(cursorIndex)}";
            } else {cursorIndex = pageContent.length();}
            if (cursorIndex == pageContent.length() && System.currentTimeMillis() % 2000 > 1000) {pageContent = STR."\{pageContent}_";}

            StringVisitable stringVisitable = StringVisitable.plain(pageContent);
            this.cachedPage = this.textRenderer.wrapLines(stringVisitable, 114);
            this.pageIndexText = Text.translatable("book.pageIndicator", this.pageIndex + 1, Math.max(this.totalPages, 1));
        }
        int k = this.textRenderer.getWidth(this.pageIndexText);
        context.drawTexture(BOOK_TEXTURE, (this.width - 192) / 2, 2, 0, 0, 192, 192);
        Objects.requireNonNull(this.textRenderer);
        int l = Math.min(128 / 9, this.cachedPage.size());
        for(int m = 0; m < l; ++m) {
            OrderedText orderedText = this.cachedPage.get(m);
            TextRenderer var10001 = this.textRenderer;
            int var10003 = i + 36;
            Objects.requireNonNull(this.textRenderer);
            context.drawText(var10001, orderedText, var10003, 32 + m * 9, 0, false);
        }
        context.drawText(this.textRenderer, this.pageIndexText, i - k + 192 - 44, 18, 0, false);
    }

}
