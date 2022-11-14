package com.jwg.coord_book;

import com.jwg.coord_book.util.ensureFileStructureExists;
import com.jwg.coord_book.util.generateConfig;
import com.jwg.coord_book.util.readConfig;
import com.jwg.coord_book.util.resetConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.jwg.coord_book.keybinds.OpenBook.openBookKeybindRegister;

public class CoordBook implements ModInitializer {
	public static final boolean developerMode = true;
	public static final String version = "1.3.0";
	public static final String project = "Coordinate-Book";
	public static String pageLocation = "CoordinateBook";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);


	public static final Identifier BOOK_ICON = new Identifier("coordbook:textures/gui/book.png");
	public static final Identifier BOOKMARK_MARKER_ICON = new Identifier("coordbook:textures/gui/bookmark-tab.png");
	public static final Identifier BOOKMARK_ICON = new Identifier("coordbook:textures/gui/bookmark-goto.png");
	public static final Identifier DELETE_ICON = new Identifier("coordbook:textures/gui/del-page.png");


	public static boolean NEEDS_SETUP = false;

	@Override
	public void onInitialize() {
		LOGGER.info("{} has started initializing!", project);
		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"/"));
		File firstPage = new File(pageLocation+"/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		boolean tmp = false;
		try {
			if (firstPage.createNewFile()){ LOGGER.info("Created first page of the book.."); NEEDS_SETUP = true; }
			else { if (!new File("config/coordinate-book/config.cfg").exists()) { NEEDS_SETUP = true; } tmp = new File("config/coordinate-book/config.cfg").createNewFile(); }
		} catch (IOException e) { System.out.println(e); }
		if (NEEDS_SETUP) { generateConfig.generate(0); resetConfig.reset(); }
		readConfig.read();

		if (!pageLocation.equals("CoordinateBook") && !new File(pageLocation).exists()) { tmp = new File(pageLocation).mkdirs(); }
		if (tmp) { LOGGER.info("Possible first time use! Thank you for using my mod!"); }

		LOGGER.info("Registering keybinds...");
		openBookKeybindRegister();
		LOGGER.info("{} has finished initializing!", project);
	}
}
