package com.jwg.coord_book.screens;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PlainTextButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jwg.coord_book.CoordBook.*;
import static net.minecraft.client.gui.screen.ingame.BookScreen.EMPTY_PROVIDER;

@Environment(EnvType.CLIENT)
public class menuScreen extends Screen {
    static int page = 0;
    public static Text PAGENO = Text.literal("Page " + page);
    public static String fileToRead = "CoordinateBook/"+page+".json";
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");

    public menuScreen(BookScreen.Contents contents) {
        this(contents, true);
    }
    public menuScreen() {
        this(EMPTY_PROVIDER, false);
    }
    public menuScreen(BookScreen.Contents contents, boolean bl) {
        super(NarratorManager.EMPTY);
    }

    protected void init() {
        this.addCloseButton();
        this.addMenuButtons();
    }
    protected void addCloseButton() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (button) -> {
            assert this.client != null;
            this.client.setScreen((Screen)null);
        }));
    }
    protected void addMenuButtons() {
        try {
            this.addDrawableChild(new PlainTextButtonWidget(this.width - 192 / 2, 15, textRenderer.getWidth(Files.readString(Paths.get(fileToRead))), 10, Text.literal(Files.readString(Paths.get(fileToRead))), (buttonWidget) -> {
            }, this.textRenderer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addDrawableChild(new PlainTextButtonWidget(this.width - textRenderer.getWidth(PAGENO) - 2, this.height - 10, textRenderer.getWidth(PAGENO), 10, PAGENO, (buttonWidget) -> {
        }, this.textRenderer));

        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 120, 196, 20, 20, 0, 0, 20, BACK_ICON, 32, 64, (button) -> {
            page = page-1;
            if (page <= -1) {
                page = 0;
            }
            PAGENO = Text.literal("Page " + page);
            assert this.client != null;
            this.client.setScreen(this);
            fileToRead = "CoordinateBook/"+page+".json";
        }, Text.translatable("jwg.button.back")));
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 100, 196, 20, 20, 0, 0, 20, NEXT_ICON, 32, 64, (button) -> {
            page = page+1;
            PAGENO = Text.literal("Page " + page);
            assert this.client != null;
            this.client.setScreen(this);
            try {
                File pageF = new File("CoordinateBook/"+page+".json");
                if (pageF.createNewFile()){
                    LOGGER.info("Created page " + page);
                }
                else{
                    LOGGER.info("Page {} already exists, reading instead..", page);
                    //TODO: Read & display page
                    fileToRead = "CoordinateBook/"+page+".json";
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }, Text.translatable("jwg.button.forwards")));

    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
        int i = (this.width - 192) / 2;
        this.drawTexture(matrices, i, 2, 0, 0, 192, 192);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void closeBookScreen() {
        assert this.client != null;
        this.client.setScreen((Screen)null);
    }
}