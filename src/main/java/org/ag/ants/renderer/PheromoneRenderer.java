package org.ag.ants.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.renderer.AbstractRenderer;
import org.ag.common.renderer.RenderedImage;
import org.ag.common.simulation.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PheromoneRenderer extends AbstractRenderer {
	private static final Logger logger = LoggerFactory
			.getLogger(PheromoneRenderer.class);
	private final Color pheromoneColor;

	public PheromoneRenderer(String name, Environment environment) {
		super(name, environment);
		this.pheromoneColor = new Color(255, 0, 0, 255);
	}

	public PheromoneRenderer(String name, Environment environment, Color pheromoneColor) {
		super(name, environment);
		this.pheromoneColor = pheromoneColor;
	}

	@Override
	public RenderedImage call() throws Exception {
		final BufferedImage bufferedImage = new BufferedImage(
				environment.getWidth(), environment.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		final Graphics2D g2d = bufferedImage.createGraphics();

		for (int l = 0; l < environment.getHeight(); l++) {
			if (Thread.currentThread().isInterrupted()) {
				logger.info("Pheromone renderer was interrupted and will not "
						+ "complete...");

				g2d.dispose();
				break;
			}

			for (int c = 0; c < environment.getWidth(); c++) {
				final PheromoneNode node = (PheromoneNode) environment.getNodeAt(l, c);
				final ChemicalCommStimulus stimulus = node.getCommunicationStimulus(ForageStimulusType.TYPE);
				double intentity = 0;
				
				if (stimulus != null) {
					intentity = stimulus.getIntensity();
				}
				
				if (intentity != 0) {
					g2d.setColor(new Color(pheromoneColor.getRed(), pheromoneColor.getGreen(), pheromoneColor.getBlue(), (int) intentity * 255));
				
				} else {
					g2d.setColor(new Color(255, 255, 255, 0));
				}
				
				g2d.drawLine(c, l, c, l);
			}
		}

		g2d.dispose();
		logger.trace("Finished rendering pheromone image.");
		return new RenderedImage(name, bufferedImage);
	}

}
