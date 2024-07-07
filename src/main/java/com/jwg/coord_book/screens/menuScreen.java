package com.jwg.coord_book.screens;

import com.jwg.coord_book.CoordBook;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class menuScreen extends Screen {

    public static int page = 0;
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");
    private final List<OrderedText> cachedPage;
    private Text pageIndexText;
    private final boolean pageTurnSound;

    public menuScreen(BookScreen.Contents contents) {
        this(contents, true);
    }

    private menuScreen(BookScreen.Contents contents, boolean bl) {
        super(NarratorManager.EMPTY);
        this.cachedPage = Collections.emptyList();
        this.pageIndexText = ScreenTexts.EMPTY;
        this.pageTurnSound = bl;
    }

    protected void init() {
        this.addButtons();
    }
    protected void addButtons() {
        //Done button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (button) -> {
            assert this.client != null;
            this.client.setScreen(null);
        }));
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

    }
    protected void goToPreviousPage() {
        --page;
        if (page <= -1) {
            page = 0;
        }
    }

    protected void goToNextPage() {
        ++page;
        if (!new File("CoordinateBook/"+page+".json").exists()) {
            try {
                if (new File("CoordinateBook/"+page+".json").createNewFile()) {
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
        this.pageIndexText = Text.translatable("book.pageIndicator", page + 1, Math.max((Objects.requireNonNull(new File("CoordinateBook/").list()).length), 1));

        int k = this.textRenderer.getWidth(this.pageIndexText);
        this.textRenderer.draw(matrices, this.pageIndexText, (float)(i - k + 192 - 44), 18.0F, 0);
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

}