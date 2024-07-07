package com.jwg.coord_book.screens;

import com.jwg.coord_book.CoordBook;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.jwg.coord_book.CoordBook.*;
import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;

@Environment(EnvType.CLIENT)
public class menuScreen extends Screen {

    boolean nextCharacterSpecial = false;
    public static int page = 0;
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");
    private List<OrderedText> cachedPage;
    private Text pageIndexText;
    private final boolean pageTurnSound;
    private String versionText;
    private String contents;

    public menuScreen(BookScreen.Contents contents) {
        this(contents, true);
    }

    private menuScreen(BookScreen.Contents contents, boolean bl) {
        super(NarratorManager.EMPTY);
        this.contents = "";
        this.versionText = "";
        this.cachedPage = Collections.emptyList();
        this.pageIndexText = ScreenTexts.NO;
        this.pageTurnSound = bl;

    }

    protected void init() {
        this.addButtons();
    }
    protected void removePage(int rmpage) {

        if (rmpage == 0) {
            LOGGER.info("Can't delete first page");
        } else if (rmpage == Objects.requireNonNull(new File(pageLocation+"/").list()).length-1) {
            goToPreviousPage();
            boolean tmp = new File(pageLocation+"/"+rmpage+".jdat").delete();
            LOGGER.info("Removed page " +rmpage);
        }  else {
            boolean tmp = new File(pageLocation+"/"+rmpage+".jdat").delete();
            try {
                System.out.println(pageLocation+"/"+rmpage+".jdat");
                tmp = new File(pageLocation+"/"+rmpage+".jdat").createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected void addButtons() {
        //Done button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (button) -> {
            assert this.client != null;
            this.client.setScreen(null);
        }));
        //Delete page button
        this.addDrawableChild(new TexturedButtonWidget(this.width -20, this.height-20, 20, 20, 0, 0, 20, DELETE_ICON, 32, 64, (button) -> {
            removePage(page);
        }, new TranslatableText("jwg.button.bookmenu")));

        //Page buttons (arrows)
        int i = (this.width - 192) / 2;
        this.addDrawableChild(new PageTurnWidget(i + 116, 159, true, (button) -> {
            this.goToNextPage();
            assert this.client != null;
            this.client.setScreen(this);
        }, this.pageTurnSound));
        this.addDrawableChild(new PageTurnWidget(i + 43, 159, false, (button) -> {
            this.goToPreviousPage();
            assert this.client != null;
            this.client.setScreen(this);
        }, this.pageTurnSound));

        if (developerMode) {
            this.versionText = "Coordinate Book "+version+" Developer build";
        } else {
            this.versionText = "Coordinate Book " + version;
        }
    }
    protected void goToPreviousPage() {
        --page;
        if (page <= -1) {
            page = 0;
        }
    }

