package com.jwg.coord_book;

import com.jwg.coord_book.util.ensureFileStructureExists;
import com.jwg.coord_book.util.generateConfig;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CoordBook implements ModInitializer {
	public static final boolean developerMode = true;
	public static final String version = "0.3.1";
	public static final String project = "Coordinate-Book";
	public static final String pageLocation = "CoordinateBook";
	public static final Logger LOGGER = LoggerFactory.getLogger(project);


	public static final Identifier BOOK_ICON = new Identifier("coordbook:textures/gui/book.png");
	public static final Identifier DELETE_ICON = new Identifier("coordbook:textures/gui/cross.png");

	public static boolean NEEDS_SETUP = false;
	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("{} has started initializing!", mod.metadata().name());

		ensureFileStructureExists.createFiles(ensureFileStructureExists.exists(pageLocation+"/"));
		File firstPage = new File(pageLocation+"/0.jdat");
		LOGGER.info("Page folder is \"{}\"", pageLocation);
		try {
			if (firstPage.createNewFile()){
				LOGGER.info("Created first page of the book..");
				NEEDS_SETUP = true;
			}
			else{
				if (!new File("config/coordinate-book/config.cfg").createNewFile()) {
					NEEDS_SETUP = true;
				} else {
					LOGGER.info("Config exists, setup not required.");
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		if (NEEDS_SETUP) {
			generateConfig.generate(0);
		}
		LOGGER.info("{} has finished initializing!", mod.metadata().name());
	}
}
