package com.jwg.coord_book;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordBook implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Coordinate Book");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("{} has started initializing!", mod.metadata().name());


		LOGGER.info("{} has finished initializing!", mod.metadata().name());
	}
}
