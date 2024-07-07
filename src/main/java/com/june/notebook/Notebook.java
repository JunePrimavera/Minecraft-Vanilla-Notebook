package com.june.notebook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.june.notebook.Keybind.openBookKeybindRegister;

public class Notebook implements ModInitializer {
	public static boolean presetsEnabled = true;
	public static String verified_page_location = "Notebook/Default";
	public static final boolean developerMode = false;
	public static final String version = "2.1.1";
	public static final String project = "Vanilla-Notebook";
	public static String pageLocation = "Notebook/Default";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);

	public static final Identifier BOOK_ICON = new Identifier("notebook:textures/gui/book.png");
	public static final Identifier BOOKMARK_ICON = new Identifier("notebook:textures/gui/bookmark-goto.png");
	public static final Identifier BOOKMARK_MARKER_ICON = new Identifier("notebook:textures/gui/bookmark.png");
	public static final Identifier DELETE_ICON = new Identifier("notebook:textures/gui/del-page.png");
	public static final Identifier CONFIG_ICON = new Identifier("notebook:textures/gui/config.png");


	public static void re_init(boolean is_p_init) {
		// This code is FAR better than before, from ~80 lines to ~40
		// Make sure the config files exist
		if (!new File("config/vanilla-notebook/config.cfg").exists()) {
			Config.generate(0);
			Config.reset();
		}
		Config.read();
		verified_page_location = pageLocation;
		// Make first pages of book if they don't exist already
		if (!new File(pageLocation+"1/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"1").mkdirs();
				b = new File(pageLocation+"1/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!new File(pageLocation+"2/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"2").mkdirs();
				b = new File(pageLocation+"2/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!new File(pageLocation+"3/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"3").mkdirs();
				b = new File(pageLocation+"3/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		// Register keybind
		if (is_p_init) {
			LOGGER.info("Registering keybinds...");
			openBookKeybindRegister();
			LOGGER.info("{} has finished initializing!", project);
		}
	}
	@Override
	public void onInitialize() { re_init(true); }

}
