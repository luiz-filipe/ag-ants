package org.ag.ants.simulation;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.NestNode;
import org.ag.common.simulation.AbsractEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntEnvironment extends AbsractEnvironment {
	private static final Logger logger = LoggerFactory
			.getLogger(AntEnvironment.class);
	
	public AntEnvironment(final int height, final int width) {
		super(EnvironmentFactory.createPheromoneGrid(new Dimension(height,
				width)), new Dimension(height, width));
	}

	public void createNestAt(final String id, final int line, final int column,
			final int height, final int width, final Color color) {

		// test if the new node would overlap an existent node
		for (int l = 0; l < height; l++) {
			for (int c = 0; c < width; c++) {
				if (this.getNodeAt(line + 1, column + 1) instanceof NestNode) {
					logger.error("Could not add nest [{}]. Specified "
							+ "position overlaps already existing nest.", id);

					return;
				}
			}
		}

		AntNest nest = new AntNest(id, new Dimension(height, width), color);
		this.addEnvironmentElement(nest, line, column);
	}
}
