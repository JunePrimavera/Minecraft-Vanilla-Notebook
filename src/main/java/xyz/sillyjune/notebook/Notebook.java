package xyz.sillyjune.notebook;

import com.google.gson.Gson;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;


public class Notebook implements ModInitializer {
    private static KeyBinding openBookKeybind;
    public static ButtonTextures b_id(String id) {
        return new ButtonTextures( // For whatever reason, you can't specify .png at the end of these. I have questions for mojang.
                new Identifier("notebook", id + "/unfocused"),
                new Identifier("notebook", id + "/focused")
        );
    }

    @Override
    public void onInitialize() {
        String config = NotebookConfig.read_config(); // Read config
        NotebookConfig cfg = NotebookConfig.default_config();
        if (config != null) { // If the config file exists, set it to whatever json was inside
            cfg = new Gson().fromJson(config, NotebookConfig.class);
        } else { // If the config file doesn't exist, get the string version of the defaults we set cfg to
            String json = new Gson().toJson(cfg);
            try { // Write the defaults to config/notebook.json
                FileWriter writer = new FileWriter("config/notebook.json");
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to create config file!");
            }
        }
        CONFIG = cfg; // Set the config
        openBookKeybindRegister(); // Register keybind for opening the notebook, ";" by default
        if (!new File(BOOK_FOLDER).exists() || !new File(BOOK_FOLDER + "/default.json").exists()) {
            try {
                Files.createDirectories(Paths.get(BOOK_FOLDER));
                NotebookData data = new NotebookData(new String[0], "default.json");
                data.write();
            } catch (IOException e) {
                LOGGER.error("failed to create " + BOOK_FOLDER);
            }
        }
        if (CONFIG.debug()) { LOGGER.error("June is very silly. Continue with extreme caution."); }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.MONTH) != Calendar.JUNE) { GAY = false; } // Gay unless proven straight.
    }

    public static void openBookKeybindRegister() { // Register keybind
        openBookKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.notebook.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_SEMICOLON, "category.notebook.keys"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openBookKeybind.wasPressed()) { // Open notebook on key press
                client.setScreen(new NotebookScreen());
            }
        });

    }
    public static final Logger LOGGER = LoggerFactory.getLogger("notebook");
    public static final ButtonTextures MAIN_BUTTON_ICON = b_id("book");
    public static final ButtonTextures NEW_PAGE_ICON = b_id("new_page");
    public static final ButtonTextures DEL_PAGE_ICON = b_id("delete_page");
    public static final ButtonTextures LAST_BOOK_ICON = b_id("last_book");
    public static final ButtonTextures RENAME_BOOK_ICON = b_id("rename_book");
    public static final ButtonTextures NEXT_BOOK_ICON = b_id("next_book");
    public static NotebookConfig CONFIG;
    public static final Identifier BOOK_TEXTURE = new Identifier("textures/gui/book.png");
    public static String BOOK_FOLDER = "Notebook";
    public static boolean GAY = true;
}
