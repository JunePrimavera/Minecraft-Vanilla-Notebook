package com.jwg.coord_book;

import com.jwg.coord_book.util.ensureFileStructureExists;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordBook implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Coordinate Book");
	public static final Identifier BOOK_ICON = new Identifier("coordbook:textures/gui/book.png");
	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("{} has started initializing!", mod.metadata().name());
		boolean importantFilesExist = ensureFileStructureExists.exists("CoordinateBook/", "coords.json");
		ensureFileStructureExists.createFiles(importantFilesExist);
		LOGGER.info("{} has finished initializing!", mod.metadata().name());
	}
}
