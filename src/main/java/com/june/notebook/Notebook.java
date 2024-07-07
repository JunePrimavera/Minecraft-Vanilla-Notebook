package com.june.notebook;

import com.june.notebook.Util.ensureFileStructureExists;
import com.june.notebook.Util.generateConfig;
import com.june.notebook.Util.readConfig;
import com.june.notebook.Util.resetConfig;
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
	public static final String version = "2.1.0";
	public static final String project = "Vanilla-Notebook";
	public static String pageLocation = "Notebook/Default";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);

	public static final Identifier BOOK_ICON = new Identifier("notebook:textures/gui/book.png");
	public static final Identifier BOOKMARK_ICON = new Identifier("notebook:textures/gui/bookmark-goto.png");
	public static final Identifier BOOKMARK_MARKER_ICON = new Identifier("notebook:textures/gui/bookmark.png");
	public static final Identifier DELETE_ICON = new Identifier("notebook:textures/gui/del-page.png");
	public static final Identifier CONFIG_ICON = new Identifier("notebook:textures/gui/config.png");

	public static boolean NEEDS_SETUP = false;

	public static void re_init(boolean is_p_init) {

		readConfig.read();
		verified_page_location = pageLocation;
		// I should probably improve this code
		// It's very cursed but in a weird way i love it
		LOGGER.info("{} has started initializing!", project);
		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"1/"));
		if (!new File(pageLocation+"1/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"1").mkdirs();
				b = new File(pageLocation+"1/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		File firstPage = new File(pageLocation+"1/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		boolean tmp = false;
		if (NEEDS_SETUP) { generateConfig.generate(0); resetConfig.reset();  }
		try {
			if (firstPage.createNewFile()){ LOGGER.info("Created first page of the book.."); NEEDS_SETUP = true; }
		} catch (IOException e) { System.out.println(e); }
		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"2/"));
		if (!new File(pageLocation+"2/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"2").mkdirs();
				b = new File(pageLocation+"2/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		firstPage = new File(pageLocation+"2/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		tmp = false;
		if (NEEDS_SETUP) { generateConfig.generate(0); resetConfig.reset();  }
		try {
			if (firstPage.createNewFile()){ LOGGER.info("Created first page of the book.."); NEEDS_SETUP = true; }
		} catch (IOException e) { System.out.println(e); }
		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"3/"));
		if (!new File(pageLocation+"3/0.jdat").exists()) {
			try {
				boolean b = new File(pageLocation+"3").mkdirs();
				b = new File(pageLocation+"3/0.jdat").createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		firstPage = new File(pageLocation+"3/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		tmp = false;
		if (NEEDS_SETUP) { generateConfig.generate(0); resetConfig.reset();  }
		try {
			if (firstPage.createNewFile()){ LOGGER.info("Created first page of the book.."); NEEDS_SETUP = true; }
		} catch (IOException e) { System.out.println(e); }



		if (!new File("config/vanilla-notebook/config.cfg").exists()) NEEDS_SETUP = true;
		try {
			tmp = new File("config/vanilla-notebook/").mkdirs();
			tmp = new File("config/vanilla-notebook/config.cfg").createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (NEEDS_SETUP) resetConfig.reset();


		readConfig.read();

		if (!pageLocation.equals("notebook") && !new File(pageLocation+"1").exists()) { tmp = new File(pageLocation+"1").mkdirs(); }
		if (!pageLocation.equals("notebook") && !new File(pageLocation+"2").exists()) { tmp = new File(pageLocation+"2").mkdirs(); }
		if (!pageLocation.equals("notebook") && !new File(pageLocation+"3").exists()) { tmp = new File(pageLocation+"3").mkdirs(); }
		if (tmp) { LOGGER.info("Possible first time use! Thank you for using my mod!"); }

		if (is_p_init) {
			LOGGER.info("Registering keybinds...");
			openBookKeybindRegister();
			LOGGER.info("{} has finished initializing!", project);
		}

	}

	@Override
	public void onInitialize() { re_init(true); }

}
