package com.june.notebook.screens;

import com.june.notebook.Gui;
import com.june.notebook.Notebook;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.june.notebook.Notebook.*;


@Environment(EnvType.CLIENT)
public class bookSelectScreen extends Screen {
    public bookSelectScreen() {
        super(Text.of(""));
    }


    protected void init() {
        assert this.client != null;
        this.addButtons();
    }


    protected void addButtons() {
        //Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            assert this.client != null;
            this.client.setScreen(null);
        }).dimensions(this.width / 2 - 100, 196, 200, 20).build());

        // Preset buttons

        AtomicReference<String> location = new AtomicReference<>(Notebook.pageLocation);
        this.addDrawableChild(ButtonWidget.builder(Text.of("1"), (button) -> {
            if (!Objects.equals(location.get(), Notebook.verified_page_location)) {
                location.set(Notebook.verified_page_location);
                Notebook.pageLocation = Notebook.verified_page_location;
            }
            Notebook.pageLocation = location+"1";
            assert this.client != null;
            this.client.setScreen(new menuScreen());
        }).dimensions(this.width / 2 - 70, 32, 20, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("2"), (button) -> {
            if (!Objects.equals(location.get(), Notebook.verified_page_location)) {
                location.set(Notebook.verified_page_location);
                Notebook.pageLocation = Notebook.verified_page_location;
            }
            Notebook.pageLocation = location+"2";
            assert this.client != null;
            this.client.setScreen(new menuScreen());
        }).dimensions(this.width / 2 - 10 , 32, 20, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("3"), (button) -> {
            if (!Objects.equals(location.get(), Notebook.verified_page_location)) {
                location.set(Notebook.verified_page_location);
                Notebook.pageLocation = Notebook.verified_page_location;
            }
            Notebook.pageLocation = location+"3";
            assert this.client != null;
            this.client.setScreen(new menuScreen());
        }).dimensions(this.width / 2 + 50, 32, 20, 20).build());

        // Config button
        this.addDrawableChild(Gui.sidebar.addSidebarButton(0, DELETE_ICON, this, "delete", 8, 8, (button -> {

        })));
        this.addDrawableChild(new TexturedButtonWidget(5, 5, 20, 20, 0, 0, 20, CONFIG_ICON, 32, 64, (button) -> {
            assert this.client != null;
            this.client.setScreen(new configScreen());
        }, Text.translatable("jwg.button.config")));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawStringWithShadow(matrices, this.textRenderer, Text.translatable("category.notebook.presets").getString(), this.width / 2 - 20 , 12, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }
}