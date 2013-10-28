package org.ag.ants.test.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageWriter {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageWriter.class);
	
	private ImageWriter() {}

	public static void writeImage(BufferedImage image, String path) {
		try {
			File file = new File(path);

			logger.debug("Starting writing environment explored image at: {}",
					path);
			
			ImageIO.write(image, "png", file);

		} catch (IOException e) {
			logger.error("Error rendering explored space. Could not write "
					+ "image at: {}", path);
			logger.error(e.getMessage());
		}
	}
}
