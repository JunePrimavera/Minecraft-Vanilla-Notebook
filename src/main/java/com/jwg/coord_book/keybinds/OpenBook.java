package com.jwg.coord_book.keybinds;

import com.jwg.coord_book.screens.menuScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)

public class OpenBook {
    private static KeyBinding openBookKeybind;
    public static void openBookKeybindRegister() {
        openBookKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.coord_book.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON,
                "category.coord_book.keys"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openBookKeybind.wasPressed()) { assert client.player != null; client.setScreen(new menuScreen()); }
        });
    }
}