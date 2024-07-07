package com.jwg.coord_book;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jwg.coord_book.util.*;

import java.io.File;
import java.io.IOException;

public class CoordBook implements ModInitializer {
	public static final boolean developerMode = false;
	public static final String version = "1.0.0";
	public static final String project = "Coordinate-Book";
	public static String pageLocation = "CoordinateBook";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);


	public static final Identifier BOOK_ICON = new Identifier("coordbook:textures/gui/book.png");
	public static final Identifier DELETE_ICON = new Identifier("coordbook:textures/gui/cross.png");

	public static boolean NEEDS_SETUP = false;

	@Override
	public void onInitialize() {
		LOGGER.info("{} has started initializing!", project);

		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"/"));
		File firstPage = new File(pageLocation+"/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		boolean tmp = false;
		try {
			if (firstPage.createNewFile()){
				LOGGER.info("Created first page of the book..");
				NEEDS_SETUP = true;
			}
			else{
				if (!new File("config/coordinate-book/config.cfg").exists()) {
					NEEDS_SETUP = true;
				}
				tmp = new File("config/coordinate-book/config.cfg").createNewFile();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		if (NEEDS_SETUP) {
			generateConfig.generate(0);
			resetConfig.reset();
		}
		readConfig.read();

		if (!pageLocation.equals("CoordinateBook") && !new File(pageLocation).exists()) {
			tmp = new File(pageLocation).mkdirs();
		}
		if (tmp) {
			LOGGER.info("Possible first time use! Thank you for using my mod!");
		}
		LOGGER.info("{} has finished initializing!", project);
	}
}
