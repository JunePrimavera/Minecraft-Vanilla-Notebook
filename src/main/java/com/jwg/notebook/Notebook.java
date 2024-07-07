package com.jwg.notebook;

import com.jwg.notebook.util.ensureFileStructureExists;
import com.jwg.notebook.util.generateConfig;
import com.jwg.notebook.util.readConfig;
import com.jwg.notebook.util.resetConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.jwg.notebook.keybinds.OpenBook.openBookKeybindRegister;

public class Notebook implements ModInitializer {
	public static final boolean developerMode = false;
	public static final String version = "2.0.0-Alpha";
	public static final String project = "Vanilla-Notebook";
	public static String pageLocation = "Notebook/Default";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);


	public static final Identifier BOOK_ICON = new Identifier("notebook:textures/gui/book.png");

	public static final Identifier BOOKMARK_ICON = new Identifier("notebook:textures/gui/bookmark-goto.png");
	public static final Identifier BOOKMARK_MARKER_ICON = new Identifier("notebook:textures/gui/bookmark.png");
	public static final Identifier DELETE_ICON = new Identifier("notebook:textures/gui/del-page.png");


	public static boolean NEEDS_SETUP = false;

	@Override
	public void onInitialize() {
		LOGGER.info("{} has started initializing!", project);
		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"/"));
		File firstPage = new File(pageLocation+"/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		boolean tmp = false;

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

		if (!pageLocation.equals("notebook") && !new File(pageLocation).exists()) { tmp = new File(pageLocation).mkdirs(); }
		if (tmp) { LOGGER.info("Possible first time use! Thank you for using my mod!"); }

		LOGGER.info("Registering keybinds...");
		openBookKeybindRegister();
		LOGGER.info("{} has finished initializing!", project);
	}
}