    protected void goToNextPage() {
        ++page;
        if (!new File(pageLocation+"/"+page+".jdat").exists()) {
            try {
                if (new File(pageLocation+"/"+page+".jdat").createNewFile()) {
                    CoordBook.LOGGER.info("page {} has been created", page);
                }
            } catch (IOException e) {
                CoordBook.LOGGER.error("page {} is unable to be created", page);
                throw new RuntimeException(e);
            }
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
        int i = (this.width - 192) / 2;
        this.drawTexture(matrices, i, 2, 0, 0, 192, 192);
        this.pageIndexText = new TranslatableText("book.pageIndicator", page + 1, Math.max((Objects.requireNonNull(new File(pageLocation+"/").list()).length), 1));


        drawStringWithShadow(matrices, this.textRenderer, String.valueOf(versionText), 2, this.height - 10, 16777215);


        StringBuilder fulldata = new StringBuilder();
        try {
            Scanner readPageContent = new Scanner(new File(pageLocation+"/"+page+".jdat"));
            while (readPageContent.hasNextLine()) {
                String data = readPageContent.nextLine();
                if (!fulldata.toString().equals("")) {
                    data = "\n" + data;
                }
                fulldata.append(data);
            }
            readPageContent.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.contents = String.valueOf(fulldata);
        StringVisitable stringVisitable = StringVisitable.plain(this.contents);
        this.cachedPage = this.textRenderer.wrapLines(stringVisitable, 114);


        int k = this.textRenderer.getWidth(this.pageIndexText);
        this.textRenderer.draw(matrices, this.pageIndexText, (float)(i - k + 192 - 44), 18.0F, 1);


        Objects.requireNonNull(this.textRenderer);
        int l = Math.min(128 / 9, this.cachedPage.size());

        for(int m = 0; m < l; ++m) {
            OrderedText orderedText = this.cachedPage.get(m);
            TextRenderer var10000 = this.textRenderer;
            float var10003 = (float)(i + 36);
            Objects.requireNonNull(this.textRenderer);
            var10000.draw(matrices, orderedText, var10003, (float)(32 + m * 9), 0);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            Style style = this.getTextStyleAt(mouseX, mouseY);
            if (style != null && this.handleTextClick(style)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    int o = 0;
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int code = getExtendedKeyCodeForChar(keyCode);
        char key = (char) code;
        String keystring = String.valueOf(key).toLowerCase();
        if (developerMode) {
            System.out.println(code + "\n" + key);
        }
        ++o;
        if (code == 0) {
            keystring = "";
            nextCharacterSpecial = true;
            o = 1;
        } else if (code == 16777475){
            keystring = "";
            if (!Objects.equals(this.contents, "")) {
                this.contents = this.contents.substring(0, this.contents.length() - 1);
                try {
                    FileWriter updatePage = new FileWriter(new File(pageLocation+"/" + page + ".jdat"));
                    updatePage.write(this.contents);
                    updatePage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (nextCharacterSpecial && o == 2) {
            keystring = keystring.toUpperCase(Locale.ROOT);
            nextCharacterSpecial = false;
            //Avert your eyes from this awful code
            //If it works, don't touch it. And it does work, kinda...
            //Needs improvement but it works-ish.
            keystring = switch (keystring) {
                case "1" -> "!";
                case "2" -> "\"";
                case "3" -> "£";
                case "4" -> "$";
                case "5" -> "%";
                case "6" -> "^";
                case "7" -> "&";
                case "8" -> "*";
                case "9" -> "(";
                case "0" -> ")";
                case "-" -> "_";
                case "=" -> "+";
                case "[" -> "{";
                case "]" -> "}";
                case ";" -> ":";
                case "#" -> "~";
                case "\\" -> "|";
                case "," -> "<";
                case "." -> ">";
                case "/" -> "?";
                case "'" -> "@";
                default -> keystring;
            };
        }
        StringBuilder fulldata = new StringBuilder();
        try {
            Scanner readPageContent = new Scanner(new File(pageLocation+"/"+page+".jdat"));
            while (readPageContent.hasNextLine()) {
                String data = readPageContent.nextLine();
                if (!fulldata.toString().equals("")) {
                    data = "\n" + data;
                }
                fulldata.append(data);
            }
            readPageContent.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fulldata = new StringBuilder(fulldata + keystring);
        this.contents = String.valueOf(fulldata);
        try {
            FileWriter updatePage = new FileWriter(new File(pageLocation+"/"+page+".jdat"));
            updatePage.write(String.valueOf(fulldata));
            updatePage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client.setScreen(this);
        return false;
    }

    @Nullable
    public Style getTextStyleAt(double x, double y) {
        if (this.cachedPage.isEmpty()) {
            return null;
        } else {
            int i = MathHelper.floor(x - (double)((this.width - 192) / 2) - 36.0);
            int j = MathHelper.floor(y - 2.0 - 30.0);
            if (i >= 0 && j >= 0) {
                Objects.requireNonNull(this.textRenderer);
                int k = Math.min(128 / 9, this.cachedPage.size());
                if (i <= 114) {
                    Objects.requireNonNull(this.client.textRenderer);
                    if (j < 9 * k + k) {
                        Objects.requireNonNull(this.client.textRenderer);
                        int l = j / 9;
                        if (l >= 0 && l < this.cachedPage.size()) {
                            OrderedText orderedText = (OrderedText)this.cachedPage.get(l);
                            return this.client.textRenderer.getTextHandler().getStyleAt(orderedText, i);
                        }

                        return null;
                    }
                }

                return null;
            } else {
                return null;
            }
        }
    }
}