package com.june.notebook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static com.june.notebook.Keybind.openBookKeybindRegister;

public class Notebook implements ModInitializer {
	public static int[] button_positions = { 0, 0, 0, 0 };
	public static boolean presetsEnabled = true;
	public static String verified_page_location = "Notebook/Default";
	public static final boolean developerMode = false;
	public static final String version = "2.2.0";
	public static final String project = "Vanilla-Notebook";
	public static String pageLocation = "Notebook/Default";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);

	public static final Identifier BOOK_ICON = new Identifier("notebook:textures/gui/book.png");
	public static final Identifier IMPORT_ICON = new Identifier("notebook:textures/gui/import.png");
	public static final Identifier EXPORT_ICON = new Identifier("notebook:textures/gui/export.png");
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
		// Make config for button placement if they don't already exist
		if (!new File("config/vanilla-notebook/buttons/mainmenu.jdat").exists()) {
			boolean b = new File("config/vanilla-notebook/buttons/").mkdirs();
			try {
				b = new File("config/vanilla-notebook/buttons/mainmenu.jdat").createNewFile();
				FileWriter w = new FileWriter("config/vanilla-notebook/buttons/mainmenu.jdat");
				w.write("0\n0");
				w.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!new File("config/vanilla-notebook/buttons/gamemenu.jdat").exists()) {
			boolean b = new File("config/vanilla-notebook/buttons/").mkdirs();
			try {
				b = new File("config/vanilla-notebook/buttons/gamemenu.jdat").createNewFile();
				FileWriter w = new FileWriter("config/vanilla-notebook/buttons/gamemenu.jdat");
				w.write("0\n0");
				w.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		// Read config for button placement
		try {
			File f = new File("config/vanilla-notebook/buttons/mainmenu.jdat");
			Scanner myReader = new Scanner(f);
			int i = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				button_positions[i+2] = Integer.parseInt(data);
				i++;
				if (i > 1) {
					break;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		try {
			File f = new File("config/vanilla-notebook/buttons/gamemenu.jdat");
			Scanner myReader = new Scanner(f);
			int i = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				button_positions[i] = Integer.parseInt(data);
				i++;
				if (i > 1) {
					break;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
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
