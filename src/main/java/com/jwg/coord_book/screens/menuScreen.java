package com.jwg.coord_book.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import static net.minecraft.client.gui.screen.ingame.BookScreen.EMPTY_PROVIDER;

@Environment(EnvType.CLIENT)
public class menuScreen extends Screen {
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
    }
    protected void addCloseButton() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 196, 200, 20, ScreenTexts.DONE, (button) -> {
            assert this.client != null;
            this.client.setScreen((Screen)null);
        }));
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