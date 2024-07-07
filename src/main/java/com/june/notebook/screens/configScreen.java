package com.june.notebook.screens;

import com.june.notebook.Notebook;
import com.june.notebook.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class configScreen extends Screen {
    private TextFieldWidget dir;
    private TextFieldWidget pg;
    private TextFieldWidget lim;
    private String newdir = Notebook.pageLocation;
    private int pagelim = menuScreen.pageLimit;
    private int startpg = menuScreen.page;
    private ButtonWidget presetButton;
    public configScreen() {
        super(Text.of(""));
    }


    protected void init() {
        assert this.client != null;
        this.addButtons();
    }


    protected void addButtons() {
        //Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            try {
                FileWriter fileOverwriter = new FileWriter("config/vanilla-notebook/config.cfg");
                fileOverwriter.write("# You need to reload for these to take affect!\n# Directory for the pages to be stored in (e.g .minecraft/CoordinateBook)\npagedirectory="+dir+"\n# Set page limits, negative numbers mean no limit\npagelimit="+pagelim+"\n# Page to start on after opening the book\nstartpage="+startpg+"\n# Presets enabled?\npresets="+Notebook.presetsEnabled);
                fileOverwriter.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
            Util.readConfig.read();
            assert this.client != null;
            this.client.setScreen(null);
        }).dimensions(this.width / 2 - 100, 196, 200, 20).build());

        // Preset toggle
        presetButton = ButtonWidget.builder(Text.of("Presets: " + Notebook.presetsEnabled), (button) -> {
            Notebook.presetsEnabled = !Notebook.presetsEnabled;

        } ).dimensions(15, 32, 80, 20).build();

        this.addDrawableChild(presetButton);

        // Page directory
        this.dir = new TextFieldWidget(this.textRenderer, 15, 62, 150, 20, Text.translatable("bookconfig.dir")) {};
        this.dir.setChangedListener((l) -> newdir = String.valueOf(l));

        // Page to start on when book opened
        this.pg = new TextFieldWidget(this.textRenderer, 15, 92, 150, 20, Text.translatable("bookconfig.startpage")) {};
        this.pg.setChangedListener((l) -> {
            if (!Objects.equals(l, "")) {
                startpg = Integer.parseInt(l);
            }
        });

        // Page limit
        this.lim = new TextFieldWidget(this.textRenderer, 15, 122, 150, 20, Text.translatable("bookconfig.limit")) {};
        this.lim.setChangedListener((l) -> {
            if (!Objects.equals(l, "")) {
                pagelim = Integer.parseInt(l);
            }
        });

        this.addDrawableChild(dir);
        this.addDrawableChild(pg);
        this.addDrawableChild(lim);
    }



    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("category.notebook.presets").getString(), this.width / 2 - 20 , 12, 16777215);

        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("bookconfig.dir").getString(), 177, 65, 16777215);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("bookconfig.startpage").getString(), 177, 95, 16777215);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("bookconfig.limit").getString(), 177, 125, 16777215);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("bookconfig.warning").getString(), 15, this.height-15, 16777215);
        presetButton.setMessage(Text.of("Presets: " + Notebook.presetsEnabled));
        super.render(matrices, mouseX, mouseY, delta);

    }
}